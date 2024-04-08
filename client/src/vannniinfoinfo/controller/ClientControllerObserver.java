package vannniinfoinfo.controller;

import vannniinfoinfo.view.utility.ClientViews;
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
    void signUp(User user);
    User getLoggedInAccount();
    LinkedList<LiveSet> getLiveSet();
    void searchLiveSets(String searchTerm);
    void sortByName(String condition);
    void sortByDate(String condition);
    void logOut();
    String getPerformerName(String liveSetId);
}
