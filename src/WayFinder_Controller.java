import com.google.firebase.database.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Serato, Jay Vince on November 01, 2017.
 */
public class WayFinder_Controller implements Initializable {
    private DatabaseReference database;

    public Pane pMap, pTapToStart;
    public VBox vbMain, vbDetails;
    public HBox hbSubMap, hbTap;
    public TableView<Bus> tvBusDetails;
    public TableColumn<Bus, String> tcBusCompany, tcBusType, tccLocation, tcWingArea, tcBayNumber, tcDestination, tcLastTrip, tcFirstTrip, tcTime, tcMaxFare;
    public TextField taTravelDistance, taTravelTime, taFareAircon, taFareOrdinary;
    public ImageView ivTuburan, ivAsturias, ivBalamban, ivToledoCity, ivPinamungajan, ivAloguinsan, ivBarili, ivDumanjug, ivRonda, ivAlcantara, ivMoalboal, ivBadian, ivAlegria, ivMalabuyoc, ivGinatilan, ivSamboan, ivOslob, ivBoljoon, ivAlcoy, ivDalaguete, ivArgao, ivSibonga, ivCarcarCity, ivZamboanga, ivBacolod, ivSantander, ivDumaguete;
    public Label lTuburan, lAsturias, lBalamban, lToledoCity, lPinamungajan, lAloguinsan, lBarili, lDumanjug, lRonda, lAlcantara, lMoalboal, lBadian, lAlegria, lMalabuyoc, lGinatilan, lSamboan, lOslob, lBoljoon, lAlcoy, lDalaguete, lArgao, lSibonga, lCarcarCity, lZamboanga, lBacolod, lSantander, lDumaguete;
    public ImageView ivBackToMap, ivPrev, ivNext, ivTerminalPath;
    public Label lBackToMap, lDestination, lPrev, lNext;
    public ListView<TextArea> lvAnnouncements;
    public ListView<MenuButton> lvFAQ;

    private ObservableList<Bus> qualifier = FXCollections.observableArrayList();
    private ObservableList<Bus> buses = FXCollections.observableArrayList();
    private ObservableList<Municipality> municipalities = FXCollections.observableArrayList();
    private ObservableList<FAQ> faq = FXCollections.observableArrayList();
    private ObservableList<Announcement> announcements = FXCollections.observableArrayList();
    Rectangle2D screen = Screen.getPrimary().getVisualBounds();
    DatabaseReference dRef;
    Background vbBackground = new Background(new BackgroundImage(new Image(new File("src/res/Details_Background.png").toURI().toString(), screen.getWidth(), screen.getHeight(), false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
    Background background = new Background(new BackgroundImage(new Image(new File("src/res/MenuBackground.png").toURI().toString(), screen.getWidth(), screen.getHeight(), false, true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DatabaseHelper.initFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance().getReference();
        ObservableList<TextArea> ann = FXCollections.observableArrayList();
        dRef = database.child("announcements");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Announcement announcement = dataSnapshot.getValue(Announcement.class);
                announcements.add(announcement);
                TextArea ta = new TextArea(announcement.getAnnouncement());
                ta.setEditable(false);
                ta.setWrapText(true);
                ta.setPrefWidth(270);
                ta.setPrefHeight(Math.ceil(announcement.getAnnouncement().length() / 45) * 30);
                ta.setFocusTraversable(false);
                ann.add(ta);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvAnnouncements.setItems(ann);

        ObservableList<MenuButton> mbs = FXCollections.observableArrayList();
        ImageView comfortRoom = new ImageView(new File("src/res/TerminalMap/GIF/Comfort_Room.gif").toURI().toString());
        comfortRoom.setFitWidth(270);
        comfortRoom.setFitHeight(270);
        ImageView diningArea = new ImageView(new File("src/res/TerminalMap/GIF/Dining_Area.jpg").toURI().toString());
        diningArea.setFitHeight(270);
        diningArea.setFitWidth(270);
        ImageView chargingStation = new ImageView(new File("src/res/TerminalMap/GIF/Charging_Station.gif").toURI().toString());
        chargingStation.setFitHeight(270);
        chargingStation.setFitWidth(270);
        dRef = database.child("faq");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //faq.clear();
                FAQ faqs = dataSnapshot.getValue(FAQ.class);
                faq.add(faqs);
                ImageView help = new ImageView(new File("src/res/help.png").toURI().toString());
                help.setFitHeight(10.5);
                help.setFitWidth(10.5);
                MenuItem menuItem;
                if (faqs.getAnswer().contains("Comfort_Room")) {
                    menuItem = new MenuItem("", comfortRoom);
                } else if (faqs.getAnswer().contains("Dining_Area")) {
                    menuItem = new MenuItem("", diningArea);
                } else if (faqs.getAnswer().contains("Charging_Station")) {
                    menuItem = new MenuItem("", chargingStation);
                } else {
                    menuItem = new MenuItem(faqs.getAnswer());
                }
                MenuButton mb = new MenuButton(faqs.getQuestion(), help, menuItem);
                mb.setFocusTraversable(false);
                mb.setPrefSize(270, 20);
                mbs.add(mb);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lvFAQ.setItems(mbs);

        dRef = database.child("municipality_info");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Municipality mun = dataSnapshot.getValue(Municipality.class);
                mun.setFareAircon(Integer.parseInt(mun.fare_aircon));
                mun.setFareOrdinary(Integer.parseInt(mun.fare_ordinary));
                mun.setTravelDistance(mun.travel_distance);
                mun.setTravelTime(mun.travel_time);
                mun.setTheName(mun.name);
                municipalities.add(mun);

                for (Municipality m : municipalities) {
                    Municipality leftMun = null, rightMun = null, subroute = null;
                    Municipality[] encompassingMunicipality = new Municipality[15];

                    String left = m.left_mun;
                    String right = m.right_mun;
                    String subR = m.subroute;
                    boolean subrouteFound = false;
                    for (Municipality n : municipalities) {
                        if (n.toString().equalsIgnoreCase(left)) {
                            leftMun = n;
                        } else if (n.toString().equalsIgnoreCase(right)) {
                            rightMun = n;
                        }
                        if (!subrouteFound && n.getTheName().equalsIgnoreCase(subR)) {
                            subroute = n;
                            subrouteFound = true;
                        }
                    }
                    ObservableList<Municipality> encompassed = FXCollections.observableArrayList();
                    String enc = m.encompassed_mun;
                    String muni = "";
                    m.setLeftMun(leftMun);
                    m.setRightMun(rightMun);
                    m.setSubrouteArea(subroute);

                    int j = 0;
                    for (Municipality n : encompassed) {
                        encompassingMunicipality[j++] = n;
                    }
                    m.setEncompassingMunicipality(encompassingMunicipality);
                }

                for (Bus bus : buses) {
                    for (Municipality m : municipalities) {
                        if (bus.destination.equalsIgnoreCase(m.toString())) {
                            bus.setFinDestination(m);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dRef = database.child("bus_info");
        database.child("bus_info").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("An addition has been detected.");
                Bus bus = dataSnapshot.getValue(Bus.class);
                bus.setBayNumber(Integer.parseInt(bus.bay_num));
                bus.setBusCompany(bus.company);
                bus.setBuses(Integer.parseInt(bus.no_of_buses));
                bus.setBusType(bus.type);
                bus.setDeparture(bus.first_departure);
                bus.setLastTrip(bus.last_trip);
                bus.setFares(Integer.parseInt(bus.fare));
                bus.setWingArea(bus.wing_area);
                bus.setTrips(Integer.parseInt(bus.no_of_trips));
                bus.setId(Integer.parseInt(bus.bus_id));
                for (Bus b : buses) {
                    if (bus.bus_id.equalsIgnoreCase(b.bus_id)) {
                        buses.remove(b);
                        System.out.println("Hello hello");
                    }
                }
                System.out.println("Adding bus right now. Bus id: " + bus.bus_id);
                buses.add(bus);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("Change detected.");
                Bus bus = dataSnapshot.getValue(Bus.class);
                bus.setBayNumber(Integer.parseInt(bus.bay_num));
                bus.setBusCompany(bus.company);
                bus.setBuses(Integer.parseInt(bus.no_of_buses));
                bus.setBusType(bus.type);
                bus.setDeparture(bus.first_departure);
                bus.setLastTrip(bus.last_trip);
                bus.setFares(Integer.parseInt(bus.fare));
                bus.setWingArea(bus.wing_area);
                bus.setTrips(Integer.parseInt(bus.no_of_trips));
                bus.setId(Integer.parseInt(bus.bus_id));
                for (Bus b : buses) {
                    if (bus.bus_id.equalsIgnoreCase(b.bus_id)) {
                        buses.remove(b);
                        System.out.println("Hello hello");
                        break;
                    }
                }
                System.out.println("Replacing bus right now. Bus id: " + bus.bus_id);
                buses.add(bus);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        vbMain.setBackground(background);
        vbMain.setPrefHeight(screen.getHeight());
        vbMain.setPrefWidth(screen.getWidth());
        hbSubMap.setPrefHeight(screen.getHeight());
        hbSubMap.setPrefWidth(screen.getWidth());
        vbDetails.setPrefWidth(screen.getWidth());
        vbDetails.setPrefHeight(screen.getHeight());
        hbTap.setPrefHeight(screen.getHeight());
        hbTap.setPrefWidth(screen.getWidth());

        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(60), () -> {
            vbDetails.setVisible(false);
            pMap.setVisible(true);
            pMap.setOpacity(0.25);
            pTapToStart.setVisible(true);
            vbMain.setBackground(background);
        }, true);
        idleMonitor.register(vbDetails, Event.ANY);

        tcBusCompany.setCellValueFactory(new PropertyValueFactory<>("busCompany"));
        tcBusType.setCellValueFactory(new PropertyValueFactory<>("busType"));
        tcWingArea.setCellValueFactory(new PropertyValueFactory<>("wingArea"));
        tcBayNumber.setCellValueFactory(new PropertyValueFactory<>("bayNumber"));
        tcDestination.setCellValueFactory(new PropertyValueFactory<>("finDestination"));
        tcFirstTrip.setCellValueFactory(new PropertyValueFactory<>("departure"));
        tcLastTrip.setCellValueFactory(new PropertyValueFactory<>("lastTrip"));
        tcTime.setCellValueFactory(new PropertyValueFactory<>("nextTime"));
        tcMaxFare.setCellValueFactory(new PropertyValueFactory<>("fares"));

        tvBusDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                newValue = oldValue;
            }
            String ivPath = "src/res/TerminalMap/GIF/";
            switch (newValue.getWingArea()) {
                case "Left Wing":
                    ivPath = ivPath.concat("LW-");
                    break;
                case "Center Wing":
                    ivPath = ivPath.concat("CW-");
                    break;
                default:
                    ivPath = ivPath.concat("RW-");
            }
            ivPath = ivPath.concat(newValue.getBayNumber() + ".gif");
            ivTerminalPath.setImage(new Image(new File(ivPath).toURI().toString()));
        });
    }

    @FXML
    public static void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void handleBacolodSelected() {
        System.out.println("bacolod selected");
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Bacolod")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleTuburanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Tuburan")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleAsturiasSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Asturias")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleBalambanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Balamban")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleToledoCitySelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Toledo City")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handlePinamungajanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Pinamungajan")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleAloguinsanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Aloguinsan")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleBariliSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Barili")) {
                handleMunicipalitySelected(m);

                break;
            }
        }
    }

    @FXML
    public void handleDumanjugSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Dumanjug")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleRondaSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Ronda")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleAlcantaraSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Alcantara")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleMoalboalSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Moalboal")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleBadianSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Badian")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleAlegriaSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Alegria")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleMalabuyocSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Malabuyoc")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleGinatilanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Ginatilan")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleSamboanSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Samboan")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleSantanderSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Santander")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleOslobSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Oslob")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleBoljoonSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Boljoon")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleAlcoySelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Alcoy")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleDalagueteSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Dalaguete")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleArgaoSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Argao")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleSibongaSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Sibonga")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleCarcarCitySelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Carcar City")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleDumagueteSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Dumaguete")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    @FXML
    public void handleZamboangaSelected() {
        for (Municipality m : municipalities) {
            if (m.getTheName().equalsIgnoreCase("Zamboanga")) {
                handleMunicipalitySelected(m);
                break;
            }
        }
    }

    private void handleMunicipalitySelected(Municipality municipality) {
        //FULL INITIALIZER
        for (Municipality m : municipalities) {
            Municipality leftMun = null, rightMun = null, subroute = null;
            Municipality[] encompassingMunicipality = new Municipality[15];

            String left = m.left_mun;
            String right = m.right_mun;
            String subR = m.subroute;
            boolean subrouteFound = false;
            for (Municipality n : municipalities) {
                if (n.toString().equalsIgnoreCase(left)) {
                    leftMun = n;
                } else if (n.toString().equalsIgnoreCase(right)) {
                    rightMun = n;
                }
                if (!subrouteFound && n.getTheName().equalsIgnoreCase(subR)) {
                    subroute = n;
                    subrouteFound = true;
                }
            }
            ObservableList<Municipality> encompassed = FXCollections.observableArrayList();
            String enc = m.encompassed_mun;
            System.out.println("At full initializer: " + enc + " is encompassed list of " + m);
            String muni = "";
            for (int j = 0; j < enc.length(); j++) {

                if (enc.charAt(j) == ';') {
                    for (Municipality n : municipalities) {
                        if (muni.equalsIgnoreCase(n.getTheName())) {
                            encompassed.add(n);
                            break;
                        }
                    }
                    muni = "";
                    j++;
                } else {
                    muni = muni.concat(enc.charAt(j) + "");
                }
            }
            m.setLeftMun(leftMun);
            m.setRightMun(rightMun);
            m.setSubrouteArea(subroute);

            int j = 0;
            for (Municipality n : encompassed) {
                encompassingMunicipality[j++] = n;
            }
            m.setEncompassingMunicipality(encompassingMunicipality);
        }

        for (Bus bus : buses) {
            for (Municipality m : municipalities) {
                if (bus.destination.equalsIgnoreCase(m.toString())) {
                    bus.setFinDestination(m);
                    break;
                }
            }
        }
        //END OF FULL INITIALIZER

        vbMain.setBackground(vbBackground);
        pMap.setVisible(false);
        vbDetails.setVisible(true);
        vbDetails.setLayoutX(0);
        vbDetails.setLayoutY(0);

        lDestination.setText(municipality.toString());
        lPrev.setText(municipality.getLeftMun().getTheName());
        lNext.setText(municipality.getRightMun().getTheName());

        System.out.println("Entering qualifier: " + "(municipality: " + municipality + ")");
        qualifier.clear();
        for (Bus bus : buses) {
            System.out.println("Bus " + bus.bus_id + ": " + bus.company + " | " + bus.type);
            Municipality destination = bus.getFinDestination();
            System.out.println(bus.getFinDestination() + " is destination.");
            if (destination.toString().contains(municipality.toString())) {
                qualifier.add(bus);
                System.out.println("Qualified by automation");
            } else if (destination.getEncompassingMunicipality()[0] != null) {
                for (Municipality m : destination.getEncompassingMunicipality()) {
                    System.out.println("Checking the nature of " + destination + " having " + m + " as encompassed.");
                    if (m != null && m.toString().equalsIgnoreCase(municipality.toString())) {
                        qualifier.add(bus);
                        System.out.println("Qualified by inspection");
                    } else if (m == null) {
                        break;
                    }
                }
            }
        }
        //qualifier = bubbleSort(qualifier);
        tvBusDetails.setItems(qualifier);
        tvBusDetails.setFocusTraversable(true);

        String ivPath = "src/res/TerminalMap/GIF/";
        switch (qualifier.get(0).getWingArea()) {
            case "Left Wing":
                ivPath = ivPath.concat("LW-");
                break;
            case "Center Wing":
                ivPath = ivPath.concat("CW-");
                break;
            default:
                ivPath = ivPath.concat("RW-");
        }
        ivPath = ivPath.concat(qualifier.get(0).getBayNumber() + ".gif");
        ivTerminalPath.setImage(new Image(new File(ivPath).toURI().toString()));

        taTravelDistance.setText(municipality.getTravelDistance());
        taTravelTime.setText(municipality.getTravelTime());
        if (municipality.getFareAircon() == 0) {
            taFareAircon.setText("-");
        } else {
            taFareAircon.setText(municipality.getFareAircon() + "");
        }
        if (municipality.getFareOrdinary() == 0) {
            taFareOrdinary.setText("-");
        } else {
            taFareOrdinary.setText(municipality.getFareOrdinary() + "");
        }
    }

    @FXML
    private void handleBackToMap() {
        vbDetails.setVisible(false);
        pMap.setVisible(true);
        pMap.setOpacity(1.0);
        vbMain.setBackground(background);
    }

    @FXML
    private void handlePrevClicked() {
        handleNaviClicked(lPrev.getText());
    }

    @FXML
    private void handleNextClicked() {
        handleNaviClicked(lNext.getText());
    }

    private void handleNaviClicked(String muni) {
        for (Municipality m : municipalities) {
            if (muni.equalsIgnoreCase(m.toString())) {
                handleMunicipalitySelected(m);
            }
        }
    }

    @FXML
    private void handleStartClicked() {
        pTapToStart.setVisible(false);
        pMap.setOpacity(1.0);
    }/*

    private ObservableList<Bus> bubbleSort(ObservableList<Bus> buses) {
        int n = buses.size();
        Bus temp;
        Bus[] arr = new Bus[50];
        for (int i = 0; i < buses.size(); i++) {
            arr[i] = buses.get(i);
        }
        *//*for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                double left, right;
                if (arr[j - 1].getNextTime().equalsIgnoreCase("Arrived")) {
                    left = 0;
                } else {
                    left = arr[j-1].nearestTime();
                }
                if (arr[j].getNextTime().equalsIgnoreCase("Arrived")) {
                    right = 0;
                } else {
                    right = arr[j].nearestTime();
                }
                if (left > right) {
                    //swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }*//*
        buses.clear();
        for (Bus b : arr) {
            if (b != null) {
                buses.add(b);
            }
        }
        return buses;
    }*/
}
