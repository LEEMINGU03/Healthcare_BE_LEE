# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project status

Spring Boot skeleton (Spring Initializr) with no application code yet beyond the default entry point. The MVP design is agreed but not implemented.

**Read `Docs/architecture.md` first** — it holds the agreed MVP architecture: system topology, the backend/AI API contracts, the DB schema, and an explicit "미결 사항" list of things the user has not decided. Wireframes it was derived from are in `Docs/*.png`.

Key points that shape any work here:
- The backend is a **context assembler** between a front server and a single AI server: it reads profile/inbody/chat history from the DB and passes them to the AI server, which stays stateless and never touches the DB.
- The backend **defines the API contract** for both the front and the AI server; the AI-side contract in `Docs/architecture.md` is a draft still pending agreement with the AI server owner.
- Auth is implemented: Google social login (`spring-boot-starter-oauth2-client`) + self-issued JWT (Access/Refresh Token, both JWT — see `com.example.Healthcare_BE.auth`). The old single-seeded-dummy-user approach is retired; `UserService.getCurrentUser()` now resolves the real logged-in user from the `JwtAuthenticationFilter`-populated `SecurityContext`.
- Deployment target is Railway; DB is PostgreSQL. Local dev now uses a local PostgreSQL instance; production DB hosting is not yet decided (Supabase is no longer used).

## Stack

- Java 21 (Gradle toolchain), Spring Boot 4.1.0, Gradle (wrapper-based, no local Gradle install required)
- Dependencies: `spring-boot-starter-security`, `spring-boot-starter-validation`, `spring-boot-starter-webmvc`, Lombok
- Test stack: JUnit 5 (`useJUnitPlatform()`), `spring-boot-starter-security-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test`
- Base package: `com.example.Healthcare_BE`

## Common commands

Use the Gradle wrapper (`gradlew.bat` on this Windows machine; PowerShell is the default shell here):

```powershell
.\gradlew.bat build            # full build (compiles + runs tests)
.\gradlew.bat bootRun           # run the application locally
.\gradlew.bat test              # run all tests
.\gradlew.bat test --tests "com.example.Healthcare_BE.HealthcareBeApplicationTests"   # run a single test class
.\gradlew.bat test --tests "*.HealthcareBeApplicationTests.contextLoads"              # run a single test method
```

## Architecture

Standard Spring Boot Gradle layout — no custom architecture exists yet:

- `src/main/java/com/example/Healthcare_BE/` — application code; currently only `HealthcareBeApplication.java` (the `@SpringBootApplication` entry point)
- `src/main/resources/application.properties` — Spring config (currently only `spring.application.name`)
- `src/test/java/com/example/Healthcare_BE/` — tests; currently only the default `contextLoads()` smoke test

As features are added, follow standard Spring Boot conventions (e.g. `controller`, `service`, `repository`, `domain`/`entity`, `config` packages under the base package) unless the user directs otherwise.
