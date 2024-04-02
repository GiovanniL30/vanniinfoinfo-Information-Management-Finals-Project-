package admin.view;

import admin.controller.AdminController;
import admin.view.components.AdminHeader;
import admin.view.panel.AdminHomePanel;
import admin.view.panel.EditPerformerPanel;
import admin.view.panel.LiveSetPanel;
import admin.view.panel.PerformerPanel;
import shared.viewComponents.LoginView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminMainFrame extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private JPanel mainLayout = new JPanel();
    private AdminController adminController;
    private AdminHeader adminHeader = new AdminHeader();

    private EditPerformerPanel editPerformerPanel;
    private LiveSetPanel liveSetPanel;
    private PerformerPanel performerPanel;

    private AdminHomePanel adminHomePanel;

    public AdminMainFrame(AdminController adminController)  {
      this.adminController = adminController;
      initializeFrame();

      adminHomePanel = new AdminHomePanel(this.adminController);

      getContentPane().add(adminHeader, BorderLayout.NORTH);
      getContentPane().add(new LoginView(adminController, true), BorderLayout.CENTER);
    }

    private void initializeFrame() {
        setSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("resources/images/GigPlatform.png").getImage());
        setTitle("GigPlatform");
        BorderLayout borderLayout = new BorderLayout();

        mainLayout.setBorder(new EmptyBorder(25, 50 ,25, 50));
        mainLayout.setBackground(Color.white);
        setContentPane(mainLayout);

        getContentPane().setLayout(borderLayout);
        setVisible(true);
    }

    public EditPerformerPanel getEditPerformerPanel() {
        return editPerformerPanel;
    }

    public void setEditPerformerPanel(EditPerformerPanel editPerformerPanel) {
        this.editPerformerPanel = editPerformerPanel;
    }

    public LiveSetPanel getLiveSetPanel() {
        return liveSetPanel;
    }

    public void setLiveSetPanel(LiveSetPanel liveSetPanel) {
        this.liveSetPanel = liveSetPanel;
    }

    public PerformerPanel getPerformerPanel() {
        return performerPanel;
    }

    public void setPerformerPanel(PerformerPanel performerPanel) {
        this.performerPanel = performerPanel;
    }
}
