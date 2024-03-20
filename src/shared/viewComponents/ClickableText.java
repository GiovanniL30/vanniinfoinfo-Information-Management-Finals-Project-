package shared.viewComponents;

import javax.swing.*;
import java.awt.*;

public class ClickableText extends JButton {

    /**
     * Constructs a ClickableText object with the specified text, width, height, and font.
     * @param buttonText th text to display
     * @param width the preferred size of the width
     * @param height the preferred height of the clickable text
     * @param font the font
     */
    public ClickableText(String buttonText, int width, int height, Font font) {
        super(buttonText); // set button text

        // Configure the button appearance
        setFocusPainted(false); // Disable focus painting
        setContentAreaFilled(false); // Disable content area filling
        setBorderPainted(false); // Disable border painting
        setOpaque(false); // Make the button transparent

        setSize(new Dimension(width, height)); // Set size
        setFont(font); // Set font
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set cursor to hand when hovering over the button
    } // end of constructor
} // end of ClickableText class

