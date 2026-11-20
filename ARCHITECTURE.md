# InterviewIQ – Technical Architecture & Software Design

## 1. Architectural Overview

**InterviewIQ** is designed following a **Layered Architecture** adhering to clean code and Object-Oriented principles (SOLID, encapsulation, separation of concerns). The business, persistence, and evaluation logic are decoupled from the graphical presentation layer.

```
+-------------------------------------------------------------------+
|                        Presentation Layer                         |
|  MainFrame | Sidebar | TopBar | 9 UI Panels | Custom Charts/Cards |
+-------------------------------------------------------------------+
                                  |
                                  v
+-------------------------------------------------------------------+
|                           Service Layer                           |
|   QuestionService | AnalyticsService | HistoryService | ThemeSvc  |
+-------------------------------------------------------------------+
                                  |
                                  v
+-------------------------------------------------------------------+
|                            Core Engine                            |
|   QuizEngine | InterviewEngine | AssessmentEngine | ScoreCalc     |
+-------------------------------------------------------------------+
                                  |
                                  v
+-------------------------------------------------------------------+
|                         Repository Layer                          |
|   QuestionRepository | JsonAttemptRepository | PreferencesRepo    |
+-------------------------------------------------------------------+
                                  |
                                  v
+-------------------------------------------------------------------+
|                     Local JSON Storage / File I/O                 |
|   user_preferences.json | attempt_history.json | question_state   |
+-------------------------------------------------------------------+
```

---

## 2. Package Organization

```
com.interviewiq/
|-- Main.java                     # Application bootstrap & entry point
|
|-- model/                        # Domain entities & Value Objects
|   |-- AssessmentType.java       # QUIZ, MOCK_INTERVIEW, CODING, PRACTICE
|   |-- AttemptRecord.java        # Unified persistent attempt entity
|   |-- CodingChallenge.java      # Algorithmic challenge model
|   |-- Difficulty.java           # EASY, MEDIUM, HARD
|   |-- Question.java             # Technical question bank item
|   |-- QuestionCategory.java     # 8 core CS domains
|   |-- QuestionState.java        # User bookmarks & completion state
|   |-- QuizQuestion.java         # 4-option MCQ question model
|   |-- TestCase.java             # Input/Output validation cases
|   |-- TestResult.java           # Evaluation outcome per test case
|   |-- TopicProgress.java        # Topic completion metrics
|   |-- UserPreferences.java      # Saved user settings
|
|-- core/                         # Core execution & scoring engines
|   |-- AssessmentEngine.java     # Safe deterministic code test runner
|   |-- InterviewEngine.java      # Mock interview state & rule evaluator
|   |-- QuizEngine.java           # MCQ test runner & palette manager
|   |-- ScoreCalculator.java      # Accuracy, grading, and readiness logic
|
|-- service/                      # Business & application services
|   |-- AnalyticsService.java     # Real-time metrics from saved history
|   |-- HistoryService.java       # Attempt history filtering & demo data
|   |-- QuestionService.java      # Search, filter, and state management
|   |-- ThemeService.java         # FlatLaf Light/Dark live theme engine
|
|-- repository/                   # Persistence & storage layer
|   |-- AttemptRepository.java    # Data access interface
|   |-- DefaultDataBank.java      # 80+ technical questions & challenges
|   |-- JsonAttemptRepository.java# Jackson JSON history store
|   |-- PreferencesRepository.java# Local preferences JSON store
|   |-- QuestionRepository.java   # Question bank & bookmark store
|
|-- ui/                           # Graphical User Interface (Swing)
|   |-- MainFrame.java            # Primary window with CardLayout
|   |-- components/               # Reusable styled UI widgets
|   |   |-- BadgeLabel.java       # Pill badges for categories & levels
|   |   |-- ModernButton.java     # Hover-animated styled buttons
|   |   |-- ModernCard.java       # Rounded card container
|   |   |-- Sidebar.java          # 9-module navigation bar
|   |   |-- StatCard.java         # KPI metric display card
|   |   |-- TopBar.java           # Breadcrumb & live theme switcher
|   |   |-- TopicPerformanceChart.java # Java 2D horizontal progress bars
|   |   |-- WeeklyActivityChart.java   # Java 2D 7-day bar chart
|   |-- panels/                   # Functional module views
|       |-- AnalyticsPanel.java
|       |-- CodingAssessmentPanel.java
|       |-- DashboardPanel.java
|       |-- HistoryPanel.java
|       |-- InterviewPrepPanel.java
|       |-- MockInterviewPanel.java
|       |-- PracticeArenaPanel.java
|       |-- QuizArenaPanel.java
|       |-- SettingsPanel.java
|
|-- util/                         # Constants, Colors & Formatting
    |-- AppConstants.java         # App metadata, fonts, file paths
    |-- ColorScheme.java          # Exact hex palettes for Light/Dark
    |-- DateTimeUtils.java        # Formats timestamps & durations
    |-- ValidationUtils.java      # Input validation utilities
```

---

## 3. Core Subsystems

### A. Dynamic Theme Switching
- Integrated with **FlatLaf 3.5.4**.
- `ThemeService` tracks theme state, installs `FlatLightLaf` or `FlatDarkLaf`, and calls `FlatLaf.updateUI()`.
- Dispatches callbacks to registered `ThemeChangeListener` components to repaint custom Java 2D widgets (such as `WeeklyActivityChart` and `TopicPerformanceChart`).
- Persists theme selection to `user_preferences.json` and restores on launch without requiring an application restart.

### B. Assessment & Evaluation Engine
- **QuizEngine**: Manages timed multiple-choice sessions, tracks answers, flag states, and provides question palette navigation.
- **InterviewEngine**: Simulates realistic mock interviews with countdown timers, candidate answer inputs, and keyword/structure heuristic evaluation.
- **AssessmentEngine**: Evaluates coding submissions against test suites deterministically, measuring execution time in milliseconds and computing passing ratios.

### C. JSON Persistence
- Backed by **Jackson 2.18.2** with `JavaTimeModule`.
- Fault-tolerant file handling: missing directories, corrupt files, or non-writable paths automatically fall back to local safe storage.
