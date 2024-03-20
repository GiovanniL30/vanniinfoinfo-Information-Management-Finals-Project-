package client.controller;

import client.view.ClientMainView;
import client.view.ClientViews;
import client.view.panels.LoginView;
import client.view.panels.SignUpPanel;

public class ClientController implements ClientControllerObserver{



    private ClientMainView clientMainView;

    public ClientController() {

    }

    @Override
    public void changeFrame(ClientViews clientViews) {

        switch (clientViews) {
            case SIGN_UP ->  {
                clientMainView.getContentPane().remove(1);
                clientMainView.getContentPane().add(new SignUpPanel(this), 1);
            }
            case LOGIN -> {
                clientMainView.getContentPane().remove(1);
                clientMainView.getContentPane().add(new LoginView(this), 1);
            }
        }

        clientMainView.getContentPane().revalidate();
        clientMainView.getContentPane().repaint();

    }

    public void setClientMainView(ClientMainView clientMainView) {
        this.clientMainView = clientMainView;
    }
}
