# JVM Basics

This note explains how a Java program like LearnTrack goes from source code to a running application, and what happens inside the Java Virtual Machine (JVM) while it runs.

## 1. JDK vs JRE vs JVM

```
┌──────────────────────────── JDK ────────────────────────────┐
│  Development tools: javac, java, jar, javadoc, jdb ...       │
│  ┌──────────────────────── JRE ──────────────────────────┐   │
│  │  Standard class libraries (java.lang, java.util ...)   │   │
│  │  ┌──────────────────── JVM ──────────────────────┐     │   │
│  │  │  Class loader, memory areas, execution engine  │     │   │
│  │  └────────────────────────────────────────────────┘     │   │
│  └────────────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

| Term | What it is | Used for |
|---|---|---|
| **JVM** (Java Virtual Machine) | An abstract machine that loads and executes bytecode (`.class` files). | *Running* Java programs. |
| **JRE** (Java Runtime Environment) | JVM + the standard libraries (`ArrayList`, `Scanner`, `LocalDate`, …). | Everything needed to *run* a Java app. |
| **JDK** (Java Development Kit) | JRE + development tools such as the `javac` compiler. | *Writing and compiling* Java apps. |

To build LearnTrack you need the **JDK**, because `javac` is only shipped with the JDK. Since Java 11, vendors usually ship only the JDK and no longer offer a separate JRE download.

## 2. From source code to a running program

```
 Main.java, Student.java, ...         (source code, human readable)
        │
        │  javac -d out ...            compile time
        ▼
 Main.class, Student.class, ...       (bytecode, platform independent)
        │
        │  java -cp out com.airtribe.learntrack.UI.Main     run time
        ▼
 ┌──────────────────── JVM ────────────────────┐
 │ 1. Class Loader   → loads .class files      │
 │ 2. Bytecode Verifier → checks they are safe │
 │ 3. Execution Engine → interprets / JIT      │
 └──────────────────────────────────────────────┘
        │
        ▼
 Machine code running on Windows / macOS / Linux
```

1. **Compile.** `javac` checks the code for errors (types, missing methods, unhandled checked exceptions such as `EntityNotFoundException`) and turns each class into a `.class` file containing **bytecode**.
2. **Run.** `java` starts a JVM, which finds `com.airtribe.learntrack.UI.Main` on the classpath (`-cp out`) and calls its `public static void main(String[] args)`.

### "Write Once, Run Anywhere"
Bytecode is not tied to any operating system or CPU. The same `out` folder compiled on Windows can run unchanged on macOS or Linux, as long as a JVM is installed there. The JVM itself is platform-specific; the bytecode is not.

## 3. Inside the JVM

### 3.1 Class Loader
Classes are loaded **lazily**, the first time they are needed, not all at startup.

- **Bootstrap class loader:** loads core Java classes (`java.lang.String`, `java.util.ArrayList`, …).
- **Platform class loader:** loads other platform modules.
- **Application class loader:** loads our classes from the classpath (`out/com/airtribe/learntrack/...`).

For example, `EnrollmentService` is loaded when `main` executes `new EnrollmentService(...)`.

After loading, the class goes through **linking** (bytecode verification, memory for static fields, symbolic reference resolution) and **initialization**, where static fields get their values. For `IdGenerator`, this is when `sid`, `cid` and `eid` are set to `1`.

### 3.2 Runtime Memory Areas

| Area | Shared? | What is stored | LearnTrack example |
|---|---|---|---|
| **Heap** | Shared by all threads | All objects and arrays | Every `Student`, `Course`, `Enrollment` object, the `ArrayList`s inside the services, the `Scanner`. |
| **Method Area / Metaspace** | Shared | Class metadata, method bytecode, **static variables**, runtime constant pool | Class info for `Student`, `Course`, …; the static counters `IdGenerator.sid/cid/eid`. |
| **Stack** (one per thread) | Per thread | A *frame* per method call holding local variables, parameters and references | When `addStudent()` runs, its frame holds `fname`, `lname`, `email`, `batch` and the `student` reference. The frame is removed when the method returns. |
| **PC Register** | Per thread | Address of the current bytecode instruction | Tracks where execution is inside the current method. |
| **Native Method Stack** | Per thread | Frames for native (non-Java) code | Used internally, e.g. when `System.out.println` writes to the console. |

Example: `Student s = new Student(1, "Asha", "Rao", "B1");`
- The **`Student` object** is created on the **heap**.
- The **reference `s`** is a local variable in the current **stack frame** and points to that heap object.
- When the student is added to `students`, the `ArrayList` (also on the heap) stores another reference to the same object.

### 3.3 Execution Engine
- **Interpreter:** reads and executes bytecode one instruction at a time. It starts quickly but runs slower.
- **JIT (Just-In-Time) compiler:** detects "hot" code that runs many times (e.g. the menu loop or `readInt`) and compiles it to native machine code for much faster execution.
- **Garbage Collector:** see below.

## 4. Garbage Collection

Java has no manual `free()` or `delete`. The **Garbage Collector (GC)** automatically frees heap memory used by objects that are no longer **reachable** from any live reference.

- In LearnTrack, students, courses and enrollments are always reachable through the services' `ArrayList`s, so they live until the program exits.
- Short-lived objects, such as the temporary `List<Enrollment>` built in `getEnrollmentsForStudent()` or the strings read from `Scanner`, become unreachable after the method returns and are collected.
- The heap is divided into a **Young Generation**, where new objects are created and most die quickly, and an **Old Generation** for long-lived objects. Collecting the young generation often and cheaply keeps GC pauses short.

## 5. Key Takeaways

- `javac` (JDK) compiles `.java` to platform-independent **bytecode**; `java` starts the **JVM** to run it.
- The JVM **loads classes on demand**, **verifies** them, then **interprets/JIT-compiles** them.
- **Objects live on the heap, local variables and references live on the stack, and static variables live in the method area.**
- Memory is reclaimed automatically by the **garbage collector** once objects are unreachable.
