package com.interviewiq.ui.panels;

import com.interviewiq.model.Difficulty;
import com.interviewiq.model.Question;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Module B: Technical Interview Question Bank with rich filtering, search,
 * answer reveal, code examples, bookmarks, and completion tracking.
 */
public class InterviewPrepPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final QuestionService questionService;

    // Filters
    private JComboBox<String> categoryCombo;
    private JComboBox<Difficulty> difficultyCombo;
    private JComboBox<String> statusCombo;
    private JTextField searchField;

    // Question List
    private DefaultListModel<Question> listModel;
    private JList<Question> questionList;
    private List<Question> currentFilteredQuestions;

    // Detail View Components
    private JLabel detailTitleLabel;
    private JPanel badgeRowPanel;
    private JTextArea questionTextArea;
    private ModernButton revealAnswerBtn;
    private JPanel answerContainer;
    private JTextArea answerTextArea;
    private JTextArea explanationTextArea;
    private JTextArea codeTextArea;
    private JPanel codeContainer;
    private ModernButton understoodBtn;
    private ModernButton bookmarkBtn;
    private JButton prevBtn;
    private JButton nextBtn;
    private JLabel questionIndexLabel;

    private boolean isAnswerRevealed = false;
    private Question selectedQuestion;

    public InterviewPrepPanel(ThemeService themeService, QuestionService questionService) {
        this.themeService = themeService;
        this.questionService = questionService;

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        // 1. Top Filter & Search Bar
        add(createFilterBar(), BorderLayout.NORTH);

        // 2. Center Split View (Left: Question List, Right: Question Details)
        add(createSplitPane(), BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        applyFilters();
    }

    private JPanel createFilterBar() {
        ModernCard bar = new ModernCard(themeService, 12, true);
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        bar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        // Search Field
        searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Search questions or tags...");
        searchField.setFont(AppConstants.FONT_REGULAR);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        // Category Filter
        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("All Categories");
        for (String cat : questionService.getDistinctCategories()) {
            categoryCombo.addItem(cat);
        }
        categoryCombo.addActionListener(e -> applyFilters());

        // Difficulty Filter
        difficultyCombo = new JComboBox<>(Difficulty.values());
        difficultyCombo.addActionListener(e -> applyFilters());

        // Status Filter
        statusCombo = new JComboBox<>(new String[]{"All Statuses", "Understood", "Bookmarked", "Unread"});
        statusCombo.addActionListener(e -> applyFilters());

        bar.add(new JLabel("🔍"));
        bar.add(searchField);
        bar.add(new JLabel("Category:"));
        bar.add(categoryCombo);
        bar.add(new JLabel("Difficulty:"));
        bar.add(difficultyCombo);
        bar.add(new JLabel("Status:"));
        bar.add(statusCombo);

        return bar;
    }

    private JSplitPane createSplitPane() {
        // Left: Question List Card
        ModernCard listCard = new ModernCard(themeService, 12, true);
        listCard.setLayout(new BorderLayout());
        listCard.setPreferredSize(new Dimension(340, 600));

        listModel = new DefaultListModel<>();
        questionList = new JList<>(listModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionList.setFont(AppConstants.FONT_REGULAR);
        questionList.setOpaque(false);
        questionList.setFixedCellHeight(46);

        // Custom renderer for question list items
        questionList.setCellRenderer((list, q, index, isSelected, cellHasFocus) -> {
            JPanel item = new JPanel(new BorderLayout(8, 2));
            item.setOpaque(true);
            ColorScheme cs = themeService.getCurrentScheme();

            if (isSelected) {
                item.setBackground(cs.isDark() ? new Color(100, 133, 255, 45) : new Color(49, 94, 251, 25));
            } else {
                item.setBackground(cs.getCards());
            }

            item.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            String statusIcon = q.isUnderstood() ? "✅ " : (q.isBookmarked() ? "⭐ " : "⚪ ");
            JLabel titleLbl = new JLabel(statusIcon + q.getTitle());
            titleLbl.setFont(AppConstants.FONT_REGULAR);
            titleLbl.setForeground(isSelected ? cs.getPrimary() : cs.getText());

            JLabel diffLbl = new JLabel(q.getDifficulty().getDisplayName());
            diffLbl.setFont(AppConstants.FONT_SMALL_BOLD);
            diffLbl.setForeground(q.getDifficulty() == Difficulty.EASY ? cs.getSuccess() :
                    (q.getDifficulty() == Difficulty.HARD ? cs.getDanger() : cs.getWarning()));

            item.add(titleLbl, BorderLayout.CENTER);
            item.add(diffLbl, BorderLayout.EAST);
            return item;
        });

        questionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Question selected = questionList.getSelectedValue();
                if (selected != null) {
                    displayQuestion(selected);
                }
            }
        });

        JScrollPane listScroll = new JScrollPane(questionList);
        listScroll.setBorder(null);
        listScroll.setOpaque(false);
        listScroll.getViewport().setOpaque(false);
        listCard.add(listScroll, BorderLayout.CENTER);

        // Right: Detailed Question View Card
        ModernCard detailCard = new ModernCard(themeService, 12, true);
        detailCard.setLayout(new BorderLayout(16, 16));
        detailCard.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        detailCard.add(createDetailHeader(), BorderLayout.NORTH);
        detailCard.add(createDetailContent(), BorderLayout.CENTER);
        detailCard.add(createDetailFooter(), BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listCard, detailCard);
        split.setDividerLocation(340);
        split.setDividerSize(6);
        split.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        split.setOpaque(false);
        return split;
    }

    private JPanel createDetailHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        detailTitleLabel = new JLabel("Select a question to prepare");
        detailTitleLabel.setFont(AppConstants.FONT_TITLE);

        badgeRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgeRowPanel.setOpaque(false);

        header.add(detailTitleLabel);
        header.add(Box.createVerticalStrut(6));
        header.add(badgeRowPanel);
        return header;
    }

    private JScrollPane createDetailContent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Question Statement
        JLabel qHeader = new JLabel("Question:");
        qHeader.setFont(AppConstants.FONT_HEADING);
        panel.add(qHeader);
        panel.add(Box.createVerticalStrut(6));

        questionTextArea = new JTextArea(4, 40);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setOpaque(false);
        questionTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(questionTextArea);
        panel.add(Box.createVerticalStrut(16));

        // Reveal Answer Button
        revealAnswerBtn = new ModernButton("Reveal Model Answer", ModernButton.ButtonType.OUTLINE, themeService);
        revealAnswerBtn.setMaximumSize(new Dimension(200, 36));
        revealAnswerBtn.addActionListener(e -> toggleRevealAnswer());
        panel.add(revealAnswerBtn);
        panel.add(Box.createVerticalStrut(14));

        // Answer Container (Initially Hidden)
        answerContainer = new JPanel();
        answerContainer.setOpaque(false);
        answerContainer.setLayout(new BoxLayout(answerContainer, BoxLayout.Y_AXIS));
        answerContainer.setVisible(false);

        JLabel ansHeader = new JLabel("Model Answer:");
        ansHeader.setFont(AppConstants.FONT_HEADING);
        answerContainer.add(ansHeader);
        answerContainer.add(Box.createVerticalStrut(6));

        answerTextArea = new JTextArea(6, 40);
        answerTextArea.setLineWrap(true);
        answerTextArea.setWrapStyleWord(true);
        answerTextArea.setEditable(false);
        answerTextArea.setFont(AppConstants.FONT_REGULAR);
        answerContainer.add(answerTextArea);
        answerContainer.add(Box.createVerticalStrut(14));

        JLabel expHeader = new JLabel("In-Depth Explanation & Key Takeaways:");
        expHeader.setFont(AppConstants.FONT_HEADING);
        answerContainer.add(expHeader);
        answerContainer.add(Box.createVerticalStrut(6));

        explanationTextArea = new JTextArea(4, 40);
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setEditable(false);
        explanationTextArea.setFont(AppConstants.FONT_REGULAR);
        answerContainer.add(explanationTextArea);
        answerContainer.add(Box.createVerticalStrut(14));

        // Code Snippet Container
        codeContainer = new JPanel(new BorderLayout());
        codeContainer.setOpaque(false);
        JLabel codeHeader = new JLabel("Code Example / Implementation:");
        codeHeader.setFont(AppConstants.FONT_HEADING);
        codeContainer.add(codeHeader, BorderLayout.NORTH);

        codeTextArea = new JTextArea(5, 40);
        codeTextArea.setFont(AppConstants.FONT_CODE);
        codeTextArea.setEditable(false);
        codeContainer.add(codeTextArea, BorderLayout.CENTER);

        answerContainer.add(codeContainer);
        panel.add(answerContainer);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private JPanel createDetailFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel leftActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftActions.setOpaque(false);

        understoodBtn = new ModernButton("Mark as Understood", ModernButton.ButtonType.SUCCESS, themeService);
        understoodBtn.addActionListener(e -> {
            if (selectedQuestion != null) {
                questionService.toggleUnderstood(selectedQuestion.getId());
                updateButtonStates();
                questionList.repaint();
            }
        });

        bookmarkBtn = new ModernButton("Bookmark", ModernButton.ButtonType.OUTLINE, themeService);
        bookmarkBtn.addActionListener(e -> {
            if (selectedQuestion != null) {
                questionService.toggleBookmark(selectedQuestion.getId());
                updateButtonStates();
                questionList.repaint();
            }
        });

        leftActions.add(understoodBtn);
        leftActions.add(bookmarkBtn);

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightNav.setOpaque(false);

        prevBtn = new ModernButton("← Previous", ModernButton.ButtonType.OUTLINE, themeService);
        prevBtn.addActionListener(e -> navigateQuestion(-1));

        questionIndexLabel = new JLabel("0 / 0");
        questionIndexLabel.setFont(AppConstants.FONT_SMALL_BOLD);

        nextBtn = new ModernButton("Next →", ModernButton.ButtonType.OUTLINE, themeService);
        nextBtn.addActionListener(e -> navigateQuestion(1));

        rightNav.add(prevBtn);
        rightNav.add(questionIndexLabel);
        rightNav.add(nextBtn);

        footer.add(leftActions, BorderLayout.WEST);
        footer.add(rightNav, BorderLayout.EAST);
        return footer;
    }

    private void applyFilters() {
        String cat = (String) categoryCombo.getSelectedItem();
        Difficulty diff = (Difficulty) difficultyCombo.getSelectedItem();
        String query = searchField.getText();
        String status = (String) statusCombo.getSelectedItem();

        currentFilteredQuestions = questionService.filterQuestions(cat, diff, query, status);
        listModel.clear();
        for (Question q : currentFilteredQuestions) {
            listModel.addElement(q);
        }

        if (!currentFilteredQuestions.isEmpty()) {
            questionList.setSelectedIndex(0);
        } else {
            clearDetailView();
        }
    }

    private void displayQuestion(Question q) {
        this.selectedQuestion = q;
        detailTitleLabel.setText(q.getTitle());

        badgeRowPanel.removeAll();
        badgeRowPanel.add(BadgeLabel.createCategoryBadge(q.getCategory(), themeService));
        badgeRowPanel.add(BadgeLabel.createDifficultyBadge(q.getDifficulty(), themeService));
        if (q.getTags() != null) {
            for (String tag : q.getTags()) {
                badgeRowPanel.add(new BadgeLabel("#" + tag, themeService.getCurrentScheme().isDark() ? new Color(30, 45, 65) : new Color(240, 244, 250), themeService.getCurrentScheme().getTextMuted()));
            }
        }
        badgeRowPanel.revalidate();
        badgeRowPanel.repaint();

        questionTextArea.setText(q.getQuestionText());
        answerTextArea.setText(q.getAnswerText());
        explanationTextArea.setText(q.getExplanationText());

        if (q.getCodeExample() != null && !q.getCodeExample().trim().isEmpty()) {
            codeTextArea.setText(q.getCodeExample());
            codeContainer.setVisible(true);
        } else {
            codeContainer.setVisible(false);
        }

        // Reset answer reveal
        isAnswerRevealed = false;
        answerContainer.setVisible(false);
        revealAnswerBtn.setText("Reveal Model Answer");

        updateButtonStates();

        int currentIndex = questionList.getSelectedIndex();
        questionIndexLabel.setText((currentIndex + 1) + " / " + listModel.getSize());
        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(currentIndex < listModel.getSize() - 1);
    }

    private void clearDetailView() {
        selectedQuestion = null;
        detailTitleLabel.setText("No questions match the current filter");
        badgeRowPanel.removeAll();
        badgeRowPanel.revalidate();
        badgeRowPanel.repaint();
        questionTextArea.setText("");
        answerTextArea.setText("");
        explanationTextArea.setText("");
        codeTextArea.setText("");
        answerContainer.setVisible(false);
        questionIndexLabel.setText("0 / 0");
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);
    }

    private void toggleRevealAnswer() {
        isAnswerRevealed = !isAnswerRevealed;
        answerContainer.setVisible(isAnswerRevealed);
        revealAnswerBtn.setText(isAnswerRevealed ? "Hide Model Answer" : "Reveal Model Answer");
        revalidate();
        repaint();
    }

    private void updateButtonStates() {
        if (selectedQuestion != null) {
            understoodBtn.setText(selectedQuestion.isUnderstood() ? "✓ Completed (Click to Reset)" : "Mark as Understood");
            bookmarkBtn.setText(selectedQuestion.isBookmarked() ? "★ Bookmarked" : "☆ Bookmark");
        }
    }

    private void navigateQuestion(int delta) {
        int idx = questionList.getSelectedIndex() + delta;
        if (idx >= 0 && idx < listModel.getSize()) {
            questionList.setSelectedIndex(idx);
            questionList.ensureIndexIsVisible(idx);
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        detailTitleLabel.setForeground(scheme.getText());
        questionTextArea.setForeground(scheme.getText());
        answerTextArea.setForeground(scheme.getText());
        explanationTextArea.setForeground(scheme.getText());
        codeTextArea.setBackground(scheme.isDark() ? new Color(15, 23, 42) : new Color(245, 247, 252));
        codeTextArea.setForeground(scheme.isDark() ? new Color(130, 220, 160) : new Color(20, 110, 50));
        questionList.repaint();
        repaint();
    }
}
