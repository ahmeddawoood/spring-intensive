# Challenge 01 — Order checkout through an injected payment contract

Status — Defined. Implementation not started or verified by this challenge.
All checkboxes describe work to do. No test results are claimed.

## Starting point

Source — [Java OOP notes](https://app.notion.com/p/3b7bfe3d7a59808a9bfdd63dbf8b74e2).

Repository inspected at commit `4c751e219ca7513eb7489374930614ef7f26ea69`.
It contains employee CRUD/search, registration, HTTP Basic authentication, a global validation handler, and one context-loading test. The inspected tree has no order/payment feature.
The POM declares Java 21 and Spring Boot 4.1.1. Keep the project's declared versions for this exercise. Resolve baseline build problems separately and record them before attributing failures to your changes.

This exercise turns the notes' OrderService and PaymentService examples into observable behavior. It supports your Java Backend Engineering plus Enterprise AI Integration direction by practising a replaceable provider boundary. An AI provider can use this pattern later. This challenge does not require an LLM.

## Requirement

Add an authenticated `POST /orders/checkout` endpoint that simulates paying an order.
The caller supplies `orderId` and `amount`. OrderService validates the input, delegates once to PaymentService, and returns a result.
Use constructor injection and an interface. The service must not construct or inspect a concrete payment provider.

This is a checkout simulation. There is no order persistence, actual charge, card data, retry, or idempotency guarantee. Repeating a request repeats the simulation.

### Contract

Request fields

| Field | Java type | Rule |
| --- | --- | --- |
| orderId | String | Required and not blank. Preserve a valid value as supplied |
| amount | BigDecimal | Required, greater than zero, at most 8 integer digits and 2 fraction digits |

Use EUR as the fixed exercise currency. Do not use double for amounts.

For an authenticated request with orderId `ORD-1001` and amount `49.90`, return HTTP 200 and these fields

| Field | Expected value |
| --- | --- |
| orderId | ORD-1001 |
| amount | 49.90 as a JSON number, compared numerically in tests |
| currency | EUR |
| status | PAID |
| provider | CARD for the default provider |

A BANK provider returns the same business result with provider BANK.
Both implementations simulate approval for valid input. Do not introduce random success or failure.

Invalid fields return HTTP 400 with a field-to-message map using the existing validation handler.
Malformed JSON returns HTTP 400. Its error body is outside this exercise's contract.
Unauthenticated requests return HTTP 401 through the existing security configuration.

## Acceptance criteria

- [ ] AC1 — A valid authenticated request returns HTTP 200 and the agreed fields.
- [ ] AC2 — Missing, null, empty, and whitespace-only orderId values return 400.
- [ ] AC3 — Missing/null amount, zero, negative amounts, more than 2 fraction digits, or more than 8 integer digits return 400.
- [ ] AC4 — Invalid HTTP input never reaches PaymentService. Direct invalid OrderService calls throw IllegalArgumentException before invoking it.
- [ ] AC5 — A valid service call invokes PaymentService exactly once with the supplied orderId and amount.
- [ ] AC6 — OrderService holds a private final PaymentService supplied through its constructor. It has no concrete provider dependency, provider switch, or instanceof branch.
- [ ] AC7 — Swapping CARD for BANK through Spring configuration requires no OrderService edits. Exactly one PaymentService bean is selected in each supported configuration.
- [ ] AC8 — A provider exception propagates from OrderService. The service must not manufacture PAID on failure. HTTP failure-body design is deferred.
- [ ] AC9 — Unauthenticated checkout returns 401. Existing employee and registration behavior stays intact.
- [ ] AC10 — The required tests pass and you can explain the concept mapping below.

## Package design

Use the existing root `com.ahmed.springintensive`.
Add lowercase feature packages. Do not rename the existing uppercase packages during this exercise.

| Package relative to root | Types to add | Responsibility |
| --- | --- | --- |
| checkout.api | CheckoutController, CheckoutRequest, CheckoutResponse | HTTP mapping and input validation |
| checkout.application | OrderService | Validate direct calls and coordinate payment |
| checkout.application | PaymentService | Provider contract owned by the consumer |
| checkout.model | PaymentResult | Immutable result containing orderId, amount, currency, status, provider |
| checkout.payment | CardPaymentService, BankPaymentService | Simulated implementations |
| checkout.config | PaymentConfiguration | Select one implementation for injection |

Suggested method contracts

- `PaymentService.pay(String orderId, BigDecimal amount)` returns PaymentResult.
- `OrderService.checkout(String orderId, BigDecimal amount)` returns PaymentResult.
- CheckoutController maps PaymentResult to CheckoutResponse.

Use records for request/response/result data if helpful. Use a class for OrderService.
Keep HTTP concerns out of PaymentService and OrderService.
Use one configuration-owned bean selected by `checkout.payment-provider`, defaulting to `card`.
Supported values are `card` and `bank`. Reject any other configured value at startup.
Keep provider selection inside configuration. Avoid also component-scanning both providers as beans.

## Work through these steps

### Step 1 — Establish the baseline

- [ ] Use Java 21 and the Maven wrapper.
- [ ] Run the existing tests before editing code and record the outcome.
- [ ] Separate dependency-resolution or PostgreSQL setup failures from challenge failures.
- [ ] Read the Java notes' composition, DI, dependency inversion, and polymorphism sections.
- [ ] Create an implementation branch when you begin coding.

The existing full application context includes JPA and database configuration. It may require PostgreSQL. Focused challenge tests below should not depend on a running database.

### Step 2 — Define the contract

- [ ] Create PaymentResult and PaymentService.
- [ ] Write the field rules and method signature before writing behavior.
- [ ] Explain why OrderService should refer to PaymentService.

Checkpoint — The project compiles. No provider, HTTP route, or completion claim is needed yet.

### Step 3 — Write the first failing service test

- [ ] Create a recording fake implementing PaymentService in test sources.
- [ ] Pass it to OrderService through the constructor.
- [ ] Assert the result and the number and arguments of provider calls.
- [ ] Run the test and observe a meaningful failure before implementing the behavior.

Checkpoint — You can describe what the failing test requires.

### Step 4 — Implement service behavior

- [ ] Implement input guards and a single delegated payment call.
- [ ] Add the invalid-input and provider-failure tests.
- [ ] Keep Spring context startup out of these unit tests.

Checkpoint — Unit tests pass with the fake, without a database or provider network calls.

### Step 5 — Let Spring assemble the objects

- [ ] Implement the CARD and BANK simulations.
- [ ] Add configuration that chooses one PaymentService.
- [ ] Register OrderService and use constructor injection.
- [ ] Test both choices and the unsupported-value failure.

Checkpoint — Changing a property selects a different implementation without changing OrderService.

### Step 6 — Add the HTTP boundary

- [ ] Add the validated request DTO, controller, and response DTO.
- [ ] Reuse the existing field validation handler.
- [ ] Retain the existing HTTP Basic security policy.
- [ ] Add HTTP tests with security filters enabled.

Checkpoint — Check status, response fields, validation, and authentication through MockMvc.

### Step 7 — Explain and submit evidence

- [ ] Run the challenge tests and record the exact command and result.
- [ ] Run the full suite with its database prerequisite satisfied.
- [ ] Record any remaining baseline failure separately. Do not label a failing full suite as passing.
- [ ] Demonstrate a request under each provider configuration.
- [ ] Explain the six concepts below in your own words.

## Tests to write

These are test specifications, not implemented or executed tests.
Mirror the new packages under `src/test/java/com/ahmed/springintensive`.

| Test class | Cases and assertions | Criteria |
| --- | --- | --- |
| OrderServiceTest | Valid 49.90 payment returns fake result and records exactly one call with ORD-1001 and the amount | AC1, AC5 |
| OrderServiceTest | Null/empty/blank orderId and each invalid amount throw IllegalArgumentException with zero calls | AC2–AC4 |
| OrderServiceTest | 0.01 and 99999999.99 are accepted. 100000000 and 1.001 are rejected | AC3, AC4 |
| OrderServiceTest | Provider throws a chosen RuntimeException and the same exception escapes | AC8 |
| PaymentProviderTest | CARD and BANK preserve valid input, use EUR and PAID, and identify their provider | AC1, AC7 |
| PaymentWiringTest | A small Spring context with only checkout configuration/service selects CARD by default, BANK when configured, and fails for an unsupported value | AC6, AC7 |
| CheckoutControllerTest | Authenticated valid request returns 200 and all six fields | AC1 |
| CheckoutControllerTest | Each invalid/missing/null field returns 400, with the relevant field key, and no service invocation | AC2–AC4 |
| CheckoutControllerTest | Malformed JSON returns 400. Missing authentication returns 401 and no service invocation | AC9 |

For OrderServiceTest, use plain JUnit and a recording fake. Mockito is optional.
For PaymentWiringTest, use a small context instead of loading the whole application.
For CheckoutControllerTest, use an MVC slice, import the existing SecurityConfig and GlobalExceptionHandler, and replace OrderService with a test double.
Supply test-only authentication support required by that security slice. If using Spring Security test helpers, add `org.springframework.security:spring-security-test` with test scope and the version managed by the project.
Do not disable filters to make the 401 test pass. Use the testing APIs supplied by the project's Boot version rather than copying imports from a Boot 3 tutorial.

Commands from the repository root after the named tests exist

Windows PowerShell

```powershell
.\mvnw.cmd "-Dtest=OrderServiceTest,PaymentProviderTest,PaymentWiringTest,CheckoutControllerTest" test
.\mvnw.cmd test
```

Linux or macOS

```bash
sh mvnw -Dtest=OrderServiceTest,PaymentProviderTest,PaymentWiringTest,CheckoutControllerTest test
sh mvnw test
```

Do not add disabled or empty tests as completion evidence.

## Gradual hints

Read one hint only after attempting its step. Stop once you can continue.

<details>
<summary>Hint 1 — Identify the roles</summary>

OrderService decides when to request payment. PaymentService describes the operation. CardPaymentService performs the simulated provider work. OrderService has a payment dependency. It is not a payment provider.
</details>

<details>
<summary>Hint 2 — Start from the constructor</summary>

Give OrderService one constructor parameter of type PaymentService and store it in a private final field. A unit test can supply a fake without starting Spring.
</details>

<details>
<summary>Hint 3 — Make the fake observable</summary>

Let the fake record call count, orderId, and amount. Return a known PaymentResult. Assert both the returned value and those recorded inputs.
</details>

<details>
<summary>Hint 4 — Validate before delegation</summary>

Check null before accessing values. Use isBlank for the identifier and BigDecimal comparison for positivity. Enforce the same integer/fraction digit limits at the HTTP boundary and in direct service calls. Include a boundary test before writing the checks.
</details>

<details>
<summary>Hint 5 — Separate object creation from business behavior</summary>

A configuration method can choose CARD or BANK using the property and return the PaymentService interface. The implementations themselves do not both need service annotations. Object construction belongs here, not inside OrderService.
</details>

<details>
<summary>Hint 6 — Keep the controller small</summary>

Validate CheckoutRequest at the request boundary, call checkout once, and map the returned result. Use the existing validation advice for field errors. Let the security filter reject anonymous requests before controller execution.
</details>

<details>
<summary>Hint 7 — Diagnose test failures by layer</summary>

If unit tests fail, inspect guards and delegation. If wiring fails, inspect bean count and property selection. If HTTP tests fail, inspect authentication, validation, and JSON mapping. A PostgreSQL connection failure in a full context does not prove the payment logic is wrong.
</details>

## Explain what you built

| Concept from the notes | Evidence to point to |
| --- | --- |
| Class and object | OrderService definition and the instance constructed in a test |
| Interface | PaymentService contract shared by providers and the fake |
| Composition | OrderService's PaymentService field |
| Dependency injection | Constructor parameter supplied by a test or Spring |
| Dependency inversion | OrderService depends on the consumer-owned interface |
| Polymorphism | The same pay call runs CARD, BANK, or fake behavior |

Explain what would change if you added a third provider, and what would stay in OrderService.
Then explain how the same separation could support a future AI summarisation provider. Do not add an AI SDK, RAG pipeline, database table, frontend, or deployment in this challenge.

## Completion record

Fill this in only after implementation.

- Implementation commit — pending
- Focused test command and result — pending
- Full-suite command and result — pending
- CARD and BANK demonstration — pending
- Explanation of the six concepts — pending

Next action — Begin Step 1, then define PaymentResult and PaymentService. Leave the remaining boxes unchecked until you produce their evidence.
