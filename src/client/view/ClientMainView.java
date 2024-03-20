package client.view;

import client.controller.ClientController;
import client.controller.ClientControllerObserver;
import client.view.components.Header;
import client.view.panels.SignUpPanel;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import shared.viewComponents.Picture;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientMainView extends JFrame {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    private JPanel mainLayout = new JPanel();
    private Header header = new Header();
    private ClientControllerObserver clientControllerObserver;

    public ClientMainView(ClientController clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;
        initializeFrame();
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(new SignUpPanel(clientControllerObserver), BorderLayout.CENTER);
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

}