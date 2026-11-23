# Baseline: inspect before changing code

Repository snapshot inspected 2026-11-23 (not an executed test run).

Observed:
- Java 21 and Spring Boot 4.1.1 are declared in pom.xml.
- Employee CRUD/search, registration, HTTP Basic authorization and PostgreSQL are present.
- Only one context-loading test is visible in src/test.
- application.properties uses local PostgreSQL defaults and development-level Spring Security debug logging.
- The first documented order/checkout challenge has acceptance criteria but no corresponding checkout feature in the inspected tree.
- There is no GitHub Actions workflow in the inspected tree.

Initial priority:
1. Reproducible build/test environment.
2. CI feedback for each pull request.
3. Focused fast unit tests for the first challenge.
4. Later: replace local-only defaults with environment-based configuration, add migration strategy, review CSRF/security assumptions and ensure logs are safe.

The development Docker credentials are for a disposable local-only database, not production.

Definition of done for the baseline:
- PowerShell verification script completes on the learner's computer.
- CI reports a build and test result for the PR.
- Existing failures are recorded and separated from new work.
