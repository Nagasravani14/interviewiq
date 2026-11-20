package com.interviewiq;

import com.interviewiq.ui.MainFrame;
import com.interviewiq.web.InterviewIQWebServer;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

/**
 * Application entry point for InterviewIQ.
 * Supports both Desktop Swing GUI and localhost Web server.
 */
public class Main {
    public static void main(String[] args) {
        boolean startWeb = false;
        boolean forceWebOnly = false;
        int port = 8080;

        for (String arg : args) {
            if ("--web".equalsIgnoreCase(arg) || "--server".equalsIgnoreCase(arg) || "web".equalsIgnoreCase(arg)) {
                startWeb = true;
                forceWebOnly = true;
            }
            if ("--both".equalsIgnoreCase(arg)) {
                startWeb = true;
            }
            if (arg.startsWith("--port=")) {
                try {
                    port = Integer.parseInt(arg.substring(7));
                } catch (Exception ignored) {}
            }
        }

        if (GraphicsEnvironment.isHeadless()) {
            startWeb = true;
            forceWebOnly = true;
        }

        if (startWeb) {
            try {
                InterviewIQWebServer webServer = new InterviewIQWebServer(port);
                int actualPort = webServer.start();
                if (forceWebOnly) {
                    // Keep process alive for web requests
                    Thread.currentThread().join();
                    return;
                }
            } catch (Exception e) {
                System.err.println("Could not start localhost web server on port " + port + ": " + e.getMessage());
            }
        }

        // Configure high-DPI scaling and crisp font rendering for Swing desktop UI
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Fatal error starting InterviewIQ Desktop: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
