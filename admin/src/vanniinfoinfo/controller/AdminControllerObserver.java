package vanniinfoinfo.controller;

import vanniinfoinfo.view.utility.AdminPanel;
import shared.referenceClasses.Genre;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.referenceClasses.PerformerType;

import java.util.LinkedList;

public interface AdminControllerObserver {

    void changeFrame(AdminPanel adminPanel);
    void editPerformerFrame(Performer performer);
    void updatePerformer(Performer performer);
    void addPerformer(Performer performer);
    void addLiveSet(Performer performer, LiveSet liveSet);
    void editLiveSet(LiveSet liveSet);
    void openEditLiveSet(LiveSet liveSet, LinkedList<Performer> performers);
    void openAddLiveSet(LiveSet liveSet, LinkedList<Performer> performers);
    void showLiveSetBuyer(LiveSet liveSet);
    LinkedList<Genre> getGenres();
    LinkedList<PerformerType> getPerformerTypes();
    LinkedList<Performer> getPerformers();
    void searchPerformers(String searchTerm);
    void searchLiveSetsAdmin(String searchTerm);
    void showViewers(String livesetID);

}
