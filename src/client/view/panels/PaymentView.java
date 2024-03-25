package client.view.panels;

import client.controller.ClientControllerObserver;
import client.view.ClientViews;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.FilledButton;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class PaymentView extends JPanel {

    private LiveSet liveSet;
    private Performer performer;
    private ClientControllerObserver clientControllerObserver;

    public PaymentView(LiveSet liveSet, Performer performer, ClientControllerObserver clientControllerObserver) {
        this.liveSet = liveSet;
        this.performer = performer;
        this.clientControllerObserver = clientControllerObserver;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 20));
        add(header(), BorderLayout.NORTH);
        add(liveSetPanel(), BorderLayout.CENTER);
        add(southPanel(), BorderLayout.SOUTH);
    }

    private JPanel header() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(Color.WHITE);
        JLabel label = new JLabel("PAYMENT");
        label.setFont(FontFactory.newPoppinsDefault(15));
        header.add(label);
        return header;
    }

    private JPanel liveSetPanel() {
        JPanel liveSetPanel = new JPanel();

        liveSetPanel.setSize(new Dimension(600, 600));
        liveSetPanel.setBackground(Color.WHITE);

        Picture picture = new Picture(liveSet.getThumbnail(), 600, 300);
        picture.setBackground(Color.black);


        liveSetPanel.add(picture);
        return liveSetPanel;

    }

    private JPanel southPanel() {
        boolean haveLoyalty = clientControllerObserver.getLoggedInAccount().isHaveEarnedLoyalty();

        JPanel southPanel = new JPanel(new GridLayout(2, 1, 0, 30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);
        Button cancel = new Button("CANCEL", new Dimension(300, 50), FontFactory.newPoppinsBold(13));
        cancel.addActionListener( e -> clientControllerObserver.changeFrame(ClientViews.HOME));
        FilledButton confirm = new FilledButton("CONFIRM", new Dimension(300, 50), FontFactory.newPoppinsBold(13), ColorFactory.red(), Color.WHITE);
        buttonPanel.add(cancel);
        buttonPanel.add(confirm);

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        textPanel.setBackground(Color.white);
        JLabel loyaltyText = new JLabel((haveLoyalty ? "Have loyalty" :  "No Loyalty Card") );
        loyaltyText.setFont(FontFactory.newPoppinsDefault(15));
        int price = (haveLoyalty) ? (int) ((liveSet.getPrice()) - (liveSet.getPrice() * .20)) : liveSet.getPrice();
        JLabel totalText = new JLabel("Total" + " PHP" + price );
        totalText.setFont(FontFactory.newPoppinsBold(18));
        textPanel.add(loyaltyText);
        textPanel.add(totalText);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.white);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel ticketPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ticketPanel.setBackground(Color.WHITE);
        JLabel ticket = new JLabel("Ticket Price: PHP"+liveSet.getPrice());
        ticket.setFont(FontFactory.newPoppinsDefault(15));
        ticketPanel.add(ticket);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        datePanel.setBackground(Color.WHITE);
        JLabel date = new JLabel(UtilityMethods.formatDate(liveSet.getDate()) + " " + UtilityMethods.formatTime(liveSet.getTime()));
        date.setFont(FontFactory.newPoppinsDefault(15));
        datePanel.add(date);

        JPanel pPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pPanel.setBackground(Color.white);
        JLabel performerName = new JLabel(performer.getPerformerName());
        performerName.setForeground(ColorFactory.red());
        performerName.setFont(FontFactory.newPoppinsBold(25));
        pPanel.add(performerName);

        infoPanel.add(pPanel);
        infoPanel.add(ticketPanel);
        infoPanel.add(datePanel);
        infoPanel.add(textPanel);


        southPanel.add(infoPanel);
        southPanel.add(buttonPanel);

        southPanel.setBackground(Color.white);
        return southPanel;
    }

}
