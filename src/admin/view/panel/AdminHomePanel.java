package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.utility.AdminPanel;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;

import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {

    public AdminHomePanel(AdminControllerObserver adminControllerObserver) {

        Button performer = new Button("Performers", new Dimension(100, 50), FontFactory.newPoppinsBold(15));
        Button liveSet = new Button("LiveSet", new Dimension(100, 50), FontFactory.newPoppinsBold(15));


        add(performer);
        add(liveSet);

        performer.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.PERFORMER));
        liveSet.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.LIVE_SET));

    }

}
