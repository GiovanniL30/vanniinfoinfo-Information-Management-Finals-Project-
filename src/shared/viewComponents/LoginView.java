package shared.viewComponents;

import client.controller.ClientControllerObserver;
import client.view.utility.ClientViews;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    private FilledButton loginButton;
    private ClientControllerObserver clientControllerObserver;
    private boolean isAdmin;

    public LoginView(ClientControllerObserver clientControllerObserver, boolean isAdmin) {
        this.clientControllerObserver = clientControllerObserver;
        this.isAdmin = isAdmin;

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;

        setBackground(Color.white);
        setLayout(new GridBagLayout());

        Picture heroPicture = new Picture((isAdmin) ? "resources/images/Admin_login_image.png" : "resources/images/login-header.png", 500, 130);
        heroPicture.setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,0 ));
        Button signUpButton = new Button("Sign up", new Dimension(200, 50) , FontFactory.newPoppinsDefault(12));

        if(isAdmin){
            loginButton = new FilledButton("Login", new Dimension(420, 50) ,FontFactory.newPoppinsDefault(12), ColorFactory.red(), Color.white);
        }else {
            buttonPanel.add(signUpButton);
            loginButton = new FilledButton("Login", new Dimension(200, 50) ,FontFactory.newPoppinsDefault(12), ColorFactory.red(), Color.white);
        }
        buttonPanel.add(loginButton);

        JPanel fieldInputs = fieldInputs();

        add(heroPicture, constraints);

        constraints.gridy = 1;
        add(fieldInputs, constraints);

        constraints.gridy = 2;
        add(buttonPanel, constraints);

        signUpButton.addActionListener(e -> clientControllerObserver.changeFrame(ClientViews.SIGN_UP));

    }

    private JPanel fieldInputs(){

        JPanel fieldInputPanel = new JPanel();
        fieldInputPanel.setLayout(new BoxLayout(fieldInputPanel, BoxLayout.Y_AXIS));

        FieldInput userName = new FieldInput("User Name", new Dimension(420, 60), 40, 1, false);
        FieldInput password = new FieldInput("Password", new Dimension(420, 60), 40, 1, true);

        fieldInputPanel.add(userName);
        fieldInputPanel.add(password);

        loginButton.addActionListener( e -> {

            String name = userName.getInput();
            String pass = password.getInput();

            if(UtilityMethods.haveNullOrEmpty(name, pass)) {
                return;
            }

            if(isAdmin) {

            }else {
                clientControllerObserver.logIn(name, pass);
            }


        });
        return fieldInputPanel;
    }

}
