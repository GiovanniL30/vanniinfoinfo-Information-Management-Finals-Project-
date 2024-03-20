package client.view.panels;

import client.controller.ClientControllerObserver;
import client.view.ClientViews;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    public LoginView(ClientControllerObserver clientControllerObserver) {
        setBackground(Color.white);

        Button button = new Button("Sign up", new Dimension(100, 50) , FontFactory.newPoppinsDefault(12));
        add(button);
        button.addActionListener(e -> clientControllerObserver.changeFrame(ClientViews.SIGN_UP));


    }

}
