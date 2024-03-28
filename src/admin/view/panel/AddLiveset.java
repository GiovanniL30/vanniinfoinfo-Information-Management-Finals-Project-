package admin.view.panel;

import admin.controller.AdminControllerObserver;
import admin.view.AdminMainFrame;
import admin.view.utility.AdminPanel;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.DropDown;
import shared.viewComponents.FieldInput;
import shared.viewComponents.FilledButton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedList;

public class AddLiveset extends JPanel {
    private LinkedList<Performer> performer;
    private AdminControllerObserver adminControllerObserver;
    private LiveSet liveSet;
//    private FieldInput dateInput;
//    private FieldInput timeInput;
    private JLabel thumbnailPreview;
    private String thumbnailPath;

    public AddLiveset(LiveSet liveSet, LinkedList<Performer> performer, AdminControllerObserver adminControllerObserver) {
        this.adminControllerObserver = adminControllerObserver;
        this.performer = performer;
        this.liveSet = liveSet;

        setBackground(Color.white);
        setLayout(new BorderLayout(0, 20));

        add(northPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);
    }

    private JPanel northPanel() {
        JPanel n = new JPanel(new FlowLayout(FlowLayout.CENTER));
        n.setBackground(Color.WHITE);

        JLabel label = new JLabel("Add Liveset");
        label.setFont(FontFactory.newPoppinsBold(16));
        n.add(label);
        return n;
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel liveSetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        liveSetPanel.setBackground(Color.WHITE);

        // Create an array of performer names
        String[] performerNames = new String[performer.size()];
        for (int i = 0; i < performer.size(); i++) {
            performerNames[i] = performer.get(i).getPerformerName();
        }

        DropDown performerName = new DropDown(new Dimension(355, 60), "Performer", performerNames);
        FieldInput streamURL = new FieldInput("Stream URL", new Dimension(AdminMainFrame.WIDTH - 650, 30), 40, 1, false);
        streamURL.getTextField().setText("");
        liveSetPanel.add(performerName);
        liveSetPanel.add(streamURL);

        panel.add(liveSetPanel);


//        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        dateTimePanel.setBackground(Color.WHITE);
//
//        // Create date input
//        dateInput = new FieldInput("", new Dimension(150, 40), 40, 1, true);
//        dateInput.getTextField().setToolTipText("Date (YYYY-MM-DD)");
//        dateTimePanel.add(dateInput);
//
//        // Create time input
//        timeInput = new FieldInput("", new Dimension(100, 40), 40, 1, true);
//        timeInput.getTextField().setToolTipText("Time (HH:MM)");
//        dateTimePanel.add(timeInput);
//
//        panel.add(dateTimePanel);


        JPanel thumbnailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thumbnailPanel.setBackground(Color.WHITE);

        Button uploadThumbnail = new Button("Upload Thumbnail", new Dimension(400, 40), FontFactory.newPoppinsDefault(14));
        uploadThumbnail.addActionListener(e -> handleThumbnailUpload());
        thumbnailPanel.add(uploadThumbnail);

        thumbnailPreview = new JLabel();
        thumbnailPreview.setPreferredSize(new Dimension(600, 300));
        thumbnailPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        thumbnailPanel.add(thumbnailPreview);

        panel.add(thumbnailPanel);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.setBackground(Color.white);
        Button cancel = new Button("CANCEL", new Dimension(537, 50), FontFactory.newPoppinsBold(14));
        FilledButton addLiveset = new FilledButton("ADD", new Dimension(537, 50), FontFactory.newPoppinsBold(14), ColorFactory.red(), Color.WHITE);
        buttonPanel.add(cancel);
        buttonPanel.add(addLiveset);

        panel.add(Box.createVerticalStrut(30));
        panel.add(buttonPanel);

        cancel.addActionListener(e -> adminControllerObserver.changeFrame(AdminPanel.LIVE_SET));

        addLiveset.addActionListener(e -> {
            String selectedPerformer = performerName.getChoice();
            String newStreamURL = streamURL.getInput();
//            String newDate = dateInput.getInput();
//            String newTime = timeInput.getInput();

            // Validate input
//            if (UtilityMethods.haveNullOrEmpty(selectedPerformer, newStreamURL, newDate, newTime, thumbnailPath)) {
//                JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }


            String performerID = getPerformerIdByName(selectedPerformer);
//            LiveSet newLiveSet = new LiveSet(UtilityMethods.generateRandomID(), "Open", 0, newDate, newTime, thumbnailPath, streamURL, performerID);
//            adminControllerObserver.addLiveSet(newLiveSet);
        });
        return panel;
    }

    private void handleThumbnailUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Choose Thumbnail");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            thumbnailPath = selectedFile.getAbsolutePath();

            // Update the thumbnail preview
            ImageIcon thumbnailIcon = new ImageIcon(thumbnailPath);
            Image scaledImage = thumbnailIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
            thumbnailPreview.setIcon(new ImageIcon(scaledImage));
        }
    }

    private String getPerformerIdByName(String performerName) {
        for (Performer performer : this.performer) {
            if (performer.getPerformerName().equals(performerName)) {
                return performer.getPerformerID();
            }
        }
        return null;
    }
}
