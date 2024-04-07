package client.controller;

import client.view.ClientMainFrame;
import client.view.utility.ClientViews;
import client.view.components.AccessGigDialog;
import client.view.components.TicketsPanel;
import client.view.panels.HomeView;
import shared.controller.LoginController;
import shared.model.Response;
import shared.referenceClasses.*;
import shared.viewComponents.LoginView;
import client.view.panels.PaymentView;
import client.view.panels.SignUpView;
import shared.model.Database;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ClientController implements ClientControllerObserver, LoginController {


    private Loading loading;
    private User loggedInAccount = new User("asc", "asc", "asc", "acs", "asc", "cas", 1, "asc", Optional.empty(), "asc");

    private  AccessGigDialog accessGigDialog;

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
                        LoginView loginView = new LoginView(ClientController.this, false);
                        clientMainFrame.setLoginView(loginView);
                        loginView.getSignUpButton().addActionListener(e -> changeFrame(ClientViews.SIGN_UP));
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
                                return Database.getMyPurchases(loggedInAccount.getUserID()).getPayload();
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
       LinkedList<Performer> performers = Database.getPerformers().getPayload();
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
                return Database.logIn(userName, password).getPayload();
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

        if(!liveSet.getStatus().equals("Open")){
            JOptionPane.showMessageDialog(clientMainFrame, "Sorry the live set have been canceled");
            return;
        }

        Response<User> userResponse = Database.getTicketUser(ticketId, loggedInAccount.getUserID());

        if(userResponse.isSuccess() && userResponse.getPayload().getUserID().equals(loggedInAccount.getUserID())) {

            Optional<Ticket> temp =  Database.getTickets().getPayload().stream().filter(ticket -> ticket.getTicketID().equals(ticketId) && ticket.getLiveSetID().equals(liveSet.getLiveSetID())).findFirst();
            if(temp.isEmpty()) {
                JOptionPane.showMessageDialog(clientMainFrame, "The ticket entered was not for this live set");
                return;
            }

            accessGigDialog.dispose();
            try {
                Desktop.getDesktop().browse(new URI(liveSet.getStreamLinkURL()));
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(clientMainFrame, "The link on this live set is broken");
            }
            return;
        }



       Response<String> response =  Database.accessLiveSet(ticketId, loggedInAccount.getUserID(),liveSet.getLiveSetID());

       if(response.isSuccess()){


           accessGigDialog.dispose();
           try {
               Desktop.getDesktop().browse(new URI(liveSet.getStreamLinkURL()));
           } catch (IOException | URISyntaxException e) {
              JOptionPane.showMessageDialog(clientMainFrame, "The link on this live set is broken");
           }
       }else {
           JOptionPane.showMessageDialog(clientMainFrame, response.getPayload());
       }

    }

    @Override
    public void openAccess(LiveSet liveSet) {
        accessGigDialog = new AccessGigDialog(clientMainFrame, liveSet, this);
        accessGigDialog.setVisible(true);
    }

    @Override
    public void signUp(User user) {
        Response<String> response = Database.signUp(user);

        if(response.isSuccess()) {
            changeFrame(ClientViews.LOGIN);
        }

        JOptionPane.showMessageDialog(clientMainFrame, response.getPayload());
    }

    @Override
    public User getLoggedInAccount() {
        return loggedInAccount;
    }

    @Override
    public LinkedList<LiveSet> getLiveSet() {
        Database.updateCompletedLiveSets();
        return Database.getLiveSets().getPayload();
    }

    public void setClientMainView(ClientMainFrame clientMainFrame) {
        this.clientMainFrame = clientMainFrame;
        loading = new Loading(clientMainFrame);
    }

    @Override
    public void searchLiveSets(String searchTerm) {
        LinkedList<LiveSet> searchResults = Database.searchLiveSets(searchTerm).getPayload();
        clientMainFrame.getHomeView().getLiveSetPane().populateView(searchResults);
    }
    @Override
    public void sortByName(String condition) {
        LinkedList<LiveSet> sortResults = Database.sortByName(condition).getPayload();
        clientMainFrame.getHomeView().getLiveSetPane().populateView(sortResults);
    }
    @Override
    public void sortByDate(String condition) {
        LinkedList<LiveSet> sortResults = Database.sortByDate(condition).getPayload();
        clientMainFrame.getHomeView().getLiveSetPane().populateView(sortResults);
    }

    @Override
    public void logOut() {
        loggedInAccount = null;
        changeFrame(ClientViews.LOGIN);
        JOptionPane.showMessageDialog(clientMainFrame, "Logged Out");
    }

    @Override
    public String getPerformerName(String liveSetId) {
        return Database.getPerformerName(liveSetId);
    }
}
