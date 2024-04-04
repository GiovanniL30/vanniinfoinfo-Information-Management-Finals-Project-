package client.view.components;

import client.controller.ClientControllerObserver;
import client.view.ClientMainFrame;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.IconButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class LiveSetPane extends JPanel {

    private final JPanel holder = new JPanel();
    private final ClientControllerObserver clientControllerObserver;
    private final JScrollPane scrollPane;

    public LiveSetPane(LinkedList<LiveSet> liveSets, ClientControllerObserver clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;
        setBackground(Color.white);

        holder.setLayout(new GridLayout(0, 2, 20, 20));
        populateView(liveSets);
        holder.setBackground(Color.white);
        holder.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(holder);
        scrollPane.setPreferredSize(new Dimension(ClientMainFrame.WIDTH , 600));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(15);
        verticalScrollBar.setBlockIncrement(110);

        scrollPane.setBackground(Color.white);

        add(scrollPane);
    }

    public void populateView(LinkedList<LiveSet> liveSets) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                holder.removeAll();
                holder.setLayout(new GridLayout(0, 2, 20, 20));

                for (LiveSet liveSet : liveSets) {
                    if(!liveSet.getStatus().equals("Open")) continue;
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
                    panel.setBackground(Color.white);
                    panel.setPreferredSize(new Dimension(400, 450));
                    IconButton liveSetImage = new IconButton(liveSet.getThumbnail(), 500, 400);
                    liveSetImage.addActionListener(e -> clientControllerObserver.openLiveSet(liveSet));
                    JLabel performerName = new JLabel(clientControllerObserver.getPerformerName(liveSet.getLiveSetID()));
                    performerName.setFont(FontFactory.newPoppinsBold(16));

                    JLabel schedule = new JLabel(UtilityMethods.formatDate(liveSet.getDate()));
                    schedule.setFont(FontFactory.newPoppinsDefault(16));

                    panel.add(liveSetImage);
                    panel.add(performerName);
                    panel.add(schedule);

                    holder.add(panel);
                    holder.revalidate();
                    holder.repaint();
                }

                return null;
            }
        }.execute();

    }

    public void openLiveSet(LiveSet liveSet, Performer performer) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                holder.removeAll();
                holder.setLayout(new FlowLayout(FlowLayout.CENTER));
                OpenedLiveSet openedLiveSet = new OpenedLiveSet(liveSet, performer, clientControllerObserver);
                holder.add(openedLiveSet);
                holder.revalidate();
                holder.repaint();

                SwingUtilities.invokeLater(() -> {
                    JViewport viewport = scrollPane.getViewport();
                    viewport.setViewPosition(new Point(0, 0));
                });

                return null;
            }
        }.execute();

    }


}
