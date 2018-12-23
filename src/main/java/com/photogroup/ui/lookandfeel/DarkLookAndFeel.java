package com.photogroup.ui.lookandfeel;

import javax.swing.UIDefaults;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

@SuppressWarnings("restriction")
public class DarkLookAndFeel extends WindowsLookAndFeel {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.java.swing.plaf.windows.WindowsLookAndFeel#initSystemColorDefaults(javax.swing.UIDefaults)
     */
    @Override
    protected void initSystemColorDefaults(UIDefaults table) {
        String[] defaultSystemColors = { "desktop", "#005C5C", /* Color of the desktop background */
                "activeCaption", "#000080", /* Color for captions (title bars) when they are active. */
                "activeCaptionText", "#FFFFFF", /* Text color for text in captions (title bars). */
                "activeCaptionBorder", "#C0C0C0", /* Border color for caption (title bar) window borders. */
                "inactiveCaption", "#808080", /* Color for captions (title bars) when not active. */
                "inactiveCaptionText", "#C0C0C0", /* Text color for text in inactive captions (title bars). */
                "inactiveCaptionBorder", "#C0C0C0", /* Border color for inactive caption (title bar) window borders. */
                "window", "#FFFFFF", /* Default color for the interior of windows */
                "windowBorder", "#000000", /* ??? */
                "windowText", "#000000", /* ??? */
                "menu", "#C0C0C0", /* Background color for menus */
                "menuPressedItemB", "#000080", /* LightShadow of menubutton highlight */
                "menuPressedItemF", "#FFFFFF", /* Default color for foreground "text" in menu item */
                "menuText", "#000000", /* Text color for menus */
                "text", "#C0C0C0", /* Text background color */
                "textText", "#000000", /* Text foreground color */
                "textHighlight", "#000080", /* Text background color when selected */
                "textHighlightText", "#FFFFFF", /* Text color when selected */
                "textInactiveText", "#808080", /* Text color when disabled */
                "control", "#C0C0C0", /* Default color for controls (buttons, sliders, etc) */
                "controlText", "#000000", /* Default color for text in controls */
                "controlHighlight", "#C0C0C0",

                /* "controlHighlight", "#E0E0E0", */ /* Specular highlight (opposite of the shadow) */
                "controlLtHighlight", "#FFFFFF", /* Highlight color for controls */
                "controlShadow", "#808080", /* Shadow color for controls */
                "controlDkShadow", "#000000", /* Dark shadow color for controls */
                "scrollbar", "#E0E0E0", /* Scrollbar background (usually the "track") */
                "info", "#FFFFE1", /* ??? */
                "infoText", "#000000" /* ??? */
        };

        loadSystemColors(table, defaultSystemColors, isNativeLookAndFeel());
    }
}
