package shared.viewComponents;

import client.view.utility.ClientViews;
import shared.controller.LoginController;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {

    private FilledButton loginButton;
    private LoginController loginController;
    private boolean isAdmin;
    private Button signUpButton;

    public LoginView(LoginController loginController, boolean isAdmin) {
        this.loginController = loginController;
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
        signUpButton = new Button("Sign up", new Dimension(200, 50) , FontFactory.newPoppinsDefault(12));

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

    }

    private JPanel fieldInputs(){

        JPanel fieldInputPanel = new JPanel();
        fieldInputPanel.setLayout(new BoxLayout(fieldInputPanel, BoxLayout.Y_AXIS));

        FieldInput userName = new FieldInput("User Name", new Dimension(420, 60), 20, 1, false);
        FieldInput password = new FieldInput("Password", new Dimension(420, 60), 20, 1, true);

        fieldInputPanel.add(userName);
        fieldInputPanel.add(password);

        loginButton.addActionListener( e -> {

            String name = userName.getInput();
            String pass = password.getInput();

            if(UtilityMethods.haveNullOrEmpty(name, pass)) {
                return;
            }


            loginController.logIn(name, pass);
        });
        return fieldInputPanel;
    }


    public Button getSignUpButton() {
        return signUpButton;
    }
}
