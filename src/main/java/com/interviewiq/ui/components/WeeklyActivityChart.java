package com.interviewiq.ui.components;

import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom Java 2D bar chart rendering weekly preparation activity over the past 7 days.
 */
public class WeeklyActivityChart extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private Map<String, Integer> data = new LinkedHashMap<>();

    public WeeklyActivityChart(ThemeService themeService) {
        this.themeService = themeService;
        setOpaque(false);
        setPreferredSize(new Dimension(380, 220));
        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
    }

    public void setData(Map<String, Integer> data) {
        this.data = data != null ? new LinkedHashMap<>(data) : new LinkedHashMap<>();
        repaint();
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        ColorScheme cs = themeService != null ? themeService.getCurrentScheme() : new ColorScheme(false);
        int width = getWidth();
        int height = getHeight();

        int padLeft = 35;
        int padRight = 20;
        int padTop = 30;
        int padBottom = 35;

        int chartWidth = width - padLeft - padRight;
        int chartHeight = height - padTop - padBottom;

        if (chartWidth <= 0 || chartHeight <= 0) {
            g2.dispose();
            return;
        }

        // Find max value
        int maxVal = 4; // minimum scale
        for (int v : data.values()) {
            if (v > maxVal) maxVal = v;
        }

        // Draw horizontal grid lines & Y labels
        g2.setFont(AppConstants.FONT_SMALL);
        FontMetrics fm = g2.getFontMetrics();
        int gridSteps = 4;
        for (int i = 0; i <= gridSteps; i++) {
            int yVal = (maxVal * i) / gridSteps;
            int yPos = padTop + chartHeight - (int) (((double) i / gridSteps) * chartHeight);

            // Grid line
            g2.setColor(cs.isDark() ? new Color(255, 255, 255, 18) : new Color(0, 0, 0, 15));
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawLine(padLeft, yPos, padLeft + chartWidth, yPos);

            // Y Axis Label
            g2.setColor(cs.getTextMuted());
            String yLabel = String.valueOf(yVal);
            g2.drawString(yLabel, padLeft - fm.stringWidth(yLabel) - 6, yPos + 4);
        }

        if (data.isEmpty()) {
            g2.setColor(cs.getTextMuted());
            String emptyMsg = "No attempts recorded this week";
            g2.drawString(emptyMsg, padLeft + (chartWidth - fm.stringWidth(emptyMsg)) / 2, padTop + chartHeight / 2);
            g2.dispose();
            return;
        }

        // Draw Bars
        int barCount = data.size();
        int slotWidth = chartWidth / barCount;
        int barWidth = Math.max(16, slotWidth - 18);

        int idx = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int val = entry.getValue();
            int barHeight = (int) (((double) val / maxVal) * chartHeight);
            int x = padLeft + idx * slotWidth + (slotWidth - barWidth) / 2;
            int y = padTop + chartHeight - barHeight;

            // Bar background slot (subtle track)
            g2.setColor(cs.isDark() ? new Color(255, 255, 255, 8) : new Color(0, 0, 0, 6));
            g2.fillRoundRect(x, padTop, barWidth, chartHeight, 6, 6);

            // Bar fill
            if (val > 0) {
                g2.setColor(cs.getPrimary());
                g2.fillRoundRect(x, y, barWidth, barHeight, 6, 6);

                // Value above bar
                g2.setColor(cs.getText());
                g2.setFont(AppConstants.FONT_SMALL_BOLD);
                String valStr = String.valueOf(val);
                g2.drawString(valStr, x + (barWidth - g2.getFontMetrics().stringWidth(valStr)) / 2, y - 5);
            }

            // X Axis Day Label
            g2.setColor(cs.getTextMuted());
            g2.setFont(AppConstants.FONT_SMALL);
            String day = entry.getKey();
            g2.drawString(day, x + (barWidth - g2.getFontMetrics().stringWidth(day)) / 2, padTop + chartHeight + 18);

            idx++;
        }

        g2.dispose();
    }
}
