package client.controller;

import client.view.ClientViews;
import shared.referenceClasses.LiveSet;

import java.util.LinkedList;

public interface ClientControllerObserver {

    void changeFrame(ClientViews clientViews);
    void openLiveSet(LiveSet liveSet);
    LinkedList<LiveSet> getLiveSet();

}
