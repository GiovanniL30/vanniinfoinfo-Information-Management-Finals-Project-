package vanniinfoinfo.view.utility;

import vanniinfoinfo.controller.AdminControllerObserver;
import vanniinfoinfo.view.AdminMainFrame;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.viewComponents.Button;
import shared.viewComponents.FieldInput;
import shared.viewComponents.FilledButton;
import shared.viewComponents.IconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AdminSearchBar extends JPanel {

    private AdminPanel adminPanel;

    private AdminControllerObserver adminControllerObserver;
    private FilledButton add;
    private FieldInput searchField;

    public AdminSearchBar(AdminPanel adminPanel, AdminControllerObserver adminControllerObserver) {
        this.adminPanel = adminPanel;
        this.adminControllerObserver = adminControllerObserver;

        setBackground(Color.white);
        setLayout(new BorderLayout(0, 50));


        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setPreferredSize(new Dimension(AdminMainFrame.WIDTH, 50));
        centerPanel.setBackground(Color.WHITE);

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(Color.white);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;

        IconButton back = new IconButton("resources/images/back.png", 40, 40);
        add = new FilledButton("ADD", new Dimension(120, 50), FontFactory.newPoppinsBold(13), ColorFactory.red(), Color.WHITE);
        buttonsPanel.add(back, constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(0, 40, 0, 0);
        buttonsPanel.add(add, constraints);

        searchField = new FieldInput("", new Dimension(600, 50), Integer.MAX_VALUE, 0, false);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel();
        label.setFont(FontFactory.newPoppinsBold(14));
        labelPanel.add(label);

        if(adminPanel.equals(AdminPanel.PERFORMER)) {
            label.setText("Performers");
        }else {
            label.setText("Manage Live Set");
        }

        centerPanel.add(buttonsPanel, BorderLayout.WEST);
        centerPanel.add(searchField, BorderLayout.EAST);

        add(labelPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        back.addActionListener( e -> adminControllerObserver.changeFrame(AdminPanel.HOME));

        searchField.getTextField().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {

                if (!Character.isLetterOrDigit(e.getKeyChar())) {
                    if(!(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) return;
                }

                if (e.isAltDown() || e.isShiftDown() || e.isControlDown()) {
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {

                    if (searchField.getInput() != null && searchField.getInput().isEmpty()) {
                        searchField.removeError();
                    }
                    return;
                }


                if (searchField.getInput() != null) {
                    searchField.removeError();

                    if (adminPanel.equals(AdminPanel.PERFORMER)) {
                        adminControllerObserver.searchPerformers(searchField.getInput());
                    } else {
                        adminControllerObserver.searchLiveSetsAdmin(searchField.getInput());
                    }
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }


    public Button getAddButton() {
        return add;
    }
}
