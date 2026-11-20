package com.interviewiq.ui.components;

import com.interviewiq.model.Difficulty;
import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

/**
 * Modern pill badge for difficulties, categories, tags, and status labels.
 */
public class BadgeLabel extends JLabel {
    private Color badgeBackground;
    private Color badgeForeground;

    public BadgeLabel(String text, Color bg, Color fg) {
        super(text);
        this.badgeBackground = bg;
        this.badgeForeground = fg;
        setFont(AppConstants.FONT_SMALL_BOLD);
        setForeground(fg);
        setOpaque(false);
    }

    public static BadgeLabel createDifficultyBadge(Difficulty difficulty, ThemeService themeService) {
        ColorScheme cs = themeService != null ? themeService.getCurrentScheme() : new ColorScheme(false);
        Color bg;
        Color fg = Color.WHITE;

        switch (difficulty) {
            case EASY:
                bg = cs.getSuccess();
                break;
            case HARD:
                bg = cs.getDanger();
                break;
            case MEDIUM:
            default:
                bg = cs.getWarning();
                break;
        }

        return new BadgeLabel(" " + difficulty.getDisplayName() + " ", bg, fg);
    }

    public static BadgeLabel createCategoryBadge(String category, ThemeService themeService) {
        ColorScheme cs = themeService != null ? themeService.getCurrentScheme() : new ColorScheme(false);
        Color bg = cs.isDark() ? new Color(50, 70, 110) : new Color(225, 235, 255);
        Color fg = cs.getPrimary();
        return new BadgeLabel(" " + category + " ", bg, fg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(badgeBackground);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        return new Insets(3, 8, 3, 8);
    }
}
