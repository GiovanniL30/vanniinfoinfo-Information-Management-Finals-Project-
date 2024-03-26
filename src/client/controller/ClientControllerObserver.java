package client.controller;

import client.view.ClientViews;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.referenceClasses.User;

import java.util.LinkedList;

public interface ClientControllerObserver {

    void changeFrame(ClientViews clientViews);
    void openLiveSet(LiveSet liveSet);
    void openPaymentView(LiveSet liveSet, Performer performer);
    void logIn(String userName, String password);
    void purchaseTicket(String liveSetID);
    User getLoggedInAccount();
    LinkedList<LiveSet> getLiveSet();

}
