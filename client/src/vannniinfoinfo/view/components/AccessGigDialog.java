package vannniinfoinfo.view.components;

import vannniinfoinfo.controller.ClientControllerObserver;
import shared.referenceClasses.LiveSet;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.FieldInput;
import shared.viewComponents.FilledButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccessGigDialog extends JDialog {

    private ClientControllerObserver clientControllerObserver;
    private LiveSet liveSet;

    public AccessGigDialog(JFrame frame, LiveSet liveSet, ClientControllerObserver clientControllerObserver) {
        super(frame, "Access liveset", true );
        setLocationRelativeTo(null);
        this.liveSet = liveSet;
        this.clientControllerObserver = clientControllerObserver;
        setSize(new Dimension(500, 220));
        setResizable(false);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setBackground(Color.white);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(Color.white);
        JLabel headerTitle = new JLabel("Enter your Ticket Number");
        headerTitle.setFont(FontFactory.newPoppinsDefault(15));
        header.add(headerTitle);

        FieldInput fieldInput = new FieldInput("", new Dimension(400, 50), 40, 1, false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        FilledButton accessButton = new FilledButton("ACCESS", new Dimension(190, 50), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.WHITE);
        Button cancelButton = new Button("Cancel", new Dimension(190, 50), FontFactory.newPoppinsBold(14));
        buttonPanel.add(cancelButton);
        buttonPanel.add(accessButton);
        buttonPanel.setBackground(Color.white);
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        getContentPane().add(header);
        getContentPane().add(fieldInput);
        getContentPane().add(buttonPanel);

        cancelButton.addActionListener(e -> dispose());
        accessButton.addActionListener( e ->  {
            String ticket = fieldInput.getInput();

            if(ticket == null) {
                return;
            }

            clientControllerObserver.accessLiveSet(liveSet, ticket);
        });
    }


}
