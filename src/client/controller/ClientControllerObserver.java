package client.controller;

import client.view.ClientViews;
import shared.referenceClasses.LiveSet;

import java.util.LinkedList;

public interface ClientControllerObserver {

    void changeFrame(ClientViews clientViews);
    LinkedList<LiveSet> getLiveSet();

}
