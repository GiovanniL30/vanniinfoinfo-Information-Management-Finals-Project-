package client.view.utility;

import shared.viewComponents.DropDown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SortDropDown extends DropDown {
    private static final String[] options = {"Name ^", "Name v", "Date ^", "Date v"};
    private int selectedIndex;

    public SortDropDown(Dimension dimension) {
        super(dimension, "", options);
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}