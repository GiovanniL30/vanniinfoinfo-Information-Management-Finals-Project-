package client.view.components;

import client.controller.ClientControllerObserver;
import client.view.ClientMainView;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.ClickableText;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ViewLiveSetSubHeader extends JPanel {

    private final ClickableText liveSets = new ClickableText("Live Sets", 100, 50, FontFactory.newPoppinsBold(14));
    private final ClickableText myTickets = new ClickableText("My Tickets", 100, 50, FontFactory.newPoppinsBold(14));
    private final ClickableText logout = new ClickableText("Logout", 100, 50, FontFactory.newPoppinsBold(14));

    private final ClientControllerObserver clientControllerObserver;
    private LinkedList<ClickableText> clickableTexts = new LinkedList<>();

    public ViewLiveSetSubHeader(ClientControllerObserver clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;

        setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(ClientMainView.WIDTH, 50));

        add(liveSets);
        add(myTickets);
        add(logout);

        clickableTexts.add(liveSets);
        clickableTexts.add(myTickets);
        setCurrentButton(liveSets);
    }

    public void setCurrentButton(ClickableText currentButton) {

        currentButton.setEnabled(false);

        for(ClickableText clickableText: clickableTexts) {

            if(!clickableText.equals(currentButton)) {
                clickableText.setEnabled(true);
            }

        }

    }

    public ClickableText getLiveSets() {
        return liveSets;
    }

    public ClickableText getMyTickets() {
        return myTickets;
    }
}
