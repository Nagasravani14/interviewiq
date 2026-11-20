package com.interviewiq.ui.panels;

import com.interviewiq.model.Difficulty;
import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.ModernButton;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Module I: User preferences, profile settings, Light/Dark theme toggle,
 * default assessment configurations, data management, and About section.
 */
public class SettingsPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final PreferencesRepository preferencesRepository;
    private final HistoryService historyService;
    private final QuestionService questionService;
    private final Runnable onPreferencesUpdated;

    // Form inputs
    private JTextField displayNameField;
    private JTextField targetRoleField;
    private JComboBox<Integer> dailyGoalCombo;
    private JComboBox<String> themeCombo;
    private JComboBox<Difficulty> defaultDiffCombo;
    private JComboBox<Integer> defaultQuizLengthCombo;
    private JComboBox<Integer> defaultTimerCombo;
    private JCheckBox timerEnabledCheck;

    public SettingsPanel(ThemeService themeService, PreferencesRepository preferencesRepository,
                         HistoryService historyService, QuestionService questionService,
                         Runnable onPreferencesUpdated) {
        this.themeService = themeService;
        this.preferencesRepository = preferencesRepository;
        this.historyService = historyService;
        this.questionService = questionService;
        this.onPreferencesUpdated = onPreferencesUpdated;

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // 1. Profile Settings
        content.add(createProfileCard());
        content.add(Box.createVerticalStrut(18));

        // 2. Appearance & Assessment Defaults
        content.add(createAssessmentDefaultsCard());
        content.add(Box.createVerticalStrut(18));

        // 3. Data Storage & Reset Card
        content.add(createDataManagementCard());
        content.add(Box.createVerticalStrut(18));

        // 4. About InterviewIQ
        content.add(createAboutCard());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        loadCurrentPreferences();
    }

    private JPanel createProfileCard() {
        ModernCard card = new ModernCard(themeService, 14, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("User Profile & Career Goal");
        title.setFont(AppConstants.FONT_HEADING);
        card.add(title);
        card.add(Box.createVerticalStrut(14));

        displayNameField = new JTextField(20);
        targetRoleField = new JTextField(20);
        dailyGoalCombo = new JComboBox<>(new Integer[]{3, 5, 8, 10, 15});

        card.add(createRow("Display Name:", displayNameField));
        card.add(Box.createVerticalStrut(10));
        card.add(createRow("Target Software Role:", targetRoleField));
        card.add(Box.createVerticalStrut(10));
        card.add(createRow("Daily Question Target:", dailyGoalCombo));
        card.add(Box.createVerticalStrut(16));

        ModernButton saveProfileBtn = new ModernButton("Save Profile Changes", ModernButton.ButtonType.PRIMARY, themeService);
        saveProfileBtn.addActionListener(e -> savePreferences());
        card.add(saveProfileBtn);

        return card;
    }

    private JPanel createAssessmentDefaultsCard() {
        ModernCard card = new ModernCard(themeService, 14, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Appearance & Assessment Preferences");
        title.setFont(AppConstants.FONT_HEADING);
        card.add(title);
        card.add(Box.createVerticalStrut(14));

        themeCombo = new JComboBox<>(new String[]{"Light Mode", "Dark Mode"});
        themeCombo.addActionListener(e -> {
            boolean wantDark = "Dark Mode".equals(themeCombo.getSelectedItem());
            if (themeService.isDark() != wantDark) {
                themeService.setDark(wantDark);
            }
        });

        defaultDiffCombo = new JComboBox<>(Difficulty.values());
        defaultQuizLengthCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        defaultTimerCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20, 30});
        timerEnabledCheck = new JCheckBox("Enable live countdown timer for quizzes and interviews");
        timerEnabledCheck.setOpaque(false);
        timerEnabledCheck.setFont(AppConstants.FONT_REGULAR);

        card.add(createRow("Theme Mode (Live Preview):", themeCombo));
        card.add(Box.createVerticalStrut(10));
        card.add(createRow("Default Assessment Difficulty:", defaultDiffCombo));
        card.add(Box.createVerticalStrut(10));
        card.add(createRow("Default Quiz Length:", defaultQuizLengthCombo));
        card.add(Box.createVerticalStrut(10));
        card.add(createRow("Default Interview Timer (Minutes):", defaultTimerCombo));
        card.add(Box.createVerticalStrut(10));
        card.add(timerEnabledCheck);
        card.add(Box.createVerticalStrut(16));

        ModernButton saveDefaultsBtn = new ModernButton("Apply Preferences", ModernButton.ButtonType.PRIMARY, themeService);
        saveDefaultsBtn.addActionListener(e -> savePreferences());
        card.add(saveDefaultsBtn);

        return card;
    }

    private JPanel createDataManagementCard() {
        ModernCard card = new ModernCard(themeService, 14, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Data Persistence & Storage");
        title.setFont(AppConstants.FONT_HEADING);
        card.add(title);
        card.add(Box.createVerticalStrut(6));

        JLabel pathLbl = new JLabel("Storage Path: " + preferencesRepository.getStoragePath().getParent());
        pathLbl.setFont(AppConstants.FONT_SMALL);
        pathLbl.setForeground(themeService.getCurrentScheme().getTextMuted());
        card.add(pathLbl);
        card.add(Box.createVerticalStrut(14));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setOpaque(false);

        ModernButton loadDemoBtn = new ModernButton("Load Realistic Sample Data", ModernButton.ButtonType.OUTLINE, themeService);
        loadDemoBtn.addActionListener(e -> {
            historyService.loadDemoData();
            UserPreferences p = preferencesRepository.load();
            p.setDemoDataLoaded(true);
            preferencesRepository.save(p);
            JOptionPane.showMessageDialog(this, "Sample demo assessment records loaded successfully!", "Demo Loaded", JOptionPane.INFORMATION_MESSAGE);
            if (onPreferencesUpdated != null) onPreferencesUpdated.run();
        });

        ModernButton resetProgressBtn = new ModernButton("Reset Question Bookmarks & Progress", ModernButton.ButtonType.OUTLINE, themeService);
        resetProgressBtn.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Reset all bookmarks and understood questions to unread?", "Reset Progress", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                questionService.resetAllProgress();
                JOptionPane.showMessageDialog(this, "Question bookmarks and progress reset.", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ModernButton clearDataBtn = new ModernButton("Clear All Attempt History", ModernButton.ButtonType.DANGER, themeService);
        clearDataBtn.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this, "Permanently delete all assessment attempts?", "Clear All Data", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                historyService.clearHistory();
                UserPreferences p = preferencesRepository.load();
                p.setDemoDataLoaded(false);
                preferencesRepository.save(p);
                JOptionPane.showMessageDialog(this, "All history records erased.", "History Cleared", JOptionPane.INFORMATION_MESSAGE);
                if (onPreferencesUpdated != null) onPreferencesUpdated.run();
            }
        });

        btnRow.add(loadDemoBtn);
        btnRow.add(resetProgressBtn);
        btnRow.add(clearDataBtn);
        card.add(btnRow);

        return card;
    }

    private JPanel createAboutCard() {
        ModernCard card = new ModernCard(themeService, 14, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel(AppConstants.APP_NAME + " – " + AppConstants.APP_SUBTITLE);
        title.setFont(AppConstants.FONT_HEADING);
        card.add(title);
        card.add(Box.createVerticalStrut(8));

        JLabel info = new JLabel("<html><body>" +
                "<b>Version:</b> 1.0.0 (Production Build)<br>" +
                "<b>Architecture:</b> Layered Swing MVC with FlatLaf Modern Theme Engine & Jackson Persistence<br>" +
                "<b>Tech Stack:</b> Java 17+, FlatLaf 3.5.4, Jackson 2.18.2, JUnit 5.11.4, Maven 3.9.9<br>" +
                "<b>Engineered for:</b> Software Engineering Students & Technical Interview Preparation" +
                "</body></html>");
        info.setFont(AppConstants.FONT_REGULAR);
        card.add(info);

        return card;
    }

    private JPanel createRow(String label, Component comp) {
        JPanel p = new JPanel(new BorderLayout(14, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(540, 36));

        JLabel l = new JLabel(label);
        l.setFont(AppConstants.FONT_REGULAR_BOLD);
        p.add(l, BorderLayout.WEST);
        p.add(comp, BorderLayout.EAST);
        return p;
    }

    private void loadCurrentPreferences() {
        UserPreferences p = preferencesRepository.load();
        displayNameField.setText(p.getDisplayName());
        targetRoleField.setText(p.getTargetRole());
        dailyGoalCombo.setSelectedItem(p.getDailyQuestionGoal());
        themeCombo.setSelectedItem("DARK".equalsIgnoreCase(p.getTheme()) ? "Dark Mode" : "Light Mode");
        defaultDiffCombo.setSelectedItem(p.getDefaultDifficulty());
        defaultQuizLengthCombo.setSelectedItem(p.getDefaultQuizLength());
        defaultTimerCombo.setSelectedItem(p.getDefaultTimerMinutes());
        timerEnabledCheck.setSelected(p.isTimerEnabled());
    }

    private void savePreferences() {
        UserPreferences p = preferencesRepository.load();
        p.setDisplayName(displayNameField.getText().trim().isEmpty() ? "Alex Johnson" : displayNameField.getText().trim());
        p.setTargetRole(targetRoleField.getText().trim());
        p.setDailyQuestionGoal((Integer) dailyGoalCombo.getSelectedItem());
        p.setDefaultDifficulty((Difficulty) defaultDiffCombo.getSelectedItem());
        p.setDefaultQuizLength((Integer) defaultQuizLengthCombo.getSelectedItem());
        p.setDefaultTimerMinutes((Integer) defaultTimerCombo.getSelectedItem());
        p.setTimerEnabled(timerEnabledCheck.isSelected());

        preferencesRepository.save(p);
        JOptionPane.showMessageDialog(this, "Preferences saved successfully!", "Settings Saved", JOptionPane.INFORMATION_MESSAGE);

        if (onPreferencesUpdated != null) {
            onPreferencesUpdated.run();
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        repaint();
    }
}
