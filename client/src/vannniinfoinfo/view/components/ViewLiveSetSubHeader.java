package vannniinfoinfo.view.components;

import vannniinfoinfo.controller.ClientControllerObserver;
import vannniinfoinfo.view.ClientMainFrame;
import vannniinfoinfo.view.utility.ClientViews;
import vannniinfoinfo.view.utility.SearchBar;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.ClickableText;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ViewLiveSetSubHeader extends JPanel {

    private final ClickableText liveSets = new ClickableText("Live Sets", 100, 50, FontFactory.newPoppinsBold(14));
    private final ClickableText myTickets = new ClickableText("My Tickets", 150, 50, FontFactory.newPoppinsBold(14));
    private final ClickableText logout = new ClickableText("Logout", 100, 50, FontFactory.newPoppinsBold(14));
    private final ClientControllerObserver clientControllerObserver;
    private LinkedList<ClickableText> clickableTexts = new LinkedList<>();
    private final SearchBar searchBar = new SearchBar(new Dimension(700, 45));
    private boolean haveSearched = false;
    public ViewLiveSetSubHeader(ClientControllerObserver clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;

        setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(ClientMainFrame.WIDTH, 50));

        add(searchBar, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(liveSets);
        buttonsPanel.add(myTickets);
        buttonsPanel.add(logout);
        add(buttonsPanel, BorderLayout.CENTER);

        clickableTexts.add(liveSets);
        clickableTexts.add(myTickets);
        setCurrentButton(liveSets);


        logout.addActionListener(e -> clientControllerObserver.logOut());
        liveSets.addActionListener(e-> clientControllerObserver.changeFrame(ClientViews.HOME));
        myTickets.addActionListener(e -> {
            clientControllerObserver.changeFrame(ClientViews.MY_TICKETS);
            removeSearchBar();
            searchBar.hideDropDown();
        });

        searchBar.getSearchButton().addActionListener(e -> {
            String searchTerm = searchBar.getSearchField().getText().toLowerCase();
            if(searchTerm.isEmpty()){
                searchBar.showDropDown();
            }else {
                clientControllerObserver.searchLiveSets(searchTerm);
                searchBar.hideDropDown();
                haveSearched = true;
            }
        });

        searchBar.getClearButton().addActionListener(e -> {
            if(!haveSearched) return;
            searchBar.getSearchField().setText("");
            searchBar.showDropDown();
            clientControllerObserver.changeFrame(ClientViews.HOME);
        });
        searchBar.getSortDropDown().addActionListener(e -> {
            String selectedItem = searchBar.getSortDropDown().getChoice();

            switch (selectedItem) {
                case "Name ^":
                    clientControllerObserver.sortByName("ASC");
                    break;
                case "Name v":
                    clientControllerObserver.sortByName("DESC");
                    break;
                case "Date ^":
                    clientControllerObserver.sortByDate("ASC");
                    break;
                case "Date v":
                    clientControllerObserver.sortByDate("DESC");
                    break;
                default:
                    break;
            }
            haveSearched = true;
        });
    }

    public void setCurrentButton(ClickableText currentButton) {

        currentButton.setEnabled(false);

        for(ClickableText clickableText: clickableTexts) {

            if(!clickableText.equals(currentButton)) {
                clickableText.setEnabled(true);
            }

        }

    }
    private void removeSearchBar() {
        remove(searchBar);
        revalidate();
        repaint();
    }

    public ClickableText getLiveSets() {
        return liveSets;
    }

    public ClickableText getMyTickets() {
        return myTickets;
    }
}
