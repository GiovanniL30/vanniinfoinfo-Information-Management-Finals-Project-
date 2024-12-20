package vanniinfoinfo.view.panel;

import vanniinfoinfo.controller.AdminControllerObserver;
import vanniinfoinfo.view.AdminMainFrame;
import vanniinfoinfo.view.utility.AdminPanel;
import vanniinfoinfo.view.utility.AdminSearchBar;
import shared.model.Database;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.ClickableText;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class LiveSetPanel extends JPanel {
    private AdminSearchBar adminSearchBar;
    private JPanel scrollPaneHolder = new JPanel();
    private static LinkedList<Performer> performers;
    private static AdminControllerObserver adminControllerObserver;

    public LiveSetPanel(LinkedList<LiveSet> liveSets, LinkedList<Performer> performers, AdminControllerObserver adminControllerObserver) {
        LiveSetPanel.adminControllerObserver = adminControllerObserver;
        LiveSetPanel.performers = performers;
        setBackground(Color.white);

        setLayout(new BorderLayout());

        adminSearchBar = new AdminSearchBar(AdminPanel.LIVE_SET, adminControllerObserver);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.setBackground(Color.white);

        scrollPaneHolder.setLayout(new GridLayout(0, 1, 0, 20));
        scrollPaneHolder.setBackground(Color.WHITE);
        scrollPaneHolder.setBorder(new EmptyBorder(10, 10 ,10, 10));
        add(adminSearchBar, BorderLayout.NORTH);


        populateLiveSet(liveSets, performers);
        JScrollPane scrollPane = new JScrollPane(scrollPaneHolder);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(AdminMainFrame.WIDTH - 120, 480));

        centerPanel.add(new Header());
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        adminSearchBar.getAddButton().addActionListener(e -> {
            adminControllerObserver.openAddLiveSet(null, performers);
        });

    }

    public void populateLiveSet(LinkedList<LiveSet> liveSets, LinkedList<Performer> performers) {


        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {


                for(LiveSet liveSet : liveSets.stream().sorted(Comparator.comparing(LiveSet::getStatus).reversed()).collect(Collectors.toCollection(LinkedList::new))) {


                    for(Performer performer : performers) {

                        if(liveSet.getPerformerID().equals(performer.getPerformerID())) {
                            scrollPaneHolder.add(new LiveSetCard(liveSet, performer));
                            scrollPaneHolder.revalidate();
                            scrollPaneHolder.repaint();
                        }

                    }

                }

                return null;
            }


        }.execute();

    }

    private static class Header extends JPanel {

        public Header() {
            setBackground(Color.white);
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setPreferredSize(new Dimension(AdminMainFrame.WIDTH, 40));

            ClickableText liveSetID = new ClickableText("Live Set ID",250, 50, FontFactory.newPoppinsDefault(14));
            ClickableText performerName = new ClickableText("Performer Name", 265, 50, FontFactory.newPoppinsDefault(14));
            ClickableText date = new ClickableText("Date",150, 50, FontFactory.newPoppinsDefault(14));
            ClickableText time = new ClickableText("Time",150, 50, FontFactory.newPoppinsDefault(14));
            ClickableText status = new ClickableText("Status", 100, 50, FontFactory.newPoppinsDefault(14));
            ClickableText price = new ClickableText("Price", 100, 50, FontFactory.newPoppinsDefault(14));

            liveSetID.setEnabled(false);
            performerName.setEnabled(false);
            date.setEnabled(false);
            time.setEnabled(false);
            status.setEnabled(false);
            price.setEnabled(false);

            add(liveSetID);
            add(performerName);
            add(date);
            add(time);
            add(status);
            add(price);
        }

    }


    private static class LiveSetCard extends JPanel{

        public LiveSetCard(LiveSet liveSet, Performer performer) {

            setBackground(Color.white);

            setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setPreferredSize(new Dimension(AdminMainFrame.WIDTH - 180, 120));


            JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            firstRow.setPreferredSize(new Dimension(AdminMainFrame.WIDTH, 60));
            Button liveSetID = new Button(liveSet.getLiveSetID(), new Dimension(250, 50), FontFactory.newPoppinsDefault(14));
            Button performerName = new Button(performer.getPerformerName(), new Dimension(265, 50), FontFactory.newPoppinsDefault(14));
            Button date = new Button(liveSet.getDate().toString(), new Dimension(150, 50), FontFactory.newPoppinsDefault(14));
            Button time = new Button(UtilityMethods.formatTime(liveSet.getTime()), new Dimension(150, 50), FontFactory.newPoppinsDefault(14));
            Button status = new Button(liveSet.getStatus(), new Dimension(100, 50), FontFactory.newPoppinsDefault(14));
            Button price = new Button(liveSet.getPrice()+"", new Dimension(95, 50), FontFactory.newPoppinsDefault(14));
            Button editButton = new Button("EDIT", new Dimension(95, 50), FontFactory.newPoppinsDefault(14));
            Button view = new Button("Purchases", new Dimension(150, 50), FontFactory.newPoppinsDefault(14));
            Button viewers = new Button("Viewers", new Dimension(150, 50), FontFactory.newPoppinsDefault(14));

            if(!liveSet.getStatus().equals("Open")) {
                firstRow.setBackground(ColorFactory.red());
            }

            liveSetID.setEnabled(false);
            performerName.setEnabled(false);
            date.setEnabled(false);
            time.setEnabled(false);
            status.setEnabled(false);
            price.setEnabled(false);

            firstRow.add(liveSetID);
            firstRow.add(performerName);
            firstRow.add(date);
            firstRow.add(time);
            firstRow.add(status);
            firstRow.add(price);

            JPanel lastRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            lastRow.setBackground(Color.WHITE);
            lastRow.add(view);
            lastRow.add(viewers);

            if(liveSet.getStatus().equals("Open")) {
                lastRow.add(editButton);
            }



            add(firstRow);
            add(lastRow);

            view.addActionListener( e -> adminControllerObserver.showLiveSetBuyer(liveSet));
            editButton.addActionListener(e -> adminControllerObserver.openEditLiveSet(liveSet, performers));
            viewers.addActionListener( e -> adminControllerObserver.showViewers(liveSet.getLiveSetID()));
        }

    }

    public void searchLiveSetsAdmin(String searchTerm) {
        LinkedList<LiveSet> searchResults = Database.searchLiveSetsAdmin(searchTerm).getPayload();
        clearLiveSet();
        populateLiveSet(searchResults, performers);
    }

    private void clearLiveSet() {
        scrollPaneHolder.removeAll();
        scrollPaneHolder.revalidate();
        scrollPaneHolder.repaint();
    }
}
