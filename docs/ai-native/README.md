# AI-native Backend Engineering — operating system (2026–2027)

## Career target
Build evidence for Java Backend and AI-enabled software engineering roles in Romania.
The objective is not memorizing tools. It is the ability to ship reliable systems, inspect AI-generated code, and explain design decisions during interviews.

## What already exists
Repository contains Spring Boot 4.1.1, Java 21, employee CRUD/search, HTTP Basic authorization, PostgreSQL configuration, and a context-loading test. The order checkout exercise is documented but not considered implemented. This document does not claim tests have passed.

## Twelve-month structure
- Months 1–3: Java/Spring architecture, SQL, tests, HTTP, security. Evidence: well-tested, documented API.
- Months 4–6: PostgreSQL migrations, CI/CD, Docker, observability, a deployable modular monolith. Start relevant job applications once evidence exists.
- Months 7–9: queues, reliability, incident handling, LLM provider integration, tool calling, evaluation sets, prompt-injection defense, budget and privacy controls.
- Months 10–12: system design drills, code reviews, debugging, English interviews, portfolio and ongoing applications.

Every 4 weeks: compare current developer-agent capabilities, update the tool choice if evidence justifies it, and keep fundamental engineering targets unchanged.

## Weekly workflow
- Select one user-visible capability.
- Write a small acceptance test first.
- Use an AI agent for boilerplate or alternatives, never as the final authority.
- Review architecture, data access and auth; then run unit/integration tests.
- Record exact test output and one architectural decision.
- Explain the change in English as a 2-minute demo.

## Quality gates for every deliverable
- Builds using the Maven wrapper.
- Tests actually run and results are recorded.
- No credentials in diffs.
- Authentication/authorization behavior tested where relevant.
- README and a reproducible run command.
- A reviewable pull request with screenshots or command output when useful.

## Today — one action
On Windows PowerShell, clone the repository if needed, switch to `ai-native-engineering-2027`, and run:

~~~powershell
git fetch origin
git switch ai-native-engineering-2027
powershell -ExecutionPolicy Bypass -File .\scripts\verify.ps1
~~~

The verification script starts an isolated LOCAL PostgreSQL Docker container on port 5433, then runs Maven tests with environment overrides. It does not claim any result until executed.

Once baseline tests are understood, work on `docs/challenges/01-order-checkout.md`, beginning at Step 2 and writing a failing service test before implementing the feature.

## Four measurable signals
1. Number of tested and merged features.
2. Regression defects and security issues caught before merge.
3. Ability to explain the service and database tradeoffs without AI.
4. Number of relevant interviews and recruiter replies, not merely applications sent.

If an AI tool can generate the feature in seconds, raise the challenge: verify concurrency, authorization, persistence, failure modes, cost, and monitoring.
