package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.utility.AdminPanel;
import admin.view.utility.AdminSearchBar;

import javax.swing.*;
import java.awt.*;

public class PerformerPanel extends JPanel {

    private AdminSearchBar adminSearchBar;

    public PerformerPanel(AdminControllerObserver adminControllerObserver) {
        setBackground(Color.white);

        setLayout(new BorderLayout());

        adminSearchBar = new AdminSearchBar(AdminPanel.PERFORMER, adminControllerObserver);



        add(adminSearchBar, BorderLayout.NORTH);
    }



}
