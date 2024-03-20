package shared.viewComponents;

import javax.swing.*;
import java.awt.*;

public class FilledButton extends Button {

    public FilledButton(String buttonText, Dimension size, Font font, Color fillColor, Color textColor) {
        super(buttonText, size, font);

        setForeground(textColor);
        setBackground(fillColor);
    }

}
