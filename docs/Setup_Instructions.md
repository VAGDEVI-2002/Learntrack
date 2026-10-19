# Setup Instructions

This guide walks through setting up a machine to compile and run LearnTrack from scratch.

## 1. Install the JDK

LearnTrack needs a **Java Development Kit (JDK) 8 or later**. It was developed and tested with **JDK 21** (Amazon Corretto 21).

> A JRE alone is not enough. You need the JDK because it includes the `javac` compiler.

Any of these distributions works:
- [Amazon Corretto](https://aws.amazon.com/corretto/)
- [Eclipse Temurin (Adoptium)](https://adoptium.net/)
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

### Windows
1. Download the `.msi` installer for JDK 21 (x64) and run it.
2. On the installer screen, enable the options to **set `JAVA_HOME`** and **add to `PATH`** if they are offered.
3. If the installer doesn't set them, set them manually:
   - Open **Start → "Edit the system environment variables" → Environment Variables**.
   - Under *System variables*, click **New**: name `JAVA_HOME`, value = the JDK folder (e.g. `C:\Program Files\Amazon Corretto\jdk21.x.x_x`).
   - Edit the `Path` variable and add `%JAVA_HOME%\bin`.
4. Close and reopen any terminal windows so they pick up the new variables.

### macOS
```bash
brew install --cask corretto@21     # or download the .pkg installer from the vendor site
```

### Linux (Debian/Ubuntu)
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

## 2. Verify the installation

Open a new terminal and run:

```
java -version
javac -version
```

Both commands should print a version number, for example:

```
openjdk version "21.0.x" ...
javac 21.0.x
```

If either command says *"not recognized"* or *"command not found"*, the JDK `bin` folder is not on your `PATH`. Go back to step 1.

## 3. Get the project

Copy or clone the project so you have this folder layout:

```
LearnTrack/
├── README.md
├── docs/
└── src/com/airtribe/learntrack/...
```

Open a terminal **inside the `LearnTrack` folder**. All commands below are run from there.

## 4. Compile

**Windows (PowerShell)**
```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
```

**macOS / Linux / Git Bash**
```bash
javac -d out $(find src -name "*.java")
```

This compiles every `.java` file under `src` and writes the `.class` files into a new `out` folder, keeping the package folder structure (`out/com/airtribe/learntrack/...`). No output means compilation succeeded.

## 5. Run

```
java -cp out com.airtribe.learntrack.UI.Main
```

- `-cp out` tells the JVM to look for classes in the `out` folder.
- `com.airtribe.learntrack.UI.Main` is the fully qualified name of the class containing `main`.

You should see the main menu:

```
========LearnTrack==========
1. Student Management
2. Course Management
3. Enrollment Management
0. Exit
Enter your choice:
```

Enter `0` to exit.

## 6. (Optional) Run from IntelliJ IDEA

1. **File → Open** and select the `LearnTrack` folder.
2. Go to **File → Project Structure → Project** and choose your installed JDK as the **SDK**.
3. Make sure `src` is marked as a sources root (it appears blue). If not, right-click `src` → **Mark Directory as → Sources Root**.
4. Open `src/com/airtribe/learntrack/UI/Main.java` and click the green **Run** icon next to `main`.
5. Interact with the program in the **Run** window at the bottom.

## 7. Troubleshooting

| Problem | Cause / Fix |
|---|---|
| `'javac' is not recognized...` / `command not found` | JDK not installed, or its `bin` folder is not on `PATH`. See step 1, then open a new terminal. |
| `Error: Could not find or load main class com.airtribe.learntrack.UI.Main` | Run the command from the `LearnTrack` folder, use `-cp out`, and use the full class name including the package. Recompile if the `out` folder is missing. |
| `error: file not found: src\...` | You are not in the `LearnTrack` folder. `cd` into it first. |
| `UnsupportedClassVersionError` | The code was compiled with a newer JDK than the one running it. Make sure `java -version` and `javac -version` show the same version. |
| IntelliJ shows red errors on every import | No SDK selected, or `src` isn't a sources root. See step 6. |
