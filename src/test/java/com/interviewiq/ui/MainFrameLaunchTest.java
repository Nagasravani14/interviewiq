package com.interviewiq.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class MainFrameLaunchTest {

    @Test
    @DisplayName("Verify MainFrame instantiates cleanly and navigates to all 9 modules")
    void testMainFrameInstantiationAndNavigation() throws InterruptedException, InvocationTargetException {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping UI test in headless environment");
            return;
        }

        SwingUtilities.invokeAndWait(() -> {
            MainFrame frame = new MainFrame();
            assertNotNull(frame);
            assertEquals("InterviewIQ – Smart Mock Interview & Assessment Platform", frame.getTitle());

            // Navigate through each of the 9 modules to verify no runtime NPE or exception
            String[] modules = {"dashboard", "prep", "practice", "coding", "interview", "quiz", "analytics", "history", "settings"};
            for (String module : modules) {
                frame.navigateTo(module);
            }

            frame.dispose();
        });
    }
}
