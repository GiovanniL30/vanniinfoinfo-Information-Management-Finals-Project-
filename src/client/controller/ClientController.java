package client.controller;

import client.view.ClientMainView;
import client.view.ClientViews;
import client.view.panels.HomeView;
import client.view.panels.LoginView;
import client.view.panels.SignUpView;
import shared.Database;
import shared.referenceClasses.LiveSet;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.util.LinkedList;

public class ClientController implements ClientControllerObserver{


    private Loading loading;


    private ClientMainView clientMainView;

    public ClientController() {

    }

    @Override
    public void changeFrame(ClientViews clientViews) {


        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                switch (clientViews) {
                    case SIGN_UP ->  {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.getContentPane().add(new SignUpView(ClientController.this), 1);
                    }
                    case LOGIN -> {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.getContentPane().add(new LoginView(ClientController.this), 1);
                    }
                    case HOME -> {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.getContentPane().add(new HomeView(ClientController.this), 1);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                clientMainView.getContentPane().revalidate();
                clientMainView.getContentPane().repaint();
                loading.setVisible(false);
            }
        }.execute();


        loading.setVisible(true);

    }

    @Override
    public LinkedList<LiveSet> getLiveSet() {
        return Database.getLiveSets();
    }

    public void setClientMainView(ClientMainView clientMainView) {
        this.clientMainView = clientMainView;
        loading = new Loading(clientMainView);
    }
}
