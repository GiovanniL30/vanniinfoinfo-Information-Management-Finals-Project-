package client.controller;

import client.view.ClientMainView;
import client.view.ClientViews;
import client.view.panels.HomeView;
import client.view.panels.LoginView;
import client.view.panels.SignUpView;
import shared.Database;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Optional;

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
                        clientMainView.setSignUpView(new SignUpView(ClientController.this));
                        clientMainView.getContentPane().add(clientMainView.getSignUpView(), 1);
                    }
                    case LOGIN -> {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.setLoginView(new LoginView(ClientController.this));
                        clientMainView.getContentPane().add(clientMainView.getLoginView(), 1);
                    }
                    case HOME -> {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.setHomeView(new HomeView(ClientController.this));
                        clientMainView.getContentPane().add(clientMainView.getHomeView(), 1);
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
    public void openLiveSet(LiveSet liveSet) {
       LinkedList<Performer> performers =  Database.getPerformers();
       Optional<Performer> performer = performers.stream().filter(p -> p.getPerformerID().equals(liveSet.getPerformerID())).findAny();
       performer.ifPresent(value -> clientMainView.getHomeView().getLiveSetPane().openLiveSet(liveSet, value));
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
