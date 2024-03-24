package client.view.panels;

import client.controller.ClientControllerObserver;
import client.view.ClientViews;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.FieldInput;
import shared.viewComponents.FilledButton;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    public LoginView(ClientControllerObserver clientControllerObserver) {

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;

        setBackground(Color.white);
        setLayout(new GridBagLayout());

        Picture heroPicture = new Picture("resources/images/login-header.png", 500, 130);
        heroPicture.setBackground(Color.WHITE);

        JPanel fieldInputs = fieldInputs();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,0 ));
        Button signUpButton = new Button("Sign up", new Dimension(200, 50) , FontFactory.newPoppinsDefault(12));
        FilledButton loginButton = new FilledButton("Login", new Dimension(200, 50) ,FontFactory.newPoppinsDefault(12), ColorFactory.red(), Color.white);
        buttonPanel.add(signUpButton);
        buttonPanel.add(loginButton);

        add(heroPicture, constraints);

        constraints.gridy = 1;
        add(fieldInputs, constraints);

        constraints.gridy = 2;
        add(buttonPanel, constraints);

        signUpButton.addActionListener(e -> clientControllerObserver.changeFrame(ClientViews.SIGN_UP));
        loginButton.addActionListener( e -> clientControllerObserver.changeFrame(ClientViews.HOME));
    }

    private JPanel fieldInputs(){

        JPanel fieldInputPanel = new JPanel();
        fieldInputPanel.setLayout(new BoxLayout(fieldInputPanel, BoxLayout.Y_AXIS));

        FieldInput userName = new FieldInput("User Name", new Dimension(420, 60), 40, 1, false);
        FieldInput password = new FieldInput("Password", new Dimension(420, 60), 40, 1, true);

        fieldInputPanel.add(userName);
        fieldInputPanel.add(password);

        return fieldInputPanel;
    }

}
