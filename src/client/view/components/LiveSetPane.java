package client.view.components;

import client.controller.ClientControllerObserver;
import client.view.ClientMainView;
import shared.referenceClasses.LiveSet;
import shared.viewComponents.IconButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedList;

public class LiveSetPane extends JPanel {

    private final JPanel holder = new JPanel();
    private final ClientControllerObserver clientControllerObserver;

    public LiveSetPane(LinkedList<LiveSet> liveSets, ClientControllerObserver clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;
        setBackground(Color.white);

        holder.setLayout(new GridLayout(0, 2, 20, 20));
        populateView(liveSets);
        holder.setBackground(Color.white);
        holder.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(holder);
        scrollPane.setPreferredSize(new Dimension(ClientMainView.WIDTH - 100, 600));
        scrollPane.setBackground(Color.white);

        add(scrollPane);
    }

    public void populateView(LinkedList<LiveSet> liveSets) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                holder.removeAll();

                for(LiveSet liveSet: liveSets) {
                    JPanel panel = new JPanel();
                    panel.add(new IconButton(liveSet.getThumbnail(), 400, 400));
                    holder.add(panel);
                    holder.revalidate();
                    holder.repaint();
                }

                return null;
            }
        }.execute();

    }


}
