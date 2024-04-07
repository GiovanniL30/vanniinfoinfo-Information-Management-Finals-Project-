package vanniinfoinfo.view.panel;

import vanniinfoinfo.controller.AdminControllerObserver;
import vanniinfoinfo.view.AdminMainFrame;
import vanniinfoinfo.view.utility.AdminPanel;
import vanniinfoinfo.view.utility.AdminSearchBar;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.FilledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class PerformerPanel extends JPanel {

    private AdminSearchBar adminSearchBar;
    private AdminControllerObserver adminControllerObserver;
    private JPanel scrollPaneHolder = new JPanel();
    private LinkedList<Performer> allPerformers;

    public PerformerPanel(LinkedList<Performer> performers, AdminControllerObserver adminControllerObserver) {
        this.adminControllerObserver = adminControllerObserver;
        setBackground(Color.white);

        setLayout(new BorderLayout(0, 30));

        scrollPaneHolder.setLayout(new GridLayout(0, 1, 0, 20));
        scrollPaneHolder.setBackground(Color.WHITE);
        scrollPaneHolder.setBorder(new EmptyBorder(10, 10 ,10, 10));
        populatePerformers(performers);

        adminSearchBar = new AdminSearchBar(AdminPanel.PERFORMER, adminControllerObserver);
        add(adminSearchBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollPaneHolder);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(AdminMainFrame.WIDTH, 550));
        add(scrollPane, BorderLayout.CENTER);

        adminSearchBar.getAddButton().addActionListener(e -> {
            Performer newPerformer = new Performer("", "", "", "", "", "Active");
            AddPerformer addPerformerPanel = new AddPerformer(newPerformer, adminControllerObserver);
            this.removeAll();
            this.add(addPerformerPanel);
            this.revalidate();
            this.repaint();
        });
    }

    public void populatePerformers(LinkedList<Performer> performers) {
        allPerformers = performers;

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                scrollPaneHolder.removeAll();
                scrollPaneHolder.revalidate();
                scrollPaneHolder.repaint();

                for(Performer performer:  performers) {
                    scrollPaneHolder.add(new PerformerList(performer, adminControllerObserver));
                    scrollPaneHolder.revalidate();
                    scrollPaneHolder.repaint();
                }

                return null;
            }
        }.execute();

    }

    private static class PerformerList extends JPanel{

        public PerformerList(Performer performer, AdminControllerObserver adminControllerObserver) {
            setBackground(ColorFactory.whitishGrey());
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(AdminMainFrame.WIDTH - 150, 60));


            JLabel performerName = new JLabel((!performer.getPerformerStatus().equals("Active") ? "NOT ACTIVE: ": "" )+ " " + performer.getPerformerName());
            performerName.setFont(FontFactory.newPoppinsDefault(14));
            if(!performer.getPerformerStatus().equals("Active")) {
                performerName.setForeground(ColorFactory.red());
            }
            FilledButton editButton = new FilledButton("EDIT", new Dimension(100, 50), FontFactory.newPoppinsBold(13), ColorFactory.red(), Color.WHITE);

            add(performerName, BorderLayout.WEST);
            add(editButton, BorderLayout.EAST);

            editButton.addActionListener( e -> adminControllerObserver.editPerformerFrame(performer));
        }
    }




}
