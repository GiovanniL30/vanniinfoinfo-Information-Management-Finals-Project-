package client.view.utility;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.DropDown;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private Button searchButton;
    private Button clearButton;
    private SortDropDown sortDropDown;

    public SearchBar(Dimension dimension) {
        setLayout(new BorderLayout());
        setPreferredSize(dimension);

        searchField = new JTextField();
        searchButton = new Button("Search", new Dimension(100, 48), FontFactory.newPoppinsDefault(18));
        clearButton = new Button("X", new Dimension(50, 48), FontFactory.newPoppinsDefault(18));

        searchButton.setBackground(Color.red);
        searchButton.setForeground(Color.WHITE);

        clearButton.setBackground(Color.red);
        clearButton.setForeground(Color.WHITE);

        sortDropDown = new SortDropDown(new Dimension(120, 48));
        sortDropDown.setVisible(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, -5));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(sortDropDown);

        add(searchField, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public SortDropDown getSortDropDown() {
        return sortDropDown;
    }
    public void showDropDown() {
        sortDropDown.setVisible(true);
    }
    public void hideDropDown() {
        sortDropDown.setVisible(false);
    }
}