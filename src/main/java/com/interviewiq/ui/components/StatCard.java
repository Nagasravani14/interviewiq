package com.interviewiq.ui.components;

import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Metric card displaying a single high-level key performance indicator with icon and trend.
 */
public class StatCard extends ModernCard {
    private final JLabel titleLabel;
    private final JLabel valueLabel;
    private final JLabel subtitleLabel;
    private final JLabel iconLabel;
    private final Color accentColor;

    public StatCard(String title, String value, String subtitle, String iconSymbol, Color accentColor, ThemeService themeService) {
        super(themeService, 16, true);
        this.accentColor = accentColor;
        setLayout(new BorderLayout(12, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        setPreferredSize(new Dimension(240, 115));

        // Header with title and icon
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        titleLabel = new JLabel(title);
        titleLabel.setFont(AppConstants.FONT_SMALL_BOLD);

        iconLabel = new JLabel(iconSymbol);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        iconLabel.setForeground(accentColor);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(iconLabel, BorderLayout.EAST);

        // Body with big value
        valueLabel = new JLabel(value);
        valueLabel.setFont(AppConstants.FONT_BIG_STAT);

        // Footer with subtitle / trend
        subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(AppConstants.FONT_SMALL);

        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        contentPanel.setOpaque(false);
        contentPanel.add(valueLabel);
        contentPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        updateColors();
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public void setSubtitle(String subtitle) {
        subtitleLabel.setText(subtitle);
    }

    private void updateColors() {
        ColorScheme cs = themeService.getCurrentScheme();
        titleLabel.setForeground(cs.getTextMuted());
        valueLabel.setForeground(cs.getText());
        subtitleLabel.setForeground(cs.getTextMuted());
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        super.onThemeChanged(scheme);
        updateColors();
    }
}
