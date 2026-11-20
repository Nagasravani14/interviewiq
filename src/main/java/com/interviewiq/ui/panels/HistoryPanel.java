package com.interviewiq.ui.panels;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.model.AttemptRecord;
import com.interviewiq.service.HistoryService;
import com.interviewiq.service.QuestionService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.ModernButton;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;
import com.interviewiq.util.DateTimeUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import java.util.Map;

/**
 * Module H: Attempt history table with search, filters, sorting,
 * detailed attempt inspector dialog, and clear history functionality.
 */
public class HistoryPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final HistoryService historyService;

    // Filters
    private JTextField searchField;
    private JComboBox<AssessmentType> typeFilterCombo;
    private JComboBox<String> sortCombo;

    // Table
    private DefaultTableModel tableModel;
    private JTable historyTable;
    private List<AttemptRecord> currentRecords;

    public HistoryPanel(ThemeService themeService, HistoryService historyService) {
        this.themeService = themeService;
        this.historyService = historyService;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 20, 24));

        add(createFilterToolbar(), BorderLayout.NORTH);
        add(createTableCard(), BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        refreshHistory();
    }

    private JPanel createFilterToolbar() {
        ModernCard toolbar = new ModernCard(themeService, 12, true);
        toolbar.setLayout(new BorderLayout(14, 0));
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        searchField = new JTextField(14);
        searchField.putClientProperty("JTextField.placeholderText", "Search assessments...");
        searchField.setFont(AppConstants.FONT_REGULAR);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { refreshHistory(); }
            @Override public void removeUpdate(DocumentEvent e) { refreshHistory(); }
            @Override public void changedUpdate(DocumentEvent e) { refreshHistory(); }
        });

        typeFilterCombo = new JComboBox<>(AssessmentType.values());
        typeFilterCombo.addActionListener(e -> refreshHistory());

        sortCombo = new JComboBox<>(new String[]{"Newest First", "Oldest First", "Highest Score", "Longest Duration"});
        sortCombo.addActionListener(e -> refreshHistory());

        left.add(new JLabel("🔍"));
        left.add(searchField);
        left.add(new JLabel("Type:"));
        left.add(typeFilterCombo);
        left.add(new JLabel("Sort By:"));
        left.add(sortCombo);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        ModernButton inspectBtn = new ModernButton("View Breakdown", ModernButton.ButtonType.PRIMARY, themeService);
        inspectBtn.addActionListener(e -> inspectSelectedAttempt());

        ModernButton clearBtn = new ModernButton("Clear History", ModernButton.ButtonType.OUTLINE, themeService);
        clearBtn.addActionListener(e -> confirmClearHistory());

        right.add(inspectBtn);
        right.add(clearBtn);

        toolbar.add(left, BorderLayout.WEST);
        toolbar.add(right, BorderLayout.EAST);
        return toolbar;
    }

    private JPanel createTableCard() {
        ModernCard card = new ModernCard(themeService, 12, true);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        tableModel = new DefaultTableModel(new String[]{
                "Date & Time", "Type", "Title / Category", "Score", "Accuracy", "Duration", "Status"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        historyTable = new JTable(tableModel);
        historyTable.setFont(AppConstants.FONT_REGULAR);
        historyTable.setRowHeight(34);
        historyTable.setShowVerticalLines(false);
        historyTable.setShowHorizontalLines(true);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(260);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(90);

        historyTable.getColumnModel().getColumn(0).setCellRenderer(center);
        historyTable.getColumnModel().getColumn(1).setCellRenderer(center);
        historyTable.getColumnModel().getColumn(3).setCellRenderer(center);
        historyTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(AppConstants.FONT_REGULAR_BOLD);
                String valStr = String.valueOf(value).replace("%", "");
                try {
                    double d = Double.parseDouble(valStr);
                    l.setForeground(d >= 70 ? themeService.getCurrentScheme().getSuccess() : themeService.getCurrentScheme().getWarning());
                } catch (Exception ignored) {
                    l.setForeground(themeService.getCurrentScheme().getText());
                }
                return l;
            }
        });
        historyTable.getColumnModel().getColumn(5).setCellRenderer(center);
        historyTable.getColumnModel().getColumn(6).setCellRenderer(center);

        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    inspectSelectedAttempt();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    public void refreshHistory() {
        AssessmentType type = (AssessmentType) typeFilterCombo.getSelectedItem();
        String q = searchField != null ? searchField.getText() : "";
        String sortIdx = (String) sortCombo.getSelectedItem();

        String sortKey = "NEW_FIRST";
        if ("Oldest First".equals(sortIdx)) sortKey = "OLD_FIRST";
        else if ("Highest Score".equals(sortIdx)) sortKey = "SCORE_HIGH";
        else if ("Longest Duration".equals(sortIdx)) sortKey = "DURATION_LONG";

        currentRecords = historyService.filterAttempts(type, null, q, sortKey);
        tableModel.setRowCount(0);

        for (AttemptRecord r : currentRecords) {
            tableModel.addRow(new Object[]{
                    DateTimeUtils.formatDateTime(r.getTimestamp()),
                    r.getType().getDisplayName(),
                    r.getTitle(),
                    Math.round(r.getScore()) + " / " + Math.round(r.getMaxScore()),
                    Math.round(r.getAccuracyPercentage()) + "%",
                    DateTimeUtils.formatDurationFriendly(r.getDurationSeconds()),
                    r.getStatus()
            });
        }
    }

    private void inspectSelectedAttempt() {
        int row = historyTable.getSelectedRow();
        if (row < 0 || row >= currentRecords.size()) {
            JOptionPane.showMessageDialog(this, "Please select an attempt from the table to view details.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AttemptRecord r = currentRecords.get(row);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Attempt Breakdown: " + r.getTitle(), JDialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(620, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        panel.setBackground(themeService.getCurrentScheme().getCards());

        // Header info
        JPanel top = new JPanel(new GridLayout(3, 2, 8, 4));
        top.setOpaque(false);
        top.add(new JLabel("Date: " + DateTimeUtils.formatDateTime(r.getTimestamp())));
        top.add(new JLabel("Type: " + r.getType().getDisplayName()));
        top.add(new JLabel("Accuracy: " + Math.round(r.getAccuracyPercentage()) + "% (" + r.getNotes() + ")"));
        top.add(new JLabel("Time Spent: " + DateTimeUtils.formatDurationFriendly(r.getDurationSeconds())));
        top.add(new JLabel("Completed Items: " + r.getCompletedItems() + " / " + r.getTotalItems()));
        top.add(new JLabel("Status: " + r.getStatus()));
        panel.add(top, BorderLayout.NORTH);

        // Details breakdown
        StringBuilder sb = new StringBuilder();
        sb.append("--- Detailed Question / Evaluation Summary ---\n\n");
        if (r.getDetails() != null && !r.getDetails().isEmpty()) {
            for (Map.Entry<String, String> entry : r.getDetails().entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append("No additional granular log recorded for this session.");
        }

        JTextArea detailText = new JTextArea(sb.toString());
        detailText.setLineWrap(true);
        detailText.setWrapStyleWord(true);
        detailText.setEditable(false);
        detailText.setFont(AppConstants.FONT_REGULAR);
        detailText.setBackground(themeService.getCurrentScheme().isDark() ? new Color(15, 23, 42) : new Color(245, 248, 253));
        detailText.setForeground(themeService.getCurrentScheme().getText());

        panel.add(new JScrollPane(detailText), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bot.setOpaque(false);
        bot.add(closeBtn);
        panel.add(bot, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void confirmClearHistory() {
        int opt = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete all saved assessment history records?\nThis action cannot be undone.",
                "Clear History Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            historyService.clearHistory();
            refreshHistory();
            JOptionPane.showMessageDialog(this, "History cleared successfully.", "History Cleared", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        historyTable.setBackground(scheme.getCards());
        historyTable.setForeground(scheme.getText());
        historyTable.setGridColor(scheme.getBorders());
        repaint();
    }
}
