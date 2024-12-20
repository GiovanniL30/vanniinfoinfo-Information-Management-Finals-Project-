package vannniinfoinfo;

import vannniinfoinfo.controller.ClientController;
import vannniinfoinfo.view.ClientMainFrame;

import javax.swing.*;

public class ClientApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater( () -> {
            ClientController clientController = new ClientController();
            ClientMainFrame clientMainFrame = new ClientMainFrame(clientController);
            clientController.setClientMainView(clientMainFrame);
        });

    }

}
