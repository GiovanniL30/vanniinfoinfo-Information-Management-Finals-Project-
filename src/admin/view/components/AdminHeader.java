package admin.view.components;

import client.view.ClientMainFrame;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class AdminHeader extends JPanel {

    public AdminHeader() {
        setPreferredSize(new Dimension(ClientMainFrame.WIDTH, 50));
        setLayout(new BorderLayout());
        setBackground(Color.white);

        Picture logo = new Picture("resources/images/GigPlatform.png", 100, 30);
        logo.setBackground(Color.WHITE);


        add(logo, BorderLayout.WEST);
    }


}
