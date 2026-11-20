package com.interviewiq.util;

import java.awt.Color;

/**
 * Manages theme palettes for Light and Dark themes as specified in product design.
 */
public class ColorScheme {

    // Light Theme Colors
    public static final Color LIGHT_BACKGROUND = Color.decode("#F6F8FC");
    public static final Color LIGHT_SIDEBAR    = Color.decode("#FFFFFF");
    public static final Color LIGHT_PRIMARY    = Color.decode("#315EFB");
    public static final Color LIGHT_SECONDARY  = Color.decode("#64748B");
    public static final Color LIGHT_CARDS      = Color.decode("#FFFFFF");
    public static final Color LIGHT_BORDERS    = Color.decode("#E5EAF2");
    public static final Color LIGHT_SUCCESS    = Color.decode("#16A36A");
    public static final Color LIGHT_WARNING    = Color.decode("#E9A23B");
    public static final Color LIGHT_DANGER     = Color.decode("#E05252");
    public static final Color LIGHT_TEXT       = Color.decode("#172033");
    public static final Color LIGHT_TEXT_MUTED = Color.decode("#64748B");
    public static final Color LIGHT_ACCENT     = Color.decode("#EEF2FF");

    // Dark Theme Colors
    public static final Color DARK_BACKGROUND = Color.decode("#0C1220");
    public static final Color DARK_SIDEBAR    = Color.decode("#111A2B");
    public static final Color DARK_PRIMARY    = Color.decode("#6485FF");
    public static final Color DARK_SECONDARY  = Color.decode("#9BAAC2");
    public static final Color DARK_CARDS      = Color.decode("#172338");
    public static final Color DARK_BORDERS    = Color.decode("#26364F");
    public static final Color DARK_SUCCESS    = Color.decode("#36C98F");
    public static final Color DARK_WARNING    = Color.decode("#F5B94C");
    public static final Color DARK_DANGER     = Color.decode("#F17878");
    public static final Color DARK_TEXT       = Color.decode("#F1F5FB");
    public static final Color DARK_TEXT_MUTED = Color.decode("#9BAAC2");
    public static final Color DARK_ACCENT     = Color.decode("#1E2A42");

    private final boolean dark;

    public ColorScheme(boolean dark) {
        this.dark = dark;
    }

    public boolean isDark() {
        return dark;
    }

    public Color getBackground() {
        return dark ? DARK_BACKGROUND : LIGHT_BACKGROUND;
    }

    public Color getSidebar() {
        return dark ? DARK_SIDEBAR : LIGHT_SIDEBAR;
    }

    public Color getPrimary() {
        return dark ? DARK_PRIMARY : LIGHT_PRIMARY;
    }

    public Color getSecondary() {
        return dark ? DARK_SECONDARY : LIGHT_SECONDARY;
    }

    public Color getCards() {
        return dark ? DARK_CARDS : LIGHT_CARDS;
    }

    public Color getBorders() {
        return dark ? DARK_BORDERS : LIGHT_BORDERS;
    }

    public Color getSuccess() {
        return dark ? DARK_SUCCESS : LIGHT_SUCCESS;
    }

    public Color getWarning() {
        return dark ? DARK_WARNING : LIGHT_WARNING;
    }

    public Color getDanger() {
        return dark ? DARK_DANGER : LIGHT_DANGER;
    }

    public Color getText() {
        return dark ? DARK_TEXT : LIGHT_TEXT;
    }

    public Color getTextMuted() {
        return dark ? DARK_TEXT_MUTED : LIGHT_TEXT_MUTED;
    }

    public Color getAccent() {
        return dark ? DARK_ACCENT : LIGHT_ACCENT;
    }
}
