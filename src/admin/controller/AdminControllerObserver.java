package admin.controller;

import admin.view.utility.AdminPanel;
import shared.referenceClasses.Performer;

public interface AdminControllerObserver {

    void changeFrame(AdminPanel adminPanel);
    void editPerformerFrame(Performer performer);
    void updatePerformer(Performer performer);


}
