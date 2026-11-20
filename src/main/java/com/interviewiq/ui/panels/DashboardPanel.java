package com.interviewiq.ui.panels;

import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.service.AnalyticsService;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.ModernButton;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.ui.components.StatCard;
import com.interviewiq.ui.components.TopicPerformanceChart;
import com.interviewiq.ui.components.WeeklyActivityChart;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;
import com.interviewiq.util.DateTimeUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Module A: Dashboard overview with welcome banner, metrics, custom charts,
 * recent activity, and smart preparation recommendations.
 */
public class DashboardPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final AnalyticsService analyticsService;
    private final HistoryService historyService;
    private final QuestionService questionService;
    private final PreferencesRepository preferencesRepository;
    private final Consumer<String> navigationHandler;

    private JLabel greetingLabel;
    private JLabel quoteLabel;
    private StatCard statAttempted;
    private StatCard statAccuracy;
    private StatCard statCoding;
    private StatCard statMock;

    private WeeklyActivityChart weeklyChart;
    private TopicPerformanceChart topicChart;

    private DefaultTableModel recentTableModel;
    private JTable recentTable;
    private JPanel revisionPanel;
    private JButton demoDataBtn;

    public DashboardPanel(ThemeService themeService, AnalyticsService analyticsService,
                          HistoryService historyService, QuestionService questionService,
                          PreferencesRepository preferencesRepository, Consumer<String> navigationHandler) {
        this.themeService = themeService;
        this.analyticsService = analyticsService;
        this.historyService = historyService;
        this.questionService = questionService;
        this.preferencesRepository = preferencesRepository;
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // 1. Welcome Greeting Banner
        content.add(createWelcomeBanner());
        content.add(Box.createVerticalStrut(18));

        // 2. Statistics KPI Cards (4 cards)
        content.add(createStatsRow());
        content.add(Box.createVerticalStrut(18));

        // 3. Middle Section: Charts (Weekly Activity & Topic Performance)
        content.add(createChartsRow());
        content.add(Box.createVerticalStrut(18));

        // 4. Bottom Section: Recent Activity & Revision Recommendations
        content.add(createBottomRow());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        refreshData();
    }

    private JPanel createWelcomeBanner() {
        ModernCard banner = new ModernCard(themeService, 16, true);
        banner.setLayout(new BorderLayout(16, 12));
        banner.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        greetingLabel = new JLabel("Welcome back!");
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        quoteLabel = new JLabel("“Consistency is the foundation of interview mastery. Solve 5 problems today!”");
        quoteLabel.setFont(AppConstants.FONT_REGULAR);

        left.add(greetingLabel);
        left.add(Box.createVerticalStrut(4));
        left.add(quoteLabel);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        rightActions.setOpaque(false);

        demoDataBtn = new ModernButton("Demo Data", ModernButton.ButtonType.OUTLINE, themeService);
        demoDataBtn.setFont(AppConstants.FONT_SMALL_BOLD);
        demoDataBtn.addActionListener(e -> toggleDemoData());

        ModernButton continueBtn = new ModernButton("Continue Prep", ModernButton.ButtonType.OUTLINE, themeService);
        continueBtn.addActionListener(e -> navigationHandler.accept("prep"));

        ModernButton startMockBtn = new ModernButton("Start Mock Interview", ModernButton.ButtonType.PRIMARY, themeService);
        startMockBtn.addActionListener(e -> navigationHandler.accept("interview"));

        rightActions.add(demoDataBtn);
        rightActions.add(continueBtn);
        rightActions.add(startMockBtn);

        banner.add(left, BorderLayout.WEST);
        banner.add(rightActions, BorderLayout.EAST);
        return banner;
    }

    private JPanel createStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);

        ColorScheme cs = themeService.getCurrentScheme();
        statAttempted = new StatCard("Total Attempts", "0", "Quizzes & assessments", "📝", cs.getPrimary(), themeService);
        statAccuracy = new StatCard("Average Accuracy", "0%", "Across all assessments", "🎯", cs.getSuccess(), themeService);
        statCoding = new StatCard("Coding Solved", "0", "Algorithms completed", "💻", cs.getWarning(), themeService);
        statMock = new StatCard("Mock Interviews", "0", "Simulations completed", "🎙️", cs.getPrimary(), themeService);

        row.add(statAttempted);
        row.add(statAccuracy);
        row.add(statCoding);
        row.add(statMock);
        return row;
    }

    private JPanel createChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);

        // Chart 1: Weekly Activity
        ModernCard card1 = new ModernCard(themeService);
        card1.setLayout(new BorderLayout());
        card1.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel title1 = new JLabel("Weekly Activity Trend");
        title1.setFont(AppConstants.FONT_HEADING);
        title1.setForeground(themeService.getCurrentScheme().getText());
        card1.add(title1, BorderLayout.NORTH);

        weeklyChart = new WeeklyActivityChart(themeService);
        card1.add(weeklyChart, BorderLayout.CENTER);

        // Chart 2: Topic Mastery
        ModernCard card2 = new ModernCard(themeService);
        card2.setLayout(new BorderLayout());
        card2.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel title2 = new JLabel("Topic Mastery (% Accuracy)");
        title2.setFont(AppConstants.FONT_HEADING);
        title2.setForeground(themeService.getCurrentScheme().getText());
        card2.add(title2, BorderLayout.NORTH);

        topicChart = new TopicPerformanceChart(themeService);
        card2.add(topicChart, BorderLayout.CENTER);

        row.add(card1);
        row.add(card2);
        return row;
    }

    private JPanel createBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);

        // Recent Activity Table
        ModernCard recentCard = new ModernCard(themeService);
        recentCard.setLayout(new BorderLayout(0, 10));
        recentCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel recentTitle = new JLabel("Recent Activity");
        recentTitle.setFont(AppConstants.FONT_HEADING);
        recentTitle.setForeground(themeService.getCurrentScheme().getText());

        JButton viewAllBtn = new JButton("View All →");
        viewAllBtn.setFont(AppConstants.FONT_SMALL_BOLD);
        viewAllBtn.setBorderPainted(false);
        viewAllBtn.setContentAreaFilled(false);
        viewAllBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewAllBtn.setForeground(themeService.getCurrentScheme().getPrimary());
        viewAllBtn.addActionListener(e -> navigationHandler.accept("history"));

        headerPanel.add(recentTitle, BorderLayout.WEST);
        headerPanel.add(viewAllBtn, BorderLayout.EAST);
        recentCard.add(headerPanel, BorderLayout.NORTH);

        recentTableModel = new DefaultTableModel(new String[]{"Title", "Type", "Score / Acc", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        recentTable = new JTable(recentTableModel);
        recentTable.setRowHeight(32);
        recentTable.setShowVerticalLines(false);
        recentTable.setShowHorizontalLines(true);
        recentTable.setFont(AppConstants.FONT_SMALL);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        recentTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        recentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        recentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane tableScroll = new JScrollPane(recentTable);
        tableScroll.setBorder(null);
        tableScroll.setOpaque(false);
        tableScroll.getViewport().setOpaque(false);
        tableScroll.setPreferredSize(new Dimension(380, 150));
        recentCard.add(tableScroll, BorderLayout.CENTER);

        // Smart Revision & Practice Recommendations
        ModernCard recCard = new ModernCard(themeService);
        recCard.setLayout(new BorderLayout(0, 10));
        recCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel recTitle = new JLabel("Recommended Focus Topics");
        recTitle.setFont(AppConstants.FONT_HEADING);
        recTitle.setForeground(themeService.getCurrentScheme().getText());
        recCard.add(recTitle, BorderLayout.NORTH);

        revisionPanel = new JPanel();
        revisionPanel.setOpaque(false);
        revisionPanel.setLayout(new BoxLayout(revisionPanel, BoxLayout.Y_AXIS));

        recCard.add(revisionPanel, BorderLayout.CENTER);

        row.add(recentCard);
        row.add(recCard);
        return row;
    }

    public void refreshData() {
        UserPreferences prefs = preferencesRepository.load();

        // Greeting
        LocalTime now = LocalTime.now();
        String timeGreeting = now.getHour() < 12 ? "Good morning" : (now.getHour() < 17 ? "Good afternoon" : "Good evening");
        greetingLabel.setText(timeGreeting + ", " + prefs.getDisplayName() + "! Ready to prepare?");

        // Stats
        statAttempted.setValue(String.valueOf(analyticsService.getTotalAttempts()));
        statAccuracy.setValue(analyticsService.getAverageAccuracy() + "%");
        statCoding.setValue(String.valueOf(analyticsService.getCodingProblemsSolved()));
        statMock.setValue(String.valueOf(analyticsService.getMockInterviewsCompleted()));

        // Charts
        weeklyChart.setData(analyticsService.getWeeklyActivity());
        topicChart.setData(analyticsService.getCategoryAccuracyMap());

        // Recent Activity Table
        recentTableModel.setRowCount(0);
        List<AttemptRecord> recent = analyticsService.getRecentAttempts(5);
        if (recent.isEmpty()) {
            recentTableModel.addRow(new Object[]{"No recent activity", "-", "-", "-"});
        } else {
            for (AttemptRecord r : recent) {
                recentTableModel.addRow(new Object[]{
                        r.getTitle(),
                        r.getType().getDisplayName(),
                        Math.round(r.getAccuracyPercentage()) + "%",
                        DateTimeUtils.formatShortDate(r.getTimestamp())
                });
            }
        }

        // Recommendations
        revisionPanel.removeAll();
        List<Map.Entry<String, Double>> focusTopics = analyticsService.getTopicsNeedingImprovement(3);
        if (focusTopics.isEmpty()) {
            JLabel emptyLabel = new JLabel("Complete assessments to receive personalized topic recommendations!");
            emptyLabel.setFont(AppConstants.FONT_SMALL);
            emptyLabel.setForeground(themeService.getCurrentScheme().getTextMuted());
            revisionPanel.add(emptyLabel);
        } else {
            for (Map.Entry<String, Double> entry : focusTopics) {
                JPanel item = new JPanel(new BorderLayout(8, 4));
                item.setOpaque(false);
                item.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));

                JLabel topicName = new JLabel("📌 " + entry.getKey());
                topicName.setFont(AppConstants.FONT_REGULAR_BOLD);
                topicName.setForeground(themeService.getCurrentScheme().getText());

                JLabel accLabel = new JLabel(Math.round(entry.getValue()) + "% Accuracy");
                accLabel.setFont(AppConstants.FONT_SMALL_BOLD);
                accLabel.setForeground(entry.getValue() < 60 ? themeService.getCurrentScheme().getDanger() : themeService.getCurrentScheme().getWarning());

                item.add(topicName, BorderLayout.WEST);
                item.add(accLabel, BorderLayout.EAST);
                revisionPanel.add(item);
            }
        }
        revisionPanel.revalidate();
        revisionPanel.repaint();

        demoDataBtn.setText(prefs.isDemoDataLoaded() ? "Clear Demo" : "Load Demo");
    }

    private void toggleDemoData() {
        UserPreferences prefs = preferencesRepository.load();
        if (prefs.isDemoDataLoaded()) {
            historyService.clearHistory();
            prefs.setDemoDataLoaded(false);
            preferencesRepository.save(prefs);
            refreshData();
            JOptionPane.showMessageDialog(this, "Sample demo data has been cleared.", "Data Cleared", JOptionPane.INFORMATION_MESSAGE);
        } else {
            historyService.loadDemoData();
            prefs.setDemoDataLoaded(true);
            preferencesRepository.save(prefs);
            refreshData();
            JOptionPane.showMessageDialog(this, "Loaded realistic sample demo data for exploration!", "Demo Data Ready", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        greetingLabel.setForeground(scheme.getText());
        quoteLabel.setForeground(scheme.getTextMuted());
        recentTable.setBackground(scheme.getCards());
        recentTable.setForeground(scheme.getText());
        recentTable.setGridColor(scheme.getBorders());
        repaint();
    }
}
