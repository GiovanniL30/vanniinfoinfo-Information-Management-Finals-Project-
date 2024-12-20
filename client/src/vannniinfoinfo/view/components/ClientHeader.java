package vannniinfoinfo.view.components;

import shared.viewComponents.IconButton;
import vannniinfoinfo.view.ClientMainFrame;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Picture;

import javax.swing.*;
import java.awt.*;

public class ClientHeader extends JPanel {

    private JLabel userName;
    private IconButton logo;
    public ClientHeader(String name) {
        setPreferredSize(new Dimension(ClientMainFrame.WIDTH, 50));
        setLayout(new BorderLayout());
        setBackground(Color.white);

        logo = new IconButton("resources/images/GigPlatform.png", 100, 30);
        logo.setBackground(Color.WHITE);

        userName = new JLabel(name.toUpperCase());
        userName.setFont(FontFactory.newPoppinsBold(15));
        userName.setForeground(ColorFactory.red());
        add(logo, BorderLayout.WEST);
        add(userName, BorderLayout.EAST);
    }

    public void setUserName(String name) {
        userName.setText(name);
        revalidate();
        repaint();
    }

    public IconButton getLogo() {
        return logo;
    }
}
