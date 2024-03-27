package admin.controller;

import admin.view.AdminMainFrame;
import admin.view.panel.AdminHomePanel;
import admin.view.panel.LiveSetPanel;
import admin.view.panel.PerformerPanel;
import admin.view.utility.AdminPanel;
import shared.viewComponents.Loading;

import javax.swing.*;

public class AdminController implements AdminControllerObserver{

    private AdminMainFrame adminMainFrame;
    private Loading loading;

    @Override
    public void changeFrame(AdminPanel adminPanel) {

        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {

                adminMainFrame.getContentPane().remove(1);

                switch (adminPanel) {
                    case HOME -> adminMainFrame.getContentPane().add(new AdminHomePanel(AdminController.this), 1);
                    case PERFORMER -> {
                        PerformerPanel performerPanel = new PerformerPanel(AdminController.this);
                        adminMainFrame.setPerformerPanel(performerPanel);
                        adminMainFrame.getContentPane().add(performerPanel, 1);
                    }case LIVE_SET -> {
                        LiveSetPanel liveSetPanel = new LiveSetPanel(AdminController.this);
                        adminMainFrame.setLiveSetPanel(liveSetPanel);
                        adminMainFrame.getContentPane().add(liveSetPanel, 1);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                adminMainFrame.revalidate();
                adminMainFrame.repaint();
                loading.setVisible(false);
            }
        }.execute();

        loading.setVisible(true);


    }

    public void setAdminMainFrame(AdminMainFrame adminMainFrame) {
        this.adminMainFrame = adminMainFrame;
        loading = new Loading(adminMainFrame);
    }
}
