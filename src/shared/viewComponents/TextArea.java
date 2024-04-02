package shared.viewComponents;


import javax.swing.*;
import java.awt.*;

public class TextArea extends JPanel {

    private JTextArea jTextArea = new JTextArea();
    public TextArea(Dimension dimension, String title, String defaultText) {


        setBackground(Color.white);
        setPreferredSize(dimension);
        setLayout(new BorderLayout());

        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        JLabel label = new JLabel(title);

        jTextArea.setText(defaultText);

        add(label, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
    }

    public String getText() {
        String s = jTextArea.getText().trim();

        if(s.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Description is Empty");
            return null;
        }

        return s;
    }


}
