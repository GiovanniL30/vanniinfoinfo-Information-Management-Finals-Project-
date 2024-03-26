package client.controller;

import client.view.ClientMainView;
import client.view.ClientViews;
import client.view.components.TicketsPanel;
import client.view.panels.HomeView;
import shared.viewComponents.LoginView;
import client.view.panels.PaymentView;
import client.view.panels.SignUpView;
import shared.Database;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.referenceClasses.User;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Optional;

public class ClientController implements ClientControllerObserver{


    private Loading loading;
    private User loggedInAccount = new User("asc", "asc", "asc", "acs", "asc", "cas", 1, "asc", true, "asc");


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
                        clientMainView.setLoginView(new LoginView(ClientController.this, false));
                        clientMainView.getContentPane().add(clientMainView.getLoginView(), 1);
                    }
                    case HOME, LIVE_SETS -> {
                        clientMainView.getContentPane().remove(1);
                        clientMainView.setHomeView(new HomeView(ClientController.this));
                        clientMainView.getContentPane().add(clientMainView.getHomeView(), 1);
                    }
                    case MY_TICKETS -> {
                        clientMainView.getHomeView().remove(1);
                        clientMainView.getHomeView().add(new TicketsPanel(), 1);
                        clientMainView.getHomeView().getSubHeader().setCurrentButton(clientMainView.getHomeView().getSubHeader().getMyTickets());
                        clientMainView.getHomeView().revalidate();
                        clientMainView.getHomeView().repaint();
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
    public void openPaymentView(LiveSet liveSet, Performer performer) {
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                clientMainView.getContentPane().remove(1);
                clientMainView.getContentPane().add(new PaymentView(liveSet, performer, ClientController.this), 1);
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
    public void logIn(String userName, String password) {

        Optional<User> user = Database.logIn(userName, password);

        if(user.isPresent()) {

            if(user.get().getUserType().equals("Admin")) {
                JOptionPane.showMessageDialog(clientMainView, "Invalid Credentials");
                return;
            }

            loggedInAccount = user.get();
            changeFrame(ClientViews.HOME);
            clientMainView.getHeader().setUserName(loggedInAccount.getFirstName() + " " + loggedInAccount.getLastName());
            JOptionPane.showMessageDialog(clientMainView, "Log in Success");
        }else {
            JOptionPane.showMessageDialog(clientMainView, "Invalid Credentials");
        }
    }

    @Override
    public User getLoggedInAccount() {
        return loggedInAccount;
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
