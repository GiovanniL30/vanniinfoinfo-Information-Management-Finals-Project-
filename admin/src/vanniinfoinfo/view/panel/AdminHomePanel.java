package vanniinfoinfo.view.panel;

import vanniinfoinfo.controller.AdminControllerObserver;
import vanniinfoinfo.view.utility.AdminPanel;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class AdminHomePanel extends JPanel {

    public AdminHomePanel(AdminControllerObserver adminControllerObserver) {
        setBackground(Color.white);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);
        JLabel title = new JLabel("WELCOME TO ADMIN DASHBOARD");
        title.setFont(FontFactory.newPoppinsBold(30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        Picture picture = new Picture("resources/images/login-header.png", 450, 200);
        picture.setBackground(Color.white);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBackground(Color.white);
        Button performer = new Button("Performers", new Dimension(115, 50), FontFactory.newPoppinsBold(15));
        Button liveSet = new Button("Livesets", new Dimension(115, 50), FontFactory.newPoppinsBold(15));


        panel.add(Box.createVerticalStrut(150));
        panel.add(title);
        panel.add(picture);
        panel.add(buttonsPanel);
        buttonsPanel.add(performer);
        buttonsPanel.add(liveSet);
        add(panel);

        performer.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.PERFORMER));
        liveSet.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.LIVE_SET));
    }

}
