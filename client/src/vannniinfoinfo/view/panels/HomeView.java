package vannniinfoinfo.view.panels;

import vannniinfoinfo.controller.ClientControllerObserver;
import vannniinfoinfo.view.ClientMainFrame;
import vannniinfoinfo.view.components.LiveSetPane;
import vannniinfoinfo.view.components.ViewLiveSetSubHeader;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JPanel {

    private final ViewLiveSetSubHeader subHeader;
    private final ClientControllerObserver clientControllerObserver;
    private LiveSetPane liveSetPane;

    public HomeView(ClientControllerObserver clientControllerObserver) {

        this.clientControllerObserver = clientControllerObserver;
        subHeader = new ViewLiveSetSubHeader(this.clientControllerObserver);

        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(ClientMainFrame.WIDTH, ClientMainFrame.HEIGHT));

        add(subHeader, BorderLayout.NORTH);

        new Thread(() -> {
            liveSetPane = new LiveSetPane(clientControllerObserver.getLiveSet(), clientControllerObserver);
            add(liveSetPane);
            revalidate();
            repaint();
        }).start();
    }

    public LiveSetPane getLiveSetPane() {
        return liveSetPane;
    }

    public ViewLiveSetSubHeader getSubHeader() {
        return subHeader;
    }
}
