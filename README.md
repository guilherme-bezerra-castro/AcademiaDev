# AcademiaDev — Plataforma de Cursos Online
 
Protótipo de linha de comando desenvolvido em Java puro, seguindo estritamente os princípios da **Clean Architecture**. Desenvolvido para avaliação
 
---
 
## Como executar
 
### No Eclipse
1. Importe o projeto: `File → Import → Existing Projects into Workspace`
2. Selecione a pasta `AcademiaDev`
3. Clique com o botão direito em `Main.java` → `Run As → Java Application`
4. O console integrado do Eclipse funciona como terminal
### Via linha de comando
```bash
# Compilar (a partir da raiz do projeto)
javac -d out $(find src -name "*.java")
 
# Executar
java -cp out com.academiadev.main.Main
```
 
### Usuários pré-cadastrados para login
| E-mail                    | Perfil  | Plano   |
|---------------------------|---------|---------|
| admin@academiadev.com     | Admin   | —       |
| lucas@email.com           | Student | BASIC   |
| mariana@email.com         | Student | PREMIUM |
| pedro@email.com           | Student | BASIC   |
 
---

## Justificativas de Design
 
### 1. Como a Regra da Dependência foi seguida
 
A Clean Architecture determina que as dependências de código-fonte só podem apontar para dentro, ou seja, em direção às políticas de mais alto nível. Neste projeto, a hierarquia é:
 
```
infrastructure -> application -> domain -> main -> todos os outros
```
 
**Domain:** não importa nenhuma classe de `application` ou `infrastructure`. Verificação direta: nenhum arquivo em `domain/` contém `import com.academiadev.application` ou `import com.academiadev.infrastructure`. O domain depende apenas de `java.time` e `java.lang`, da própria JDK.
 
**Application:** importa apenas `domain`. As interfaces de repositório (`CourseRepository`, `UserRepository`, etc.) vivem em `application.repositories` e definem contratos em termos de entidades do domain. Os Use Cases recebem essas interfaces por injeção de dependência via construtor, isto é, nunca instanciam implementações concretas. Nenhum arquivo em `application/` contém `import com.academiadev.infrastructure`.
 
**Infrastructure:** conhece `application` (implementa suas interfaces) e `domain` (manipula suas entidades). É a única camada que sabe que os dados estão em `HashMap`, que a fila é um `ArrayDeque`, e que existe um `Scanner` de console.
 
**Main:** é a única classe que importa todas as camadas — é seu papel exclusivo. Nenhuma outra classe da aplicação tem essa visão global.
 
---
 
### 2. Como a Injeção de Dependência foi feita em Main.java
 
A injeção de dependência foi feita via construtor, sem frameworks como Spring. O `Main.java` segue uma ordem deliberada de instanciação:
 
```java
// 1 - Infraestrutura concreta (implementações)
CourseRepository courseRepo = new CourseRepositoryInMemory();
UserRepository   userRepo   = new UserRepositoryInMemory();
// ...
 
// 2 - Use Cases recebem interfaces, nunca implementações
EnrollStudentUseCase enrollUseCase =
    new EnrollStudentUseCase(courseRepo, enrollRepo);
//                            ↑ tipo declarado é a interface
//                              a implementação concreta é injetada aqui
 
// 3 - Controller recebe use cases
ConsoleController controller =
    new ConsoleController(scanner, view, userRepo, csvExporter, enrollUseCase, cancelUseCase, ...);
```
 
Isso garante que `EnrollStudentUseCase`, por exemplo, não sabe se os dados estão em um `HashMap`, em um banco PostgreSQL ou em um arquivo XML. Ele programa contra a interface `CourseRepository`. Para trocar a persistência por um banco real no futuro, basta criar `CourseRepositoryJpa implements CourseRepository` e alterar apenas o `Main.java`, garantido que não haja impacto nos Use Cases.
 
---
 
### 3. Como a camada Domain foi mantida pura
 
Três decisões de design garantem a pureza do domain:
 
**a) Entidades sem dependência de frameworks:** `Course`, `Student`, `Enrollment` e as demais entidades são POJOs (Plain Old Java Objects) puros. Não há annotations de JPA, Jackson, Lombok ou qualquer biblioteca externa.
 
**b) Lógica de negócio dentro das entidades:** O método `student.canEnroll(activeEnrollments)` em `Student` e o método `enrollment.updateProgress(percent)` em `Enrollment` contêm regras de negócio que pertencem ao domain. A validação `0 <= progress <= 100` lança `BusinessException`, que também fica no domain. Isso evita que a lógica de negócio vaze para os UseCases ou para a UI.
 
**c) Repositórios como abstrações no domain/application:** O domain nunca sabe como os dados são persistidos. As interfaces de repositório estão em `application.repositories` e definem contratos em termos de entidades do domain. As implementações concretas (`*InMemory`) ficam em `infrastructure.persistence` e o domain não as vê.
 
---
 
### 4. Como os detalhes foram isolados na camada Infrastructure
 
**Persistência em memória.** As quatro implementações de repositório (`CourseRepositoryInMemory`, `UserRepositoryInMemory`, `EnrollmentRepositoryInMemory`, `SupportTicketQueueInMemory`) encapsulam completamente as estruturas de dados:
 
- `Map<String, Course>` para cursos — chave é o `title`, garantindo unicidade sem lógica extra
- `Map<String, User>` para usuários — chave é o `email`, garantindo unicidade
- `List<Enrollment>` para matrículas — busca linear; suficiente para um protótipo
- `ArrayDeque<SupportTicket>` para a fila — garante FIFO por contrato da estrutura
Nenhuma dessas estruturas é conhecida fora de `infrastructure.persistence`. Se a persistência mudar para PostgreSQL, por exemplo, os Use Cases e o Domain não precisam ser tocados.
 
**CSV com Reflection.** A classe `GenericCsvExporter` usa `java.lang.reflect.Field` para extrair dinamicamente os atributos de qualquer objeto. Isso é um detalhe de "framework" (usa a API de Reflection da JVM) e por isso reside em `infrastructure.utils`. O fluxo respeitado é:
 
```
ConsoleController
-> chama ReportUseCase.allCourses()   (obtém List<Course>)
-> passa para GenericCsvExporter      (serializa para CSV)
```
 
Os UseCases nunca importam `GenericCsvExporter`. A camada `application` permanece completamente ignorante de como os dados são formatados para saída.
 
**UI de console.** `ConsoleView` só imprime (nunca lê input e nunca contém lógica condicional de negócio). `ConsoleController` lê o input, traduz para chamadas de UseCase e trata exceções exibindo mensagens amigáveis. Essa separação garante que trocar o console por uma interface web exigiria apenas uma nova implementação de controller e view — os UseCases permaneceriam intocados.
 
---

## Contexto do projeto
> Este projeto foi desenvolvido como trabalho avaliativo da disciplina de Engenharia de Software no curso de Análise e Desenvolvimento de Sistemas (IFSP).

---
 
## Ferramentas e Tecnologias
 
- **Java 17** — uso de `switch` com arrow, `List.of()`, `var`, `Stream API`, `Optional`
- **Collections Framework** — `HashMap`, `ArrayDeque`, `ArrayList`
- **Stream API** — todos os relatórios implementados com streams em `ReportUseCase`
- **Reflection API** — `GenericCsvExporter` em `infrastructure.utils`
- **Nenhuma dependência externa** — projeto compilável com JDK puro, sem Maven ou Gradle
