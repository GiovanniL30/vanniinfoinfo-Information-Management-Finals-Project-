package client.view.components;

import client.controller.ClientControllerObserver;
import client.view.ClientViews;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.FilledButton;
import shared.viewComponents.IconButton;
import shared.viewComponents.Picture;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OpenedLiveSet extends JPanel {

    private LiveSet liveSet;
    private Performer performer;
    private ClientControllerObserver clientControllerObserver;

    public OpenedLiveSet(LiveSet liveSet, Performer performer, ClientControllerObserver clientControllerObserver) {
        this.liveSet = liveSet;
        this.performer = performer;
        this.clientControllerObserver = clientControllerObserver;

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBorder(new EmptyBorder(0, 40, 0, 0));
        headerPanel.setBackground(Color.white);
        JLabel headerTitle = new JLabel("PURCHASE TICKET FOR / ACCESS THIS GIG");
        headerTitle.setFont(FontFactory.newPoppinsDefault(13));

        IconButton backButton = new IconButton("resources/images/back.png", 40, 40);

        backButton.setBounds(0, 0, 50 ,50);
        headerPanel.add(backButton);
        headerPanel.add(headerTitle);


        JPanel descriptionPanel = performerCard();
        liveDate();

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(liveDate(), BorderLayout.NORTH);
        southPanel.add(buttonPanel(), BorderLayout.SOUTH);


        add(headerPanel, BorderLayout.NORTH);
        add(descriptionPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        backButton.addActionListener( e -> clientControllerObserver.changeFrame(ClientViews.HOME));
    }

    private JPanel buttonPanel() {

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        buttons.setBorder(new EmptyBorder(0, 0, 0,120));
        buttons.setBackground(Color.WHITE);

        Button accessGig = new Button("ACCESS GIG", new Dimension(530, 60), FontFactory.newPoppinsBold(14));
        accessGig.setForeground(ColorFactory.red());
        FilledButton purchaseTicket = new FilledButton("PURCHASE TICKET", new Dimension(530, 60), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.WHITE);

        buttons.add(accessGig);
        buttons.add(purchaseTicket);

        purchaseTicket.addActionListener( e -> clientControllerObserver.openPaymentView(liveSet, performer));
        return buttons;
    }

    private JPanel liveDate() {
        JPanel liveDatePanel = new JPanel();
        liveDatePanel.setBorder(new EmptyBorder(0, 0, 0,120));
        liveDatePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));


        String formattedDate = UtilityMethods.formatDate(liveSet.getDate());
        String formattedTime = UtilityMethods.formatTime(liveSet.getTime());

        JLabel dateLabel = new JLabel(formattedDate);
        dateLabel.setFont(FontFactory.newPoppinsDefault(15));
        JLabel timeLabel = new JLabel("Show starts at " + formattedTime);
        timeLabel.setFont(FontFactory.newPoppinsDefault(15));

        liveDatePanel.add(dateLabel);
        liveDatePanel.add(timeLabel);


        return liveDatePanel;
    }


    private JPanel performerCard(){
        Picture liveSetImage = new Picture(liveSet.getThumbnail(), 610, 300);
        liveSetImage.setBackground(Color.WHITE);

        JPanel informationPanel = new JPanel();
        GridBagConstraints informationLayout = new GridBagConstraints();
        informationLayout.gridx = 0;
        informationLayout.gridy = 0;
        informationLayout.weightx = 2.0;
        informationLayout.fill = 2;

        informationPanel.setLayout(new GridBagLayout());
        informationPanel.setBackground(Color.WHITE);


        JLabel performerName = new JLabel(performer.getPerformerName());
        performerName.setFont(FontFactory.newPoppinsBold(25));
        performerName.setForeground(ColorFactory.red());
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBackground(Color.white);
        namePanel.add(performerName);

        JLabel performerDescription = new JLabel(performer.getDescription());
        performerDescription.setFont(FontFactory.newPoppinsDefault(15));

        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20 ,20));
        descriptionPanel.setBackground(Color.white);
        descriptionPanel.add(performerDescription);
        informationPanel.add(namePanel, informationLayout);
        informationLayout.gridy = 1;
        informationPanel.add(performerDescription, informationLayout);


        JPanel rightSidePanel = new JPanel();
        rightSidePanel.setLayout(new BorderLayout(0, 100));
        rightSidePanel.setBackground(Color.white);
        rightSidePanel.add(informationPanel, BorderLayout.NORTH);

        JLabel liveSetStatus = new JLabel(liveSet.getStatus().toUpperCase());
        liveSetStatus.setForeground(ColorFactory.red());
        liveSetStatus.setFont(FontFactory.newPoppinsBold(15));
        rightSidePanel.add(liveSetStatus, BorderLayout.SOUTH);

        rightSidePanel.setPreferredSize(new Dimension(600, 300));
        descriptionPanel.add(liveSetImage);
        descriptionPanel.add(rightSidePanel);

        return descriptionPanel;
    }
}
