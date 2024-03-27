package admin;

import admin.controller.AdminController;
import admin.view.AdminMainFrame;
import client.controller.ClientController;
import client.view.ClientMainFrame;

import javax.swing.*;

public class AdminApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater( () -> {
            AdminController adminController = new AdminController();
            AdminMainFrame adminMainFrame = new AdminMainFrame(adminController);
            adminController.setAdminMainFrame(adminMainFrame);
        });

    }
}
