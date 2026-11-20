package com.interviewiq.ui.panels;

import com.interviewiq.core.InterviewEngine;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.BadgeLabel;
import com.interviewiq.ui.components.ModernButton;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;
import com.interviewiq.util.DateTimeUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Module E: Realistic Mock Interview simulation with setup wizard, live countdown timer,
 * candidate answer input, and comprehensive final performance report.
 */
public class MockInterviewPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final QuestionService questionService;
    private final HistoryService historyService;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    // 1. Setup Screen Controls
    private JComboBox<String> setupCategoryCombo;
    private JComboBox<Difficulty> setupDifficultyCombo;
    private JComboBox<Integer> setupQuestionCountCombo;
    private JComboBox<Integer> setupDurationCombo;

    // 2. Active Interview Screen Controls
    private InterviewEngine currentEngine;
    private Timer interviewTimer;
    private JLabel timerLabel;
    private JLabel progressLabel;
    private JLabel questionTitleLabel;
    private JPanel badgePanel;
    private JTextArea questionPromptArea;
    private JTextArea candidateAnswerArea;
    private JLabel wordCountLabel;
    private ModernButton flagBtn;
    private ModernButton prevBtn;
    private ModernButton nextBtn;
    private JPanel questionPalettePanel;
    private List<JButton> paletteButtons = new ArrayList<>();

    // 3. Results Screen Controls
    private JLabel resultScoreLabel;
    private JLabel resultStatusLabel;
    private JLabel resultTimeLabel;
    private JLabel resultCompletionLabel;
    private JPanel questionsReviewPanel;

    public MockInterviewPanel(ThemeService themeService, QuestionService questionService, HistoryService historyService) {
        this.themeService = themeService;
        this.questionService = questionService;
        this.historyService = historyService;

        setLayout(new BorderLayout());
        setOpaque(false);

        container.setOpaque(false);
        container.add(createSetupScreen(), "SETUP");
        container.add(createActiveScreen(), "ACTIVE");
        container.add(createResultsScreen(), "RESULTS");

        add(container, BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        showScreen("SETUP");
    }

    private void showScreen(String name) {
        cardLayout.show(container, name);
    }

    // ==========================================
    // SCREEN 1: SETUP
    // ==========================================
    private JPanel createSetupScreen() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        wrapper.setOpaque(false);

        ModernCard card = new ModernCard(themeService, 16, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        card.setPreferredSize(new Dimension(560, 520));

        JLabel title = new JLabel("Mock Interview Simulation");
        title.setFont(AppConstants.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Simulate a real-world technical interview with realistic timed questions.");
        subtitle.setFont(AppConstants.FONT_REGULAR);
        subtitle.setForeground(themeService.getCurrentScheme().getTextMuted());
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));

        // Form Fields
        setupCategoryCombo = new JComboBox<>(new String[]{
                "General Technical Interview",
                "Java Programming",
                "Python Programming",
                "Data Structures & Algorithms",
                "Object-Oriented Programming",
                "Database Management Systems",
                "Operating Systems",
                "Computer Networks",
                "Software Engineering"
        });
        setupDifficultyCombo = new JComboBox<>(new Difficulty[]{Difficulty.ALL, Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD});
        setupDifficultyCombo.setSelectedItem(Difficulty.MEDIUM);
        setupQuestionCountCombo = new JComboBox<>(new Integer[]{3, 5, 8, 10});
        setupQuestionCountCombo.setSelectedItem(5);
        setupDurationCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        setupDurationCombo.setSelectedItem(15);

        card.add(createFormField("Interview Focus Domain:", setupCategoryCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Target Difficulty:", setupDifficultyCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Number of Questions:", setupQuestionCountCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Session Duration (Minutes):", setupDurationCombo));
        card.add(Box.createVerticalStrut(28));

        ModernButton startBtn = new ModernButton("Start Mock Interview Now 🚀", ModernButton.ButtonType.PRIMARY, themeService);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startBtn.setMaximumSize(new Dimension(500, 44));
        startBtn.setAlignmentX(LEFT_ALIGNMENT);
        startBtn.addActionListener(e -> startInterview());

        card.add(startBtn);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel createFormField(String labelText, JComboBox<?> combo) {
        JPanel field = new JPanel(new BorderLayout(10, 4));
        field.setOpaque(false);
        field.setMaximumSize(new Dimension(500, 50));
        field.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(AppConstants.FONT_REGULAR_BOLD);
        combo.setPreferredSize(new Dimension(240, 32));

        field.add(lbl, BorderLayout.WEST);
        field.add(combo, BorderLayout.EAST);
        return field;
    }

    // ==========================================
    // SCREEN 2: ACTIVE INTERVIEW
    // ==========================================
    private JPanel createActiveScreen() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        // 1. Sticky Header (Timer, Progress, End Button)
        ModernCard header = new ModernCard(themeService, 12, true);
        header.setLayout(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        progressLabel = new JLabel("Question 1 of 5");
        progressLabel.setFont(AppConstants.FONT_HEADING);

        timerLabel = new JLabel("⏱️ 15:00");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(themeService.getCurrentScheme().getPrimary());

        ModernButton finishBtn = new ModernButton("Finish Interview", ModernButton.ButtonType.DANGER, themeService);
        finishBtn.setPreferredSize(new Dimension(130, 34));
        finishBtn.addActionListener(e -> confirmEndInterview());

        header.add(progressLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.CENTER);
        header.add(finishBtn, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        // 2. Center: Question and Candidate Answer
        ModernCard centerCard = new ModernCard(themeService, 14, true);
        centerCard.setLayout(new BorderLayout(16, 14));
        centerCard.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel qTop = new JPanel();
        qTop.setOpaque(false);
        qTop.setLayout(new BoxLayout(qTop, BoxLayout.Y_AXIS));

        questionTitleLabel = new JLabel("Question Title");
        questionTitleLabel.setFont(AppConstants.FONT_TITLE);

        badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgePanel.setOpaque(false);

        questionPromptArea = new JTextArea(3, 40);
        questionPromptArea.setLineWrap(true);
        questionPromptArea.setWrapStyleWord(true);
        questionPromptArea.setEditable(false);
        questionPromptArea.setOpaque(false);
        questionPromptArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        qTop.add(questionTitleLabel);
        qTop.add(Box.createVerticalStrut(4));
        qTop.add(badgePanel);
        qTop.add(Box.createVerticalStrut(8));
        qTop.add(questionPromptArea);
        centerCard.add(qTop, BorderLayout.NORTH);

        // Candidate Answer Area
        JPanel ansPanel = new JPanel(new BorderLayout(0, 6));
        ansPanel.setOpaque(false);

        JLabel ansHint = new JLabel("Your Structured Answer (Include: Definition, Key mechanisms, Code/Example, and Trade-offs):");
        ansHint.setFont(AppConstants.FONT_SMALL_BOLD);
        ansPanel.add(ansHint, BorderLayout.NORTH);

        candidateAnswerArea = new JTextArea(8, 40);
        candidateAnswerArea.setFont(AppConstants.FONT_REGULAR);
        candidateAnswerArea.setLineWrap(true);
        candidateAnswerArea.setWrapStyleWord(true);
        candidateAnswerArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { saveCurrentAnswer(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { saveCurrentAnswer(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { saveCurrentAnswer(); }
        });

        JScrollPane ansScroll = new JScrollPane(candidateAnswerArea);
        ansPanel.add(ansScroll, BorderLayout.CENTER);

        wordCountLabel = new JLabel("Word count: 0 words");
        wordCountLabel.setFont(AppConstants.FONT_SMALL);
        ansPanel.add(wordCountLabel, BorderLayout.SOUTH);

        centerCard.add(ansPanel, BorderLayout.CENTER);

        // Bottom Navigation & Question Palette
        JPanel botNav = new JPanel(new BorderLayout(10, 8));
        botNav.setOpaque(false);

        questionPalettePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        questionPalettePanel.setOpaque(false);
        botNav.add(questionPalettePanel, BorderLayout.NORTH);

        JPanel navBtns = new JPanel(new BorderLayout());
        navBtns.setOpaque(false);

        prevBtn = new ModernButton("← Previous Question", ModernButton.ButtonType.OUTLINE, themeService);
        prevBtn.addActionListener(e -> navigateQuestion(-1));

        flagBtn = new ModernButton("🚩 Flag for Review", ModernButton.ButtonType.OUTLINE, themeService);
        flagBtn.addActionListener(e -> toggleFlag());

        nextBtn = new ModernButton("Next Question →", ModernButton.ButtonType.PRIMARY, themeService);
        nextBtn.addActionListener(e -> navigateQuestion(1));

        navBtns.add(prevBtn, BorderLayout.WEST);
        navBtns.add(flagBtn, BorderLayout.CENTER);
        navBtns.add(nextBtn, BorderLayout.EAST);
        botNav.add(navBtns, BorderLayout.SOUTH);

        centerCard.add(botNav, BorderLayout.SOUTH);
        p.add(centerCard, BorderLayout.CENTER);
        return p;
    }

    // ==========================================
    // SCREEN 3: FINAL REPORT
    // ==========================================
    private JPanel createResultsScreen() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        ModernCard reportCard = new ModernCard(themeService, 16, true);
        reportCard.setLayout(new BorderLayout(0, 16));
        reportCard.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

        // Header KPI summary
        JPanel topSummary = new JPanel(new GridLayout(1, 4, 14, 0));
        topSummary.setOpaque(false);

        resultScoreLabel = new JLabel("0.0 / 100", SwingConstants.CENTER);
        resultScoreLabel.setFont(AppConstants.FONT_BIG_STAT);
        resultScoreLabel.setForeground(themeService.getCurrentScheme().getPrimary());

        resultStatusLabel = new JLabel("Evaluation", SwingConstants.CENTER);
        resultStatusLabel.setFont(AppConstants.FONT_HEADING);

        resultTimeLabel = new JLabel("00:00", SwingConstants.CENTER);
        resultTimeLabel.setFont(AppConstants.FONT_BIG_STAT);

        resultCompletionLabel = new JLabel("0%", SwingConstants.CENTER);
        resultCompletionLabel.setFont(AppConstants.FONT_BIG_STAT);

        topSummary.add(createSummaryPill("Performance Score", resultScoreLabel));
        topSummary.add(createSummaryPill("Interview Readiness", resultStatusLabel));
        topSummary.add(createSummaryPill("Time Taken", resultTimeLabel));
        topSummary.add(createSummaryPill("Questions Attempted", resultCompletionLabel));

        reportCard.add(topSummary, BorderLayout.NORTH);

        // Center: Question-by-Question breakdown & self ratings
        questionsReviewPanel = new JPanel();
        questionsReviewPanel.setOpaque(false);
        questionsReviewPanel.setLayout(new BoxLayout(questionsReviewPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(questionsReviewPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        reportCard.add(scroll, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setOpaque(false);

        ModernButton retakeBtn = new ModernButton("Take Another Interview", ModernButton.ButtonType.PRIMARY, themeService);
        retakeBtn.addActionListener(e -> showScreen("SETUP"));

        footer.add(retakeBtn);
        reportCard.add(footer, BorderLayout.SOUTH);

        wrapper.add(reportCard, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createSummaryPill(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(true);
        p.setBackground(themeService.getCurrentScheme().isDark() ? new Color(20, 30, 48) : new Color(243, 246, 252));
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(AppConstants.FONT_SMALL_BOLD);
        t.setForeground(themeService.getCurrentScheme().getTextMuted());

        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private void startInterview() {
        String category = (String) setupCategoryCombo.getSelectedItem();
        Difficulty difficulty = (Difficulty) setupDifficultyCombo.getSelectedItem();
        int count = (Integer) setupQuestionCountCombo.getSelectedItem();
        int minutes = (Integer) setupDurationCombo.getSelectedItem();

        currentEngine = new InterviewEngine(category, difficulty, questionService.getAllQuestions(), count, minutes * 60L);

        // Build question palette buttons
        questionPalettePanel.removeAll();
        paletteButtons.clear();
        for (int i = 0; i < currentEngine.getTotalQuestions(); i++) {
            final int idx = i;
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.setFont(AppConstants.FONT_SMALL_BOLD);
            btn.setPreferredSize(new Dimension(36, 32));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                saveCurrentAnswer();
                currentEngine.jumpTo(idx);
                renderQuestion();
            });
            paletteButtons.add(btn);
            questionPalettePanel.add(btn);
        }
        questionPalettePanel.revalidate();
        questionPalettePanel.repaint();

        // Start Countdown Timer
        if (interviewTimer != null && interviewTimer.isRunning()) {
            interviewTimer.stop();
        }
        interviewTimer = new Timer(1000, e -> {
            if (currentEngine != null) {
                currentEngine.decrementTimer();
                updateTimerDisplay();
                if (currentEngine.getRemainingSeconds() <= 0) {
                    interviewTimer.stop();
                    JOptionPane.showMessageDialog(this, "Interview time has expired! Generating your performance evaluation.", "Time Expired", JOptionPane.WARNING_MESSAGE);
                    finishInterview();
                }
            }
        });
        interviewTimer.start();

        renderQuestion();
        showScreen("ACTIVE");
    }

    private void updateTimerDisplay() {
        if (currentEngine == null) return;
        long rem = currentEngine.getRemainingSeconds();
        timerLabel.setText("⏱️ " + DateTimeUtils.formatSecondsToMinutes(rem));
        ColorScheme cs = themeService.getCurrentScheme();
        if (rem < 60) {
            timerLabel.setForeground(cs.getDanger());
        } else if (rem < 180) {
            timerLabel.setForeground(cs.getWarning());
        } else {
            timerLabel.setForeground(cs.getPrimary());
        }
    }

    private void renderQuestion() {
        if (currentEngine == null) return;
        Question q = currentEngine.getCurrentQuestion();
        if (q == null) return;

        progressLabel.setText("Question " + (currentEngine.getCurrentIndex() + 1) + " of " + currentEngine.getTotalQuestions());
        questionTitleLabel.setText(q.getTitle());

        badgePanel.removeAll();
        badgePanel.add(BadgeLabel.createCategoryBadge(q.getCategory(), themeService));
        badgePanel.add(BadgeLabel.createDifficultyBadge(q.getDifficulty(), themeService));
        badgePanel.revalidate();
        badgePanel.repaint();

        questionPromptArea.setText(q.getQuestionText());

        // Load Candidate's previous response
        candidateAnswerArea.setText(currentEngine.getAnswer(currentEngine.getCurrentIndex()));
        updateWordCount();

        prevBtn.setEnabled(currentEngine.hasPrevious());
        nextBtn.setText(currentEngine.hasNext() ? "Next Question →" : "Finish Interview ✓");
        flagBtn.setText(currentEngine.isFlagged(currentEngine.getCurrentIndex()) ? "🚩 Flagged" : "⚐ Flag for Review");

        updatePaletteStyles();
    }

    private void saveCurrentAnswer() {
        if (currentEngine != null) {
            currentEngine.saveAnswer(currentEngine.getCurrentIndex(), candidateAnswerArea.getText());
            updateWordCount();
            updatePaletteStyles();
        }
    }

    private void updateWordCount() {
        String text = candidateAnswerArea.getText().trim();
        int words = text.isEmpty() ? 0 : text.split("\\s+").length;
        wordCountLabel.setText("Word count: " + words + " words (Target: 30-80 words with technical depth)");
    }

    private void toggleFlag() {
        if (currentEngine != null) {
            currentEngine.toggleFlag(currentEngine.getCurrentIndex());
            flagBtn.setText(currentEngine.isFlagged(currentEngine.getCurrentIndex()) ? "🚩 Flagged" : "⚐ Flag for Review");
            updatePaletteStyles();
        }
    }

    private void updatePaletteStyles() {
        if (currentEngine == null) return;
        ColorScheme cs = themeService.getCurrentScheme();
        for (int i = 0; i < paletteButtons.size(); i++) {
            JButton btn = paletteButtons.get(i);
            boolean isCur = i == currentEngine.getCurrentIndex();
            boolean isAns = currentEngine.isAnswered(i);
            boolean isFlg = currentEngine.isFlagged(i);

            if (isCur) {
                btn.setBackground(cs.getPrimary());
                btn.setForeground(Color.WHITE);
            } else if (isFlg) {
                btn.setBackground(cs.getWarning());
                btn.setForeground(Color.WHITE);
            } else if (isAns) {
                btn.setBackground(cs.getSuccess());
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(cs.getCards());
                btn.setForeground(cs.getText());
            }
        }
    }

    private void navigateQuestion(int delta) {
        saveCurrentAnswer();
        if (delta > 0) {
            if (currentEngine.hasNext()) {
                currentEngine.next();
                renderQuestion();
            } else {
                confirmEndInterview();
            }
        } else if (delta < 0) {
            if (currentEngine.hasPrevious()) {
                currentEngine.previous();
                renderQuestion();
            }
        }
    }

    private void confirmEndInterview() {
        int answered = currentEngine.getAnsweredCount();
        int total = currentEngine.getTotalQuestions();
        String msg = "Are you sure you want to end this interview session?\n\nQuestions Answered: " + answered + " / " + total;
        int opt = JOptionPane.showConfirmDialog(this, msg, "End Interview Confirmation", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            if (interviewTimer != null) interviewTimer.stop();
            finishInterview();
        }
    }

    private void finishInterview() {
        saveCurrentAnswer();
        AttemptRecord record = currentEngine.submit();
        historyService.recordAttempt(record);

        // Populate Results Screen
        resultScoreLabel.setText(Math.round(record.getAccuracyPercentage()) + "%");
        resultStatusLabel.setText(record.getNotes());
        resultTimeLabel.setText(DateTimeUtils.formatDurationFriendly(record.getDurationSeconds()));
        resultCompletionLabel.setText(record.getCompletedItems() + " / " + record.getTotalItems());

        questionsReviewPanel.removeAll();
        for (int i = 0; i < currentEngine.getTotalQuestions(); i++) {
            Question q = currentEngine.getQuestions().get(i);
            double score = currentEngine.evaluateAnswer(i);
            String candAns = currentEngine.getAnswer(i);

            ModernCard reviewItem = new ModernCard(themeService, 12, true);
            reviewItem.setLayout(new BorderLayout(10, 8));
            reviewItem.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            JLabel qTitle = new JLabel("Q" + (i + 1) + ": " + q.getTitle() + " — Score: " + Math.round(score) + "/100");
            qTitle.setFont(AppConstants.FONT_HEADING);
            qTitle.setForeground(score >= 70 ? themeService.getCurrentScheme().getSuccess() : themeService.getCurrentScheme().getWarning());

            JTextArea ansReview = new JTextArea("Your Response:\n" + (candAns.isEmpty() ? "(No answer submitted)" : candAns) +
                    "\n\nModel Answer Reference:\n" + q.getAnswerText());
            ansReview.setFont(AppConstants.FONT_REGULAR);
            ansReview.setLineWrap(true);
            ansReview.setWrapStyleWord(true);
            ansReview.setEditable(false);
            ansReview.setOpaque(false);

            reviewItem.add(qTitle, BorderLayout.NORTH);
            reviewItem.add(ansReview, BorderLayout.CENTER);
            questionsReviewPanel.add(reviewItem);
            questionsReviewPanel.add(Box.createVerticalStrut(10));
        }
        questionsReviewPanel.revalidate();
        questionsReviewPanel.repaint();

        showScreen("RESULTS");
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        questionTitleLabel.setForeground(scheme.getText());
        questionPromptArea.setForeground(scheme.getText());
        candidateAnswerArea.setBackground(scheme.isDark() ? new Color(13, 20, 36) : new Color(250, 252, 255));
        candidateAnswerArea.setForeground(scheme.getText());
        wordCountLabel.setForeground(scheme.getTextMuted());
        updatePaletteStyles();
        repaint();
    }
}
