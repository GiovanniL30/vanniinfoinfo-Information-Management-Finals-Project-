package client.view.components;

import client.view.ClientMainView;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {


    public Header() {
        setPreferredSize(new Dimension(ClientMainView.WIDTH, 35));
        setLayout(new BorderLayout());
        setBackground(Color.white);

        Picture logo = new Picture("resources/images/GigPlatform.png", 100, 30);
        logo.setBackground(Color.WHITE);
        add(logo, BorderLayout.WEST);
    }

}
