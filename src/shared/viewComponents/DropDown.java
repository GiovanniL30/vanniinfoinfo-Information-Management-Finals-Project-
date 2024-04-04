package shared.viewComponents;

import shared.utilityClasses.FontFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DropDown extends JPanel {
    private JComboBox<String> comboBox = new JComboBox<>();

    public DropDown(Dimension dimension, String title, String[] contents) {

        setBackground(Color.white);
       // setPreferredSize(dimension);
        setLayout(new BorderLayout());


        comboBox.setFont(FontFactory.newPoppinsDefault(13));
        comboBox.setPreferredSize(dimension);


        for (String s : contents) {
            comboBox.addItem(s);
        }
        comboBox.setSelectedItem(0);
        comboBox.setSelectedIndex(0);

        JLabel label = new JLabel(title);

        add(label, BorderLayout.NORTH);
        add(comboBox, BorderLayout.CENTER);

    }

    public String getChoice(){
       return Objects.requireNonNull(comboBox.getSelectedItem()).toString();
    }

    public int choiceIndex() {
        return comboBox.getSelectedIndex();
    }

    public void enable(boolean e){
        comboBox.setEnabled(e);
    }
    public void addActionListener(ActionListener listener) {
        comboBox.addActionListener(listener);
    }


}
