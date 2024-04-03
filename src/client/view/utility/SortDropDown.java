package client.view.utility;

import shared.viewComponents.DropDown;

import javax.swing.*;
import java.awt.*;

public class SortDropDown extends DropDown {
    private static final String[] options = {"Name ^", "Name v", "Date ^", "Date v"};

    public SortDropDown(Dimension dimension) {
        super(dimension, "Sort", options);
    }
}