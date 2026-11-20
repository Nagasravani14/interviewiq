package com.interviewiq.ui.panels;

import com.interviewiq.model.AssessmentType;
import com.interviewiq.service.AnalyticsService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.ui.components.ModernCard;
import com.interviewiq.ui.components.StatCard;
import com.interviewiq.ui.components.TopicPerformanceChart;
import com.interviewiq.ui.components.WeeklyActivityChart;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;
import com.interviewiq.util.DateTimeUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Map;

/**
 * Module G: Comprehensive Analytics Dashboard showing readiness metrics,
 * weekly activity patterns, topic mastery, and distribution of attempts.
 */
public class AnalyticsPanel extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final AnalyticsService analyticsService;

    // Top Metric Cards
    private StatCard totalAttemptsCard;
    private StatCard accuracyCard;
    private StatCard studyTimeCard;
    private StatCard streakCard;

    // Charts
    private WeeklyActivityChart weeklyChart;
    private TopicPerformanceChart topicChart;

    // Assessment Type Distribution Panel (Custom Donut/Bar)
    private JPanel distributionContainer;
    private JPanel strengthsContainer;
    private JPanel weaknessesContainer;

    public AnalyticsPanel(ThemeService themeService, AnalyticsService analyticsService) {
        this.themeService = themeService;
        this.analyticsService = analyticsService;

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        // 1. Top KPI Row
        content.add(createTopKPIRow());
        content.add(Box.createVerticalStrut(18));

        // 2. Middle Charts Row
        content.add(createChartsRow());
        content.add(Box.createVerticalStrut(18));

        // 3. Bottom Row: Distribution & Recommendations
        content.add(createBottomRow());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        refreshData();
    }

    private JPanel createTopKPIRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);

        ColorScheme cs = themeService.getCurrentScheme();
        totalAttemptsCard = new StatCard("Total Assessments", "0", "Evaluated sessions", "📈", cs.getPrimary(), themeService);
        accuracyCard = new StatCard("Overall Accuracy", "0%", "Cumulative performance", "🎯", cs.getSuccess(), themeService);
        studyTimeCard = new StatCard("Time Practiced", "0 min", "Active interview time", "⏱️", cs.getWarning(), themeService);
        streakCard = new StatCard("Current Streak", "0 days", "Consistent study habits", "🔥", cs.getPrimary(), themeService);

        row.add(totalAttemptsCard);
        row.add(accuracyCard);
        row.add(studyTimeCard);
        row.add(streakCard);
        return row;
    }

    private JPanel createChartsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);

        // Chart 1: Activity
        ModernCard c1 = new ModernCard(themeService);
        c1.setLayout(new BorderLayout());
        c1.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel l1 = new JLabel("Weekly Activity Pattern");
        l1.setFont(AppConstants.FONT_HEADING);
        c1.add(l1, BorderLayout.NORTH);
        weeklyChart = new WeeklyActivityChart(themeService);
        c1.add(weeklyChart, BorderLayout.CENTER);

        // Chart 2: Topic Mastery
        ModernCard c2 = new ModernCard(themeService);
        c2.setLayout(new BorderLayout());
        c2.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel l2 = new JLabel("Topic Mastery Breakdown");
        l2.setFont(AppConstants.FONT_HEADING);
        c2.add(l2, BorderLayout.NORTH);
        topicChart = new TopicPerformanceChart(themeService);
        c2.add(topicChart, BorderLayout.CENTER);

        row.add(c1);
        row.add(c2);
        return row;
    }

    private JPanel createBottomRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 14, 0));
        row.setOpaque(false);

        // Assessment Type Distribution
        ModernCard distCard = new ModernCard(themeService);
        distCard.setLayout(new BorderLayout(0, 10));
        distCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel distTitle = new JLabel("Assessment Type Distribution");
        distTitle.setFont(AppConstants.FONT_HEADING);
        distCard.add(distTitle, BorderLayout.NORTH);

        distributionContainer = new JPanel();
        distributionContainer.setOpaque(false);
        distributionContainer.setLayout(new BoxLayout(distributionContainer, BoxLayout.Y_AXIS));
        distCard.add(distributionContainer, BorderLayout.CENTER);

        // Strengths & Weaknesses
        ModernCard focusCard = new ModernCard(themeService);
        focusCard.setLayout(new BorderLayout(0, 10));
        focusCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel focusTitle = new JLabel("Skill Strengths & Focus Areas");
        focusTitle.setFont(AppConstants.FONT_HEADING);
        focusCard.add(focusTitle, BorderLayout.NORTH);

        JPanel focusBody = new JPanel(new GridLayout(2, 1, 0, 12));
        focusBody.setOpaque(false);

        strengthsContainer = new JPanel();
        strengthsContainer.setOpaque(false);
        strengthsContainer.setLayout(new BoxLayout(strengthsContainer, BoxLayout.Y_AXIS));

        weaknessesContainer = new JPanel();
        weaknessesContainer.setOpaque(false);
        weaknessesContainer.setLayout(new BoxLayout(weaknessesContainer, BoxLayout.Y_AXIS));

        focusBody.add(strengthsContainer);
        focusBody.add(weaknessesContainer);
        focusCard.add(focusBody, BorderLayout.CENTER);

        row.add(distCard);
        row.add(focusCard);
        return row;
    }

    public void refreshData() {
        totalAttemptsCard.setValue(String.valueOf(analyticsService.getTotalAttempts()));
        accuracyCard.setValue(analyticsService.getAverageAccuracy() + "%");
        studyTimeCard.setValue(DateTimeUtils.formatDurationFriendly(analyticsService.getTotalStudyTimeSeconds()));
        streakCard.setValue(analyticsService.calculateStudyStreakDays() + " days");

        weeklyChart.setData(analyticsService.getWeeklyActivity());
        topicChart.setData(analyticsService.getCategoryAccuracyMap());

        // Assessment Type Distribution
        distributionContainer.removeAll();
        Map<AssessmentType, Integer> dist = analyticsService.getAssessmentTypeDistribution();
        int total = analyticsService.getTotalAttempts();

        for (Map.Entry<AssessmentType, Integer> e : dist.entrySet()) {
            JPanel row = new JPanel(new BorderLayout(8, 4));
            row.setOpaque(false);
            row.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));

            JLabel name = new JLabel(e.getKey().getDisplayName());
            name.setFont(AppConstants.FONT_REGULAR);
            name.setForeground(themeService.getCurrentScheme().getText());

            double pct = total > 0 ? ((double) e.getValue() / total) * 100.0 : 0.0;
            JLabel val = new JLabel(e.getValue() + " attempts (" + Math.round(pct) + "%)");
            val.setFont(AppConstants.FONT_SMALL_BOLD);
            val.setForeground(themeService.getCurrentScheme().getPrimary());

            row.add(name, BorderLayout.WEST);
            row.add(val, BorderLayout.EAST);
            distributionContainer.add(row);
        }
        distributionContainer.revalidate();
        distributionContainer.repaint();

        // Strengths & Weaknesses
        strengthsContainer.removeAll();
        JLabel strHead = new JLabel("Top Strengths (High Accuracy):");
        strHead.setFont(AppConstants.FONT_SMALL_BOLD);
        strHead.setForeground(themeService.getCurrentScheme().getSuccess());
        strengthsContainer.add(strHead);
        strengthsContainer.add(Box.createVerticalStrut(4));

        List<Map.Entry<String, Double>> strengths = analyticsService.getTopStrengths(2);
        if (strengths.isEmpty()) {
            JLabel empty = new JLabel("Complete assessments to highlight your top strengths.");
            empty.setFont(AppConstants.FONT_SMALL);
            strengthsContainer.add(empty);
        } else {
            for (Map.Entry<String, Double> s : strengths) {
                JLabel item = new JLabel("⭐ " + s.getKey() + " (" + Math.round(s.getValue()) + "%)");
                item.setFont(AppConstants.FONT_REGULAR);
                strengthsContainer.add(item);
            }
        }

        weaknessesContainer.removeAll();
        JLabel weakHead = new JLabel("Areas Needing Revision (Low Accuracy):");
        weakHead.setFont(AppConstants.FONT_SMALL_BOLD);
        weakHead.setForeground(themeService.getCurrentScheme().getWarning());
        weaknessesContainer.add(weakHead);
        weaknessesContainer.add(Box.createVerticalStrut(4));

        List<Map.Entry<String, Double>> weaknesses = analyticsService.getTopicsNeedingImprovement(2);
        if (weaknesses.isEmpty()) {
            JLabel empty = new JLabel("No areas of concern identified yet.");
            empty.setFont(AppConstants.FONT_SMALL);
            weaknessesContainer.add(empty);
        } else {
            for (Map.Entry<String, Double> w : weaknesses) {
                JLabel item = new JLabel("📌 " + w.getKey() + " (" + Math.round(w.getValue()) + "%)");
                item.setFont(AppConstants.FONT_REGULAR);
                weaknessesContainer.add(item);
            }
        }

        strengthsContainer.revalidate();
        strengthsContainer.repaint();
        weaknessesContainer.revalidate();
        weaknessesContainer.repaint();
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        repaint();
    }
}
