package client.view;

import client.controller.ClientController;
import client.controller.ClientControllerObserver;
import client.view.components.ClientHeader;
import client.view.panels.HomeView;
import client.view.utility.ClientViews;
import shared.viewComponents.LoginView;
import client.view.panels.SignUpView;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientMainFrame extends JFrame {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    private JPanel mainLayout = new JPanel();
    private ClientHeader clientHeader = new ClientHeader("");
    private ClientControllerObserver clientControllerObserver;
    private HomeView homeView;
    private LoginView loginView;
    private SignUpView signUpView;

    public ClientMainFrame(ClientController clientControllerObserver) {
        this.clientControllerObserver = clientControllerObserver;
        initializeFrame();
        getContentPane().add(clientHeader, BorderLayout.NORTH);
        loginView = new LoginView(clientControllerObserver, false);
        loginView.getSignUpButton().addActionListener(e -> this.clientControllerObserver.changeFrame(ClientViews.SIGN_UP));
        getContentPane().add(loginView, BorderLayout.CENTER);
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

    public void setHomeView(HomeView homeView) {
        this.homeView = homeView;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setSignUpView(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    public HomeView getHomeView() {
        return homeView;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public SignUpView getSignUpView() {
        return signUpView;
    }

    public ClientHeader getHeader() {
        return clientHeader;
    }
}
