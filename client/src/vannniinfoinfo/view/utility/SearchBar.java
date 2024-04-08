package vannniinfoinfo.view.utility;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.DropDown;
import shared.viewComponents.IconButton;

import javax.swing.*;
import java.awt.*;

public class SearchBar extends JPanel {
    private JTextField searchField;
    private IconButton searchButton;
    private Button clearButton;
    private DropDown dropDown;

    public SearchBar(Dimension dimension) {
        setLayout(new BorderLayout());
        setPreferredSize(dimension);

        searchField = new JTextField();
        searchButton = new IconButton("resources/images/searchIcon.png", 48, 48);
        clearButton = new Button("X", new Dimension(50, 48), FontFactory.newPoppinsDefault(15));

        searchButton.setFocusable(true);

        clearButton.setBackground(Color.red);
        clearButton.setForeground(Color.WHITE);

        dropDown = new DropDown(new Dimension(120, 48),"" ,new String[]{"Name ^", "Name v", "Date ^", "Date v"});
        dropDown.setVisible(true);

        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(Color.WHITE);

        searchBarPanel.add(searchField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, -5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        searchBarPanel.add(buttonPanel, BorderLayout.EAST);

        add(searchBarPanel, BorderLayout.CENTER);
        add(dropDown, BorderLayout.EAST);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public IconButton getSearchButton() {
        return searchButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public DropDown getSortDropDown() {
        return dropDown;
    }
    public void showDropDown() {
        dropDown.setVisible(true);
    }
    public void hideDropDown() {
        dropDown.setVisible(false);
    }
}