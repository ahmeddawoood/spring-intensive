# Architecture quality gate (baseline)

The existing application currently uses a layered layout with **case-sensitive** packages:
`Controller`, `Service`, `Repository`, `Entity`, `DTO`, and `Security`.

`src/test/java/com/ahmed/springintensive/ArchitectureRulesTest.java` encodes four rules:

1. Controllers do not depend directly on repositories.
2. Application services do not depend on controllers.
3. Repositories do not depend on controllers or services.
4. Entities do not depend on controllers, services, or repositories.

These are **baseline boundaries**, not proof of Clean Architecture. Current DTO/entity coupling, application service responsibilities, and security decisions still require human review. Do not claim the code is production-ready because the gate passes.

## Run locally

Java 21 is required. After starting the development PostgreSQL service, run:

```powershell
.\mvnw.cmd -B -ntp test
```

Or run only this architecture test (no database or Docker required):

```powershell
.\mvnw.cmd -B -ntp -Dtest=ArchitectureRulesTest test
```

GitHub Actions invokes `bash ./mvnw -B -ntp verify` for pull requests. The ArchUnit tests run as part of Maven's `test` phase; a broken rule fails the build.

## Deliberate regression test

To confirm the guard really catches a violation, temporarily add a `Repository` type dependency to a `Controller` and run the architecture test. It must fail. Revert the temporary change afterwards. Do not commit the deliberately failing change.

## Next improvements

- Unit tests for authorization and business rules
- Testcontainers integration tests against PostgreSQL
- SAST and dependency scanning
- A separate AI pull-request reviewer, which requires the repository owner's approval / GitHub App installation

Do not use `@ArchIgnore` or exemptions to hide violations without a documented architectural decision.
