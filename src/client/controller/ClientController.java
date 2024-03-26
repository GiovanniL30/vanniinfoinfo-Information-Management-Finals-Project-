package client.controller;

import client.view.ClientMainView;
import client.view.ClientViews;
import client.view.components.AccessGigDialog;
import client.view.components.TicketsPanel;
import client.view.panels.HomeView;
import shared.referenceClasses.Purchased;
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
import java.util.concurrent.ExecutionException;

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

                        new SwingWorker<LinkedList<Purchased>, Void>() {
                            @Override
                            protected LinkedList<Purchased> doInBackground() {
                                return Database.getMyPurchases(loggedInAccount.getUserID());
                            }

                            @Override
                            protected void done() {
                                try {
                                    clientMainView.getHomeView().add(new TicketsPanel(loggedInAccount, get(), ClientController.this), 1);
                                    clientMainView.getHomeView().getSubHeader().setCurrentButton(clientMainView.getHomeView().getSubHeader().getMyTickets());
                                    clientMainView.getHomeView().revalidate();
                                    clientMainView.getHomeView().repaint();
                                } catch (InterruptedException | ExecutionException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }.execute();

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

        new SwingWorker<Optional<User>, Void>() {
            @Override
            protected Optional<User> doInBackground() {
                return Database.logIn(userName, password);
            }

            @Override
            protected void done() {
                loading.setVisible(false);
                try {
                    Optional<User> user = get();
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
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute();

        loading.setVisible(true);

    }

    @Override
    public void purchaseTicket(String liveSetID) {

        if(Database.havePurchased(loggedInAccount.getUserID(),  liveSetID)) {
            JOptionPane.showMessageDialog(clientMainView, "You have already purchased a ticket for this liveset");
            return;
        }

        if(Database.addPurchase(liveSetID, loggedInAccount.getUserID())) {
            JOptionPane.showMessageDialog(clientMainView, "You have successfully bought a ticket for this liveset");
            changeFrame(ClientViews.HOME);
        }

    }

    @Override
    public void accessLiveSet(LiveSet liveSet, String ticketId) {

    }

    @Override
    public void openAccess(LiveSet liveSet) {
        AccessGigDialog accessGigDialog = new AccessGigDialog(clientMainView, liveSet, this);
        accessGigDialog.setVisible(true);
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
