package client.view.panels;

import client.controller.ClientControllerObserver;
import client.view.ClientMainView;
import client.view.components.LiveSetPane;
import client.view.components.ViewLiveSetSubHeader;
import shared.Database;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {

    private ClientControllerObserver clientControllerObserver;
    private final ViewLiveSetSubHeader subHeader;
    private Loading loading = new Loading(null);
    public HomeView(ClientControllerObserver clientControllerObserver) {

        this.clientControllerObserver = clientControllerObserver;
        subHeader = new ViewLiveSetSubHeader(this.clientControllerObserver);

        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(ClientMainView.WIDTH, ClientMainView.HEIGHT));

        add(subHeader, BorderLayout.NORTH);

        new Thread(() -> {
            add(new LiveSetPane(Database.getLiveSets(), clientControllerObserver));
            revalidate();
            repaint();
        } ).start();
    }


}
