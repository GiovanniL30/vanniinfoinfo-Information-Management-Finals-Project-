package vanniinfoinfo.view.panel;

import vanniinfoinfo.controller.AdminControllerObserver;
import vanniinfoinfo.view.AdminMainFrame;
import vanniinfoinfo.view.utility.AdminPanel;
import shared.referenceClasses.Performer;
import shared.referenceClasses.PerformerType;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.*;
import shared.viewComponents.Button;
import shared.viewComponents.TextArea;

import javax.swing.*;
import java.awt.*;

public class EditPerformerPanel extends JPanel {
    private Performer performer;
    private AdminControllerObserver adminControllerObserver;

    public EditPerformerPanel(Performer performer, AdminControllerObserver adminControllerObserver) {

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

        JLabel label = new JLabel("EDIT PERFORMER");
        label.setFont(FontFactory.newPoppinsBold(16));
        n.add(label);
        return n;
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        FieldInput performerName = new FieldInput("Performer", new Dimension(AdminMainFrame.WIDTH - 125, 60), 40, 1, false);
        performerName.getTextField().setText(performer.getPerformerName());

        JPanel performerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        performerPanel.setBackground(Color.white);
        performerPanel.add(performerName);

        JPanel dropDowns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dropDowns.setBackground(Color.WHITE);
        DropDown genre = new DropDown(new Dimension(355, 60), "Genre", UtilityMethods.populateGenres(adminControllerObserver.getGenres()));
        DropDown performerStatus = new DropDown(new Dimension(355, 60), "Performer Status", performer.getPerformerStatus().equals("Active") ? new String[]{"Active", "Inactive"} : new String[] {"Inactive", "Active"});
        DropDown performerType = new DropDown(new Dimension(355, 60), "Performer Type", adminControllerObserver.getPerformerTypes().stream().map(PerformerType::getTypeName).toList().toArray(new String[0]));
        dropDowns.add(genre);
        dropDowns.add(performerStatus);
        dropDowns.add(performerType);

        TextArea description = new TextArea(new Dimension(AdminMainFrame.WIDTH - 60, 200), "Description", performer.getDescription());

        panel.add(performerPanel);
        panel.add(dropDowns);

        panel.add(Box.createVerticalStrut(30));
        panel.add(description);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.white);
        Button cancel = new Button("CANCEL", new Dimension(537, 50), FontFactory.newPoppinsBold(14));
        FilledButton save = new FilledButton("SAVE", new Dimension(537, 50), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.WHITE);
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        panel.add(Box.createVerticalStrut(30));
        panel.add(buttonPanel);

        cancel.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.PERFORMER));

        save.addActionListener(e -> {

            String newName = performerName.getInput();
            String newDescription = description.getText();
            String newGenre = adminControllerObserver.getGenres().get(genre.choiceIndex()).getGenreName();
            String newStatus = performerStatus.getChoice();
            String newType = adminControllerObserver.getPerformerTypes().get(performerType.choiceIndex()).getTypeName();

            if(UtilityMethods.haveNullOrEmpty(newName, newDescription)) {
                return;
            }

            Performer updatedPerformer = new Performer(performer.getPerformerID(), newName, newGenre, newType, newDescription, newStatus);
            adminControllerObserver.updatePerformer(updatedPerformer);
        });

        return panel;
    }



}
