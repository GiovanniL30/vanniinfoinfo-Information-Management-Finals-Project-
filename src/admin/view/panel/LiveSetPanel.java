package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.utility.AdminPanel;
import admin.view.utility.AdminSearchBar;

import javax.swing.*;
import java.awt.*;

public class LiveSetPanel extends JPanel {
    private AdminSearchBar adminSearchBar;
    public LiveSetPanel(AdminControllerObserver adminControllerObserver) {
        setBackground(Color.white);

        setLayout(new BorderLayout());

        adminSearchBar = new AdminSearchBar(AdminPanel.LIVE_SET, adminControllerObserver);



        add(adminSearchBar, BorderLayout.NORTH);
    }
}
