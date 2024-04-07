package client.view.components;

import client.controller.ClientControllerObserver;
import client.view.ClientMainFrame;
import shared.referenceClasses.Purchased;
import shared.referenceClasses.User;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.Picture;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedList;

public class TicketsPanel extends JPanel {

    private final JPanel holder = new JPanel();

    private ClientControllerObserver clientControllerObserver;
    private static User user;

    public TicketsPanel(User user, LinkedList<Purchased> purchasedLinkedList, ClientControllerObserver clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;
        TicketsPanel.user = user;

        holder.setLayout(new GridLayout(0,1, 0, 20));
        holder.setBorder(new EmptyBorder(20, 0, 20, 0));
        populateView(purchasedLinkedList);
        JScrollPane scrollPane = new JScrollPane(holder);
        scrollPane.setPreferredSize(new Dimension(ClientMainFrame.WIDTH - 100, 600));
        add(scrollPane);
    }

    private void populateView(LinkedList<Purchased> purchased) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                for(Purchased p : purchased) {
                    holder.add(new PurchasedCard(p));
                    holder.revalidate();
                    holder.repaint();
                }
                return null;

            }
        }.execute();

    }

    private static class PurchasedCard extends JPanel {

        public PurchasedCard(Purchased purchased) {
            boolean notOpen = !purchased.getLiveSetStatus().equals("Open");

            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(new EmptyBorder(0, 30, 0, 0));

            Picture picture = new Picture(purchased.getLiveSetThumbnail(), 300, 200);

            JPanel purchaseInformationPanel = new JPanel();
            purchaseInformationPanel.setLayout(new BoxLayout(purchaseInformationPanel, BoxLayout.Y_AXIS));
            purchaseInformationPanel.setPreferredSize(new Dimension(450, 100));
            JLabel name = new JLabel(purchased.getPerformerName());
            name.setForeground(ColorFactory.red());
            name.setFont(FontFactory.newPoppinsBold(20));
            name.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            JLabel purchasedDate = new JLabel("Purchased Date: " + UtilityMethods.formatDate(purchased.getDate()) + " " + UtilityMethods.formatTime(purchased.getTime()));
            purchasedDate.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            purchasedDate.setFont(FontFactory.newPoppinsDefault(15));
             JLabel totalPrice = new JLabel("Total: " + (user.getLoyaltyCard().isPresent() ? UtilityMethods.computeDiscount(purchased.getLiveSetPrice()) : purchased.getLiveSetPrice()));
            totalPrice.setFont(FontFactory.newPoppinsDefault(15));
             totalPrice.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            purchaseInformationPanel.add(name);
            purchaseInformationPanel.add(purchasedDate);
            purchaseInformationPanel.add(totalPrice);


            JPanel ticketInformationPanel = new JPanel();
            ticketInformationPanel.setLayout(new BoxLayout(ticketInformationPanel, BoxLayout.Y_AXIS));
            ticketInformationPanel.setSize(new Dimension(200, 100));
            JLabel ticketNumber = new JLabel("Ticket Number: " + purchased.getTicketId());
            ticketNumber.setFont(FontFactory.newPoppinsBold(15));
            ticketNumber.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            JLabel canceled = new JLabel(("Live Set " + purchased.getLiveSetStatus()).toUpperCase());
            canceled.setFont(FontFactory.newPoppinsBold(18));
            canceled.setForeground(ColorFactory.red());
            canceled.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            JLabel status = new JLabel(purchased.getTicketStatus() == null ? "NOT USED" : "USED BY: " + purchased.getUserName());
            status.setHorizontalAlignment(BoxLayout.LINE_AXIS);
            status.setFont(FontFactory.newPoppinsBold(15));

            if(notOpen) ticketInformationPanel.add(canceled);
            ticketInformationPanel.add(status);
           ticketInformationPanel.add(ticketNumber);



            Button copyID = new Button("Copy Ticket Number", new Dimension(100, 50), FontFactory.newPoppinsDefault(13));
            if(!notOpen)ticketInformationPanel.add(copyID);

            copyID.addActionListener(e -> {
                StringSelection selection = new StringSelection(purchased.getTicketId());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                JOptionPane.showMessageDialog(null, "Copied to clipboard");
            });

            JPanel rightSide = new JPanel(new BorderLayout());
            rightSide.setBorder(new EmptyBorder(25, 0, 0 ,0));
            rightSide.setPreferredSize(new Dimension(450, 250));
            rightSide.add(purchaseInformationPanel, BorderLayout.NORTH);
            rightSide.add(ticketInformationPanel, BorderLayout.CENTER);

            add(picture);
            add(rightSide);
        }


    }
}
