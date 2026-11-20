package com.interviewiq.ui.components;

import com.interviewiq.service.ThemeService;
import com.interviewiq.util.AppConstants;
import com.interviewiq.util.ColorScheme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Modern navigation sidebar with 9 main module items, active pill indicators, and custom logo.
 */
public class Sidebar extends JPanel implements ThemeService.ThemeChangeListener {

    public static class NavItem {
        private final String id;
        private final String label;
        private final String iconSymbol;
        private NavButton button;

        public NavItem(String id, String label, String iconSymbol) {
            this.id = id;
            this.label = label;
            this.iconSymbol = iconSymbol;
        }

        public String getId() { return id; }
        public String getLabel() { return label; }
        public String getIconSymbol() { return iconSymbol; }
    }

    private class NavButton extends JButton {
        private final NavItem item;
        private boolean active = false;
        private boolean hover = false;

        public NavButton(NavItem item) {
            super("  " + item.getIconSymbol() + "   " + item.getLabel());
            this.item = item;
            item.button = this;
            setFont(AppConstants.FONT_REGULAR_BOLD);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(225, 42));
            setPreferredSize(new Dimension(225, 42));
            setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

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

            addActionListener(e -> selectItem(item.getId()));
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ColorScheme cs = themeService.getCurrentScheme();
            int w = getWidth();
            int h = getHeight();

            if (active) {
                // Active background pill
                g2.setColor(cs.isDark() ? new Color(100, 133, 255, 45) : new Color(49, 94, 251, 25));
                g2.fillRoundRect(8, 2, w - 16, h - 4, 10, 10);

                // Active left accent bar
                g2.setColor(cs.getPrimary());
                g2.fillRoundRect(8, 6, 4, h - 12, 4, 4);

                setForeground(cs.getPrimary());
            } else if (hover) {
                g2.setColor(cs.isDark() ? new Color(255, 255, 255, 12) : new Color(0, 0, 0, 10));
                g2.fillRoundRect(8, 2, w - 16, h - 4, 10, 10);
                setForeground(cs.getText());
            } else {
                setForeground(cs.getTextMuted());
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private final ThemeService themeService;
    private final List<NavItem> navItems = new ArrayList<>();
    private final Consumer<String> onNavigationSelect;
    private String activeId = "dashboard";

    private final JLabel brandTitleLabel;
    private final JLabel brandSubLabel;
    private final JLabel footerVersionLabel;

    public Sidebar(ThemeService themeService, Consumer<String> onNavigationSelect) {
        this.themeService = themeService;
        this.onNavigationSelect = onNavigationSelect;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(235, 800));
        setMinimumSize(new Dimension(200, 600));

        // 1. Header (Logo & Brand)
        JPanel brandPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw custom stylized logo
                int logoX = 18;
                int logoY = 18;
                int logoSize = 34;
                g2.setColor(themeService.getCurrentScheme().getPrimary());
                g2.fillRoundRect(logoX, logoY, logoSize, logoSize, 10, 10);

                // Draw "IQ" symbol inside
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
                g2.drawString("IQ", logoX + 6, logoY + 24);
                g2.dispose();
            }
        };
        brandPanel.setOpaque(false);
        brandPanel.setPreferredSize(new Dimension(235, 75));
        brandPanel.setLayout(null);

        brandTitleLabel = new JLabel("InterviewIQ");
        brandTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        brandTitleLabel.setBounds(62, 16, 150, 24);

        brandSubLabel = new JLabel("Smart Mock & Prep");
        brandSubLabel.setFont(AppConstants.FONT_SMALL);
        brandSubLabel.setBounds(62, 38, 150, 16);

        brandPanel.add(brandTitleLabel);
        brandPanel.add(brandSubLabel);

        add(brandPanel, BorderLayout.NORTH);

        // 2. Navigation List
        JPanel navListPanel = new JPanel();
        navListPanel.setOpaque(false);
        navListPanel.setLayout(new BoxLayout(navListPanel, BoxLayout.Y_AXIS));
        navListPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        initNavItems();
        for (NavItem item : navItems) {
            NavButton btn = new NavButton(item);
            navListPanel.add(btn);
            navListPanel.add(Box.createVerticalStrut(4));
        }

        add(navListPanel, BorderLayout.CENTER);

        // 3. Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        footerPanel.setOpaque(false);

        footerVersionLabel = new JLabel("InterviewIQ v1.0.0");
        footerVersionLabel.setFont(AppConstants.FONT_SMALL);
        footerPanel.add(footerVersionLabel);

        add(footerPanel, BorderLayout.SOUTH);

        if (themeService != null) {
            themeService.addThemeChangeListener(this);
        }
        updateColors();
        for (NavItem item : navItems) {
            if (item.button != null) {
                item.button.setActive(item.getId().equalsIgnoreCase("dashboard"));
            }
        }
    }

    private void initNavItems() {
        navItems.add(new NavItem("dashboard", "Dashboard", "🏠"));
        navItems.add(new NavItem("prep", "Interview Prep", "📚"));
        navItems.add(new NavItem("practice", "Practice Arena", "🎯"));
        navItems.add(new NavItem("coding", "Coding Assessment", "💻"));
        navItems.add(new NavItem("interview", "Mock Interview", "🎙️"));
        navItems.add(new NavItem("quiz", "Quiz Arena", "📝"));
        navItems.add(new NavItem("analytics", "Analytics", "📊"));
        navItems.add(new NavItem("history", "History", "⏱️"));
        navItems.add(new NavItem("settings", "Settings", "⚙️"));
    }

    public void selectItem(String id) {
        this.activeId = id;
        for (NavItem item : navItems) {
            if (item.button != null) {
                item.button.setActive(item.getId().equalsIgnoreCase(id));
            }
        }
        if (onNavigationSelect != null) {
            onNavigationSelect.accept(id);
        }
    }

    public String getActiveId() {
        return activeId;
    }

    private void updateColors() {
        ColorScheme cs = themeService.getCurrentScheme();
        setBackground(cs.getSidebar());
        brandTitleLabel.setForeground(cs.getText());
        brandSubLabel.setForeground(cs.getTextMuted());
        footerVersionLabel.setForeground(cs.getTextMuted());
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
        g2.setColor(themeService.getCurrentScheme().getSidebar());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Right subtle vertical border
        g2.setColor(themeService.getCurrentScheme().getBorders());
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        g2.dispose();
    }
}
