package client;

import client.controller.ClientController;
import client.view.ClientMainView;

import javax.swing.*;

public class ClientApplication {

    public static void main(String[] args) {

        SwingUtilities.invokeLater( () -> {
            ClientController clientController = new ClientController();
            ClientMainView clientMainView = new ClientMainView(clientController);
            clientController.setClientMainView(clientMainView);
        });

    }

}
