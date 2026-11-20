package com.interviewiq.web;

/**
 * Generates the complete HTML5, CSS3, and JavaScript Single-Page Application
 * for running InterviewIQ in a web browser on localhost.
 */
public final class WebPageGenerator {
    private WebPageGenerator() {}

    public static String getHtml() {
        return "<!DOCTYPE html>\n" +
"<html lang=\"en\">\n" +
"<head>\n" +
"  <meta charset=\"UTF-8\" />\n" +
"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
"  <title>InterviewIQ – Smart Mock Interview & Assessment Platform</title>\n" +
"  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
"  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap\" rel=\"stylesheet\">\n" +
"  <style>\n" +
"    :root {\n" +
"      --bg: #F6F8FC;\n" +
"      --sidebar: #FFFFFF;\n" +
"      --cards: #FFFFFF;\n" +
"      --primary: #315EFB;\n" +
"      --primary-hover: #2347c7;\n" +
"      --secondary: #64748B;\n" +
"      --borders: #E5EAF2;\n" +
"      --success: #16A36A;\n" +
"      --warning: #E9A23B;\n" +
"      --danger: #E05252;\n" +
"      --text: #172033;\n" +
"      --text-muted: #64748B;\n" +
"      --card-shadow: 0 4px 12px rgba(0,0,0,0.04);\n" +
"      --code-bg: #F8FAFC;\n" +
"    }\n" +
"    [data-theme=\"dark\"] {\n" +
"      --bg: #0C1220;\n" +
"      --sidebar: #111A2B;\n" +
"      --cards: #172338;\n" +
"      --primary: #6485FF;\n" +
"      --primary-hover: #7b99ff;\n" +
"      --secondary: #9BAAC2;\n" +
"      --borders: #26364F;\n" +
"      --success: #36C98F;\n" +
"      --warning: #F5B94C;\n" +
"      --danger: #F17878;\n" +
"      --text: #F1F5FB;\n" +
"      --text-muted: #9BAAC2;\n" +
"      --card-shadow: 0 4px 14px rgba(0,0,0,0.2);\n" +
"      --code-bg: #0F172A;\n" +
"    }\n" +
"    * { box-sizing: border-box; margin: 0; padding: 0; }\n" +
"    body {\n" +
"      font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;\n" +
"      background: var(--bg);\n" +
"      color: var(--text);\n" +
"      display: flex;\n" +
"      height: 100vh;\n" +
"      overflow: hidden;\n" +
"      transition: background 0.25s, color 0.25s;\n" +
"    }\n" +
"    #sidebar {\n" +
"      width: 250px;\n" +
"      background: var(--sidebar);\n" +
"      border-right: 1px solid var(--borders);\n" +
"      display: flex;\n" +
"      flex-direction: column;\n" +
"      flex-shrink: 0;\n" +
"    }\n" +
"    .logo-container {\n" +
"      padding: 20px 24px;\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      gap: 12px;\n" +
"      border-bottom: 1px solid var(--borders);\n" +
"    }\n" +
"    .logo-icon {\n" +
"      width: 36px;\n" +
"      height: 36px;\n" +
"      background: var(--primary);\n" +
"      border-radius: 8px;\n" +
"      color: #fff;\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      justify-content: center;\n" +
"      font-weight: 700;\n" +
"      font-size: 16px;\n" +
"    }\n" +
"    .logo-text h1 { font-size: 17px; font-weight: 700; }\n" +
"    .logo-text p { font-size: 11px; color: var(--text-muted); }\n" +
"    .nav-list {\n" +
"      list-style: none;\n" +
"      padding: 16px 12px;\n" +
"      overflow-y: auto;\n" +
"      flex: 1;\n" +
"    }\n" +
"    .nav-item {\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      gap: 12px;\n" +
"      padding: 10px 14px;\n" +
"      border-radius: 8px;\n" +
"      font-size: 13px;\n" +
"      font-weight: 600;\n" +
"      color: var(--text-muted);\n" +
"      cursor: pointer;\n" +
"      transition: all 0.2s;\n" +
"      margin-bottom: 4px;\n" +
"    }\n" +
"    .nav-item:hover {\n" +
"      background: rgba(49, 94, 251, 0.08);\n" +
"      color: var(--text);\n" +
"    }\n" +
"    .nav-item.active {\n" +
"      background: rgba(49, 94, 251, 0.12);\n" +
"      color: var(--primary);\n" +
"      border-left: 3px solid var(--primary);\n" +
"    }\n" +
"    .sidebar-footer {\n" +
"      padding: 16px;\n" +
"      border-top: 1px solid var(--borders);\n" +
"      font-size: 11px;\n" +
"      color: var(--text-muted);\n" +
"      text-align: center;\n" +
"    }\n" +
"    #main-content {\n" +
"      flex: 1;\n" +
"      display: flex;\n" +
"      flex-direction: column;\n" +
"      overflow: hidden;\n" +
"    }\n" +
"    #topbar {\n" +
"      height: 64px;\n" +
"      background: var(--cards);\n" +
"      border-bottom: 1px solid var(--borders);\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      justify-content: space-between;\n" +
"      padding: 0 28px;\n" +
"      flex-shrink: 0;\n" +
"    }\n" +
"    .topbar-left h2 { font-size: 18px; font-weight: 700; }\n" +
"    .topbar-left p { font-size: 12px; color: var(--text-muted); }\n" +
"    .topbar-right {\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      gap: 14px;\n" +
"    }\n" +
"    .btn {\n" +
"      padding: 8px 14px;\n" +
"      border-radius: 8px;\n" +
"      font-size: 12px;\n" +
"      font-weight: 600;\n" +
"      border: none;\n" +
"      cursor: pointer;\n" +
"      display: inline-flex;\n" +
"      align-items: center;\n" +
"      gap: 6px;\n" +
"      transition: all 0.2s;\n" +
"    }\n" +
"    .btn-primary { background: var(--primary); color: #fff; }\n" +
"    .btn-primary:hover { background: var(--primary-hover); }\n" +
"    .btn-outline { background: transparent; border: 1px solid var(--borders); color: var(--text); }\n" +
"    .btn-outline:hover { background: rgba(0,0,0,0.04); }\n" +
"    .btn-success { background: var(--success); color: #fff; }\n" +
"    .btn-danger { background: var(--danger); color: #fff; }\n" +
"    .user-pill {\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      gap: 8px;\n" +
"      padding: 4px 10px;\n" +
"      background: rgba(0,0,0,0.04);\n" +
"      border-radius: 20px;\n" +
"      font-size: 12px;\n" +
"      font-weight: 600;\n" +
"    }\n" +
"    .user-avatar {\n" +
"      width: 24px;\n" +
"      height: 24px;\n" +
"      background: var(--primary);\n" +
"      color: #fff;\n" +
"      border-radius: 50%;\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      justify-content: center;\n" +
"      font-size: 10px;\n" +
"    }\n" +
"    .page-container {\n" +
"      flex: 1;\n" +
"      overflow-y: auto;\n" +
"      padding: 24px 28px;\n" +
"    }\n" +
"    .view-section { display: none; }\n" +
"    .view-section.active { display: block; }\n" +
"    .card {\n" +
"      background: var(--cards);\n" +
"      border: 1px solid var(--borders);\n" +
"      border-radius: 12px;\n" +
"      padding: 20px;\n" +
"      box-shadow: var(--card-shadow);\n" +
"      margin-bottom: 20px;\n" +
"    }\n" +
"    .grid-4 {\n" +
"      display: grid;\n" +
"      grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));\n" +
"      gap: 16px;\n" +
"      margin-bottom: 20px;\n" +
"    }\n" +
"    .grid-2 {\n" +
"      display: grid;\n" +
"      grid-template-columns: 1fr 1fr;\n" +
"      gap: 20px;\n" +
"    }\n" +
"    .stat-card {\n" +
"      background: var(--cards);\n" +
"      border: 1px solid var(--borders);\n" +
"      border-radius: 12px;\n" +
"      padding: 16px 20px;\n" +
"      box-shadow: var(--card-shadow);\n" +
"    }\n" +
"    .stat-header {\n" +
"      display: flex;\n" +
"      justify-content: space-between;\n" +
"      font-size: 12px;\n" +
"      font-weight: 600;\n" +
"      color: var(--text-muted);\n" +
"      margin-bottom: 8px;\n" +
"    }\n" +
"    .stat-val { font-size: 26px; font-weight: 700; }\n" +
"    .stat-sub { font-size: 11px; color: var(--text-muted); margin-top: 4px; }\n" +
"    .badge {\n" +
"      display: inline-block;\n" +
"      padding: 3px 8px;\n" +
"      border-radius: 6px;\n" +
"      font-size: 11px;\n" +
"      font-weight: 600;\n" +
"    }\n" +
"    .badge-easy { background: rgba(22, 163, 106, 0.15); color: var(--success); }\n" +
"    .badge-medium { background: rgba(233, 162, 59, 0.15); color: var(--warning); }\n" +
"    .badge-hard { background: rgba(224, 82, 82, 0.15); color: var(--danger); }\n" +
"    .badge-cat { background: rgba(49, 94, 251, 0.12); color: var(--primary); }\n" +
"    table {\n" +
"      width: 100%;\n" +
"      border-collapse: collapse;\n" +
"      font-size: 13px;\n" +
"    }\n" +
"    th, td {\n" +
"      padding: 10px 14px;\n" +
"      text-align: left;\n" +
"      border-bottom: 1px solid var(--borders);\n" +
"    }\n" +
"    th {\n" +
"      color: var(--text-muted);\n" +
"      font-size: 11px;\n" +
"      text-transform: uppercase;\n" +
"      font-weight: 600;\n" +
"    }\n" +
"    input[type=\"text\"], select, textarea {\n" +
"      background: var(--cards);\n" +
"      border: 1px solid var(--borders);\n" +
"      color: var(--text);\n" +
"      border-radius: 8px;\n" +
"      padding: 8px 12px;\n" +
"      font-size: 13px;\n" +
"      font-family: inherit;\n" +
"    }\n" +
"    textarea.code-editor {\n" +
"      font-family: 'JetBrains Mono', monospace;\n" +
"      background: var(--code-bg);\n" +
"      width: 100%;\n" +
"      height: 240px;\n" +
"      resize: vertical;\n" +
"    }\n" +
"    pre code {\n" +
"      font-family: 'JetBrains Mono', monospace;\n" +
"      font-size: 12px;\n" +
"      background: var(--code-bg);\n" +
"      display: block;\n" +
"      padding: 12px;\n" +
"      border-radius: 8px;\n" +
"      border: 1px solid var(--borders);\n" +
"      overflow-x: auto;\n" +
"    }\n" +
"    .progress-bar-container {\n" +
"      width: 100%;\n" +
"      height: 8px;\n" +
"      background: rgba(0,0,0,0.06);\n" +
"      border-radius: 4px;\n" +
"      overflow: hidden;\n" +
"      margin: 6px 0;\n" +
"    }\n" +
"    .progress-bar-fill {\n" +
"      height: 100%;\n" +
"      background: var(--primary);\n" +
"      border-radius: 4px;\n" +
"    }\n" +
"    .quiz-option {\n" +
"      padding: 12px 16px;\n" +
"      border: 1px solid var(--borders);\n" +
"      border-radius: 8px;\n" +
"      margin-bottom: 10px;\n" +
"      cursor: pointer;\n" +
"      transition: all 0.2s;\n" +
"      display: flex;\n" +
"      align-items: center;\n" +
"      gap: 12px;\n" +
"    }\n" +
"    .quiz-option:hover { background: rgba(49, 94, 251, 0.05); border-color: var(--primary); }\n" +
"    .quiz-option.selected { background: rgba(49, 94, 251, 0.12); border-color: var(--primary); font-weight: 600; }\n" +
"    .palette-btn {\n" +
"      width: 34px;\n" +
"      height: 34px;\n" +
"      border-radius: 6px;\n" +
"      border: 1px solid var(--borders);\n" +
"      background: var(--cards);\n" +
"      color: var(--text);\n" +
"      font-weight: 600;\n" +
"      cursor: pointer;\n" +
"    }\n" +
"    .palette-btn.active { background: var(--primary); color: #fff; }\n" +
"    .palette-btn.answered { background: var(--success); color: #fff; }\n" +
"    .palette-btn.flagged { background: var(--warning); color: #fff; }\n" +
"  </style>\n" +
"</head>\n" +
"<body>\n" +
"  <aside id=\"sidebar\">\n" +
"    <div class=\"logo-container\">\n" +
"      <div class=\"logo-icon\">IQ</div>\n" +
"      <div class=\"logo-text\">\n" +
"        <h1>InterviewIQ</h1>\n" +
"        <p>Smart Prep & Assessment</p>\n" +
"      </div>\n" +
"    </div>\n" +
"    <ul class=\"nav-list\">\n" +
"      <li class=\"nav-item active\" onclick=\"switchView('dashboard')\">🏠 Dashboard</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('prep')\">📚 Interview Prep</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('practice')\">🎯 Practice Arena</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('coding')\">💻 Coding Assessment</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('interview')\">🎙️ Mock Interview</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('quiz')\">📝 Quiz Arena</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('analytics')\">📊 Analytics</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('history')\">⏱️ History</li>\n" +
"      <li class=\"nav-item\" onclick=\"switchView('settings')\">⚙️ Settings</li>\n" +
"    </ul>\n" +
"    <div class=\"sidebar-footer\">\n" +
"      InterviewIQ v1.0.0 (Localhost Server)\n" +
"    </div>\n" +
"  </aside>\n" +
"\n" +
"  <main id=\"main-content\">\n" +
"    <header id=\"topbar\">\n" +
"      <div class=\"topbar-left\">\n" +
"        <h2 id=\"header-title\">Dashboard</h2>\n" +
"        <p id=\"header-subtitle\">Welcome to your preparation dashboard</p>\n" +
"      </div>\n" +
"      <div class=\"topbar-right\">\n" +
"        <button class=\"btn btn-outline\" onclick=\"switchView('quiz')\">Quick Quiz</button>\n" +
"        <button class=\"btn btn-primary\" onclick=\"switchView('interview')\">Mock Interview</button>\n" +
"        <button class=\"btn btn-outline\" onclick=\"toggleTheme()\" id=\"theme-toggle-btn\">🌙 Dark</button>\n" +
"        <div class=\"user-pill\">\n" +
"          <div class=\"user-avatar\" id=\"user-avatar-initials\">AJ</div>\n" +
"          <span id=\"user-display-name\">Alex Johnson</span>\n" +
"        </div>\n" +
"      </div>\n" +
"    </header>\n" +
"\n" +
"    <div class=\"page-container\">\n" +
"      <!-- 1. DASHBOARD VIEW -->\n" +
"      <section id=\"view-dashboard\" class=\"view-section active\">\n" +
"        <div class=\"card\" style=\"display:flex; justify-content:space-between; align-items:center;\">\n" +
"          <div>\n" +
"            <h2 id=\"dash-greeting\" style=\"font-size:20px; font-weight:700;\">Welcome back, Alex!</h2>\n" +
"            <p style=\"color:var(--text-muted); margin-top:4px;\">\"Consistency is the foundation of interview mastery. Solve 5 problems today!\"</p>\n" +
"          </div>\n" +
"          <div style=\"display:flex; gap:10px;\">\n" +
"            <button class=\"btn btn-outline\" onclick=\"toggleDemoData()\" id=\"dash-demo-btn\">Load Demo Data</button>\n" +
"            <button class=\"btn btn-primary\" onclick=\"switchView('prep')\">Continue Learning</button>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div class=\"grid-4\">\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>TOTAL ATTEMPTS</span><span>📝</span></div>\n" +
"            <div class=\"stat-val\" id=\"dash-attempts\">0</div>\n" +
"            <div class=\"stat-sub\">Quizzes & mock sessions</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>AVG ACCURACY</span><span>🎯</span></div>\n" +
"            <div class=\"stat-val\" id=\"dash-accuracy\" style=\"color:var(--success);\">0%</div>\n" +
"            <div class=\"stat-sub\">Across all assessments</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>CODING SOLVED</span><span>💻</span></div>\n" +
"            <div class=\"stat-val\" id=\"dash-coding\" style=\"color:var(--warning);\">0</div>\n" +
"            <div class=\"stat-sub\">Algorithms completed</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>MOCK INTERVIEWS</span><span>🎙️</span></div>\n" +
"            <div class=\"stat-val\" id=\"dash-mocks\" style=\"color:var(--primary);\">0</div>\n" +
"            <div class=\"stat-sub\">Simulations completed</div>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div class=\"grid-2\">\n" +
"          <div class=\"card\">\n" +
"            <h3 style=\"font-size:14px; font-weight:700; margin-bottom:14px;\">Recent Activity</h3>\n" +
"            <table>\n" +
"              <thead><tr><th>Title</th><th>Type</th><th>Score</th><th>Date</th></tr></thead>\n" +
"              <tbody id=\"dash-recent-table\"><tr><td colspan=\"4\" style=\"text-align:center; color:var(--text-muted);\">No activity yet</td></tr></tbody>\n" +
"            </table>\n" +
"          </div>\n" +
"          <div class=\"card\">\n" +
"            <h3 style=\"font-size:14px; font-weight:700; margin-bottom:14px;\">Recommended Revision Topics</h3>\n" +
"            <div id=\"dash-recommendations\" style=\"display:flex; flex-direction:column; gap:10px;\">\n" +
"              <p style=\"color:var(--text-muted); font-size:13px;\">Complete quizzes and mock interviews to receive personalized recommendations!</p>\n" +
"            </div>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 2. INTERVIEW PREP VIEW -->\n" +
"      <section id=\"view-prep\" class=\"view-section\">\n" +
"        <div class=\"card\" style=\"display:flex; gap:12px; align-items:center; flex-wrap:wrap;\">\n" +
"          <input type=\"text\" id=\"prep-search\" placeholder=\"Search questions...\" oninput=\"filterPrepQuestions()\" style=\"width:220px;\">\n" +
"          <select id=\"prep-cat\" onchange=\"filterPrepQuestions()\"><option value=\"ALL\">All Categories</option></select>\n" +
"          <select id=\"prep-diff\" onchange=\"filterPrepQuestions()\">\n" +
"            <option value=\"ALL\">All Difficulties</option>\n" +
"            <option value=\"EASY\">Easy</option>\n" +
"            <option value=\"MEDIUM\">Medium</option>\n" +
"            <option value=\"HARD\">Hard</option>\n" +
"          </select>\n" +
"        </div>\n" +
"        <div style=\"display:grid; grid-template-columns: 320px 1fr; gap:20px;\">\n" +
"          <div class=\"card\" style=\"max-height:650px; overflow-y:auto; padding:12px;\" id=\"prep-list\">\n" +
"            <!-- Question list loaded dynamically -->\n" +
"          </div>\n" +
"          <div class=\"card\" id=\"prep-detail\" style=\"display:none;\">\n" +
"            <div style=\"display:flex; justify-content:space-between; align-items:flex-start;\">\n" +
"              <h2 id=\"prep-title\" style=\"font-size:18px; font-weight:700;\">Question Title</h2>\n" +
"              <div id=\"prep-badges\"></div>\n" +
"            </div>\n" +
"            <p id=\"prep-text\" style=\"font-size:15px; margin: 16px 0; line-height:1.5;\"></p>\n" +
"            <button class=\"btn btn-outline\" onclick=\"togglePrepAnswer()\" id=\"prep-reveal-btn\">Reveal Model Answer</button>\n" +
"            <div id=\"prep-answer-block\" style=\"display:none; margin-top:16px;\">\n" +
"              <h4 style=\"margin-bottom:6px;\">Model Answer:</h4>\n" +
"              <p id=\"prep-answer\" style=\"font-size:13px; line-height:1.6; white-space:pre-wrap; margin-bottom:14px;\"></p>\n" +
"              <h4 style=\"margin-bottom:6px;\">Explanation:</h4>\n" +
"              <p id=\"prep-explanation\" style=\"font-size:13px; line-height:1.6; white-space:pre-wrap; margin-bottom:14px;\"></p>\n" +
"              <pre><code id=\"prep-code\"></code></pre>\n" +
"            </div>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 3. PRACTICE ARENA VIEW -->\n" +
"      <section id=\"view-practice\" class=\"view-section\">\n" +
"        <div class=\"card\" style=\"display:flex; justify-content:space-between; align-items:center;\">\n" +
"          <div style=\"display:flex; gap:12px; align-items:center;\">\n" +
"            <select id=\"practice-cat\" onchange=\"loadPractice()\"><option value=\"ALL\">All Categories</option></select>\n" +
"            <button class=\"btn btn-outline\" onclick=\"shufflePractice()\">🔀 Shuffle</button>\n" +
"          </div>\n" +
"          <div style=\"display:flex; align-items:center; gap:12px;\">\n" +
"            <span id=\"practice-progress-txt\" style=\"font-size:12px; font-weight:600;\">0 / 0 Understood</span>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div class=\"card\" style=\"max-width:800px; margin:0 auto; padding:32px;\">\n" +
"          <div id=\"practice-badges\" style=\"margin-bottom:12px;\"></div>\n" +
"          <h2 id=\"practice-title\" style=\"font-size:20px; font-weight:700;\">Select a topic to practice</h2>\n" +
"          <p id=\"practice-text\" style=\"font-size:16px; margin:18px 0; line-height:1.6;\"></p>\n" +
"          <button class=\"btn btn-primary\" onclick=\"togglePracticeAnswer()\" id=\"practice-reveal-btn\">Reveal Solution</button>\n" +
"          <div id=\"practice-answer-block\" style=\"display:none; margin-top:20px; border-top:1px solid var(--borders); padding-top:16px;\">\n" +
"            <h4 style=\"margin-bottom:6px;\">Answer:</h4>\n" +
"            <p id=\"practice-ans\" style=\"font-size:14px; white-space:pre-wrap; line-height:1.6;\"></p>\n" +
"          </div>\n" +
"          <div style=\"display:flex; justify-content:space-between; margin-top:28px;\">\n" +
"            <button class=\"btn btn-outline\" onclick=\"prevPractice()\">← Previous</button>\n" +
"            <span id=\"practice-counter\" style=\"font-size:13px; font-weight:600;\">0 / 0</span>\n" +
"            <button class=\"btn btn-outline\" onclick=\"nextPractice()\">Next →</button>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 4. CODING ASSESSMENT VIEW -->\n" +
"      <section id=\"view-coding\" class=\"view-section\">\n" +
"        <div style=\"display:grid; grid-template-columns: 280px 1fr 1fr; gap:18px;\">\n" +
"          <div class=\"card\" style=\"padding:12px; max-height:650px; overflow-y:auto;\" id=\"coding-challenge-list\">\n" +
"            <!-- List of challenges -->\n" +
"          </div>\n" +
"          <div class=\"card\">\n" +
"            <h3 id=\"coding-title\" style=\"font-size:16px; font-weight:700;\">Challenge</h3>\n" +
"            <div id=\"coding-badges\" style=\"margin:8px 0;\"></div>\n" +
"            <p id=\"coding-desc\" style=\"font-size:13px; line-height:1.5; white-space:pre-wrap;\"></p>\n" +
"            <h4 style=\"margin:14px 0 6px;\">Constraints:</h4>\n" +
"            <pre><code id=\"coding-constraints\"></code></pre>\n" +
"          </div>\n" +
"          <div class=\"card\">\n" +
"            <div style=\"display:flex; justify-content:space-between; margin-bottom:10px;\">\n" +
"              <span style=\"font-weight:600; font-size:13px;\">Java Solution (JDK 17+)</span>\n" +
"              <button class=\"btn btn-outline\" onclick=\"resetCode()\" style=\"padding:4px 8px;\">Reset</button>\n" +
"            </div>\n" +
"            <textarea id=\"coding-editor\" class=\"code-editor\"></textarea>\n" +
"            <div style=\"display:flex; gap:10px; margin-top:10px;\">\n" +
"              <button class=\"btn btn-outline\" onclick=\"runCodeTests(false)\">▶ Run Tests</button>\n" +
"              <button class=\"btn btn-primary\" onclick=\"runCodeTests(true)\">✓ Submit Solution</button>\n" +
"            </div>\n" +
"            <div id=\"coding-results\" style=\"margin-top:14px;\"></div>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 5. MOCK INTERVIEW VIEW -->\n" +
"      <section id=\"view-interview\" class=\"view-section\">\n" +
"        <div id=\"interview-setup\" class=\"card\" style=\"max-width:550px; margin:40px auto; padding:32px;\">\n" +
"          <h2 style=\"font-size:20px; font-weight:700; margin-bottom:6px;\">Mock Interview Simulation</h2>\n" +
"          <p style=\"color:var(--text-muted); font-size:13px; margin-bottom:20px;\">Simulate a real-time technical interview with timed structured responses.</p>\n" +
"          <div style=\"display:flex; flex-direction:column; gap:14px;\">\n" +
"            <div>\n" +
"              <label style=\"font-size:12px; font-weight:600;\">Domain Category:</label>\n" +
"              <select id=\"interview-cat\" style=\"width:100%; margin-top:4px;\">\n" +
"                <option>General Technical Interview</option>\n" +
"                <option>Java Programming</option>\n" +
"                <option>Python Programming</option>\n" +
"                <option>Data Structures & Algorithms</option>\n" +
"                <option>Database Management Systems</option>\n" +
"                <option>Operating Systems</option>\n" +
"              </select>\n" +
"            </div>\n" +
"            <div>\n" +
"              <label style=\"font-size:12px; font-weight:600;\">Questions Count:</label>\n" +
"              <select id=\"interview-count\" style=\"width:100%; margin-top:4px;\"><option>3</option><option selected>5</option></select>\n" +
"            </div>\n" +
"            <div>\n" +
"              <label style=\"font-size:12px; font-weight:600;\">Time Limit (Minutes):</label>\n" +
"              <select id=\"interview-time\" style=\"width:100%; margin-top:4px;\"><option>5</option><option selected>10</option><option>15</option></select>\n" +
"            </div>\n" +
"            <button class=\"btn btn-primary\" style=\"margin-top:12px;\" onclick=\"startMockInterview()\">Start Mock Interview 🚀</button>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div id=\"interview-active\" class=\"card\" style=\"display:none; max-width:850px; margin:0 auto;\">\n" +
"          <div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--borders); padding-bottom:12px; margin-bottom:16px;\">\n" +
"            <span id=\"interview-prog\" style=\"font-weight:700;\">Question 1 of 5</span>\n" +
"            <span id=\"interview-timer\" style=\"font-size:18px; font-weight:700; color:var(--primary);\">⏱️ 10:00</span>\n" +
"            <button class=\"btn btn-danger\" onclick=\"finishMockInterview()\">Finish Interview</button>\n" +
"          </div>\n" +
"          <h3 id=\"interview-q-title\" style=\"font-size:18px; font-weight:700;\">Question</h3>\n" +
"          <p id=\"interview-q-text\" style=\"font-size:15px; margin:12px 0; line-height:1.5;\"></p>\n" +
"          <label style=\"font-size:12px; font-weight:600;\">Your Structured Answer:</label>\n" +
"          <textarea id=\"interview-answer\" style=\"width:100%; height:160px; margin-top:6px;\"></textarea>\n" +
"          <div style=\"display:flex; justify-content:space-between; margin-top:16px;\">\n" +
"            <button class=\"btn btn-outline\" onclick=\"prevInterviewQ()\">← Previous</button>\n" +
"            <button class=\"btn btn-primary\" onclick=\"nextInterviewQ()\">Next Question →</button>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 6. QUIZ ARENA VIEW -->\n" +
"      <section id=\"view-quiz\" class=\"view-section\">\n" +
"        <div id=\"quiz-setup\" class=\"card\" style=\"max-width:550px; margin:40px auto; padding:32px;\">\n" +
"          <h2 style=\"font-size:20px; font-weight:700; margin-bottom:6px;\">MCQ Quiz Assessment</h2>\n" +
"          <p style=\"color:var(--text-muted); font-size:13px; margin-bottom:20px;\">Timed multiple-choice questions testing computer science fundamentals.</p>\n" +
"          <div style=\"display:flex; flex-direction:column; gap:14px;\">\n" +
"            <div>\n" +
"              <label style=\"font-size:12px; font-weight:600;\">Subject:</label>\n" +
"              <select id=\"quiz-cat\" style=\"width:100%; margin-top:4px;\"><option value=\"ALL\">All Categories</option></select>\n" +
"            </div>\n" +
"            <div>\n" +
"              <label style=\"font-size:12px; font-weight:600;\">Questions:</label>\n" +
"              <select id=\"quiz-count\" style=\"width:100%; margin-top:4px;\"><option selected>5</option><option>10</option></select>\n" +
"            </div>\n" +
"            <button class=\"btn btn-primary\" style=\"margin-top:12px;\" onclick=\"startQuiz()\">Launch Quiz 🎯</button>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div id=\"quiz-active\" class=\"card\" style=\"display:none; max-width:800px; margin:0 auto;\">\n" +
"          <div style=\"display:flex; justify-content:space-between; align-items:center; border-bottom:1px solid var(--borders); padding-bottom:12px; margin-bottom:16px;\">\n" +
"            <span id=\"quiz-prog\" style=\"font-weight:700;\">Question 1 of 5</span>\n" +
"            <span id=\"quiz-timer\" style=\"font-size:18px; font-weight:700; color:var(--primary);\">⏱️ 05:00</span>\n" +
"            <button class=\"btn btn-success\" onclick=\"submitQuiz()\">Submit Quiz</button>\n" +
"          </div>\n" +
"          <h3 id=\"quiz-q-text\" style=\"font-size:16px; font-weight:600; margin-bottom:18px;\">Question</h3>\n" +
"          <div id=\"quiz-options-container\"></div>\n" +
"          <div style=\"display:flex; justify-content:space-between; margin-top:20px;\">\n" +
"            <button class=\"btn btn-outline\" onclick=\"prevQuizQ()\">← Previous</button>\n" +
"            <button class=\"btn btn-primary\" onclick=\"nextQuizQ()\">Next Question →</button>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 7. ANALYTICS VIEW -->\n" +
"      <section id=\"view-analytics\" class=\"view-section\">\n" +
"        <div class=\"grid-4\">\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>TOTAL ASSESSMENTS</span><span>📈</span></div>\n" +
"            <div class=\"stat-val\" id=\"ana-total\">0</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>OVERALL ACCURACY</span><span>🎯</span></div>\n" +
"            <div class=\"stat-val\" id=\"ana-acc\" style=\"color:var(--success);\">0%</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>STUDY TIME</span><span>⏱️</span></div>\n" +
"            <div class=\"stat-val\" id=\"ana-time\">0 min</div>\n" +
"          </div>\n" +
"          <div class=\"stat-card\">\n" +
"            <div class=\"stat-header\"><span>STREAK</span><span>🔥</span></div>\n" +
"            <div class=\"stat-val\" id=\"ana-streak\" style=\"color:var(--warning);\">0 days</div>\n" +
"          </div>\n" +
"        </div>\n" +
"        <div class=\"grid-2\">\n" +
"          <div class=\"card\">\n" +
"            <h3 style=\"font-size:14px; font-weight:700; margin-bottom:12px;\">Topic Accuracy Breakdown</h3>\n" +
"            <div id=\"ana-topic-bars\"></div>\n" +
"          </div>\n" +
"          <div class=\"card\">\n" +
"            <h3 style=\"font-size:14px; font-weight:700; margin-bottom:12px;\">Assessment Distribution</h3>\n" +
"            <div id=\"ana-dist-bars\"></div>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 8. HISTORY VIEW -->\n" +
"      <section id=\"view-history\" class=\"view-section\">\n" +
"        <div class=\"card\" style=\"display:flex; justify-content:space-between; align-items:center;\">\n" +
"          <div style=\"display:flex; gap:12px;\">\n" +
"            <input type=\"text\" id=\"hist-search\" placeholder=\"Search history...\" oninput=\"loadHistory()\">\n" +
"          </div>\n" +
"          <button class=\"btn btn-danger\" onclick=\"clearHistory()\">Clear History</button>\n" +
"        </div>\n" +
"        <div class=\"card\">\n" +
"          <table>\n" +
"            <thead><tr><th>Date</th><th>Type</th><th>Title</th><th>Score</th><th>Accuracy</th><th>Duration</th><th>Status</th></tr></thead>\n" +
"            <tbody id=\"hist-table-body\"><tr><td colspan=\"7\" style=\"text-align:center;\">No history records yet.</td></tr></tbody>\n" +
"          </table>\n" +
"        </div>\n" +
"      </section>\n" +
"\n" +
"      <!-- 9. SETTINGS VIEW -->\n" +
"      <section id=\"view-settings\" class=\"view-section\">\n" +
"        <div class=\"card\" style=\"max-width:600px;\">\n" +
"          <h3 style=\"font-size:16px; font-weight:700; margin-bottom:14px;\">User Profile</h3>\n" +
"          <label style=\"font-size:12px; font-weight:600;\">Display Name:</label>\n" +
"          <input type=\"text\" id=\"setting-name\" style=\"width:100%; margin:4px 0 14px;\">\n" +
"          <button class=\"btn btn-primary\" onclick=\"saveSettings()\">Save Changes</button>\n" +
"        </div>\n" +
"        <div class=\"card\" style=\"max-width:600px;\">\n" +
"          <h3 style=\"font-size:16px; font-weight:700; margin-bottom:14px;\">Data Persistence</h3>\n" +
"          <div style=\"display:flex; gap:10px;\">\n" +
"            <button class=\"btn btn-outline\" onclick=\"toggleDemoData()\">Toggle Demo Data</button>\n" +
"            <button class=\"btn btn-danger\" onclick=\"clearHistory()\">Erase All History</button>\n" +
"          </div>\n" +
"        </div>\n" +
"      </section>\n" +
"    </div>\n" +
"  </main>\n" +
"\n" +
"  <script>\n" +
"    let allQuestions = [];\n" +
"    let allChallenges = [];\n" +
"    let currentQuizQuestions = [];\n" +
"    let quizIndex = 0;\n" +
"    let quizAnswers = {};\n" +
"    let currentMockQuestions = [];\n" +
"    let mockIndex = 0;\n" +
"    let mockAnswers = {};\n" +
"    let practiceQuestions = [];\n" +
"    let practiceIdx = 0;\n" +
"    let selectedChallenge = null;\n" +
"\n" +
"    function switchView(viewName) {\n" +
"      document.querySelectorAll('.view-section').forEach(el => el.classList.remove('active'));\n" +
"      document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));\n" +
"      document.getElementById('view-' + viewName).classList.add('active');\n" +
"      \n" +
"      const navMap = { dashboard: 0, prep: 1, practice: 2, coding: 3, interview: 4, quiz: 5, analytics: 6, history: 7, settings: 8 };\n" +
"      const navItems = document.querySelectorAll('.nav-item');\n" +
"      if (navItems[navMap[viewName]]) navItems[navMap[viewName]].classList.add('active');\n" +
"\n" +
"      document.getElementById('header-title').innerText = viewName.toUpperCase();\n" +
"      if (viewName === 'dashboard') loadDashboard();\n" +
"      if (viewName === 'analytics') loadAnalytics();\n" +
"      if (viewName === 'history') loadHistory();\n" +
"    }\n" +
"\n" +
"    function toggleTheme() {\n" +
"      const current = document.documentElement.getAttribute('data-theme');\n" +
"      const next = current === 'dark' ? 'light' : 'dark';\n" +
"      document.documentElement.setAttribute('data-theme', next);\n" +
"      document.getElementById('theme-toggle-btn').innerText = next === 'dark' ? '☀️ Light' : '🌙 Dark';\n" +
"    }\n" +
"\n" +
"    async function loadDashboard() {\n" +
"      const res = await fetch('/api/stats');\n" +
"      const data = await res.json();\n" +
"      document.getElementById('dash-attempts').innerText = data.totalAttempts;\n" +
"      document.getElementById('dash-accuracy').innerText = data.averageAccuracy + '%';\n" +
"      document.getElementById('dash-coding').innerText = data.codingSolved;\n" +
"      document.getElementById('dash-mocks').innerText = data.mockInterviews;\n" +
"      document.getElementById('dash-greeting').innerText = 'Welcome back, ' + (data.displayName || 'Alex') + '!';\n" +
"      document.getElementById('user-display-name').innerText = data.displayName || 'Alex Johnson';\n" +
"\n" +
"      const recTable = document.getElementById('dash-recent-table');\n" +
"      if (data.recent && data.recent.length > 0) {\n" +
"        recTable.innerHTML = data.recent.map(r => `<tr><td>${r.title}</td><td><span class=\"badge badge-cat\">${r.type}</span></td><td><b>${Math.round(r.accuracyPercentage)}%</b></td><td>${r.timestamp.split('T')[0]}</td></tr>`).join('');\n" +
"      } else {\n" +
"        recTable.innerHTML = '<tr><td colspan=\"4\" style=\"text-align:center; color:var(--text-muted);\">No activity recorded yet</td></tr>';\n" +
"      }\n" +
"    }\n" +
"\n" +
"    async function initData() {\n" +
"      loadDashboard();\n" +
"      const res = await fetch('/api/questions');\n" +
"      allQuestions = await res.json();\n" +
"      \n" +
"      const cats = [...new Set(allQuestions.map(q => q.category))].sort();\n" +
"      const prepCat = document.getElementById('prep-cat');\n" +
"      const practCat = document.getElementById('practice-cat');\n" +
"      const quizCat = document.getElementById('quiz-cat');\n" +
"      cats.forEach(c => {\n" +
"        prepCat.innerHTML += `<option value=\"${c}\">${c}</option>`;\n" +
"        practCat.innerHTML += `<option value=\"${c}\">${c}</option>`;\n" +
"        quizCat.innerHTML += `<option value=\"${c}\">${c}</option>`;\n" +
"      });\n" +
"      filterPrepQuestions();\n" +
"      loadPractice();\n" +
"      loadCoding();\n" +
"    }\n" +
"\n" +
"    function filterPrepQuestions() {\n" +
"      const q = document.getElementById('prep-search').value.toLowerCase();\n" +
"      const cat = document.getElementById('prep-cat').value;\n" +
"      const diff = document.getElementById('prep-diff').value;\n" +
"\n" +
"      const filtered = allQuestions.filter(item => {\n" +
"        if (cat !== 'ALL' && item.category !== cat) return false;\n" +
"        if (diff !== 'ALL' && item.difficulty !== diff) return false;\n" +
"        if (q && !item.title.toLowerCase().includes(q) && !item.questionText.toLowerCase().includes(q)) return false;\n" +
"        return true;\n" +
"      });\n" +
"\n" +
"      const listEl = document.getElementById('prep-list');\n" +
"      if (filtered.length === 0) {\n" +
"        listEl.innerHTML = '<p style=\"color:var(--text-muted); padding:10px;\">No matching questions.</p>';\n" +
"        return;\n" +
"      }\n" +
"      listEl.innerHTML = filtered.map(item => `\n" +
"        <div onclick=\"selectPrepQuestion('${item.id}')\" style=\"padding:10px; border-radius:8px; cursor:pointer; margin-bottom:6px; border-bottom:1px solid var(--borders);\">\n" +
"          <div style=\"font-size:13px; font-weight:600;\">${item.title}</div>\n" +
"          <div style=\"font-size:11px; color:var(--text-muted); margin-top:4px;\">${item.category} • <span class=\"badge badge-${item.difficulty.toLowerCase()}\">${item.difficulty}</span></div>\n" +
"        </div>\n" +
"      `).join('');\n" +
"      selectPrepQuestion(filtered[0].id);\n" +
"    }\n" +
"\n" +
"    function selectPrepQuestion(id) {\n" +
"      const q = allQuestions.find(i => i.id === id);\n" +
"      if (!q) return;\n" +
"      document.getElementById('prep-detail').style.display = 'block';\n" +
"      document.getElementById('prep-title').innerText = q.title;\n" +
"      document.getElementById('prep-badges').innerHTML = `<span class=\"badge badge-cat\">${q.category}</span> <span class=\"badge badge-${q.difficulty.toLowerCase()}\">${q.difficulty}</span>`;\n" +
"      document.getElementById('prep-text').innerText = q.questionText;\n" +
"      document.getElementById('prep-answer').innerText = q.answerText;\n" +
"      document.getElementById('prep-explanation').innerText = q.explanationText;\n" +
"      document.getElementById('prep-code').innerText = q.codeExample || '// No code example';\n" +
"      document.getElementById('prep-answer-block').style.display = 'none';\n" +
"      document.getElementById('prep-reveal-btn').innerText = 'Reveal Model Answer';\n" +
"    }\n" +
"\n" +
"    function togglePrepAnswer() {\n" +
"      const b = document.getElementById('prep-answer-block');\n" +
"      const btn = document.getElementById('prep-reveal-btn');\n" +
"      if (b.style.display === 'none') {\n" +
"        b.style.display = 'block';\n" +
"        btn.innerText = 'Hide Model Answer';\n" +
"      } else {\n" +
"        b.style.display = 'none';\n" +
"        btn.innerText = 'Reveal Model Answer';\n" +
"      }\n" +
"    }\n" +
"\n" +
"    function loadPractice() {\n" +
"      const cat = document.getElementById('practice-cat').value;\n" +
"      practiceQuestions = allQuestions.filter(q => cat === 'ALL' || q.category === cat);\n" +
"      practiceIdx = 0;\n" +
"      displayPractice();\n" +
"    }\n" +
"    function shufflePractice() {\n" +
"      practiceQuestions.sort(() => Math.random() - 0.5);\n" +
"      practiceIdx = 0;\n" +
"      displayPractice();\n" +
"    }\n" +
"    function displayPractice() {\n" +
"      if (practiceQuestions.length === 0) return;\n" +
"      const q = practiceQuestions[practiceIdx];\n" +
"      document.getElementById('practice-title').innerText = q.title;\n" +
"      document.getElementById('practice-text').innerText = q.questionText;\n" +
"      document.getElementById('practice-ans').innerText = q.answerText;\n" +
"      document.getElementById('practice-counter').innerText = (practiceIdx + 1) + ' / ' + practiceQuestions.length;\n" +
"      document.getElementById('practice-badges').innerHTML = `<span class=\"badge badge-cat\">${q.category}</span> <span class=\"badge badge-${q.difficulty.toLowerCase()}\">${q.difficulty}</span>`;\n" +
"      document.getElementById('practice-answer-block').style.display = 'none';\n" +
"      document.getElementById('practice-reveal-btn').innerText = 'Reveal Solution';\n" +
"    }\n" +
"    function togglePracticeAnswer() {\n" +
"      const b = document.getElementById('practice-answer-block');\n" +
"      b.style.display = b.style.display === 'none' ? 'block' : 'none';\n" +
"      document.getElementById('practice-reveal-btn').innerText = b.style.display === 'none' ? 'Reveal Solution' : 'Hide Solution';\n" +
"    }\n" +
"    function prevPractice() { if (practiceIdx > 0) { practiceIdx--; displayPractice(); } }\n" +
"    function nextPractice() { if (practiceIdx < practiceQuestions.length - 1) { practiceIdx++; displayPractice(); } }\n" +
"\n" +
"    async function loadCoding() {\n" +
"      const res = await fetch('/api/coding');\n" +
"      allChallenges = await res.json();\n" +
"      const listEl = document.getElementById('coding-challenge-list');\n" +
"      listEl.innerHTML = allChallenges.map((c, i) => `\n" +
"        <div onclick=\"selectChallenge(${i})\" style=\"padding:10px; border-radius:8px; cursor:pointer; margin-bottom:6px; border-bottom:1px solid var(--borders);\">\n" +
"          <div style=\"font-size:13px; font-weight:600;\">${c.title}</div>\n" +
"          <span class=\"badge badge-${c.difficulty.toLowerCase()}\">${c.difficulty}</span>\n" +
"        </div>\n" +
"      `).join('');\n" +
"      selectChallenge(0);\n" +
"    }\n" +
"    function selectChallenge(i) {\n" +
"      selectedChallenge = allChallenges[i];\n" +
"      if (!selectedChallenge) return;\n" +
"      document.getElementById('coding-title').innerText = selectedChallenge.title;\n" +
"      document.getElementById('coding-badges').innerHTML = `<span class=\"badge badge-cat\">${selectedChallenge.category}</span> <span class=\"badge badge-${selectedChallenge.difficulty.toLowerCase()}\">${selectedChallenge.difficulty}</span>`;\n" +
"      document.getElementById('coding-desc').innerText = selectedChallenge.description;\n" +
"      document.getElementById('coding-constraints').innerText = selectedChallenge.constraints;\n" +
"      document.getElementById('coding-editor').value = selectedChallenge.starterCode;\n" +
"      document.getElementById('coding-results').innerHTML = '';\n" +
"    }\n" +
"    function resetCode() {\n" +
"      if (selectedChallenge) document.getElementById('coding-editor').value = selectedChallenge.starterCode;\n" +
"    }\n" +
"    async function runCodeTests(isSubmit) {\n" +
"      const code = document.getElementById('coding-editor').value;\n" +
"      const res = await fetch('/api/coding/run', {\n" +
"        method: 'POST',\n" +
"        headers: { 'Content-Type': 'application/json' },\n" +
"        body: JSON.stringify({ challengeId: selectedChallenge.id, code: code, submit: isSubmit })\n" +
"      });\n" +
"      const result = await res.json();\n" +
"      const resEl = document.getElementById('coding-results');\n" +
"      resEl.innerHTML = `\n" +
"        <div style=\"padding:10px; border-radius:8px; background:rgba(0,0,0,0.04); font-size:12px;\">\n" +
"          <b>Passed:</b> ${result.passedCount} / ${result.totalCount} (Score: ${result.scorePercentage}%)<br>\n" +
"          <span style=\"color:${result.passedCount === result.totalCount ? 'var(--success)' : 'var(--warning)'}; font-weight:600;\">${result.feedback}</span>\n" +
"        </div>\n" +
"      `;\n" +
"      if (isSubmit) alert('Assessment submitted! Recorded in History & Analytics.');\n" +
"    }\n" +
"\n" +
"    async function startQuiz() {\n" +
"      const cat = document.getElementById('quiz-cat').value;\n" +
"      const count = parseInt(document.getElementById('quiz-count').value);\n" +
"      const res = await fetch('/api/quiz?cat=' + encodeURIComponent(cat) + '&count=' + count);\n" +
"      currentQuizQuestions = await res.json();\n" +
"      quizIndex = 0;\n" +
"      quizAnswers = {};\n" +
"      document.getElementById('quiz-setup').style.display = 'none';\n" +
"      document.getElementById('quiz-active').style.display = 'block';\n" +
"      displayQuizQ();\n" +
"    }\n" +
"    function displayQuizQ() {\n" +
"      const q = currentQuizQuestions[quizIndex];\n" +
"      document.getElementById('quiz-prog').innerText = 'Question ' + (quizIndex + 1) + ' of ' + currentQuizQuestions.length;\n" +
"      document.getElementById('quiz-q-text').innerText = q.questionText;\n" +
"      const cont = document.getElementById('quiz-options-container');\n" +
"      cont.innerHTML = q.options.map((opt, i) => `\n" +
"        <div class=\"quiz-option ${quizAnswers[quizIndex] === i ? 'selected' : ''}\" onclick=\"selectQuizOption(${i})\">\n" +
"          <b>${String.fromCharCode(65 + i)}.</b> ${opt}\n" +
"        </div>\n" +
"      `).join('');\n" +
"    }\n" +
"    function selectQuizOption(i) {\n" +
"      quizAnswers[quizIndex] = i;\n" +
"      displayQuizQ();\n" +
"    }\n" +
"    function prevQuizQ() { if (quizIndex > 0) { quizIndex--; displayQuizQ(); } }\n" +
"    function nextQuizQ() { if (quizIndex < currentQuizQuestions.length - 1) { quizIndex++; displayQuizQ(); } else { submitQuiz(); } }\n" +
"    async function submitQuiz() {\n" +
"      let correct = 0;\n" +
"      currentQuizQuestions.forEach((q, i) => {\n" +
"        if (quizAnswers[i] === q.correctOptionIndex) correct++;\n" +
"      });\n" +
"      const acc = Math.round((correct / currentQuizQuestions.length) * 100);\n" +
"      await fetch('/api/quiz/submit', {\n" +
"        method: 'POST',\n" +
"        headers: { 'Content-Type': 'application/json' },\n" +
"        body: JSON.stringify({ total: currentQuizQuestions.length, correct: correct, accuracy: acc })\n" +
"      });\n" +
"      alert('Quiz Submitted! Score: ' + correct + ' / ' + currentQuizQuestions.length + ' (' + acc + '%)');\n" +
"      document.getElementById('quiz-setup').style.display = 'block';\n" +
"      document.getElementById('quiz-active').style.display = 'none';\n" +
"      switchView('analytics');\n" +
"    }\n" +
"\n" +
"    async function loadAnalytics() {\n" +
"      const res = await fetch('/api/stats');\n" +
"      const data = await res.json();\n" +
"      document.getElementById('ana-total').innerText = data.totalAttempts;\n" +
"      document.getElementById('ana-acc').innerText = data.averageAccuracy + '%';\n" +
"      document.getElementById('ana-time').innerText = Math.round(data.totalStudyTime / 60) + ' min';\n" +
"      document.getElementById('ana-streak').innerText = data.streak + ' days';\n" +
"      \n" +
"      const bars = document.getElementById('ana-topic-bars');\n" +
"      bars.innerHTML = Object.entries(data.topicAccuracy || {}).map(([topic, acc]) => `\n" +
"        <div style=\"margin-bottom:10px;\">\n" +
"          <div style=\"display:flex; justify-content:space-between; font-size:12px; margin-bottom:2px;\">\n" +
"            <span>${topic}</span><b>${Math.round(acc)}%</b>\n" +
"          </div>\n" +
"          <div class=\"progress-bar-container\"><div class=\"progress-bar-fill\" style=\"width:${acc}%;\"></div></div>\n" +
"        </div>\n" +
"      `).join('') || '<p style=\"color:var(--text-muted); font-size:12px;\">No topic results recorded yet.</p>';\n" +
"    }\n" +
"\n" +
"    async function loadHistory() {\n" +
"      const res = await fetch('/api/history');\n" +
"      const list = await res.json();\n" +
"      const tbody = document.getElementById('hist-table-body');\n" +
"      if (list.length === 0) {\n" +
"        tbody.innerHTML = '<tr><td colspan=\"7\" style=\"text-align:center; color:var(--text-muted);\">No history records yet.</td></tr>';\n" +
"        return;\n" +
"      }\n" +
"      tbody.innerHTML = list.map(r => `\n" +
"        <tr>\n" +
"          <td>${r.timestamp.replace('T', ' ').substring(0, 16)}</td>\n" +
"          <td><span class=\"badge badge-cat\">${r.type}</span></td>\n" +
"          <td>${r.title}</td>\n" +
"          <td>${Math.round(r.score)} / ${Math.round(r.maxScore)}</td>\n" +
"          <td><b>${Math.round(r.accuracyPercentage)}%</b></td>\n" +
"          <td>${r.durationSeconds}s</td>\n" +
"          <td>${r.status}</td>\n" +
"        </tr>\n" +
"      `).join('');\n" +
"    }\n" +
"\n" +
"    async function clearHistory() {\n" +
"      if (!confirm('Are you sure you want to clear all assessment history?')) return;\n" +
"      await fetch('/api/history/clear', { method: 'POST' });\n" +
"      alert('History cleared.');\n" +
"      loadHistory();\n" +
"    }\n" +
"\n" +
"    async function toggleDemoData() {\n" +
"      await fetch('/api/demo-data', { method: 'POST' });\n" +
"      alert('Demo data updated.');\n" +
"      loadDashboard();\n" +
"    }\n" +
"\n" +
"    window.onload = initData;\n" +
"  </script>\n" +
"</body>\n" +
"</html>";
    }
}
