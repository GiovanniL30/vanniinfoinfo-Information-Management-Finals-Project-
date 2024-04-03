package admin.controller;

import admin.view.AdminMainFrame;
import admin.view.components.LiveSetDialog;
import admin.view.panel.AdminHomePanel;
import admin.view.panel.EditPerformerPanel;
import admin.view.panel.LiveSetPanel;
import admin.view.panel.PerformerPanel;
import admin.view.utility.AdminPanel;
import admin.view.utility.LiveSetDialogType;
import shared.controller.LoginController;
import shared.model.Database;
import shared.referenceClasses.Genre;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.referenceClasses.User;
import shared.viewComponents.Loading;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AdminController implements AdminControllerObserver, LoginController {

    private AdminMainFrame adminMainFrame;
    private LiveSetDialog liveSetDialog;
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

                    }
                    case LIVE_SET -> {

                        LiveSetPanel liveSetPanel = new LiveSetPanel(Database.getLiveSets().getPayload(), Database.getPerformers().getPayload(), AdminController.this);
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
        if (Database.addPerformer(performer)) {
            changeFrame(AdminPanel.PERFORMER);
            JOptionPane.showMessageDialog(adminMainFrame, "Added Performer successfully");
        } else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error adding the performer");
        }
    }

    @Override
    public void updatePerformer(Performer performer) {

        if (Database.updatePerformer(performer)) {
            changeFrame(AdminPanel.PERFORMER);
            JOptionPane.showMessageDialog(adminMainFrame, "Updated Performer successfully");
        } else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error updating the performer");
        }

    }

    @Override
    public void addLiveSet(LiveSet liveSet) {

        if (Database.addLiveSet(liveSet)) {
            changeFrame(AdminPanel.LIVE_SET);
            liveSetDialog.dispose();
            JOptionPane.showMessageDialog(adminMainFrame, "Added LiveSet successfully");
        } else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error adding the LiveSet");
        }

    }

    @Override
    public void editLiveSet(LiveSet liveSet) {

        if (Database.editLiveSet(liveSet)) {
            changeFrame(AdminPanel.LIVE_SET);
            liveSetDialog.dispose();
            JOptionPane.showMessageDialog(adminMainFrame, "Edited LiveSet successfully");
        } else {
            JOptionPane.showMessageDialog(adminMainFrame, "Having error editing the LiveSet");
        }

    }

    @Override
    public void openEditLiveSet(LiveSet liveSet, LinkedList<Performer> performers) {
        liveSetDialog = new LiveSetDialog(adminMainFrame, liveSet, performers, this, LiveSetDialogType.EDIT);
        liveSetDialog.setVisible(true);
    }

    @Override
    public void openAddLiveSet(LiveSet liveSet, LinkedList<Performer> performers) {
        liveSetDialog = new LiveSetDialog(adminMainFrame, liveSet, performers, this, LiveSetDialogType.ADD);
        liveSetDialog.setVisible(true);
    }

    public LinkedList<Performer> getPerformers() {
        return Database.getPerformers().getPayload();
    }

    public LinkedList<Genre> getGenres() {
        return Database.getGenres().getPayload();
    }

    public void setAdminMainFrame(AdminMainFrame adminMainFrame) {
        this.adminMainFrame = adminMainFrame;
        loading = new Loading(adminMainFrame);
    }

    @Override
    public void logIn(String userName, String password) {
        new SwingWorker<Optional<User>, Void>() {
            @Override
            protected Optional<User> doInBackground() {
                return Database.logIn(userName, password).getPayload();
            }

            @Override
            protected void done() {
                loading.setVisible(false);
                try {
                    Optional<User> user = get();
                    if (user.isPresent()) {

                        if (!user.get().getUserType().equals("Admin")) {
                            JOptionPane.showMessageDialog(adminMainFrame, "Invalid Credentials");
                            return;
                        }

                        changeFrame(AdminPanel.HOME);
                        JOptionPane.showMessageDialog(adminMainFrame, "Log in Success");
                    } else {
                        JOptionPane.showMessageDialog(adminMainFrame, "Invalid Credentials");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute();

        loading.setVisible(true);
    }

    @Override
    public void searchPerformers(String searchTerm) {
        LinkedList<Performer> searchResults = Database.searchPerformers(searchTerm).getPayload();
        adminMainFrame.getPerformerPanel().populatePerformers(searchResults);
    }


}
