# 🌀 Mini Spring Clone - Lightweight Java Web Framework 

A minimalistic web framework inspired by Spring, allowing developers to map HTTP requests to controller methods and define dependencies, which are automatically injected and resolved at runtime.

The framework handles HTTP communication via sockets, encapsulates request and response data into Java objects, and implements routing and dependency injection mechanisms using metaprogramming — through annotations, reflection, and recursion.



### Features

* Annotation-Based Routing System — `@Controller`, `@GET`, `@POST`, `@Path`
* Dependency Injection Engine — `@Autowired`, `@Qualifier`, `@Bean`, `@Component`, `@Service`
* Lifecycle Support — Singleton & Prototype bean scopes
* Recursive Injection — Supports deep dependency trees
* Reflection-Based Implementation — No external libraries used
* Encapsulated HTTP Request/Response — Object-based abstractions for clarity and reusability
* Console Logging for DI — Enabled with `@Autowired(verbose = true)`



### Example Controller



```java
@Controller
public class ProfessorController {

    @Autowired(verbose = true)
    private Professor professor;

    @Qualifier("professorImpl")
    @Autowired
    private ProfessorInterface professorInterface;

    //...

    @GET
    @Path(path = "/professors")
    public Response getProfessors() {
        return new JsonResponse(professors);
    }

    @POST
    @Path(path = "/professors")
    public Response createProfessor(Map<String, String> parameters) {
        // Handle creation logic
    }

    // ...
}
```

### How To Use 

* Run the project from the root directory (e.g., directly from your IDE)
* The server listens on localhost:8080 
* POST request body must follow this format: param1=value1&param2=value2
* To enable console logging during DI, set `verbose = true` in the `@Autowired` annotation
* Example: To call a method mapped to /professors, send:


    GET http://localhost:8080/professors

### How It Works 

1. Framework Initialization
   * Scans all .java files in src/main/java
   * Detects all classes annotated with `@Controller`
   * Maps controller methods using `@Path` and `@GET` / `@POST`
   * Builds the dependency container from `@Service`, `@Bean`, `@Component` and `@Qualifier` annotations


2. Dependency Injection
   * Recursively resolves all dependencies
   * Supports both singleton and prototype lifecycles
   * Uses reflection to inject objects into fields
   * Resolves interface bindings via `@Qualifier` 
   

3. Request Lifecycle
    * Reads and parses raw socket input
    * Extracts HTTP method, path, headers and parameters
    * Matches route to controller method
    * Calls the method using reflection
    * Serializes the returned Response object and sends it as raw HTTP

