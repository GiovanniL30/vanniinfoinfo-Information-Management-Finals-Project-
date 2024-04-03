package client.view.utility;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.DropDown;

import javax.swing.*;
import java.awt.*;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private Button searchButton;
    private Button clearButton;
    private DropDown sortDropDown;

    public SearchBar(Dimension dimension) {
        setLayout(new BorderLayout());
        setPreferredSize(dimension);

        searchField = new JTextField();
        searchButton = new Button("Search", new Dimension(100, 50), FontFactory.newPoppinsDefault(18));
        clearButton = new Button("X", new Dimension(50, 50), FontFactory.newPoppinsDefault(18));

        searchButton.setBackground(Color.red);
        searchButton.setForeground(Color.WHITE);

        clearButton.setBackground(Color.red);
        clearButton.setForeground(Color.WHITE);

        sortDropDown = new SortDropDown(new Dimension(120, 50));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
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

    public DropDown getSortDropDown() {
        return sortDropDown;
    }
}