# InterviewIQ – User Guide

Welcome to **InterviewIQ – Smart Mock Interview & Assessment Platform**! This guide explains how to navigate, practice, and track your software engineering preparation.

---

## 1. Getting Started

### System Requirements
- **Operating System:** Windows 10/11, macOS, or Linux.
- **Java Runtime:** Java 17 or newer (OpenJDK / Oracle JDK / JetBrains Runtime).
- **RAM:** Minimum 512 MB free.
- **Display Resolution:** Recommended 1366 x 850 or higher.

### Launching the Application
- **Windows (Double-Click):** Run `run.bat` or `run.ps1`.
- **Command Line:**
  ```cmd
  java -jar target/interviewiq-1.0.0.jar
  ```
- **From Source with Maven:**
  ```cmd
  mvnw.cmd clean package
  run.bat
  ```

---

## 2. Navigating the 9 Modules

### 1. Dashboard
- **Welcome Banner:** Personalized greeting, study quote, and quick links to start practice.
- **KPI Metrics:** Track total attempts, average accuracy, solved coding challenges, and mock interview count.
- **Charts:** View the 7-day preparation activity chart and topic mastery progress bars.
- **Recent Activity:** Quick view of your most recent attempts.
- **Demo Data Toggle:** Click "Demo Data" in the top banner or Settings to load realistic sample attempts for demonstration purposes, or clear them anytime.

### 2. Interview Prep (Question Bank)
- **Category Filter:** Filter across 8 major domains (Java, Python, DSA, OOP, DBMS, OS, Networks, Software Engineering).
- **Search & Difficulty:** Live search questions by keywords or tags, and filter by Easy, Medium, or Hard.
- **Study Mode:** Click "Reveal Model Answer" to inspect model answers, in-depth explanations, and code examples.
- **Bookmarks & Completion:** Click "Mark as Understood" or "Bookmark" to persist your preparation status.

### 3. Practice Arena
- **Flashcard Drill:** Rapidly cycle through technical questions for immediate recall practice.
- **Topic Completion:** View your topic completion progress bar and percentage.
- **Shuffle:** Click "Shuffle" to test yourself in randomized order.

### 4. Coding Assessment
- **Problem Bank:** Solve standard interview challenges:
  - Reverse an Array
  - Two Sum
  - Valid Palindrome
  - Maximum Subarray Sum (Kadane's)
  - Move Zeroes
  - Binary Search
  - Container With Most Water
  - Longest Substring Without Repeating Characters
  - Valid Anagram
- **Solution Editor:** Built-in Java editor with line numbers and monospace font.
- **Run Tests:** Click "Run Tests" to evaluate your code against sample test cases and inspect the execution time.
- **Submit Solution:** Click "Submit Solution" to record the score in your history and analytics.

### 5. Mock Interview
- **Setup Wizard:** Configure interview domain, difficulty, question count (3, 5, 8, 10), and time limit.
- **Live Interview:** Countdown timer warns you at 3 minutes (orange) and 1 minute (red). Type your structured answer including definitions, mechanisms, examples, and trade-offs.
- **Question Palette:** Jump between questions or flag questions for review.
- **Evaluation Report:** Rule-based feedback checks keyword coverage, depth, and structural elements, comparing your submission with model answers.

### 6. Quiz Arena
- **Timed MCQ:** Multiple-choice assessment with single-select answers.
- **Palette Navigation:** Color-coded buttons show answered (green), unanswered (white/card), and flagged (yellow) questions.
- **Results & Review:** Immediate score report with detailed explanations for every correct and incorrect answer.

### 7. Analytics
- **Readiness Score:** Tracks your cumulative preparation metrics.
- **Activity Trends:** Weekly distribution of study activity.
- **Strengths & Weaknesses:** Identifies your highest scoring topics and subjects that require revision.

### 8. History
- **Search & Filter:** Filter by Assessment Type (Quiz, Mock Interview, Coding, Practice) or sort by date, score, or duration.
- **View Breakdown:** Double-click any row or click "View Breakdown" to inspect the detailed question log and answers.
- **Clear History:** Reset history records with confirmation.

### 9. Settings
- **User Profile:** Change your display name, target engineering role, and daily problem goal.
- **Theme Selection:** Switch between Light Theme and Dark Theme with immediate live preview.
- **Data Tools:** Load sample demo data, export, or reset progress.
