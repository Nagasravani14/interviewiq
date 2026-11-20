package com.interviewiq.ui.panels;

import com.interviewiq.core.QuizEngine;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.QuizQuestion;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
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
 * Module F: MCQ Quiz Arena with timed questions, 4 interactive options,
 * color-coded question palette, mark for review, and deep-dive answer explanations.
 */
public class QuizArenaPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final QuestionService questionService;
    private final HistoryService historyService;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    // 1. Setup Controls
    private JComboBox<String> quizCategoryCombo;
    private JComboBox<Difficulty> quizDifficultyCombo;
    private JComboBox<Integer> quizCountCombo;
    private JComboBox<Integer> quizTimeCombo;

    // 2. Active Quiz Controls
    private QuizEngine currentEngine;
    private Timer quizTimer;
    private JLabel timerLabel;
    private JLabel progressLabel;
    private JLabel questionTextLabel;
    private JPanel badgePanel;
    private final List<JRadioButton> optionRadios = new ArrayList<>();
    private final List<JPanel> optionCardPanels = new ArrayList<>();
    private ButtonGroup optionGroup;

    private ModernButton flagBtn;
    private ModernButton prevBtn;
    private ModernButton nextBtn;
    private JPanel palettePanel;
    private final List<JButton> paletteButtons = new ArrayList<>();

    // 3. Results Screen Controls
    private JLabel scoreBadgeLabel;
    private JLabel accuracyLabel;
    private JLabel timeTakenLabel;
    private JLabel gradeLabel;
    private JPanel detailedReviewListPanel;

    public QuizArenaPanel(ThemeService themeService, QuestionService questionService, HistoryService historyService) {
        this.themeService = themeService;
        this.questionService = questionService;
        this.historyService = historyService;

        setLayout(new BorderLayout());
        setOpaque(false);

        container.setOpaque(false);
        container.add(createSetupScreen(), "SETUP");
        container.add(createActiveQuizScreen(), "ACTIVE");
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
        card.setPreferredSize(new Dimension(540, 480));

        JLabel title = new JLabel("MCQ Technical Quiz Arena");
        title.setFont(AppConstants.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Test your CS fundamentals with timed multiple-choice questions.");
        subtitle.setFont(AppConstants.FONT_REGULAR);
        subtitle.setForeground(themeService.getCurrentScheme().getTextMuted());
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));

        quizCategoryCombo = new JComboBox<>(new String[]{
                "All Categories",
                "Java Programming",
                "Python Programming",
                "Data Structures & Algorithms",
                "Database Management Systems",
                "Operating Systems",
                "Computer Networks",
                "Software Engineering"
        });
        quizDifficultyCombo = new JComboBox<>(new Difficulty[]{Difficulty.ALL, Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD});
        quizCountCombo = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        quizCountCombo.setSelectedItem(5);
        quizTimeCombo = new JComboBox<>(new Integer[]{5, 10, 15});
        quizTimeCombo.setSelectedItem(5);

        card.add(createFormField("Select Subject Category:", quizCategoryCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Difficulty Level:", quizDifficultyCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Number of Questions:", quizCountCombo));
        card.add(Box.createVerticalStrut(14));
        card.add(createFormField("Time Limit (Minutes):", quizTimeCombo));
        card.add(Box.createVerticalStrut(28));

        ModernButton startBtn = new ModernButton("Launch Quiz Assessment 🎯", ModernButton.ButtonType.PRIMARY, themeService);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startBtn.setMaximumSize(new Dimension(500, 44));
        startBtn.setAlignmentX(LEFT_ALIGNMENT);
        startBtn.addActionListener(e -> startQuiz());

        card.add(startBtn);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel createFormField(String label, JComboBox<?> combo) {
        JPanel f = new JPanel(new BorderLayout(10, 4));
        f.setOpaque(false);
        f.setMaximumSize(new Dimension(500, 50));
        f.setAlignmentX(LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(AppConstants.FONT_REGULAR_BOLD);
        combo.setPreferredSize(new Dimension(240, 32));

        f.add(l, BorderLayout.WEST);
        f.add(combo, BorderLayout.EAST);
        return f;
    }

    // ==========================================
    // SCREEN 2: ACTIVE QUIZ
    // ==========================================
    private JPanel createActiveQuizScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        // Sticky Header (Timer, Progress, Submit)
        ModernCard header = new ModernCard(themeService, 12, true);
        header.setLayout(new BorderLayout(16, 0));
        header.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        progressLabel = new JLabel("Question 1 of 5");
        progressLabel.setFont(AppConstants.FONT_HEADING);

        timerLabel = new JLabel("⏱️ 05:00");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(themeService.getCurrentScheme().getPrimary());

        ModernButton submitQuizBtn = new ModernButton("Submit Quiz", ModernButton.ButtonType.SUCCESS, themeService);
        submitQuizBtn.setPreferredSize(new Dimension(120, 34));
        submitQuizBtn.addActionListener(e -> confirmSubmitQuiz());

        header.add(progressLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.CENTER);
        header.add(submitQuizBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Center: Question and Options Card
        ModernCard center = new ModernCard(themeService, 14, true);
        center.setLayout(new BorderLayout(16, 14));
        center.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel qTop = new JPanel();
        qTop.setOpaque(false);
        qTop.setLayout(new BoxLayout(qTop, BoxLayout.Y_AXIS));

        badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgePanel.setOpaque(false);

        questionTextLabel = new JLabel("Question Statement");
        questionTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        qTop.add(badgePanel);
        qTop.add(Box.createVerticalStrut(6));
        qTop.add(questionTextLabel);
        center.add(qTop, BorderLayout.NORTH);

        // 4 Options Panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        optionsPanel.setOpaque(false);

        optionGroup = new ButtonGroup();
        optionRadios.clear();
        optionCardPanels.clear();

        for (int i = 0; i < 4; i++) {
            final int optIdx = i;
            JPanel optCard = new JPanel(new BorderLayout(10, 0));
            optCard.setOpaque(true);
            optCard.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

            JRadioButton radio = new JRadioButton();
            radio.setFont(AppConstants.FONT_REGULAR);
            radio.setOpaque(false);
            radio.addActionListener(e -> {
                if (currentEngine != null) {
                    currentEngine.selectAnswer(optIdx);
                    updatePaletteStyles();
                }
            });

            optionGroup.add(radio);
            optionRadios.add(radio);
            optionCardPanels.add(optCard);

            optCard.add(radio, BorderLayout.CENTER);
            optionsPanel.add(optCard);
        }

        center.add(optionsPanel, BorderLayout.CENTER);

        // Bottom Navigation and Palette
        JPanel bot = new JPanel(new BorderLayout(10, 8));
        bot.setOpaque(false);

        palettePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        palettePanel.setOpaque(false);
        bot.add(palettePanel, BorderLayout.NORTH);

        JPanel navRow = new JPanel(new BorderLayout());
        navRow.setOpaque(false);

        prevBtn = new ModernButton("← Previous", ModernButton.ButtonType.OUTLINE, themeService);
        prevBtn.addActionListener(e -> navigateQuestion(-1));

        flagBtn = new ModernButton("🚩 Mark for Review", ModernButton.ButtonType.OUTLINE, themeService);
        flagBtn.addActionListener(e -> toggleFlag());

        nextBtn = new ModernButton("Next →", ModernButton.ButtonType.PRIMARY, themeService);
        nextBtn.addActionListener(e -> navigateQuestion(1));

        navRow.add(prevBtn, BorderLayout.WEST);
        navRow.add(flagBtn, BorderLayout.CENTER);
        navRow.add(nextBtn, BorderLayout.EAST);
        bot.add(navRow, BorderLayout.SOUTH);

        center.add(bot, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // ==========================================
    // SCREEN 3: RESULTS & REVIEW
    // ==========================================
    private JPanel createResultsScreen() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        ModernCard card = new ModernCard(themeService, 16, true);
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        // Top KPI Cards
        JPanel summaryRow = new JPanel(new GridLayout(1, 4, 14, 0));
        summaryRow.setOpaque(false);

        scoreBadgeLabel = new JLabel("0 / 0", SwingConstants.CENTER);
        scoreBadgeLabel.setFont(AppConstants.FONT_BIG_STAT);
        scoreBadgeLabel.setForeground(themeService.getCurrentScheme().getPrimary());

        accuracyLabel = new JLabel("0%", SwingConstants.CENTER);
        accuracyLabel.setFont(AppConstants.FONT_BIG_STAT);

        timeTakenLabel = new JLabel("00:00", SwingConstants.CENTER);
        timeTakenLabel.setFont(AppConstants.FONT_BIG_STAT);

        gradeLabel = new JLabel("Grade", SwingConstants.CENTER);
        gradeLabel.setFont(AppConstants.FONT_HEADING);

        summaryRow.add(createSummaryPill("Total Score", scoreBadgeLabel));
        summaryRow.add(createSummaryPill("Accuracy", accuracyLabel));
        summaryRow.add(createSummaryPill("Time Taken", timeTakenLabel));
        summaryRow.add(createSummaryPill("Performance Grade", gradeLabel));

        card.add(summaryRow, BorderLayout.NORTH);

        // Center: Full Question Review List
        detailedReviewListPanel = new JPanel();
        detailedReviewListPanel.setOpaque(false);
        detailedReviewListPanel.setLayout(new BoxLayout(detailedReviewListPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(detailedReviewListPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footer.setOpaque(false);

        ModernButton retakeBtn = new ModernButton("Take Another Quiz", ModernButton.ButtonType.PRIMARY, themeService);
        retakeBtn.addActionListener(e -> showScreen("SETUP"));

        footer.add(retakeBtn);
        card.add(footer, BorderLayout.SOUTH);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createSummaryPill(String title, JLabel val) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(true);
        p.setBackground(themeService.getCurrentScheme().isDark() ? new Color(20, 30, 48) : new Color(243, 246, 252));
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(AppConstants.FONT_SMALL_BOLD);
        t.setForeground(themeService.getCurrentScheme().getTextMuted());

        p.add(t, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private void startQuiz() {
        String category = (String) quizCategoryCombo.getSelectedItem();
        Difficulty difficulty = (Difficulty) quizDifficultyCombo.getSelectedItem();
        int count = (Integer) quizCountCombo.getSelectedItem();
        int minutes = (Integer) quizTimeCombo.getSelectedItem();

        currentEngine = new QuizEngine(category, difficulty, questionService.getAllQuizQuestions(), count, minutes * 60L);

        // Palette
        palettePanel.removeAll();
        paletteButtons.clear();
        for (int i = 0; i < currentEngine.getTotalQuestions(); i++) {
            final int idx = i;
            JButton b = new JButton(String.valueOf(i + 1));
            b.setFont(AppConstants.FONT_SMALL_BOLD);
            b.setPreferredSize(new Dimension(36, 32));
            b.setFocusPainted(false);
            b.addActionListener(e -> {
                currentEngine.jumpTo(idx);
                renderQuestion();
            });
            paletteButtons.add(b);
            palettePanel.add(b);
        }
        palettePanel.revalidate();
        palettePanel.repaint();

        // Timer
        if (quizTimer != null && quizTimer.isRunning()) {
            quizTimer.stop();
        }
        quizTimer = new Timer(1000, e -> {
            if (currentEngine != null) {
                currentEngine.decrementTimer();
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

                if (rem <= 0) {
                    quizTimer.stop();
                    JOptionPane.showMessageDialog(this, "Time is up! Auto-submitting quiz.", "Time Expired", JOptionPane.WARNING_MESSAGE);
                    submitQuiz();
                }
            }
        });
        quizTimer.start();

        renderQuestion();
        showScreen("ACTIVE");
    }

    private void renderQuestion() {
        if (currentEngine == null) return;
        QuizQuestion q = currentEngine.getCurrentQuestion();
        if (q == null) return;

        progressLabel.setText("Question " + (currentEngine.getCurrentIndex() + 1) + " of " + currentEngine.getTotalQuestions());
        questionTextLabel.setText("<html><body style='width: 700px;'>" + q.getQuestionText() + "</body></html>");

        badgePanel.removeAll();
        badgePanel.add(BadgeLabel.createCategoryBadge(q.getCategory(), themeService));
        badgePanel.add(BadgeLabel.createDifficultyBadge(q.getDifficulty(), themeService));
        badgePanel.revalidate();
        badgePanel.repaint();

        // Populate Options
        optionGroup.clearSelection();
        Integer selectedOpt = currentEngine.getSelectedAnswer(currentEngine.getCurrentIndex());

        for (int i = 0; i < 4; i++) {
            JRadioButton radio = optionRadios.get(i);
            JPanel card = optionCardPanels.get(i);
            if (i < q.getOptions().size()) {
                radio.setText(q.getOptions().get(i));
                radio.setVisible(true);
                card.setVisible(true);
                if (selectedOpt != null && selectedOpt == i) {
                    radio.setSelected(true);
                }
            } else {
                radio.setVisible(false);
                card.setVisible(false);
            }
        }

        prevBtn.setEnabled(currentEngine.hasPrevious());
        nextBtn.setText(currentEngine.hasNext() ? "Next →" : "Finish & Review ✓");
        flagBtn.setText(currentEngine.isFlagged(currentEngine.getCurrentIndex()) ? "🚩 Flagged" : "⚐ Mark for Review");

        updatePaletteStyles();
    }

    private void toggleFlag() {
        if (currentEngine != null) {
            currentEngine.toggleFlag(currentEngine.getCurrentIndex());
            flagBtn.setText(currentEngine.isFlagged(currentEngine.getCurrentIndex()) ? "🚩 Flagged" : "⚐ Mark for Review");
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
        if (delta > 0) {
            if (currentEngine.hasNext()) {
                currentEngine.next();
                renderQuestion();
            } else {
                confirmSubmitQuiz();
            }
        } else if (delta < 0) {
            if (currentEngine.hasPrevious()) {
                currentEngine.previous();
                renderQuestion();
            }
        }
    }

    private void confirmSubmitQuiz() {
        int answered = currentEngine.getAnsweredCount();
        int total = currentEngine.getTotalQuestions();
        int flagged = currentEngine.getFlaggedCount();

        String msg = "Are you sure you want to submit your quiz?\n\n" +
                "Questions Answered: " + answered + " / " + total + "\n" +
                "Unanswered: " + (total - answered) + "\n" +
                "Flagged for Review: " + flagged;

        int opt = JOptionPane.showConfirmDialog(this, msg, "Submit Quiz Confirmation", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            if (quizTimer != null) quizTimer.stop();
            submitQuiz();
        }
    }

    private void submitQuiz() {
        AttemptRecord record = currentEngine.submit();
        historyService.recordAttempt(record);

        // Populate Results
        scoreBadgeLabel.setText(Math.round(record.getScore()) + " / " + Math.round(record.getMaxScore()));
        accuracyLabel.setText(Math.round(record.getAccuracyPercentage()) + "%");
        accuracyLabel.setForeground(record.getAccuracyPercentage() >= 70 ? themeService.getCurrentScheme().getSuccess() : themeService.getCurrentScheme().getWarning());
        timeTakenLabel.setText(DateTimeUtils.formatDurationFriendly(record.getDurationSeconds()));
        gradeLabel.setText(record.getNotes());

        detailedReviewListPanel.removeAll();
        for (int i = 0; i < currentEngine.getQuestions().size(); i++) {
            QuizQuestion q = currentEngine.getQuestions().get(i);
            Integer userChoice = currentEngine.getSelectedAnswer(i);
            boolean isCorrect = userChoice != null && userChoice == q.getCorrectOptionIndex();

            ModernCard item = new ModernCard(themeService, 12, true);
            item.setLayout(new BorderLayout(8, 6));
            item.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

            JLabel title = new JLabel("Q" + (i + 1) + ": " + q.getQuestionText() + " — " + (isCorrect ? "✅ Correct" : "❌ Incorrect"));
            title.setFont(AppConstants.FONT_HEADING);
            title.setForeground(isCorrect ? themeService.getCurrentScheme().getSuccess() : themeService.getCurrentScheme().getDanger());

            String userAnsStr = userChoice != null ? q.getOptions().get(userChoice) : "(No answer chosen)";
            String correctAnsStr = q.getOptions().get(q.getCorrectOptionIndex());

            JTextArea expArea = new JTextArea("Your Answer: " + userAnsStr +
                    "\nCorrect Answer: " + correctAnsStr +
                    "\n\nExplanation: " + q.getExplanation());
            expArea.setFont(AppConstants.FONT_REGULAR);
            expArea.setLineWrap(true);
            expArea.setWrapStyleWord(true);
            expArea.setEditable(false);
            expArea.setOpaque(false);

            item.add(title, BorderLayout.NORTH);
            item.add(expArea, BorderLayout.CENTER);
            detailedReviewListPanel.add(item);
            detailedReviewListPanel.add(Box.createVerticalStrut(10));
        }
        detailedReviewListPanel.revalidate();
        detailedReviewListPanel.repaint();

        showScreen("RESULTS");
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        questionTextLabel.setForeground(scheme.getText());
        for (JPanel c : optionCardPanels) {
            c.setBackground(scheme.isDark() ? new Color(20, 30, 48) : new Color(245, 248, 253));
        }
        for (JRadioButton r : optionRadios) {
            r.setForeground(scheme.getText());
        }
        updatePaletteStyles();
        repaint();
    }
}
