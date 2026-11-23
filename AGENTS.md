# AI-native engineering rules for spring-intensive

This repository is a Java 21 / Spring Boot 4.1.1 learning and portfolio project.

## Mission
Develop a backend engineer who can design, implement, verify, secure, and explain production-minded systems while working effectively with AI coding agents.

## Operating mode
1. Read the existing code and `docs/challenges/01-order-checkout.md` before proposing edits.
2. Never assume a task is completed because it is documented. Verify the source and tests.
3. For each task: state an acceptance criterion, inspect relevant files, propose a minimal change, implement, test, review the diff, and report evidence.
4. If a build fails before changes, report the baseline failure separately. Do not claim a passing test without an actual run.
5. Keep work small and reversible. Use a feature branch and one reviewable pull request per change. Do not merge automatically.
6. Do not ask routine preference questions. Choose safe defaults and document them. Ask only for unavailable credentials or an irreversible decision.

## Engineering standards
- Preserve Java 21 and the existing Spring Boot version unless a documented change requires an upgrade.
- Use constructor injection, cohesive packages, clear service boundaries, DTO validation, and explicit error behavior.
- Prefer a modular monolith to premature microservices.
- Keep persistence, HTTP, application logic, and external provider adapters separate.
- Never commit API keys, user data, or production credentials. Use environment variables and example values for local demos.
- Add unit tests for business logic and integration tests for behavior crossing boundaries.
- Include negative tests for validation, unauthorized access, timeouts, and provider errors when applicable.
- Do not treat AI-generated code as trusted. Review each diff for authorization flaws, accidental data exposure, unsafe defaults, unnecessary dependencies, and edge cases.
- When adding LLM capabilities, require a defined task, measurable quality evaluation, strict input/output contracts, bounded cost/time, and a non-AI fallback where practical.
- Avoid logging credentials, tokens, sensitive payloads, or unnecessary PII.
- Never weaken security just to make a test pass.

## Agent response format
Keep communication direct and brief:
- Goal and acceptance criteria
- What changed
- Tests run, with actual command and outcome
- Risks and next concrete action

## First active challenge
The authoritative, currently uncompleted assignment is `docs/challenges/01-order-checkout.md`.
Do not generate the entire solution upfront for the learner. Start with the provider interface, result contract, and a failing unit test.
The learner should explain the design; the agent should handle repetitive scaffolding, test execution where available, and code review.
