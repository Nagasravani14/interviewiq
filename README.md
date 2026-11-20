# InterviewIQ – Smart Mock Interview & Assessment Platform

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://openjdk.org/)
[![UI](https://img.shields.io/badge/UI-FlatLaf%203.5.4-indigo.svg)](https://www.formdev.com/flatlaf/)
[![Build](https://img.shields.io/badge/Build-Maven%203.9-orange.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-22%20Passed-brightgreen.svg)]()
[![License](https://img.shields.io/badge/License-Permissive-green.svg)](LICENSE)

**InterviewIQ** is a comprehensive, production-grade desktop application built with Java Swing and FlatLaf. Designed for engineering students and aspiring software developers, it simulates technical interviews, conducts timed MCQ assessments, evaluates algorithmic challenges against deterministic test suites, and tracks preparation analytics across core computer science subjects.

---

## Highlights & Key Capabilities

- **Genuine Java Desktop Application:** Pure Java 17+ desktop solution using Java Swing, FlatLaf modern UI styling, Jackson JSON persistence, and Java 2D graphics.
- **9 Fully Functional Modules:**
  1. **Dashboard:** Welcome banner, KPI cards, weekly activity bar chart, topic accuracy progress, and recent attempts.
  2. **Interview Prep:** Searchable question bank featuring **80+ in-depth technical questions** across 8 core domains.
  3. **Practice Arena:** Fast flashcard drill mode with topic completion percentages.
  4. **Coding Assessment:** 9 standard programming challenges with Java code editor, line numbers, and deterministic test suite execution.
  5. **Mock Interview:** Realistic timed technical interview simulation with candidate answer text areas and rule-based heuristic scoring.
  6. **Quiz Arena:** Timed MCQ assessments with question navigation palette, review flags, and detailed answer explanations.
  7. **Analytics:** Live metrics derived from actual saved attempts, identifying skill strengths and focus areas.
  8. **History:** Searchable, filterable attempt history log with detailed attempt inspector dialogs.
  9. **Settings:** Profile customization, Light/Dark theme switching, assessment preferences, and data persistence tools.
- **Dynamic Light & Dark Themes:** Seamless live theme toggling without restarting the application, adhering to SaaS design palettes.
- **Local JSON Persistence:** All user preferences, bookmarks, question completion states, and attempt histories survive restarts.
- **Comprehensive Test Suite:** 22 automated JUnit 5 tests covering all core scoring engines, repositories, and UI navigation.

---

## Visual Design & Theme Palettes

### Light Theme
- **Background:** `#F6F8FC`
- **Sidebar:** `#FFFFFF`
- **Primary Accent:** `#315EFB`
- **Cards & Surfaces:** `#FFFFFF`
- **Borders:** `#E5EAF2`
- **Success / Warning / Danger:** `#16A36A` / `#E9A23B` / `#E05252`
- **Text:** `#172033`

### Dark Theme
- **Background:** `#0C1220`
- **Sidebar:** `#111A2B`
- **Primary Accent:** `#6485FF`
- **Cards & Surfaces:** `#172338`
- **Borders:** `#26364F`
- **Success / Warning / Danger:** `#36C98F` / `#F5B94C` / `#F17878`
- **Text:** `#F1F5FB`

---

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17+ (Compatible with OpenJDK 17, 21, and 25) |
| UI Framework | Java Swing + FlatLaf 3.5.4 (Light & Dark look and feels) |
| Build Tool | Apache Maven 3.9.9 (with Maven Wrapper `mvnw.cmd`) |
| Data Persistence | Jackson 2.18.2 (`jackson-databind`, `jackson-datatype-jsr310`) |
| Testing | JUnit 5.11.4 (`junit-jupiter`) |
| Packaging | Maven Shade Plugin 3.6.0 (Executable standalone fat JAR) |

---

## Project Structure

```
interviewiq/
|-- src/
|   |-- main/java/com/interviewiq/
|   |   |-- Main.java                    # Application Entry Point
|   |   |-- model/                       # Data models & Enums
|   |   |-- core/                        # Quiz, Interview & Assessment engines
|   |   |-- service/                     # Business services (Analytics, History, Theme)
|   |   |-- repository/                  # JSON persistence & 80+ Question Bank
|   |   |-- ui/                          # Swing UI, Components & 9 Module Panels
|   |   |-- util/                        # Constants, Theme Palettes & Helpers
|   |-- test/java/com/interviewiq/       # 22 automated JUnit 5 unit tests
|-- pom.xml                              # Maven project configuration
|-- mvnw.cmd                             # Maven wrapper executable script
|-- run.bat                              # One-click Windows launch script
|-- run.ps1                              # PowerShell launch script
|-- README.md                            # Main project overview
|-- USER_GUIDE.md                        # User walkthrough & feature guide
|-- ARCHITECTURE.md                      # Detailed technical architecture
|-- LICENSE                              # Permissive project license
```

---

## Build & Execution Instructions

### 1. Prerequisites
- Java Development Kit (JDK) 17 or newer installed.
- (Optional) Ensure `JAVA_HOME` points to your JDK directory.

### 2. Run Using Windows Launcher
Simply double-click `run.bat` or execute in PowerShell:
```powershell
.\run.ps1
```

### 3. Run Executable Standalone JAR
```cmd
java -jar target/interviewiq-1.0.0.jar
```

### 4. Build from Source with Maven
```cmd
# Run unit tests
.\mvnw.cmd test

# Build executable fat JAR
.\mvnw.cmd clean package
```

---

## Automated Testing

Execute the comprehensive test suite:
```cmd
.\mvnw.cmd test
```

### Test Coverage Highlights:
- **`QuizEngineTest`:** Correct/incorrect answers, timer decrements, review flags, empty and partial submissions.
- **`AssessmentEngineTest`:** Deterministic code evaluation, pass/fail status, execution time measurement.
- **`InterviewEngineTest`:** Multi-question navigation, rule-based keyword & structure heuristic evaluation, completion logs.
- **`AnalyticsServiceTest`:** Accuracy calculation, streak calculation, weekly activity grouping, topic accuracy breakdown.
- **`StorageTest`:** JSON save/reload, recovery from corrupted JSON files, question bookmark state persistence.
- **`ThemeServiceTest`:** Live theme switching, event dispatching to listeners, preference persistence.
- **`MainFrameLaunchTest`:** Headless-safe instantiation and navigation through all 9 application modules.
