package shared.viewComponents;

import javax.swing.*;
import java.awt.*;

/**
 * The class for displaying an image.
 */
public class Picture extends JPanel {

    private int width;
    private int height;

    public Picture(String filePath, int width, int height) {
        this.width = width;
        this.height = height;
        ImageIcon icon = new ImageIcon(filePath);


        // Scale the image to the specified dimensions
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        // Set the size of the panel to match the image dimensions
        setSize(new Dimension(width, height));
        // Add the image to the panel
        add(new JLabel(new ImageIcon(image)));
    }

    /**
     * Updates the picture with the given file path.
     * @param  filePath the file path of the picture to be updated
     */
    public void updatePicture(String filePath){
        removeAll(); // Remove all components from the panel

        // Update the image
        ImageIcon icon = new ImageIcon(filePath);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setSize(new Dimension(width, height));
        add(new JLabel(new ImageIcon(image)));

        revalidate(); // revalidate the panel to ensure proper layout
        repaint(); // Repaint the panel to reflect the changes
    } // end of updatePicture method
} // end of class Picture

