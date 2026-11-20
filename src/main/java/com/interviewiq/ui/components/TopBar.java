package com.interviewiq.ui.components;

import com.interviewiq.model.UserPreferences;
import com.interviewiq.repository.PreferencesRepository;
import com.interviewiq.service.AnalyticsService;
import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Modern top navigation bar with breadcrumbs, theme toggle, streak indicator, and user profile.
 */
public class TopBar extends JPanel implements ThemeService.ThemeChangeListener {
    private final ThemeService themeService;
    private final PreferencesRepository preferencesRepository;
    private final AnalyticsService analyticsService;
    private final Consumer<String> onQuickAction;

    private final JLabel pageTitleLabel;
    private final JLabel pageSubtitleLabel;
    private final JButton themeToggleButton;
    private final JLabel streakLabel;
    private final JLabel userNameLabel;
    private final JPanel userPillPanel;

    public TopBar(ThemeService themeService, PreferencesRepository preferencesRepository,
                  AnalyticsService analyticsService, Consumer<String> onQuickAction) {
        this.themeService = themeService;
        this.preferencesRepository = preferencesRepository;
        this.analyticsService = analyticsService;
        this.onQuickAction = onQuickAction;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1100, 68));
        setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        setOpaque(false);

        // Left: Page Title & Breadcrumb Subtitle
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        pageTitleLabel = new JLabel("Dashboard");
        pageTitleLabel.setFont(AppConstants.FONT_TITLE);

        pageSubtitleLabel = new JLabel("Welcome back to your interview preparation dashboard");
        pageSubtitleLabel.setFont(AppConstants.FONT_SMALL);

        leftPanel.add(pageTitleLabel);
        leftPanel.add(Box.createVerticalStrut(2));
        leftPanel.add(pageSubtitleLabel);

        add(leftPanel, BorderLayout.WEST);

        // Right: Theme Toggle, Quick Actions, Profile
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 4));
        rightPanel.setOpaque(false);

        // Quick Quiz Action
        ModernButton quickQuizBtn = new ModernButton("Take Quiz", ModernButton.ButtonType.OUTLINE, themeService);
        quickQuizBtn.setFont(AppConstants.FONT_SMALL_BOLD);
        quickQuizBtn.setPreferredSize(new Dimension(100, 34));
        quickQuizBtn.addActionListener(e -> {
            if (onQuickAction != null) onQuickAction.accept("quiz");
        });

        // Quick Mock Interview Action
        ModernButton quickMockBtn = new ModernButton("Mock Interview", ModernButton.ButtonType.PRIMARY, themeService);
        quickMockBtn.setFont(AppConstants.FONT_SMALL_BOLD);
        quickMockBtn.setPreferredSize(new Dimension(125, 34));
        quickMockBtn.addActionListener(e -> {
            if (onQuickAction != null) onQuickAction.accept("interview");
        });

        // Theme Toggle Button
        themeToggleButton = new JButton(themeService.isDark() ? "☀️ Light" : "🌙 Dark") {
            private boolean hover = false;
            {
                setFont(AppConstants.FONT_SMALL_BOLD);
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(90, 34));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { hover = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ColorScheme cs = themeService.getCurrentScheme();
                int w = getWidth();
                int h = getHeight();

                g2.setColor(hover ? (cs.isDark() ? new Color(255, 255, 255, 25) : new Color(0, 0, 0, 15)) :
                        (cs.isDark() ? new Color(255, 255, 255, 12) : new Color(0, 0, 0, 8)));
                g2.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);

                g2.setColor(cs.getBorders());
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);

                g2.dispose();
                setForeground(cs.getText());
                super.paintComponent(g);
            }
        };
        themeToggleButton.addActionListener(e -> themeService.toggleTheme());

        // Streak badge
        streakLabel = new JLabel("🔥 0d");
        streakLabel.setFont(AppConstants.FONT_SMALL_BOLD);

        // User profile pill
        userPillPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ColorScheme cs = themeService.getCurrentScheme();
                g2.setColor(cs.isDark() ? new Color(255, 255, 255, 12) : new Color(0, 0, 0, 8));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        userPillPanel.setOpaque(false);
        userPillPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JLabel avatarCircle = new JLabel("AJ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(themeService.getCurrentScheme().getPrimary());
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                setForeground(Color.WHITE);
                super.paintComponent(g);
            }
        };
        avatarCircle.setFont(AppConstants.FONT_SMALL_BOLD);
        avatarCircle.setPreferredSize(new Dimension(26, 26));
        avatarCircle.setHorizontalAlignment(JLabel.CENTER);

        UserPreferences prefs = preferencesRepository.load();
        userNameLabel = new JLabel(prefs.getDisplayName());
        userNameLabel.setFont(AppConstants.FONT_REGULAR_BOLD);

        userPillPanel.add(avatarCircle);
        userPillPanel.add(userNameLabel);

        rightPanel.add(quickQuizBtn);
        rightPanel.add(quickMockBtn);
        rightPanel.add(themeToggleButton);
        rightPanel.add(streakLabel);
        rightPanel.add(userPillPanel);

        add(rightPanel, BorderLayout.EAST);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        updateColors();
        refreshUserData();
    }

    public void setPageHeader(String title, String subtitle) {
        pageTitleLabel.setText(title);
        pageSubtitleLabel.setText(subtitle);
    }

    public void refreshUserData() {
        UserPreferences prefs = preferencesRepository.load();
        userNameLabel.setText(prefs.getDisplayName());
        int streak = analyticsService.calculateStudyStreakDays();
        streakLabel.setText("🔥 " + streak + "d");
    }

    private void updateColors() {
        ColorScheme cs = themeService.getCurrentScheme();
        pageTitleLabel.setForeground(cs.getText());
        pageSubtitleLabel.setForeground(cs.getTextMuted());
        themeToggleButton.setText(themeService.isDark() ? "☀️ Light" : "🌙 Dark");
        streakLabel.setForeground(cs.getWarning());
        userNameLabel.setForeground(cs.getText());
        repaint();
    }

    @Override
    public void onThemeChanged(ColorScheme scheme) {
        updateColors();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        ColorScheme cs = themeService.getCurrentScheme();
        g2.setColor(cs.getCards());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Bottom horizontal border
        g2.setColor(cs.getBorders());
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g2.dispose();
    }
}
