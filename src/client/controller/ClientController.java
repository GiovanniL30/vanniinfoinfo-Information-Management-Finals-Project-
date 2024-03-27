package client.controller;

import client.view.ClientMainFrame;
import client.view.utility.ClientViews;
import client.view.components.AccessGigDialog;
import client.view.components.TicketsPanel;
import client.view.panels.HomeView;
import shared.referenceClasses.Purchased;
import shared.viewComponents.LoginView;
import client.view.panels.PaymentView;
import client.view.panels.SignUpView;
import shared.model.Database;
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


    private ClientMainFrame clientMainFrame;

    public ClientController() {

    }

    @Override
    public void changeFrame(ClientViews clientViews) {


        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                switch (clientViews) {
                    case SIGN_UP ->  {
                        clientMainFrame.getContentPane().remove(1);
                        clientMainFrame.setSignUpView(new SignUpView(ClientController.this));
                        clientMainFrame.getContentPane().add(clientMainFrame.getSignUpView(), 1);
                    }
                    case LOGIN -> {
                        clientMainFrame.getContentPane().remove(1);
                        clientMainFrame.setLoginView(new LoginView(ClientController.this, false));
                        clientMainFrame.getContentPane().add(clientMainFrame.getLoginView(), 1);
                    }
                    case HOME, LIVE_SETS -> {
                        clientMainFrame.getContentPane().remove(1);
                        clientMainFrame.setHomeView(new HomeView(ClientController.this));
                        clientMainFrame.getContentPane().add(clientMainFrame.getHomeView(), 1);
                    }
                    case MY_TICKETS -> {

                        clientMainFrame.getHomeView().remove(1);

                        new SwingWorker<LinkedList<Purchased>, Void>() {
                            @Override
                            protected LinkedList<Purchased> doInBackground() {
                                return Database.getMyPurchases(loggedInAccount.getUserID());
                            }

                            @Override
                            protected void done() {
                                try {
                                    clientMainFrame.getHomeView().add(new TicketsPanel(loggedInAccount, get(), ClientController.this), 1);
                                    clientMainFrame.getHomeView().getSubHeader().setCurrentButton(clientMainFrame.getHomeView().getSubHeader().getMyTickets());
                                    clientMainFrame.getHomeView().revalidate();
                                    clientMainFrame.getHomeView().repaint();
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
                clientMainFrame.getContentPane().revalidate();
                clientMainFrame.getContentPane().repaint();
                loading.setVisible(false);
            }
        }.execute();


        loading.setVisible(true);

    }

    @Override
    public void openLiveSet(LiveSet liveSet) {
       LinkedList<Performer> performers =  Database.getPerformers();
       Optional<Performer> performer = performers.stream().filter(p -> p.getPerformerID().equals(liveSet.getPerformerID())).findAny();
       performer.ifPresent(value -> clientMainFrame.getHomeView().getLiveSetPane().openLiveSet(liveSet, value));
    }

    @Override
    public void openPaymentView(LiveSet liveSet, Performer performer) {
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                clientMainFrame.getContentPane().remove(1);
                clientMainFrame.getContentPane().add(new PaymentView(liveSet, performer, ClientController.this), 1);
                return null;
            }

            @Override
            protected void done() {
                clientMainFrame.getContentPane().revalidate();
                clientMainFrame.getContentPane().repaint();
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
                            JOptionPane.showMessageDialog(clientMainFrame, "Invalid Credentials");
                            return;
                        }

                        loggedInAccount = user.get();
                        changeFrame(ClientViews.HOME);
                        clientMainFrame.getHeader().setUserName(loggedInAccount.getFirstName() + " " + loggedInAccount.getLastName());
                        JOptionPane.showMessageDialog(clientMainFrame, "Log in Success");
                    }else {
                        JOptionPane.showMessageDialog(clientMainFrame, "Invalid Credentials");
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
            JOptionPane.showMessageDialog(clientMainFrame, "You have already purchased a ticket for this liveset");
            return;
        }

        if(Database.addPurchase(liveSetID, loggedInAccount.getUserID())) {
            JOptionPane.showMessageDialog(clientMainFrame, "You have successfully bought a ticket for this liveset");
            changeFrame(ClientViews.HOME);
        }

    }

    @Override
    public void accessLiveSet(LiveSet liveSet, String ticketId) {

    }

    @Override
    public void openAccess(LiveSet liveSet) {
        AccessGigDialog accessGigDialog = new AccessGigDialog(clientMainFrame, liveSet, this);
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

    public void setClientMainView(ClientMainFrame clientMainFrame) {
        this.clientMainFrame = clientMainFrame;
        loading = new Loading(clientMainFrame);
    }
}
