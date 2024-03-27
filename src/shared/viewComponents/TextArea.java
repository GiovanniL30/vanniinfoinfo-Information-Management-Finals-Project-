package shared.viewComponents;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class TextArea extends JPanel {

    public TextArea(Dimension dimension, String title, String defaultText) {


        setBackground(Color.white);
        setPreferredSize(dimension);
        setLayout(new BorderLayout());

        JTextArea jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        JLabel label = new JLabel(title);

        jTextArea.setText(defaultText);

        add(label, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
    }


}
