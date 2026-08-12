-- Healthcare_BE 스키마 적용 스크립트
-- DB 클라이언트(psql/pgAdmin 등)에 그대로 붙여넣어 실행한다.
-- 각 테이블의 설계 근거·인덱스 설명은 database.md 3장 참고 — 이 파일은 그 DDL을 실행 순서대로 합친 것.
-- 스키마를 바꿀 때는 database.md와 이 파일을 함께 수정한다.
--
-- [2026-07 갱신] 회원가입/프로필 화면 대응:
--  * users: 프로필·운동목표(복수)·운동계획 컬럼 추가, target_gain_kg 제거
--  * inbody_records: body_fat_pct 추가
--  * 신설: user_preferences, user_ai_settings, user_notification_settings
--  * 다중선택은 text[] 배열, 허용값 검증은 백엔드에서 수행(주석 참고)
--  * '계정·보안'(비번/2단계인증/소셜연동)은 인증 도입 단계로 미룸 — 스키마 미포함
--
-- [2026-07 갱신] 구글 소셜 로그인 도입:
--  * 신설: user_social_accounts (유저 1명이 여러 provider를 연결할 수 있는 구조. 카카오 등 추후 추가 예정)
--  * 신설: refresh_tokens (JWT access token 재발급용. DB에 저장해 즉시 무효화 가능하게 함)
--
-- [2026-08 갱신] 운동 수행 기록:
--  * 신설: workout_logs (AI 루틴 수행 + 사용자 자유 입력을 한 테이블에 담음, routine_id는 SET NULL)
--
-- [2026-08 갱신] AI 부위(bodyPart) 원본 보관:
--  * routine_exercises: body_part 추가. AI가 주는 9개 값 그대로 저장(nullable, CHECK).
--    workout_logs.muscle_group(7개)과는 다른 값 도메인 — 여기는 매핑 없이 원본 그대로.

create table users
(
    id                         uuid primary key       default gen_random_uuid(),
    name                       text          not null,                                                  -- 이름
    nickname                   text,                                                                    -- 닉네임
    gender                     text          check (gender in ('MALE', 'FEMALE')),                      -- 성별 (소셜 로그인 직후엔 미입력)
    birth_date                 date,                                                                    -- 생년월일
    email                      text,                                                                    -- 이메일(표시용, 인증 추후)
    phone                      text,                                                                    -- 휴대전화 번호
    bio                        text,                                                                    -- 자기소개
    profile_image_url          text,                                                                    -- 프로필 이미지 URL
    height_cm                  numeric(4, 1) check (height_cm > 0),                                     -- 키 (소셜 로그인 직후엔 미입력)

    -- 운동 목표(복수 선택). 허용값(백엔드 검증):
    --   MUSCLE_GAIN, FAT_LOSS, FITNESS, POSTURE, REHAB, HABIT
    goals                      text[] not null default '{}',

    -- 운동 경력: UNDER_6M(6개월 미만), M6_1Y(6개월~1년), Y1_2Y(1년~2년), OVER_2Y(2년 이상)
    experience_level           text check (experience_level in ('UNDER_6M', 'M6_1Y', 'Y1_2Y', 'OVER_2Y')),

    -- 운동 계획
    workout_frequency_per_week integer check (workout_frequency_per_week between 1 and 7),              -- 주당 횟수
    workout_duration           text check (workout_duration in ('UNDER_30', 'M60', 'M90', 'OVER_120')), -- 1회 시간
    target_weight_kg           numeric(4, 1),                                                           -- 목표 체중
    target_muscle_kg           numeric(4, 1),                                                           -- 목표 골격근량
    goal_target_date           date,                                                                    -- 목표 달성 예정일

    previous_workout           text check (previous_workout in ('UPPER_BODY', 'LOWER_BODY')),           -- 전날 운동
    created_at                 timestamptz   not null default now()
);

-- 소셜 로그인 연결 정보. 유저 1명이 여러 provider를 연결할 수 있다 (구글+카카오 등).
create table user_social_accounts
(
    id               uuid primary key     default gen_random_uuid(),
    user_id          uuid        not null references users (id) on delete cascade,
    provider         text        not null check (provider in ('GOOGLE')), -- 추후 KAKAO 등 추가
    provider_user_id text        not null,                                -- 구글 sub
    created_at       timestamptz not null default now(),
    unique (provider, provider_user_id), -- 같은 소셜 계정이 다른 유저에 중복 연결되는 것 방지
    unique (user_id, provider)           -- 한 유저가 같은 provider를 두 번 연결하는 것 방지
);

-- 로그인 세션 대신 JWT(access/refresh token) 방식을 쓰기 위한 refresh token 저장소.
-- access token은 서명만 검증하는 stateless JWT라 DB에 없다. refresh token도 JWT이지만
-- 발급한 문자열을 여기 그대로 저장해, 로그아웃 등으로 이 행을 지우면 만료 전이라도 즉시 무효화된다.
create table refresh_tokens
(
    id         uuid primary key     default gen_random_uuid(),
    user_id    uuid        not null references users (id) on delete cascade,
    token      text        not null unique,
    expires_at timestamptz not null,
    created_at timestamptz not null default now()
);

create table inbody_records
(
    id                      uuid primary key       default gen_random_uuid(),
    user_id                 uuid          not null references users (id) on delete cascade,
    measured_at             date          not null,
    weight_kg               numeric(5, 2) not null check (weight_kg > 0),               -- 체중
    bmr_kcal                integer       not null check (bmr_kcal > 0),                -- 기초대사량
    skeletal_muscle_mass_kg numeric(5, 2) not null check (skeletal_muscle_mass_kg > 0), -- 골격근량
    body_fat_mass_kg        numeric(5, 2) not null check (body_fat_mass_kg >= 0),       -- 체지방량
    body_fat_pct            numeric(4, 1) check (body_fat_pct >= 0),                    -- 체지방률(%)
    created_at              timestamptz   not null default now(),
    unique (user_id, measured_at)
);

create index idx_inbody_user_measured
    on inbody_records (user_id, measured_at desc);

-- 프로필 "운동 선호 설정" — 순수 선호(다중선택). 유저 1:1.
create table user_preferences
(
    id                      uuid primary key     default gen_random_uuid(),
    user_id                 uuid        not null unique references users (id) on delete cascade,
    -- 선호 운동: WEIGHT, BODYWEIGHT, CARDIO, STRETCHING, FUNCTIONAL
    preferred_workout_types text[] not null default '{}',
    -- 불편한 부위: NECK, SHOULDER, ELBOW, WAIST, KNEE, WRIST, ANKLE, NONE
    injury_parts            text[] not null default '{}',
    created_at              timestamptz not null default now(),
    updated_at              timestamptz not null default now()
);

-- 프로필 "AI 맞춤 설정". 유저 1:1.
create table user_ai_settings
(
    id                    uuid primary key     default gen_random_uuid(),
    user_id               uuid        not null unique references users (id) on delete cascade,
    recommendation_style  text check (recommendation_style in ('SAFETY', 'EFFECT', 'SIMPLE', 'EXPLORE')), -- 추천 방식
    explanation_level     text check (explanation_level in ('SIMPLE', 'STANDARD', 'DETAILED')),           -- 설명 수준
    coach_tone            text check (coach_tone in ('CALM', 'PRO', 'MOTIVATION', 'CONCISE')),            -- 코치 말투
    auto_daily_routine    boolean     not null default true,                                              -- 오늘의 운동 루틴 추천
    auto_intensity_adjust boolean     not null default true,                                              -- 운동 기록 기반 강도 조절
    auto_weakpart_alert   boolean     not null default true,                                              -- 부족한 운동 부위 알림
    auto_restday_suggest  boolean     not null default false,                                             -- 휴식일 추천
    auto_posture_tip      boolean     not null default true,                                              -- 운동 자세 주의사항 제공
    created_at            timestamptz not null default now(),
    updated_at            timestamptz not null default now()
);

-- 프로필 "알림 설정". 유저 1:1.
create table user_notification_settings
(
    id                     uuid primary key     default gen_random_uuid(),
    user_id                uuid        not null unique references users (id) on delete cascade,
    notify_workout_start   boolean     not null default true,                                                     -- 운동 시작 알림
    notify_weekly_goal     boolean     not null default true,                                                     -- 주간 목표 진행 알림
    notify_long_absence    boolean     not null default true,                                                     -- 장기간 미운동 알림
    notify_ai_recommend    boolean     not null default false,                                                    -- AI 추천 루틴 알림
    notify_body_update     boolean     not null default true,                                                     -- 신체 정보 업데이트 알림
    notify_workout_summary boolean     not null default true,                                                     -- 운동 기록 요약 알림
    notify_service_event   boolean     not null default false,                                                    -- 서비스 공지 및 이벤트
    receive_channel        text        not null default 'APP' check (receive_channel in ('APP', 'EMAIL', 'SMS')), -- 수신 방식
    created_at             timestamptz not null default now(),
    updated_at             timestamptz not null default now()
);

create table chat_sessions
(
    id         uuid primary key     default gen_random_uuid(),
    user_id    uuid        not null references users (id) on delete cascade,
    type       text        not null check (type in ('COACHING', 'NUTRITION')),
    title      text,
    created_at timestamptz not null default now()
);

create index idx_chat_sessions_user_created
    on chat_sessions (user_id, created_at desc);

create table chat_messages
(
    id         uuid primary key     default gen_random_uuid(),
    session_id uuid        not null references chat_sessions (id) on delete cascade,
    role       text        not null check (role in ('USER', 'ASSISTANT')),
    content    text        not null,
    created_at timestamptz not null default now()
);

create index idx_chat_messages_session_created
    on chat_messages (session_id, created_at);

create table routines
(
    id              uuid primary key     default gen_random_uuid(),
    chat_message_id uuid        not null unique references chat_messages (id) on delete cascade,
    title           text        not null,
    created_at      timestamptz not null default now()
);

create table routine_exercises
(
    id          uuid primary key default gen_random_uuid(),
    routine_id  uuid    not null references routines (id) on delete cascade,
    order_no    integer not null check (order_no > 0),
    name        text    not null,
    sets        text    not null,
    reps        text    not null,
    description text,
    image_url   text,
    -- AI가 분류한 부위 원본(9개). AI가 안 줄 수 있어 nullable — workout_logs.muscle_group(7개)으로
    -- 매핑하지 않고 그대로 보관한다(나중 분석용).
    body_part   text check (body_part in
                 ('BACK', 'CHEST', 'BICEPS', 'TRICEPS', 'SHOULDER', 'CORE', 'GLUTES', 'THIGH', 'CALF')),
    unique (routine_id, order_no)
);

-- 운동 수행 기록. AI 루틴 수행(routine_id 有)과 사용자 자유 입력(routine_id 無) 둘 다 담는다.
-- routine_id는 SET NULL(cascade 아님) — routines는 chat_messages에 cascade로 매달려 있어,
-- cascade로 걸면 채팅 세션 삭제 시 운동 이력이 통째로 날아간다.
create table workout_logs
(
    id             uuid primary key       default gen_random_uuid(),
    user_id        uuid          not null references users (id) on delete cascade,
    routine_id     uuid          references routines (id) on delete set null,
    performed_at   date          not null,
    exercise_name  text          not null,
    -- 부위. AI 응답에 해당 필드가 추가되면 채워짐 — 그 전까지는 nullable.
    muscle_group   text          check (muscle_group in
                                   ('CHEST', 'BACK', 'SHOULDER', 'ARM', 'LOWER_BODY', 'CORE', 'CARDIO')),
    planned_sets   integer       check (planned_sets > 0),   -- 추천 세트 수 스냅샷(완료율 계산용)
    completed_sets integer       check (completed_sets >= 0),
    reps           integer       check (reps > 0),
    weight_kg      numeric(5, 2) check (weight_kg >= 0),
    created_at     timestamptz   not null default now()
);

create index idx_workout_logs_user_performed
    on workout_logs (user_id, performed_at desc);

create table meal_plans
(
    id              uuid primary key     default gen_random_uuid(),
    chat_message_id uuid        not null unique references chat_messages (id) on delete cascade,
    title           text        not null,
    created_at      timestamptz not null default now()
);

create table meal_plan_days
(
    id           uuid primary key default gen_random_uuid(),
    meal_plan_id uuid not null references meal_plans (id) on delete cascade,
    day_of_week  text not null check (day_of_week in ('MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT', 'SUN')),
    unique (meal_plan_id, day_of_week)
);

create table meal_plan_meals
(
    id        uuid primary key default gen_random_uuid(),
    day_id    uuid not null references meal_plan_days (id) on delete cascade,
    slot      text not null check (slot in ('BREAKFAST', 'LUNCH', 'DINNER')),
    menu      text not null,
    calories  integer check (calories >= 0),
    carbs_g   numeric(5, 1),
    protein_g numeric(5, 1),
    fat_g     numeric(5, 1),
    unique (day_id, slot)
);