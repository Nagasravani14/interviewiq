package com.interviewiq.ui.panels;

import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
import com.interviewiq.model.TopicProgress;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.BadgeLabel;
import com.interviewiq.ui.components.ModernButton;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Module C: Practice Arena for rapid drill, flashcard-style answer reveals,
 * active topic progress tracking, and mastery indicators.
 */
public class PracticeArenaPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final QuestionService questionService;

    private JComboBox<String> categoryCombo;
    private JComboBox<Difficulty> difficultyCombo;
    private JProgressBar topicProgressBar;
    private JLabel progressPercentLabel;
    private JLabel progressCountLabel;

    private ModernCard cardContainer;
    private JLabel questionTitleLabel;
    private JPanel badgePanel;
    private JTextArea questionPromptArea;
    private ModernButton revealToggleBtn;
    private JPanel answerPanel;
    private JTextArea answerBodyArea;
    private JTextArea explanationBodyArea;

    private ModernButton markUnderstoodBtn;
    private ModernButton bookmarkBtn;
    private ModernButton prevBtn;
    private ModernButton nextBtn;
    private JLabel cardIndexLabel;

    private List<Question> practiceList = new ArrayList<>();
    private int currentIndex = 0;
    private boolean answerRevealed = false;

    public PracticeArenaPanel(ThemeService themeService, QuestionService questionService) {
        this.themeService = themeService;
        this.questionService = questionService;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        // 1. Top Configuration & Progress Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Center Practice Flashcard Card
        add(createPracticeCard(), BorderLayout.CENTER);

        // 3. Bottom Controls & Navigation
        add(createBottomControls(), BorderLayout.SOUTH);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        loadPracticeSession();
    }

    private JPanel createHeaderPanel() {
        ModernCard header = new ModernCard(themeService, 12, true);
        header.setLayout(new BorderLayout(16, 10));
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JPanel leftFilters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftFilters.setOpaque(false);

        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("All Categories");
        for (String c : questionService.getDistinctCategories()) {
            categoryCombo.addItem(c);
        }
        categoryCombo.addActionListener(e -> loadPracticeSession());

        difficultyCombo = new JComboBox<>(Difficulty.values());
        difficultyCombo.addActionListener(e -> loadPracticeSession());

        ModernButton shuffleBtn = new ModernButton("🔀 Shuffle", ModernButton.ButtonType.OUTLINE, themeService);
        shuffleBtn.addActionListener(e -> {
            Collections.shuffle(practiceList);
            currentIndex = 0;
            displayCurrentCard();
        });

        leftFilters.add(new JLabel("Topic:"));
        leftFilters.add(categoryCombo);
        leftFilters.add(new JLabel("Difficulty:"));
        leftFilters.add(difficultyCombo);
        leftFilters.add(shuffleBtn);

        // Right Topic Progress
        JPanel rightProgress = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightProgress.setOpaque(false);

        progressCountLabel = new JLabel("0 / 0 Understood");
        progressCountLabel.setFont(AppConstants.FONT_SMALL_BOLD);

        topicProgressBar = new JProgressBar(0, 100);
        topicProgressBar.setPreferredSize(new Dimension(140, 14));
        topicProgressBar.setStringPainted(false);

        progressPercentLabel = new JLabel("0%");
        progressPercentLabel.setFont(AppConstants.FONT_SMALL_BOLD);

        rightProgress.add(progressCountLabel);
        rightProgress.add(topicProgressBar);
        rightProgress.add(progressPercentLabel);

        header.add(leftFilters, BorderLayout.WEST);
        header.add(rightProgress, BorderLayout.EAST);
        return header;
    }

    private JPanel createPracticeCard() {
        cardContainer = new ModernCard(themeService, 16, true);
        cardContainer.setLayout(new BorderLayout(16, 16));
        cardContainer.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Question Title and Badges
        JPanel topInfo = new JPanel();
        topInfo.setOpaque(false);
        topInfo.setLayout(new BoxLayout(topInfo, BoxLayout.Y_AXIS));

        questionTitleLabel = new JLabel("Practice Question");
        questionTitleLabel.setFont(AppConstants.FONT_TITLE);

        badgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgePanel.setOpaque(false);

        topInfo.add(questionTitleLabel);
        topInfo.add(Box.createVerticalStrut(6));
        topInfo.add(badgePanel);

        cardContainer.add(topInfo, BorderLayout.NORTH);

        // Question Body & Answer Body inside a ScrollPane
        JPanel bodyContent = new JPanel();
        bodyContent.setOpaque(false);
        bodyContent.setLayout(new BoxLayout(bodyContent, BoxLayout.Y_AXIS));

        questionPromptArea = new JTextArea(4, 40);
        questionPromptArea.setLineWrap(true);
        questionPromptArea.setWrapStyleWord(true);
        questionPromptArea.setEditable(false);
        questionPromptArea.setOpaque(false);
        questionPromptArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bodyContent.add(questionPromptArea);
        bodyContent.add(Box.createVerticalStrut(18));

        revealToggleBtn = new ModernButton("Reveal Explanation & Solution", ModernButton.ButtonType.PRIMARY, themeService);
        revealToggleBtn.setMaximumSize(new Dimension(240, 38));
        revealToggleBtn.addActionListener(e -> toggleAnswer());
        bodyContent.add(revealToggleBtn);
        bodyContent.add(Box.createVerticalStrut(18));

        // Answer panel
        answerPanel = new JPanel();
        answerPanel.setOpaque(false);
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setVisible(false);

        JLabel ansHead = new JLabel("Answer Summary:");
        ansHead.setFont(AppConstants.FONT_HEADING);
        answerPanel.add(ansHead);
        answerPanel.add(Box.createVerticalStrut(4));

        answerBodyArea = new JTextArea(5, 40);
        answerBodyArea.setLineWrap(true);
        answerBodyArea.setWrapStyleWord(true);
        answerBodyArea.setEditable(false);
        answerBodyArea.setFont(AppConstants.FONT_REGULAR);
        answerPanel.add(answerBodyArea);
        answerPanel.add(Box.createVerticalStrut(14));

        JLabel expHead = new JLabel("Detailed Concept Breakdown:");
        expHead.setFont(AppConstants.FONT_HEADING);
        answerPanel.add(expHead);
        answerPanel.add(Box.createVerticalStrut(4));

        explanationBodyArea = new JTextArea(4, 40);
        explanationBodyArea.setLineWrap(true);
        explanationBodyArea.setWrapStyleWord(true);
        explanationBodyArea.setEditable(false);
        explanationBodyArea.setFont(AppConstants.FONT_REGULAR);
        answerPanel.add(explanationBodyArea);

        bodyContent.add(answerPanel);

        JScrollPane scroll = new JScrollPane(bodyContent);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        cardContainer.add(scroll, BorderLayout.CENTER);

        return cardContainer;
    }

    private JPanel createBottomControls() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftActions.setOpaque(false);

        markUnderstoodBtn = new ModernButton("Mark as Understood", ModernButton.ButtonType.SUCCESS, themeService);
        markUnderstoodBtn.addActionListener(e -> {
            if (getCurrent() != null) {
                questionService.toggleUnderstood(getCurrent().getId());
                updateProgressUI();
                updateActionButtons();
            }
        });

        bookmarkBtn = new ModernButton("Bookmark", ModernButton.ButtonType.OUTLINE, themeService);
        bookmarkBtn.addActionListener(e -> {
            if (getCurrent() != null) {
                questionService.toggleBookmark(getCurrent().getId());
                updateActionButtons();
            }
        });

        leftActions.add(markUnderstoodBtn);
        leftActions.add(bookmarkBtn);

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightNav.setOpaque(false);

        prevBtn = new ModernButton("← Previous", ModernButton.ButtonType.OUTLINE, themeService);
        prevBtn.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayCurrentCard();
            }
        });

        cardIndexLabel = new JLabel("Card 0 / 0");
        cardIndexLabel.setFont(AppConstants.FONT_REGULAR_BOLD);

        nextBtn = new ModernButton("Next →", ModernButton.ButtonType.OUTLINE, themeService);
        nextBtn.addActionListener(e -> {
            if (currentIndex < practiceList.size() - 1) {
                currentIndex++;
                displayCurrentCard();
            }
        });

        rightNav.add(prevBtn);
        rightNav.add(cardIndexLabel);
        rightNav.add(nextBtn);

        footer.add(leftActions, BorderLayout.WEST);
        footer.add(rightNav, BorderLayout.EAST);
        return footer;
    }

    private Question getCurrent() {
        if (practiceList.isEmpty() || currentIndex < 0 || currentIndex >= practiceList.size()) {
            return null;
        }
        return practiceList.get(currentIndex);
    }

    private void loadPracticeSession() {
        String cat = (String) categoryCombo.getSelectedItem();
        Difficulty diff = (Difficulty) difficultyCombo.getSelectedItem();

        practiceList = questionService.filterQuestions(cat, diff, null, null);
        currentIndex = 0;
        displayCurrentCard();
        updateProgressUI();
    }

    private void displayCurrentCard() {
        Question q = getCurrent();
        if (q == null) {
            questionTitleLabel.setText("No questions available for this filter");
            badgePanel.removeAll();
            questionPromptArea.setText("");
            answerBodyArea.setText("");
            explanationBodyArea.setText("");
            answerPanel.setVisible(false);
            cardIndexLabel.setText("0 / 0");
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            return;
        }

        questionTitleLabel.setText(q.getTitle());

        badgePanel.removeAll();
        badgePanel.add(BadgeLabel.createCategoryBadge(q.getCategory(), themeService));
        badgePanel.add(BadgeLabel.createDifficultyBadge(q.getDifficulty(), themeService));
        badgePanel.revalidate();
        badgePanel.repaint();

        questionPromptArea.setText(q.getQuestionText());
        answerBodyArea.setText(q.getAnswerText());
        explanationBodyArea.setText(q.getExplanationText());

        answerRevealed = false;
        answerPanel.setVisible(false);
        revealToggleBtn.setText("Reveal Explanation & Solution");

        cardIndexLabel.setText("Card " + (currentIndex + 1) + " / " + practiceList.size());
        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(currentIndex < practiceList.size() - 1);

        updateActionButtons();
    }

    private void toggleAnswer() {
        answerRevealed = !answerRevealed;
        answerPanel.setVisible(answerRevealed);
        revealToggleBtn.setText(answerRevealed ? "Hide Explanation" : "Reveal Explanation & Solution");
        revalidate();
        repaint();
    }

    private void updateActionButtons() {
        Question q = getCurrent();
        if (q != null) {
            markUnderstoodBtn.setText(q.isUnderstood() ? "✓ Understood (Reset)" : "Mark as Understood");
            bookmarkBtn.setText(q.isBookmarked() ? "★ Bookmarked" : "☆ Bookmark");
        }
    }

    private void updateProgressUI() {
        String cat = (String) categoryCombo.getSelectedItem();
        TopicProgress progress = questionService.getTopicProgress(cat);

        int total = progress.getTotalQuestions();
        int understood = progress.getUnderstoodQuestions();
        int percent = (int) Math.round(progress.getCompletionRate());

        progressCountLabel.setText(understood + " / " + total + " Understood");
        topicProgressBar.setValue(percent);
        progressPercentLabel.setText(percent + "%");
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        questionTitleLabel.setForeground(scheme.getText());
        questionPromptArea.setForeground(scheme.getText());
        answerBodyArea.setForeground(scheme.getText());
        explanationBodyArea.setForeground(scheme.getText());
        cardIndexLabel.setForeground(scheme.getText());
        progressCountLabel.setForeground(scheme.getTextMuted());
        progressPercentLabel.setForeground(scheme.getPrimary());
        repaint();
    }
}
