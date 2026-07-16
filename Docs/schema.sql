-- Healthcare_BE 스키마 적용 스크립트
-- Supabase SQL Editor에 그대로 붙여넣어 실행한다.
-- 각 테이블의 설계 근거·인덱스 설명은 database.md 3장 참고 — 이 파일은 그 DDL을 실행 순서대로 합친 것.
-- 스키마를 바꿀 때는 database.md와 이 파일을 함께 수정한다.

create table users (
  id               uuid primary key default gen_random_uuid(),
  name             text not null,
  gender           text not null check (gender in ('MALE', 'FEMALE')),
  height_cm        numeric(4,1) not null check (height_cm > 0),
  target_gain_kg   numeric(4,1),
  previous_workout text check (previous_workout in ('UPPER_BODY', 'LOWER_BODY')),
  created_at       timestamptz not null default now()
);

create table inbody_records (
  id                      uuid primary key default gen_random_uuid(),
  user_id                 uuid not null references users(id) on delete cascade,
  measured_at             date not null,
  weight_kg               numeric(5,2) not null check (weight_kg > 0),
  bmr_kcal                integer      not null check (bmr_kcal > 0),
  skeletal_muscle_mass_kg numeric(5,2) not null check (skeletal_muscle_mass_kg > 0),
  body_fat_mass_kg        numeric(5,2) not null check (body_fat_mass_kg >= 0),
  created_at              timestamptz not null default now(),
  unique (user_id, measured_at)
);

create index idx_inbody_user_measured
  on inbody_records (user_id, measured_at desc);

create table chat_sessions (
  id         uuid primary key default gen_random_uuid(),
  user_id    uuid not null references users(id) on delete cascade,
  type       text not null check (type in ('COACHING', 'NUTRITION')),
  title      text,
  created_at timestamptz not null default now()
);

create index idx_chat_sessions_user_created
  on chat_sessions (user_id, created_at desc);

create table chat_messages (
  id         uuid primary key default gen_random_uuid(),
  session_id uuid not null references chat_sessions(id) on delete cascade,
  role       text not null check (role in ('USER', 'ASSISTANT')),
  content    text not null,
  created_at timestamptz not null default now()
);

create index idx_chat_messages_session_created
  on chat_messages (session_id, created_at);

create table routines (
  id              uuid primary key default gen_random_uuid(),
  chat_message_id uuid not null unique references chat_messages(id) on delete cascade,
  title           text not null,
  created_at      timestamptz not null default now()
);

create table routine_exercises (
  id           uuid primary key default gen_random_uuid(),
  routine_id   uuid not null references routines(id) on delete cascade,
  order_no     integer not null check (order_no > 0),
  name         text not null,
  sets         text not null,
  reps         text not null,
  description  text,
  image_url    text,
  unique (routine_id, order_no)
);

create table meal_plans (
  id              uuid primary key default gen_random_uuid(),
  chat_message_id uuid not null unique references chat_messages(id) on delete cascade,
  title           text not null,
  created_at      timestamptz not null default now()
);

create table meal_plan_days (
  id            uuid primary key default gen_random_uuid(),
  meal_plan_id  uuid not null references meal_plans(id) on delete cascade,
  day_of_week   text not null check (day_of_week in ('MON','TUE','WED','THU','FRI','SAT','SUN')),
  unique (meal_plan_id, day_of_week)
);

create table meal_plan_meals (
  id         uuid primary key default gen_random_uuid(),
  day_id     uuid not null references meal_plan_days(id) on delete cascade,
  slot       text not null check (slot in ('BREAKFAST', 'LUNCH', 'DINNER')),
  menu       text not null,
  calories   integer check (calories >= 0),
  carbs_g    numeric(5,1),
  protein_g  numeric(5,1),
  fat_g      numeric(5,1),
  unique (day_id, slot)
);
