package admin.view;

import admin.controller.AdminControllerObserver;
import admin.view.components.AdminHeader;
import admin.view.panel.AdminHomePanel;
import admin.view.panel.EditPerformerPanel;
import admin.view.panel.LiveSetPanel;
import admin.view.panel.PerformerPanel;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminMainFrame extends JFrame {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private JPanel mainLayout = new JPanel();
    private AdminControllerObserver adminControllerObserver;
    private AdminHeader adminHeader = new AdminHeader();

    private EditPerformerPanel editPerformerPanel;
    private LiveSetPanel liveSetPanel;
    private PerformerPanel performerPanel;

    private AdminHomePanel adminHomePanel;

    public AdminMainFrame(AdminControllerObserver adminControllerObserver)  {
      this.adminControllerObserver = adminControllerObserver;
      initializeFrame();

      adminHomePanel = new AdminHomePanel(this.adminControllerObserver);

      getContentPane().add(adminHeader, BorderLayout.NORTH);
      getContentPane().add(adminHomePanel, BorderLayout.CENTER);
    }

    private void initializeFrame() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 5);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumbArc", 3);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
            System.exit(0);
        }

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
