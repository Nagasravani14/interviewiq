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
 * Custom horizontal bar chart representing topic-wise accuracy percentages.
 */
public class TopicPerformanceChart extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private Map<String, Double> topicAccuracy = new LinkedHashMap<>();

    public TopicPerformanceChart(ThemeService themeService) {
        this.themeService = themeService;
        setOpaque(false);
        setPreferredSize(new Dimension(380, 220));
        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
    }

    public void setData(Map<String, Double> topicAccuracy) {
        this.topicAccuracy = topicAccuracy != null ? new LinkedHashMap<>(topicAccuracy) : new LinkedHashMap<>();
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

        int padLeft = 140;
        int padRight = 50;
        int padTop = 15;
        int padBottom = 15;

        int chartWidth = width - padLeft - padRight;
        int chartHeight = height - padTop - padBottom;

        if (chartWidth <= 0 || chartHeight <= 0) {
            g2.dispose();
            return;
        }

        if (topicAccuracy.isEmpty()) {
            g2.setColor(cs.getTextMuted());
            g2.setFont(AppConstants.FONT_SMALL);
            String emptyMsg = "No topic results yet. Complete a quiz or assessment!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(emptyMsg, (width - fm.stringWidth(emptyMsg)) / 2, height / 2);
            g2.dispose();
            return;
        }

        int count = Math.min(6, topicAccuracy.size()); // show top up to 6 topics
        int rowHeight = chartHeight / count;
        int barHeight = Math.max(10, Math.min(16, rowHeight - 12));

        int idx = 0;
        for (Map.Entry<String, Double> entry : topicAccuracy.entrySet()) {
            if (idx >= count) break;
            String topic = entry.getKey();
            double acc = entry.getValue();

            int yCenter = padTop + idx * rowHeight + rowHeight / 2;
            int barY = yCenter - barHeight / 2;

            // Draw Topic Label on Left
            g2.setColor(cs.getText());
            g2.setFont(AppConstants.FONT_SMALL);
            FontMetrics fm = g2.getFontMetrics();
            String truncatedTopic = topic;
            if (fm.stringWidth(truncatedTopic) > padLeft - 15) {
                while (truncatedTopic.length() > 4 && fm.stringWidth(truncatedTopic + "...") > padLeft - 15) {
                    truncatedTopic = truncatedTopic.substring(0, truncatedTopic.length() - 1);
                }
                truncatedTopic = truncatedTopic + "...";
            }
            g2.drawString(truncatedTopic, padLeft - fm.stringWidth(truncatedTopic) - 10, yCenter + fm.getAscent() / 2 - 2);

            // Draw Background Bar Track
            g2.setColor(cs.isDark() ? new Color(255, 255, 255, 12) : new Color(0, 0, 0, 10));
            g2.fillRoundRect(padLeft, barY, chartWidth, barHeight, 6, 6);

            // Draw Filled Progress Bar
            int fillWidth = (int) Math.round((acc / 100.0) * chartWidth);
            Color barColor;
            if (acc >= 75.0) {
                barColor = cs.getSuccess();
            } else if (acc >= 50.0) {
                barColor = cs.getWarning();
            } else {
                barColor = cs.getDanger();
            }

            if (fillWidth > 0) {
                g2.setColor(barColor);
                g2.fillRoundRect(padLeft, barY, fillWidth, barHeight, 6, 6);
            }

            // Draw Accuracy % Text on Right
            g2.setColor(cs.getText());
            g2.setFont(AppConstants.FONT_SMALL_BOLD);
            String accText = Math.round(acc) + "%";
            g2.drawString(accText, padLeft + chartWidth + 10, yCenter + fm.getAscent() / 2 - 2);

            idx++;
        }

        g2.dispose();
    }
}
