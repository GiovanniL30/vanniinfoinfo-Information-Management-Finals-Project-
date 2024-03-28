package admin.controller;

import admin.view.AdminMainFrame;
import admin.view.panel.AdminHomePanel;
import admin.view.panel.EditPerformerPanel;
import admin.view.panel.LiveSetPanel;
import admin.view.panel.PerformerPanel;
import admin.view.utility.AdminPanel;
import shared.model.Database;
import shared.referenceClasses.Performer;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.util.LinkedList;

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

                        PerformerPanel performerPanel = new PerformerPanel(getPerformers(), AdminController.this);
                        adminMainFrame.setPerformerPanel(performerPanel);
                        adminMainFrame.getContentPane().add(performerPanel, 1);

                    }case LIVE_SET -> {

                        LiveSetPanel liveSetPanel = new LiveSetPanel(Database.getLiveSets(), Database.getPerformers(),AdminController.this);
                        adminMainFrame.setLiveSetPanel(liveSetPanel);
                        adminMainFrame.getContentPane().add(liveSetPanel, 1);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                adminMainFrame.getContentPane().revalidate();
                adminMainFrame.getContentPane().repaint();
                loading.setVisible(false);
            }
        }.execute();

        loading.setVisible(true);


    }

    @Override
    public void editPerformerFrame(Performer performer) {
        new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                adminMainFrame.getContentPane().remove(1);
                EditPerformerPanel editPerformerPanel = new EditPerformerPanel(performer, AdminController.this);
                adminMainFrame.getContentPane().add(editPerformerPanel, 1);
                return null;
            }

            @Override
            protected void done() {
                adminMainFrame.getContentPane().revalidate();
                adminMainFrame.getContentPane().repaint();
                loading.setVisible(false);
            }
        }.execute();

        loading.setVisible(true);
    }

    @Override
    public void addPerformer(Performer performer) {
        if(Database.addPerformer(performer)) {
            changeFrame(AdminPanel.PERFORMER);
            JOptionPane.showMessageDialog(adminMainFrame, "Added Performer successfully");
        }else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error adding the performer");
        }
    }

    @Override
    public void updatePerformer(Performer performer) {

        if(Database.updatePerformer(performer)) {
            changeFrame(AdminPanel.PERFORMER);
            JOptionPane.showMessageDialog(adminMainFrame, "Updated Performer successfully");
        }else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error updating the performer");
        }

    }

    public LinkedList<Performer> getPerformers() {
        return Database.getPerformers();
    }



    public void setAdminMainFrame(AdminMainFrame adminMainFrame) {
        this.adminMainFrame = adminMainFrame;
        loading = new Loading(adminMainFrame);
    }
}
