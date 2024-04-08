package vanniinfoinfo;

import vanniinfoinfo.controller.AdminController;
import vanniinfoinfo.view.AdminMainFrame;

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
