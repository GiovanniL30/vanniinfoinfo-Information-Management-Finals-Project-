package shared.viewComponents;


import javax.swing.*;
import java.awt.*;

/**
 * Class for creating a button with an Icon
 */
public class IconButton extends JButton{
    public IconButton( String imagePath, int width, int height) {
        // Create an Image icon from the provided image path
        ImageIcon icon = new ImageIcon(imagePath);

        // Scale the image to the specified width and length
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Set various properties of the button to customize its appearance
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        // Set the scaled image as the button's icon
        setIcon(new ImageIcon(image));
        setFocusable(false);
        setSize(new Dimension(width, height));
        // Set the cursor to a hand cursor when the mouse hovers over the button
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    } // End of IconButton constructor


} // End of IconButton class

