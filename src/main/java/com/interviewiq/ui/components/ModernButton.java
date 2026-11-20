package com.interviewiq.ui.components;

import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern styled button supporting primary, secondary, success, danger, and outline styling.
 */
public class ModernButton extends JButton {
    public enum ButtonType {
        PRIMARY, SECONDARY, SUCCESS, DANGER, OUTLINE
    }

    private final ThemeService themeService;
    private final ButtonType type;
    private boolean hover = false;
    private int cornerRadius = 10;

    public ModernButton(String text, ButtonType type, ThemeService themeService) {
        super(text);
        this.type = type;
        this.themeService = themeService;
        setFont(AppConstants.FONT_REGULAR_BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ColorScheme cs = themeService != null ? themeService.getCurrentScheme() : new ColorScheme(false);
        Color baseColor;
        Color textColor = Color.WHITE;
        Color borderColor = null;

        switch (type) {
            case SUCCESS:
                baseColor = cs.getSuccess();
                break;
            case DANGER:
                baseColor = cs.getDanger();
                break;
            case SECONDARY:
                baseColor = cs.getSecondary();
                break;
            case OUTLINE:
                baseColor = hover ? (cs.isDark() ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 15)) : new Color(0, 0, 0, 0);
                textColor = cs.getText();
                borderColor = cs.getBorders();
                break;
            case PRIMARY:
            default:
                baseColor = cs.getPrimary();
                break;
        }

        if (hover && type != ButtonType.OUTLINE) {
            // brighten or darken slightly on hover
            baseColor = cs.isDark() ? baseColor.brighter() : baseColor.darker();
        }

        int w = getWidth();
        int h = getHeight();

        // Draw background
        g2.setColor(baseColor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

        // Draw outline border if applicable
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();

        setForeground(textColor);
        super.paintComponent(g);
    }
}
