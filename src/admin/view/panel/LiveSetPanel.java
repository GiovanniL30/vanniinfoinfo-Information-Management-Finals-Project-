package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.AdminMainFrame;
import admin.view.components.LiveSetDialog;
import admin.view.utility.AdminPanel;
import admin.view.utility.AdminSearchBar;
import admin.view.utility.LiveSetDialogType;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.ClickableText;
import shared.viewComponents.FilledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class LiveSetPanel extends JPanel {
    private AdminSearchBar adminSearchBar;
    private JPanel scrollPaneHolder = new JPanel();
    AdminMainFrame adminMainFrame;
    public LiveSetPanel(LinkedList<LiveSet> liveSets, LinkedList<Performer> performers, AdminControllerObserver adminControllerObserver) {
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
            LiveSet newLiveSet = new LiveSet("", "", 0, liveSets.element().getDate(), liveSets.element().getTime(), "", "", "");
            LiveSetDialog addLivesetDialog = new LiveSetDialog(adminMainFrame, newLiveSet, performers, adminControllerObserver, LiveSetDialogType.ADD);
            addLivesetDialog.setVisible(true);
        });

    }

    public void populateLiveSet(LinkedList<LiveSet> liveSets, LinkedList<Performer> performers) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {


                for(LiveSet liveSet : liveSets) {

                    for(Performer performer : performers) {

                        if(liveSet.getPerformerID().equals(performer.getPerformerID())) {
                            scrollPaneHolder.add(new LiveSetCard(liveSet, performer));
                            scrollPaneHolder.add(new LiveSetCard(liveSet, performer));  scrollPaneHolder.add(new LiveSetCard(liveSet, performer));
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

            liveSetID.setEnabled(false);
            performerName.setEnabled(false);
            date.setEnabled(false);
            time.setEnabled(false);
            status.setEnabled(false);

            add(liveSetID);
            add(performerName);
            add(date);
            add(time);
            add(status);
        }

    }


    private static class LiveSetCard extends JPanel{
        AdminMainFrame adminMainFrame;
        LinkedList<Performer> performers;
        AdminControllerObserver adminControllerObserver;

        public LiveSetCard(LiveSet liveSet, Performer performer) {

            setBackground(Color.white);
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setPreferredSize(new Dimension(AdminMainFrame.WIDTH, 60));

            Button liveSetID = new Button(liveSet.getLiveSetID(), new Dimension(250, 50), FontFactory.newPoppinsDefault(14));
            Button performerName = new Button(performer.getPerformerName(), new Dimension(265, 50), FontFactory.newPoppinsDefault(14));
            Button date = new Button(liveSet.getDate().toString(), new Dimension(150, 50), FontFactory.newPoppinsDefault(14));
            Button time = new Button(UtilityMethods.formatTime(liveSet.getTime()), new Dimension(150, 50), FontFactory.newPoppinsDefault(14));
            Button status = new Button(liveSet.getStatus(), new Dimension(100, 50), FontFactory.newPoppinsDefault(14));
            FilledButton editButton = new FilledButton("EDIT", new Dimension(95, 50), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.white);

            liveSetID.setEnabled(false);
            performerName.setEnabled(false);
            date.setEnabled(false);
            time.setEnabled(false);
            status.setEnabled(false);

            add(liveSetID);
            add(performerName);
            add(date);
            add(time);
            add(status);
            add(editButton);

            editButton.addActionListener(e -> {
                LiveSetDialog editLivesetDialog = new LiveSetDialog(adminMainFrame, liveSet, performers, adminControllerObserver, LiveSetDialogType.EDIT);
                editLivesetDialog.setVisible(true);
            });
        }

    }
}
