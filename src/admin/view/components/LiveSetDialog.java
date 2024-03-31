package admin.view.components;

import admin.controller.AdminControllerObserver;
import admin.view.AdminMainFrame;
import admin.view.utility.LiveSetDialogType;
import org.jdesktop.swingx.JXDatePicker;
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
import java.util.Calendar;
import java.util.LinkedList;

public class LiveSetDialog extends JDialog {

    private LinkedList<Performer> performer;
    private AdminControllerObserver adminControllerObserver;
    private LiveSet liveSet;
    private JLabel thumbnailPreview;
    private String thumbnailPath;
    private JXDatePicker datePicker;
    private JSpinner timeSpinner;
    private LiveSetDialogType liveSetDialogType;

    public LiveSetDialog(JFrame frame, LiveSet liveSet, LinkedList<Performer> performer, AdminControllerObserver adminControllerObserver, LiveSetDialogType liveSetDialogType) {
        super(frame);
        this.adminControllerObserver = adminControllerObserver;
        this.performer = performer;
        this.liveSet = liveSet;
        this.liveSetDialogType = liveSetDialogType;

        setTitle((liveSetDialogType == LiveSetDialogType.ADD) ? "Add Liveset" : "Edit Liveset");
        setModal(true);
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.white);

        add(northPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(frame);
    }

    private JPanel northPanel() {
        JPanel n = new JPanel(new FlowLayout(FlowLayout.CENTER));
        n.setBackground(Color.WHITE);

        JLabel label = new JLabel(liveSetDialogType == LiveSetDialogType.ADD ? "Add Liveset" : "Edit Liveset");
        label.setFont(FontFactory.newPoppinsBold(16));
        n.add(label);
        return n;
    }

    private JPanel centerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        if (liveSetDialogType == LiveSetDialogType.ADD) {
            panel.add(addLiveSetPanel());
        } else {
            panel.add(editLiveSetPanel());
        }

        return panel;
    }

    private JPanel addLiveSetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel liveSetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        liveSetPanel.setBackground(Color.WHITE);

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

        JPanel dateTimePanel = new JPanel(new BorderLayout());
        dateTimePanel.setBackground(Color.WHITE);

        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setBackground(Color.WHITE);

        JLabel dateLabel = new JLabel("Date:");
        datePanel.add(dateLabel, BorderLayout.NORTH);

        datePicker = new JXDatePicker();
        datePicker.setPreferredSize(new Dimension(200, 30));
        datePanel.add(datePicker, BorderLayout.CENTER);
        datePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20,0));
        dateTimePanel.add(datePanel, BorderLayout.WEST);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.setBackground(Color.WHITE);

        JLabel timeLabel = new JLabel("Time:");
        timePanel.add(timeLabel, BorderLayout.NORTH);

        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        spinnerModel.setCalendarField(Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(spinnerModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);
        timePanel.add(timeSpinner, BorderLayout.CENTER);

        timePanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 0));
        dateTimePanel.add(timePanel, BorderLayout.CENTER);

        FieldInput price = new FieldInput("Price", new Dimension(400, 30), 9, 1, false);
        price.getTextField().setText("");
        dateTimePanel.add(price, BorderLayout.EAST);
        panel.add(dateTimePanel);

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

        cancel.addActionListener(e -> dispose());

        addLiveset.addActionListener(e -> {
            String selectedPerformer = performerName.getChoice();
            String newStreamURL = streamURL.getInput();
            Date selectedDate = new Date(datePicker.getDate().getTime());
            java.util.Date timeDate = (java.util.Date) timeSpinner.getValue();
            Time time = new Time(timeDate.getTime());
            int priceValue;
            try {
                priceValue = Integer.parseInt(price.getInput());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Price should be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (UtilityMethods.haveNullOrEmpty(selectedPerformer, newStreamURL, thumbnailPath)) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!isValidURL(newStreamURL)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid URL for the Stream URL.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String performerID = getPerformerIdByName(selectedPerformer);

            LiveSet newLiveSet = new LiveSet(UtilityMethods.generateRandomID(), "Open",priceValue, selectedDate, time, thumbnailPath, newStreamURL,performerID);
            adminControllerObserver.addLiveSet(newLiveSet);
        });

        return panel;
    }

    /**
     * The method will send an error if
     * If newStreamURL is an empty string.
     * If newStreamURL does not start with "http://" or "https://".
     * If newStreamURL contains characters that are not allowed in a URL (spaces or special characters).
     * If newStreamURL is missing the domain name or the top-level domain ("example.com" or "www.example.com").
     * If newStreamURL does not contain a valid path
     * @param url
     * @return
     */
    private boolean isValidURL(String url) {
        String regex = "^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$";
        return url.matches(regex);
    }
    private JPanel editLiveSetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        // Add components for the "Edit Liveset" panel here


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
