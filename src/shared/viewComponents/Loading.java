package shared.viewComponents;

import javax.swing.*;
import java.awt.*;

/**
 * A custom dialog for displaying a loading indicator
 */

public class Loading extends JDialog {
    public Loading(Frame parent) {
        // call the superclass constructor with the parent frame, title, and modal
        super(parent, "Loading...", true);
        // Set the size of the dialog to 100x30 pixels
        setSize(new Dimension(100, 30));
        // Disable resizing of the dialog
        setResizable(false);
        // Center the dialog on the parent frame
        setLocationRelativeTo(null);
    } // End of Loading constructor
} // End of Loading class

