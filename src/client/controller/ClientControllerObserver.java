package client.controller;

import client.view.utility.ClientViews;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.referenceClasses.User;

import java.util.LinkedList;

public interface ClientControllerObserver {

    void changeFrame(ClientViews clientViews);
    void openLiveSet(LiveSet liveSet);
    void openPaymentView(LiveSet liveSet, Performer performer);
    void purchaseTicket(String liveSetID);
    void accessLiveSet(LiveSet liveSet, String ticketId);
    void openAccess(LiveSet liveSet);
    User getLoggedInAccount();
    LinkedList<LiveSet> getLiveSet();

}
