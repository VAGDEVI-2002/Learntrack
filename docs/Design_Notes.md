# Design Notes

## 1. Why ArrayList instead of an array?

Each service stores its records in an `ArrayList`:

```java
// StudentService
private final ArrayList<Student> students = new ArrayList<>();
// CourseService
private final ArrayList<Course> courses = new ArrayList<>();
// EnrollmentService
private final ArrayList<Enrollment> enrollments = new ArrayList<>();
```

Reasons:

- **Unknown size.** We don't know in advance how many students, courses or enrollments the user will add. An array has a fixed size set at creation (`new Student[10]`), so we would have to guess a limit or manually create a bigger array and copy elements when it fills up. `ArrayList` grows automatically.
- **Simpler code.** `add()`, `size()`, `isEmpty()` and `remove()` are built in. With an array we would need to track a separate "count" variable and write the add and shift logic ourselves.
- **Type safety with generics.** `ArrayList<Student>` only accepts `Student` objects, so mistakes are caught at compile time.
- **Works with the enhanced for loop and the `List` interface.** Methods like `listStudents()` and `getEnrollmentsForStudent()` return a `List<...>`, so callers don't depend on the exact implementation.
- **Performance is fine for this use case.** Lookups by id are a linear scan (O(n)), which is acceptable for an in-memory console app with a small number of records.

## 2. Where static members are used and why

| Location | Static member | Why static |
|---|---|---|
| `util/IdGenerator` | `private static int sid, cid, eid` | The counters must be **shared across the whole application**, not per object. Every call to `generateStudentId()` continues from the last value, so ids stay unique no matter where they are generated. They are `private` so they can only change through the generator methods (encapsulation). |
| `util/IdGenerator` | `generateStudentId()`, `generateCourseId()`, `generateEnrollmentId()` | They only work on the static counters, so there is no need to create an `IdGenerator` object. They are called as `IdGenerator.generateStudentId()`. |
| `util/InputValidations` | `validateEmail()`, `checkNoEmpty()` | Pure helper methods: the result depends only on the input argument and no object state. Static makes them easy to reuse from anywhere (`InputValidations.validateEmail(email)`). |
| `UI/Main` | `main(String[] args)` | The JVM calls `main` before any object exists, so it must be static. |
| `UI/Main` | Menu and helper methods (`showStudentMenu`, `addStudent`, `enrollStudent`, `readInt`, `readDouble`, `readEmail`, …) | They are called directly from the static `main` method, and they don't need any instance state because the `Scanner` and services are passed in as parameters. |

**Where static is *not* used:** the service classes (`StudentService`, `CourseService`, `EnrollmentService`) are normal objects created in `main`. Their lists are instance fields, so each service owns its own data. `EnrollmentService` receives the other two services through its constructor, which keeps the dependencies explicit.

## 3. Where inheritance is used and what we gained

```
        Person
   (id, firstName, lastName, email,
    getDisplayName())
        /        \
   Student      Trainer
 (batch, active)
```

- `Person` holds the fields and methods that every person in the system has: `id`, `firstName`, `lastName`, `email`, their getters and setters, and `getDisplayName()`.
- `Student extends Person` adds only what is specific to a student: `batch` and `active`.
- `Trainer extends Person` reuses everything from `Person` as-is.
- Constructors use `super(id, firstName, lastName, email)` to let `Person` initialise the common fields.

What we gained:

- **Code reuse.** The common fields and getters/setters are written once in `Person` instead of being duplicated in `Student` and `Trainer`.
- **Easier maintenance.** A change to common behaviour (for example, adding a phone number) is made in one place and both subclasses get it.
- **Polymorphism (method overriding).** `Person.getDisplayName()` returns `"firstName lastName"`. `Student` overrides it to show more details and reuses the parent version with `super.getDisplayName()`:
  ```java
  @Override
  public String getDisplayName() {
      ...
      return getId() + " | " + super.getDisplayName() + " | " + email + " | " + batch + " | " + status;
  }
  ```
  Code that holds a `Person` reference calls the right version at runtime.
- **Clear "is-a" modelling.** A Student *is a* Person and a Trainer *is a* Person, which matches the real-world domain.

`Course` and `Enrollment` don't extend `Person` because they are not people. An enrollment *links* a student and a course by their ids (composition/association), which fits better than inheritance.
