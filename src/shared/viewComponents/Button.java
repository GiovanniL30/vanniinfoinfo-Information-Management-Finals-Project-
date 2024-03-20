package shared.viewComponents;

import javax.swing.*;
import java.awt.*;

/**
 * Class for creating a texted button with specified dimensions and font
 */
public class Button extends JButton {
    public Button(String buttonText, Dimension size,  Font font) {
        super(buttonText);

        setFocusable(false);
        setPreferredSize(new Dimension(size.width, size.height));
        setFont(font);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }


}

