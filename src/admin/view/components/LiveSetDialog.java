package admin.view.components;

import admin.controller.AdminControllerObserver;
import admin.view.utility.LiveSetDialogType;
import shared.referenceClasses.LiveSet;
import shared.referenceClasses.Performer;
import shared.utilityClasses.ColorFactory;
import shared.utilityClasses.FontFactory;
import shared.utilityClasses.UtilityMethods;
import shared.viewComponents.Button;
import shared.viewComponents.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.*;


public class LiveSetDialog extends JDialog {

    private final LinkedList<Performer> performers;
    private final AdminControllerObserver adminControllerObserver;
    private final LiveSet liveSet;
    private Picture thumbnailPreview;
    private JSpinner timeSpinner;
    private final LiveSetDialogType liveSetDialogType;
    private String imagePath = "";
    private FieldInput dateInput;

    public LiveSetDialog(JFrame frame, LiveSet liveSet, LinkedList<Performer> performers, AdminControllerObserver adminControllerObserver, LiveSetDialogType liveSetDialogType) {
        super(frame);
        this.adminControllerObserver = adminControllerObserver;
        this.performers = performers;
        this.liveSet = liveSet;
        this.liveSetDialogType = liveSetDialogType;

        setTitle((liveSetDialogType == LiveSetDialogType.ADD) ? "Add Liveset" : "Edit Liveset");
        setModal(true);
        setLayout(new BorderLayout(0, 50));
        setBackground(Color.white);

        add(northPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);

        setSize(new Dimension(850, 900));
        setResizable(false);
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
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.white);
        panel.add(liveSetPanel());
        return panel;
    }

    private JPanel liveSetPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.white);

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstRow.setBackground(Color.white);

        DropDown performersDropDown;

        if (isEdit()) {
            performersDropDown = new DropDown(new Dimension(350, 50), "Performer", performers.stream().map(Performer::getPerformerName).toList().toArray(new String[0]));
            performersDropDown.enable(false);
        } else {
            performersDropDown = new DropDown(new Dimension(350, 50), "Performer", performers.stream().filter(performer -> performer.getPerformerStatus().equals("Active")).map(Performer::getPerformerName).toList().toArray(new String[0]));

        }

        FieldInput streamUrl = new FieldInput("Stream URL", new Dimension(350, 50), 200, 10, false);
        if (isEdit()) streamUrl.getTextField().setText(liveSet.getStreamLinkURL());
        firstRow.add(performersDropDown);
        firstRow.add(streamUrl);

        JPanel secondRow = new JPanel(new GridLayout(1, 2, 20, 0));
        secondRow.setSize(new Dimension(500, 50));
        secondRow.setBackground(Color.white);


        dateInput = new FieldInput("Date", new Dimension(300, 50), 10, 10, false);
        if (isEdit()) dateInput.getTextField().setText(liveSet.getDate().toString());

        JPanel spinnerPanel = new JPanel(new BorderLayout());
        JLabel spinnerLabel = new JLabel("Time");
        spinnerPanel.setBackground(Color.white);
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        spinnerModel.setCalendarField(Calendar.MINUTE);
        timeSpinner = new JSpinner(spinnerModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeEditor.enableInputMethods(false);
        timeSpinner.setPreferredSize(new Dimension(250, 50));
        spinnerPanel.add(spinnerLabel, BorderLayout.NORTH);
        spinnerPanel.add(timeSpinner, BorderLayout.CENTER);

        secondRow.add(dateInput);
        secondRow.add(spinnerPanel);


        JPanel thirdRow = new JPanel();
        thirdRow.setPreferredSize(new Dimension(620, 300));
        thirdRow.setLayout(null);
        thirdRow.setBackground(Color.white);
        Button uploadImageButton = new Button(isEdit() ? "Change Thumbnail" : "Upload Thumbnail", new Dimension(300, 50), FontFactory.newPoppinsDefault(14));
        uploadImageButton.setBounds(0, 0, 360, 50);
        thumbnailPreview = new Picture("asc", 400, 300);

        thumbnailPreview.setBounds(380, 0, 350, 300);
        if (isEdit()) {
            imagePath = liveSet.getThumbnail();
            thumbnailPreview.updatePicture(liveSet.getThumbnail());
        }else {
            thumbnailPreview.setBackground(ColorFactory.lightGrey());
        }

        thirdRow.add(uploadImageButton);
        thirdRow.add(thumbnailPreview);


        JPanel lastRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        lastRow.setBackground(Color.white);
        Button cancel = new Button("CANCEL", new Dimension(345, 50), FontFactory.newPoppinsDefault(14));
        FilledButton add = new FilledButton(isEdit() ? "SAVE CHANGES": "ADD", new Dimension(345, 50), FontFactory.newPoppinsDefault(14), ColorFactory.red(), Color.white);
        lastRow.add(cancel);
        lastRow.add(add);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pricePanel.setBackground(Color.white);
        FieldInput price = new FieldInput("Price", new Dimension( isEdit() ? 350: 700, 50), 20, 1, false);
        if (isEdit()) price.getTextField().setText(liveSet.getPrice() + "");
        pricePanel.add(price);

        DropDown statusDropdown = new DropDown(new Dimension(350, 50), "Status", liveSet.getStatus().equals("Open") ? new String[]{"Open", "Canceled"} : new String[]{"Canceled", "Open"});
        if(isEdit()) {
            pricePanel.add(statusDropdown);
        }

        panel.add(firstRow);
        panel.add(secondRow);
        panel.add(Box.createVerticalStrut(30));
        panel.add(thirdRow);
        panel.add(pricePanel);
        panel.add(lastRow);


        uploadImageButton.addActionListener(e -> {
            imagePath = uploadImage();

            if (!imagePath.isEmpty()) {
                thumbnailPreview.updatePicture(imagePath);
            }
        });

        add.addActionListener(e -> {


            if (imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Upload a Live Set Thumbnail");
                return;
            }

            String streamURL = streamUrl.getInput();
            String date = dateInput.getInput();
            java.sql.Time time = new java.sql.Time(((Date) timeSpinner.getValue()).getTime());
            String p = price.getInput();
            int intP;


            if (UtilityMethods.haveNullOrEmpty(streamURL, p, date)) {
                return;
            }


            try {
                intP = Integer.parseInt(p);
            } catch (NumberFormatException exception) {
                price.enableError("Please enter a valid price");
                return;
            }

            if (!isValidURL(streamURL)) {
                streamUrl.enableError("Please enter a valid URL (ex.https://...)");
                return;
            }

            Optional<java.sql.Date> optionalDate = getDate(date);

            if(optionalDate.isEmpty()) {
                dateInput.enableError("Please enter a valid date (format. YYYY-MM-DD)");
                return;
            }

            if (isEdit()) {
                adminControllerObserver.editLiveSet(new LiveSet(liveSet.getLiveSetID(), statusDropdown.getChoice(), intP, optionalDate.get(), time, imagePath, streamURL, liveSet.getPerformerID()));
            } else {
                adminControllerObserver.addLiveSet(performers.get(performersDropDown.choiceIndex()) ,new LiveSet(UtilityMethods.generateRandomID(), "Open", intP, optionalDate.get(), time, imagePath, streamURL, performers.get(performersDropDown.choiceIndex()).getPerformerID()));
            }

        });

        cancel.addActionListener(e -> dispose());
        return panel;
    }


    private String uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Choose Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Images", "jpg", "jpeg"));

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return saveImage(selectedFile);
        }

        return "";
    }

    private String saveImage(File imageFile) {
        Path targetPath = null;
        try {
            Path sourcePath = imageFile.toPath();
            targetPath = Paths.get("resources/images/", imageFile.getName());
            Files.copy(sourcePath, targetPath);
        } catch (IOException ignored) {
            targetPath = Paths.get("resources/images/", imageFile.getName());
        }
        return targetPath.toString();
    }




    /**
     * The method will send an error if
     * If newStreamURL is an empty string.
     * If newStreamURL does not start with "http://" or "https://".
     * If newStreamURL contains characters that are not allowed in a URL (spaces or special characters).
     * If newStreamURL is missing the domain name or the top-level domain ("example.com" or "www.example.com").
     * If newStreamURL does not contain a valid path
     *
     * @param url
     * @return
     */
    private boolean isValidURL(String url) {
        String regex = "^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$";
        return url.matches(regex);
    }

    public Optional<java.sql.Date> getDate(String dateInput) {
        String[] date = dateInput.split("-");

        if (date.length != 3) {
            return Optional.empty();
        }

        try {
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int day = Integer.parseInt(date[2]);

            if (!isValidDate(year, month, day)) {
                return Optional.empty();
            }

            LocalDate localDate = LocalDate.of(year, month, day);
            return Optional.of(java.sql.Date.valueOf(localDate));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Please enter a valid integer date number");
            return Optional.empty();
        }
    }

    private boolean isValidDate(int year, int month, int day) {
        try {
            Year tempYear = Year.of(year);
            MonthDay monthDay = MonthDay.of(month, day);
            return tempYear.isValidMonthDay(monthDay);
        } catch (Exception e) {
            return false;
        }
    }

    public FieldInput getDateInput() {
        return dateInput;
    }

    private boolean isEdit() {
        return liveSetDialogType.equals(LiveSetDialogType.EDIT);
    }
}
