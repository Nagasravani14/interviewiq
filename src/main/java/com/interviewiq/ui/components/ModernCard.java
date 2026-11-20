package com.interviewiq.ui.components;

import com.interviewiq.service.ThemeService;
import com.interviewiq.util.ColorScheme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Modern card container panel with rounded corners, themed background, and subtle border.
 */
public class ModernCard extends JPanel implements ThemeService.ThemeChangeListener {
    protected final ThemeService themeService;
    private int cornerRadius = 16;
    private boolean drawBorder = true;

    public ModernCard(ThemeService themeService) {
        this(themeService, 16, true);
    }

    public ModernCard(ThemeService themeService, int cornerRadius, boolean drawBorder) {
        this.themeService = themeService;
        this.cornerRadius = cornerRadius;
        this.drawBorder = drawBorder;
        setOpaque(false);
        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ColorScheme cs = themeService != null ? themeService.getCurrentScheme() : new ColorScheme(false);
        Color cardBg = cs.getCards();
        Color borderColor = cs.getBorders();

        int width = getWidth();
        int height = getHeight();

        // Fill background
        g2.setColor(cardBg);
        g2.fillRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        // Draw border
        if (drawBorder) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
