package admin.controller;

import admin.view.utility.AdminPanel;
import shared.referenceClasses.Genre;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;

import java.util.LinkedList;

public interface AdminControllerObserver {

    void changeFrame(AdminPanel adminPanel);
    void editPerformerFrame(Performer performer);
    void updatePerformer(Performer performer);
    void addPerformer(Performer performer);
    void addLiveSet(LiveSet liveSet);
    void editLiveSet(LiveSet liveSet);
    LinkedList<Genre> getGenres();
}
