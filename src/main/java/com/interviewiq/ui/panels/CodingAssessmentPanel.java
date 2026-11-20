package com.interviewiq.ui.panels;

import com.interviewiq.core.AssessmentEngine;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.model.CodingChallenge;
import com.interviewiq.model.Difficulty;
import com.interviewiq.model.TestCase;
import com.interviewiq.model.TestResult;
import com.interviewiq.service.HistoryService;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

/**
 * Module D: Coding Assessment with problem statement view, Java code editor,
 * deterministic test case runner, evaluation results table, and score tracking.
 */
public class CodingAssessmentPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final QuestionService questionService;
    private final HistoryService historyService;
    private final AssessmentEngine assessmentEngine;

    // Challenge List (Left sidebar)
    private DefaultListModel<CodingChallenge> challengeListModel;
    private JList<CodingChallenge> challengeList;
    private List<CodingChallenge> allChallenges;

    // Center Problem Info
    private JLabel problemTitleLabel;
    private JPanel badgeRowPanel;
    private JTextArea descriptionArea;
    private JTextArea inputFormatArea;
    private JTextArea outputFormatArea;
    private JTextArea constraintsArea;
    private JPanel testCasesContainer;

    // Right Code Editor
    private JTextArea codeEditorArea;
    private JTextArea lineNumberArea;
    private ModernButton runTestsBtn;
    private ModernButton submitBtn;
    private ModernButton resetCodeBtn;
    private JLabel statusFeedbackLabel;

    // Bottom Test Results Table
    private DefaultTableModel testTableModel;
    private JTable testResultsTable;

    private CodingChallenge selectedChallenge;
    private long sessionStartTime = System.currentTimeMillis();

    public CodingAssessmentPanel(ThemeService themeService, QuestionService questionService, HistoryService historyService) {
        this.themeService = themeService;
        this.questionService = questionService;
        this.historyService = historyService;
        this.assessmentEngine = new AssessmentEngine();

        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        add(createMainSplit(), BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        loadChallenges();
    }

    private JSplitPane createMainSplit() {
        // 1. Left: Challenge Selector
        ModernCard listCard = new ModernCard(themeService, 12, true);
        listCard.setLayout(new BorderLayout(0, 8));
        listCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        listCard.setPreferredSize(new Dimension(280, 600));

        JLabel listHeader = new JLabel("Coding Challenges");
        listHeader.setFont(AppConstants.FONT_HEADING);
        listCard.add(listHeader, BorderLayout.NORTH);

        challengeListModel = new DefaultListModel<>();
        challengeList = new JList<>(challengeListModel);
        challengeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        challengeList.setFont(AppConstants.FONT_REGULAR);
        challengeList.setOpaque(false);
        challengeList.setFixedCellHeight(44);

        challengeList.setCellRenderer((list, c, index, isSelected, cellHasFocus) -> {
            JPanel p = new JPanel(new BorderLayout(6, 2));
            p.setOpaque(true);
            ColorScheme cs = themeService.getCurrentScheme();
            if (isSelected) {
                p.setBackground(cs.isDark() ? new Color(100, 133, 255, 45) : new Color(49, 94, 251, 25));
            } else {
                p.setBackground(cs.getCards());
            }
            p.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

            JLabel title = new JLabel(c.getTitle());
            title.setFont(AppConstants.FONT_REGULAR_BOLD);
            title.setForeground(isSelected ? cs.getPrimary() : cs.getText());

            JLabel diff = new JLabel(c.getDifficulty().getDisplayName());
            diff.setFont(AppConstants.FONT_SMALL_BOLD);
            diff.setForeground(c.getDifficulty() == Difficulty.EASY ? cs.getSuccess() :
                    (c.getDifficulty() == Difficulty.HARD ? cs.getDanger() : cs.getWarning()));

            p.add(title, BorderLayout.CENTER);
            p.add(diff, BorderLayout.EAST);
            return p;
        });

        challengeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                CodingChallenge c = challengeList.getSelectedValue();
                if (c != null) selectChallenge(c);
            }
        });

        JScrollPane scrollList = new JScrollPane(challengeList);
        scrollList.setBorder(null);
        scrollList.setOpaque(false);
        scrollList.getViewport().setOpaque(false);
        listCard.add(scrollList, BorderLayout.CENTER);

        // 2. Right: Split between Problem Statement and Code Editor
        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createProblemDetailsCard(), createEditorAndResultsCard());
        rightSplit.setDividerLocation(380);
        rightSplit.setDividerSize(6);
        rightSplit.setBorder(null);
        rightSplit.setOpaque(false);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listCard, rightSplit);
        mainSplit.setDividerLocation(270);
        mainSplit.setDividerSize(6);
        mainSplit.setBorder(null);
        mainSplit.setOpaque(false);
        return mainSplit;
    }

    private JPanel createProblemDetailsCard() {
        ModernCard card = new ModernCard(themeService, 12, true);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        // Header
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        problemTitleLabel = new JLabel("Problem Statement");
        problemTitleLabel.setFont(AppConstants.FONT_TITLE);

        badgeRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        badgeRowPanel.setOpaque(false);

        top.add(problemTitleLabel);
        top.add(Box.createVerticalStrut(4));
        top.add(badgeRowPanel);
        card.add(top, BorderLayout.NORTH);

        // Content
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        descriptionArea = new JTextArea(6, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setFont(AppConstants.FONT_REGULAR);
        body.add(descriptionArea);
        body.add(Box.createVerticalStrut(14));

        JLabel constrHeader = new JLabel("Constraints & Formats:");
        constrHeader.setFont(AppConstants.FONT_HEADING);
        body.add(constrHeader);
        body.add(Box.createVerticalStrut(4));

        constraintsArea = new JTextArea(4, 30);
        constraintsArea.setLineWrap(true);
        constraintsArea.setWrapStyleWord(true);
        constraintsArea.setEditable(false);
        constraintsArea.setOpaque(false);
        constraintsArea.setFont(AppConstants.FONT_SMALL);
        body.add(constraintsArea);
        body.add(Box.createVerticalStrut(14));

        JLabel tcHeader = new JLabel("Sample Test Cases:");
        tcHeader.setFont(AppConstants.FONT_HEADING);
        body.add(tcHeader);
        body.add(Box.createVerticalStrut(6));

        testCasesContainer = new JPanel();
        testCasesContainer.setOpaque(false);
        testCasesContainer.setLayout(new BoxLayout(testCasesContainer, BoxLayout.Y_AXIS));
        body.add(testCasesContainer);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createEditorAndResultsCard() {
        ModernCard card = new ModernCard(themeService, 12, true);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        // Editor Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JLabel edLabel = new JLabel("Java Solution Editor (JDK 17+)");
        edLabel.setFont(AppConstants.FONT_HEADING);
        toolbar.add(edLabel, BorderLayout.WEST);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightActions.setOpaque(false);

        resetCodeBtn = new ModernButton("Reset Code", ModernButton.ButtonType.OUTLINE, themeService);
        resetCodeBtn.addActionListener(e -> resetBoilerplate());

        runTestsBtn = new ModernButton("▶ Run Tests", ModernButton.ButtonType.OUTLINE, themeService);
        runTestsBtn.addActionListener(e -> executeTests(false));

        submitBtn = new ModernButton("✓ Submit Solution", ModernButton.ButtonType.PRIMARY, themeService);
        submitBtn.addActionListener(e -> executeTests(true));

        rightActions.add(resetCodeBtn);
        rightActions.add(runTestsBtn);
        rightActions.add(submitBtn);
        toolbar.add(rightActions, BorderLayout.EAST);

        card.add(toolbar, BorderLayout.NORTH);

        // Center: Code Editor Area
        JPanel editorPanel = new JPanel(new BorderLayout());
        editorPanel.setOpaque(false);

        codeEditorArea = new JTextArea();
        codeEditorArea.setFont(AppConstants.FONT_CODE);
        codeEditorArea.setTabSize(4);

        lineNumberArea = new JTextArea(" 1 \n 2 \n 3 \n 4 \n 5 \n 6 \n 7 \n 8 \n 9 \n 10 \n 11 \n 12 \n 13 \n 14 \n 15 ");
        lineNumberArea.setFont(AppConstants.FONT_CODE);
        lineNumberArea.setEditable(false);
        lineNumberArea.setOpaque(true);

        codeEditorArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLineNumbers(); }
        });

        JScrollPane editorScroll = new JScrollPane(codeEditorArea);
        editorScroll.setRowHeaderView(lineNumberArea);
        editorScroll.setPreferredSize(new Dimension(450, 320));
        editorPanel.add(editorScroll, BorderLayout.CENTER);

        // Bottom: Test Results Table
        JPanel resultsPanel = new JPanel(new BorderLayout(0, 6));
        resultsPanel.setOpaque(false);
        resultsPanel.setPreferredSize(new Dimension(450, 190));

        statusFeedbackLabel = new JLabel("Ready to evaluate solution against test cases.");
        statusFeedbackLabel.setFont(AppConstants.FONT_SMALL_BOLD);
        resultsPanel.add(statusFeedbackLabel, BorderLayout.NORTH);

        testTableModel = new DefaultTableModel(new String[]{"Test #", "Status", "Input", "Expected", "Output", "Time (ms)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        testResultsTable = new JTable(testTableModel);
        testResultsTable.setFont(AppConstants.FONT_SMALL);
        testResultsTable.setRowHeight(28);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        testResultsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        testResultsTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        testResultsTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        testResultsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        testResultsTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(AppConstants.FONT_SMALL_BOLD);
                if ("PASS".equals(value)) {
                    lbl.setForeground(themeService.getCurrentScheme().getSuccess());
                } else {
                    lbl.setForeground(themeService.getCurrentScheme().getDanger());
                }
                return lbl;
            }
        });

        JScrollPane tableScroll = new JScrollPane(testResultsTable);
        resultsPanel.add(tableScroll, BorderLayout.CENTER);

        // Put editor and results in a vertical split
        JSplitPane vertSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, resultsPanel);
        vertSplit.setDividerLocation(310);
        vertSplit.setDividerSize(6);
        vertSplit.setBorder(null);
        vertSplit.setOpaque(false);

        card.add(vertSplit, BorderLayout.CENTER);
        return card;
    }

    private void updateLineNumbers() {
        int lines = codeEditorArea.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= Math.max(15, lines); i++) {
            sb.append(String.format(" %2d \n", i));
        }
        lineNumberArea.setText(sb.toString());
    }

    private void loadChallenges() {
        allChallenges = questionService.getAllCodingChallenges();
        challengeListModel.clear();
        for (CodingChallenge c : allChallenges) {
            challengeListModel.addElement(c);
        }
        if (!allChallenges.isEmpty()) {
            challengeList.setSelectedIndex(0);
        }
    }

    private void selectChallenge(CodingChallenge c) {
        this.selectedChallenge = c;
        this.sessionStartTime = System.currentTimeMillis();

        problemTitleLabel.setText(c.getTitle());
        badgeRowPanel.removeAll();
        badgeRowPanel.add(BadgeLabel.createDifficultyBadge(c.getDifficulty(), themeService));
        badgeRowPanel.add(BadgeLabel.createCategoryBadge(c.getCategory(), themeService));
        for (String t : c.getTags()) {
            badgeRowPanel.add(new BadgeLabel(t, themeService.getCurrentScheme().isDark() ? new Color(30, 45, 65) : new Color(240, 244, 250), themeService.getCurrentScheme().getTextMuted()));
        }
        badgeRowPanel.revalidate();
        badgeRowPanel.repaint();

        descriptionArea.setText(c.getDescription());
        constraintsArea.setText("Input: " + c.getInputFormat() + "\nOutput: " + c.getOutputFormat() + "\nConstraints: " + c.getConstraints());

        testCasesContainer.removeAll();
        for (int i = 0; i < Math.min(3, c.getTestCases().size()); i++) {
            TestCase tc = c.getTestCases().get(i);
            JPanel tcCard = new JPanel(new GridLayout(2, 1, 2, 2));
            tcCard.setOpaque(true);
            tcCard.setBackground(themeService.getCurrentScheme().isDark() ? new Color(25, 36, 56) : new Color(243, 246, 252));
            tcCard.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            JLabel inLbl = new JLabel("Input: " + tc.getInput());
            inLbl.setFont(AppConstants.FONT_CODE);
            JLabel outLbl = new JLabel("Expected: " + tc.getExpectedOutput());
            outLbl.setFont(AppConstants.FONT_CODE);
            tcCard.add(inLbl);
            tcCard.add(outLbl);

            testCasesContainer.add(tcCard);
            testCasesContainer.add(Box.createVerticalStrut(6));
        }
        testCasesContainer.revalidate();
        testCasesContainer.repaint();

        codeEditorArea.setText(c.getStarterCode());
        testTableModel.setRowCount(0);
        statusFeedbackLabel.setText("Loaded " + c.getTitle() + ". Click 'Run Tests' to evaluate.");
        updateLineNumbers();
    }

    private void resetBoilerplate() {
        if (selectedChallenge != null) {
            codeEditorArea.setText(selectedChallenge.getStarterCode());
            testTableModel.setRowCount(0);
            statusFeedbackLabel.setText("Reset code to starter template.");
            updateLineNumbers();
        }
    }

    private void executeTests(boolean isSubmit) {
        if (selectedChallenge == null) return;

        String userCode = codeEditorArea.getText();
        AssessmentEngine.EvaluationSummary summary = assessmentEngine.evaluateChallenge(selectedChallenge, userCode);

        // Populate Table
        testTableModel.setRowCount(0);
        for (TestResult tr : summary.getTestResults()) {
            testTableModel.addRow(new Object[]{
                    "Test " + tr.getTestCaseIndex(),
                    tr.isPassed() ? "PASS" : "FAIL",
                    tr.getInput(),
                    tr.getExpectedOutput(),
                    tr.getActualOutput(),
                    tr.getExecutionTimeMs() + "ms"
            });
        }

        statusFeedbackLabel.setText(summary.getDiagnosticFeedback() + " Score: " + summary.getScorePercentage() + "%");
        statusFeedbackLabel.setForeground(summary.isAllPassed() ? themeService.getCurrentScheme().getSuccess() : themeService.getCurrentScheme().getWarning());

        if (isSubmit) {
            long duration = Math.max(1, (System.currentTimeMillis() - sessionStartTime) / 1000);
            AttemptRecord record = assessmentEngine.createAttemptRecord(summary, duration);
            historyService.recordAttempt(record);

            String msg = "Assessment Submitted!\n\nPassed: " + summary.getPassedCount() + " / " + summary.getTotalCount() +
                    "\nScore: " + summary.getScorePercentage() + "%\nStatus: " + record.getStatus() +
                    "\nRecorded in History & Analytics.";
            JOptionPane.showMessageDialog(this, msg, "Assessment Submission", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        problemTitleLabel.setForeground(scheme.getText());
        descriptionArea.setForeground(scheme.getText());
        constraintsArea.setForeground(scheme.getTextMuted());
        codeEditorArea.setBackground(scheme.isDark() ? new Color(13, 20, 36) : new Color(250, 252, 255));
        codeEditorArea.setForeground(scheme.isDark() ? new Color(225, 235, 250) : new Color(20, 30, 50));
        codeEditorArea.setCaretColor(scheme.getPrimary());
        lineNumberArea.setBackground(scheme.isDark() ? new Color(18, 28, 48) : new Color(235, 240, 248));
        lineNumberArea.setForeground(scheme.getTextMuted());
        testResultsTable.setBackground(scheme.getCards());
        testResultsTable.setForeground(scheme.getText());
        testResultsTable.setGridColor(scheme.getBorders());
        challengeList.repaint();
        repaint();
    }
}
