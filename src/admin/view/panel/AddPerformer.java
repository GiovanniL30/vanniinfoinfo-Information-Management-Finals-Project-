package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.AdminMainFrame;
import admin.view.utility.AdminPanel;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.*;
import shared.viewComponents.Button;
import shared.viewComponents.TextArea;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class AddPerformer extends JPanel {
    private Performer performer;
    private AdminControllerObserver adminControllerObserver;


    public AddPerformer(Performer performer, AdminControllerObserver adminControllerObserver) {
        this.adminControllerObserver = adminControllerObserver;
        this.performer = performer;

        setBackground(Color.white);
        setLayout(new BorderLayout(0, 20));

        add(northPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);
    }

    private JPanel northPanel() {
        JPanel n = new JPanel(new FlowLayout(FlowLayout.CENTER));
        n.setBackground(Color.WHITE);

        JLabel label = new JLabel("Register Performer");
        label.setFont(FontFactory.newPoppinsBold(16));
        n.add(label);
        return n;
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        FieldInput performerName = new FieldInput("Performer", new Dimension(AdminMainFrame.WIDTH - 125, 60), 40, 1, false);
        performerName.getTextField().setText("");

        JPanel performerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        performerPanel.setBackground(Color.white);
        performerPanel.add(performerName);

        JPanel dropDowns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropDowns.setBackground(Color.WHITE);
        DropDown performerType = new DropDown(new Dimension(355, 60), "Performer Type", performer.getPerformerType().equals("Band") ? new String[] {"Band", "Solo"}:new String[] {"Solo", "Band"});
        DropDown genre = new DropDown(new Dimension(355, 60), "Genre", UtilityMethods.populateGenres(adminControllerObserver.getGenres()));
        dropDowns.add(genre);
        dropDowns.add(performerType);


        TextArea description = new TextArea(new Dimension(AdminMainFrame.WIDTH - 60, 200), "Description", performer.getDescription());

        panel.add(performerPanel);
        panel.add(dropDowns);
        panel.add(description);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setBackground(Color.white);
        Button cancel = new Button("CANCEL", new Dimension(537, 50), FontFactory.newPoppinsBold(14));
        FilledButton register = new FilledButton("REGISTER", new Dimension(537, 50), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.WHITE);
        buttonPanel.add(cancel);
        buttonPanel.add(register);

        panel.add(Box.createVerticalStrut(30));
        panel.add(buttonPanel);

        cancel.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.PERFORMER));

        register.addActionListener(e -> {

            String newName = performerName.getInput();
            String newDescription = description.getText();
            String newGenre = genre.getChoice();
            String newType = performerType.getChoice();

            if(UtilityMethods.haveNullOrEmpty(newName, newDescription)) {
                return;
            }

            Performer newPerformer = new Performer(UtilityMethods.generateRandomID(), newName, newGenre, newType, newDescription, "Active");
            adminControllerObserver.addPerformer(newPerformer);
        });

        return panel;
    }
}
