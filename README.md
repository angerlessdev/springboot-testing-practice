# springboot-testing-practice

Practice project to learn and apply **JUnit 5**, **Mockito**, and testing in **Spring Boot**.  
Includes examples of **unit tests** and **integration tests** with repositories, services, and custom exceptions.

## 🚀 Technologies
- Java 21
- Spring Boot
- JUnit 5
- Mockito
- Maven

## 📂 Project Structure
- `src/main/java/org/angel/test/springboot/app/models` → Entities (`Account`, `Bank`)
- `src/main/java/org/angel/test/springboot/app/exceptions` → Custom exception (`InsufficientFundsException`)
- `src/main/java/org/angel/test/springboot/app/repositories` → Repository interfaces (`AccountRepository`, `BankRepository`)
- `src/main/java/org/angel/test/springboot/app/services` → Business logic (`AccountService`, `AccountServiceImpl`)
- `src/main/java/org/angel/test/springboot/app` → Utility data (`Data`)
- `src/test/java/org/angel/test/springboot/app` → Unit and integration tests with **Mockito**

## 🧪 Running Tests
To run all tests:
```bash
mvn test
```

## 📖 Features
- Unit testing with **JUnit 5**
- Mocking dependencies with **Mockito**
- Verification of repository interactions
- Custom exception handling (`InsufficientFundsException`)
- Integration testing with real repositories
- Examples of mocking, argument matchers, and test doubles
- 
## 📄 License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
