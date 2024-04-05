package client.view.panels;

import client.controller.ClientControllerObserver;
import client.view.ClientMainFrame;
import client.view.utility.ClientViews;
import shared.referenceClasses.User;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.ClickableText;
import shared.viewComponents.FieldInput;
import shared.viewComponents.FilledButton;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class SignUpView extends JPanel {

    private FieldInput firstName;
    private FieldInput lastName;
    private FieldInput userName;
    private FieldInput email;
    private FieldInput password;

    public SignUpView(ClientControllerObserver clientControllerObserver) {

        setBackground(Color.white);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(ClientMainFrame.WIDTH, ClientMainFrame.HEIGHT));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.gridy = 0;
        layoutConstraints.gridx = 0;

        Picture heroPicture = new Picture("resources/images/sign-up-header.png", 400, 150);
        heroPicture.setBackground(Color.white);
        add(heroPicture, layoutConstraints);


        JPanel fieldInputHolders = createFieldInputs();
        fieldInputHolders.setBackground(Color.WHITE);
        layoutConstraints.gridy = 1;
        add(fieldInputHolders, layoutConstraints);

        ClickableText loginButton = new ClickableText("Already have an account? Login", 400, 50, FontFactory.newPoppinsDefault(11));
        loginButton.setForeground(ColorFactory.lightGrey());
        layoutConstraints.gridy = 2;
        add(loginButton, layoutConstraints);


        FilledButton createAccountButton = new FilledButton("CREATE ACCOUNT", new Dimension(950, 50), FontFactory.newPoppinsBold(11), ColorFactory.red(), Color.WHITE);
        layoutConstraints.gridy = 3;
        add(createAccountButton, layoutConstraints);

        loginButton.addActionListener(e -> clientControllerObserver.changeFrame(ClientViews.LOGIN));

        createAccountButton.addActionListener(action -> {

            String fName = firstName.getInput();
            String lName = lastName.getInput();
            String uName = userName.getInput();
            String p = password.getInput();
            String e = email.getInput();

            if(UtilityMethods.haveNullOrEmpty(fName, lName, uName, p, e)){
                return;
            }

            if(!UtilityMethods.isEmailValid(e)) {
                email.enableError("Please enter a valid email (ex. myname@gmail.com)");
                return;
            }

            if(uName.matches(".*\\s+.*") || p.matches(".*\\s+.*")) {

                if(uName.matches(".*\\s+.*")) {
                    userName.enableError("Spaces are not allowed here");
                }

                if(p.matches(".*\\s+.*")) {
                    password.enableError("Spaces are not allowed here");
                }

                return;
            }


            User newUser = new User(UtilityMethods.generateRandomID(), fName, lName, uName, e, p, 0, "Active", false, "Client");
            clientControllerObserver.signUp(newUser);
        });
    }

    private JPanel createFieldInputs() {
        JPanel fieldInputHolders = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;


        firstName = new FieldInput("First Name", new Dimension(450, 50), 40, 1, false);
        lastName = new FieldInput("Last Name", new Dimension(450, 50), 40, 1, false);
        userName = new FieldInput("User Name", new Dimension(450, 50), 40, 1, false);
        email = new FieldInput("Email", new Dimension(450, 50), 40, 1, false);
        password = new FieldInput("Password", new Dimension(450, 50), 40, 1, true);


        constraints.gridy = 0;
        constraints.gridx = 0;
        fieldInputHolders.add(firstName, constraints);

        constraints.gridx = 1;
        fieldInputHolders.add(lastName, constraints);


        constraints.gridy = 1;
        constraints.gridx = 0;
        fieldInputHolders.add(userName, constraints);

        constraints.gridx = 1;
        fieldInputHolders.add(email, constraints);

        constraints.gridy = 2;
        constraints.fill = 3;
        constraints.gridx = 0;


        fieldInputHolders.add(password, constraints);
        return fieldInputHolders;
    }

}
