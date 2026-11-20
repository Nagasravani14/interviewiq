package com.interviewiq.ui;

import com.interviewiq.repository.AttemptRepository;
import com.interviewiq.repository.JsonAttemptRepository;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.repository.QuestionRepository;
import com.interviewiq.service.AnalyticsService;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.Sidebar;
import com.interviewiq.ui.components.TopBar;
import com.interviewiq.ui.panels.AnalyticsPanel;
import com.interviewiq.ui.panels.CodingAssessmentPanel;
import com.interviewiq.ui.panels.DashboardPanel;
import com.interviewiq.ui.panels.HistoryPanel;
import com.interviewiq.ui.panels.InterviewPrepPanel;
import com.interviewiq.ui.panels.MockInterviewPanel;
import com.interviewiq.ui.panels.PracticeArenaPanel;
import com.interviewiq.ui.panels.QuizArenaPanel;
import com.interviewiq.ui.panels.SettingsPanel;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Main application window for InterviewIQ desktop application.
 */
public class MainFrame extends JFrame implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final PreferencesRepository preferencesRepository;
    private final AttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    private final QuestionService questionService;
    private final HistoryService historyService;
    private final AnalyticsService analyticsService;

    private Sidebar sidebar;
    private TopBar topBar;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);

    // Individual Panels
    private DashboardPanel dashboardPanel;
    private InterviewPrepPanel interviewPrepPanel;
    private PracticeArenaPanel practiceArenaPanel;
    private CodingAssessmentPanel codingAssessmentPanel;
    private MockInterviewPanel mockInterviewPanel;
    private QuizArenaPanel quizArenaPanel;
    private AnalyticsPanel analyticsPanel;
    private HistoryPanel historyPanel;
    private SettingsPanel settingsPanel;

    public MainFrame() {
        // 1. Initialize Repositories and Services
        this.preferencesRepository = new PreferencesRepository();
        this.attemptRepository = new JsonAttemptRepository();
        this.questionRepository = new QuestionRepository();

        this.themeService = new ThemeService(preferencesRepository);
        this.questionService = new QuestionService(questionRepository);
        this.historyService = new HistoryService(attemptRepository);
        this.analyticsService = new AnalyticsService(attemptRepository);

        // 2. Initialize Look and Feel
        themeService.initializeLaf();
        themeService.addThemeChangeListener(this);

        // 3. Configure Main Window
        setTitle(AppConstants.APP_NAME + " – " + AppConstants.APP_SUBTITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 850);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);

        // 4. Build Layout
        initUI();
        updateBackgroundColors();
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setOpaque(true);

        // Sidebar Navigation
        sidebar = new Sidebar(themeService, this::navigateTo);
        rootPanel.add(sidebar, BorderLayout.WEST);

        // Center Area (TopBar + Content Panels)
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);

        topBar = new TopBar(themeService, preferencesRepository, analyticsService, this::navigateTo);
        centerContainer.add(topBar, BorderLayout.NORTH);

        // Content Area with CardLayout
        contentCards.setOpaque(false);

        dashboardPanel = new DashboardPanel(themeService, analyticsService, historyService, questionService, preferencesRepository, this::navigateTo);
        interviewPrepPanel = new InterviewPrepPanel(themeService, questionService);
        practiceArenaPanel = new PracticeArenaPanel(themeService, questionService);
        codingAssessmentPanel = new CodingAssessmentPanel(themeService, questionService, historyService);
        mockInterviewPanel = new MockInterviewPanel(themeService, questionService, historyService);
        quizArenaPanel = new QuizArenaPanel(themeService, questionService, historyService);
        analyticsPanel = new AnalyticsPanel(themeService, analyticsService);
        historyPanel = new HistoryPanel(themeService, historyService);
        settingsPanel = new SettingsPanel(themeService, preferencesRepository, historyService, questionService, this::onPreferencesUpdated);

        contentCards.add(dashboardPanel, "dashboard");
        contentCards.add(interviewPrepPanel, "prep");
        contentCards.add(practiceArenaPanel, "practice");
        contentCards.add(codingAssessmentPanel, "coding");
        contentCards.add(mockInterviewPanel, "interview");
        contentCards.add(quizArenaPanel, "quiz");
        contentCards.add(analyticsPanel, "analytics");
        contentCards.add(historyPanel, "history");
        contentCards.add(settingsPanel, "settings");

        centerContainer.add(contentCards, BorderLayout.CENTER);
        rootPanel.add(centerContainer, BorderLayout.CENTER);

        setContentPane(rootPanel);
        navigateTo("dashboard");
    }

    public void navigateTo(String viewId) {
        cardLayout.show(contentCards, viewId);
        if (sidebar != null && !viewId.equalsIgnoreCase(sidebar.getActiveId())) {
            sidebar.selectItem(viewId);
        }

        if (topBar != null) {
            switch (viewId.toLowerCase()) {
                case "dashboard":
                    topBar.setPageHeader("Dashboard Overview", "Welcome back! Here is your preparation progress and activity.");
                    if (dashboardPanel != null) dashboardPanel.refreshData();
                    topBar.refreshUserData();
                    break;
                case "prep":
                    topBar.setPageHeader("Interview Preparation", "Browse 80+ in-depth technical interview questions across 8 core CS domains.");
                    break;
                case "practice":
                    topBar.setPageHeader("Practice Arena", "Rapid-drill flashcard practice to reinforce key concepts and algorithms.");
                    break;
                case "coding":
                    topBar.setPageHeader("Coding Assessment", "Solve standard coding challenges with deterministic test case evaluation.");
                    break;
                case "interview":
                    topBar.setPageHeader("Mock Interview Simulation", "Real-world timed technical interview simulation with rule-based scoring.");
                    break;
                case "quiz":
                    topBar.setPageHeader("MCQ Quiz Arena", "Test your knowledge with timed technical quizzes and concept reviews.");
                    break;
                case "analytics":
                    topBar.setPageHeader("Performance Analytics", "Data-driven insights into your strengths, accuracy, and study trends.");
                    if (analyticsPanel != null) analyticsPanel.refreshData();
                    break;
                case "history":
                    topBar.setPageHeader("Attempt History", "Review all past assessments, quizzes, and mock interview reports.");
                    if (historyPanel != null) historyPanel.refreshHistory();
                    break;
                case "settings":
                    topBar.setPageHeader("Preferences & Settings", "Customize your user profile, appearance, defaults, and manage local data.");
                    break;
            }
        }
    }

    private void onPreferencesUpdated() {
        topBar.refreshUserData();
        dashboardPanel.refreshData();
    }

    private void updateBackgroundColors() {
        ColorScheme cs = themeService.getCurrentScheme();
        getContentPane().setBackground(cs.getBackground());
        contentCards.setBackground(cs.getBackground());
        repaint();
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        updateBackgroundColors();
    }
}
