import com.google.firebase.database.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Serato, Jay Vince on November 07, 2017.
 */
public class Management_Controller implements Initializable{
    private DatabaseReference database;
    private DatabaseReference dRef;

    public ComboBox<String> cbBus, cbDestination, cbType, cbMunicipalities, cbArrived;
    public Button btnSearch, btnSave, btnBack, btnDelete, btnCancelFare, btnSaveFare, btnAnnSave, btnAnnDelete;
    public ImageView ivAddBus, ivHeader, ivAnnRight, ivAnnLeft, ivAnnQue;
    public TextField tfCompany, tfFirstDep, tfLastDep, tfTrips, tfBuses, tfFare, tfFareOrdinary, tfFareAircon, tfQuestion;
    public TextArea taAnnouncement;
    public VBox vbMainPane, vbEditBuses, vbDetail1_1, vbDetail1_2, vbDetail2_1, vbDetail2_2, vbFares, vbAnnouncements, vbArrival;
    public HBox hbDetail, hbEditBus, hbFareAir, hbFareOrd;
    public Label lMaxFare, lHeader, lAnnQue;
    public ComboBox<String> cbTypeFin, cbDestinationFin, cbWingArea, cbBayNumber, cbWingArea1, cbBayNumber1;
    public Label lErrorMessage, lErrorMessage2, lErrorMessage3;
    private ObservableList<Bus> buses = FXCollections.observableArrayList();
    private ObservableList<Municipality> municipalities = FXCollections.observableArrayList();
    private ObservableList<Announcement> announcements = FXCollections.observableArrayList();
    private ObservableList<FAQ> faq = FXCollections.observableArrayList();
    private Bus busQueriest;
    private Municipality selectedFare;
    private Announcement selectedAnn;
    private FAQ selectedFAQ;
    private int arrayAnn;

    private Background error_background = new Background(new BackgroundFill(Color.RED, null, null));
    private Background save_background = new Background(new BackgroundFill(Color.WHITE, null, null));
    private Image addBus = new Image(new File("src/res/addBus.png").toURI().toString());
    private Image editBus = new Image(new File("src/res/editBus.png").toURI().toString());
    private Image annBus = new Image(new File("src/res/editAnnouncement.png").toURI().toString());
    private Image faqBus = new Image(new File("src/res/editFAQs.png").toURI().toString());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Bus> busQuery = FXCollections.observableArrayList();
        cbBus.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                busQuery.clear();
                if (newValue == null) {
                    newValue = oldValue;
                }
                hbDetail.setDisable(true);
                cbDestination.setValue("Destination");
                cbDestination.setDisable(false);
                cbType.setDisable(true);
                cbType.setPromptText("Type");
                btnSearch.setDisable(true);

                ObservableList<String> dest = FXCollections.observableArrayList();
                System.out.println(newValue + " is compared;");
                for (Bus bus : buses) {
                    System.out.println(bus.getBusCompany());
                    System.out.println(bus.getFinDestination() + " is dest");
                    if (bus.getBusCompany().equalsIgnoreCase(newValue) && !dest.contains(bus.getFinDestination().toString())) {
                        dest.add(bus.getFinDestination().toString());
                        busQuery.add(bus);
                    }
                }
                cbDestination.setItems(dest);
            }
        });
        ObservableList<Bus> busQuerier = FXCollections.observableArrayList();
        cbDestination.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                busQuerier.clear();
                if (newValue == null) {
                    newValue = oldValue;
                }
                cbType.setDisable(false);
                btnSearch.setDisable(true);
                ObservableList<String> ty = FXCollections.observableArrayList();
                for (Bus bus : busQuery) {
                    if (bus.getFinDestination().toString().equalsIgnoreCase(newValue) && !ty.contains(bus.getBusType())) {
                        ty.add(bus.getBusType());
                        busQuerier.add(bus);
                    }
                }
                if (!btnSave.getText().equalsIgnoreCase("Save Fares")) {
                    cbType.setItems(ty);
                }
            }
        });
        cbType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    newValue = oldValue;
                }
                for (Bus bus : busQuerier) {
                    if(bus.getBusType().equalsIgnoreCase(newValue)) {
                        busQueriest = bus;
                    }
                }
                btnSearch.setDisable(false);
            }
        });
        cbMunicipalities.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    newValue = oldValue;
                }
                for (Municipality m : municipalities) {
                    if (m.getTheName().equalsIgnoreCase(newValue)) {
                        tfFareAircon.setText(m.fare_aircon);
                        tfFareOrdinary.setText(m.fare_ordinary);
                        hbFareAir.setDisable(false);
                        hbFareOrd.setDisable(false);
                        selectedFare = m;
                    }
                }
            }
        });

        try {
            DatabaseHelper.initFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        database = FirebaseDatabase.getInstance().getReference();
        dRef = database.child("announcements");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Announcement announcement = dataSnapshot.getValue(Announcement.class);
                System.out.println("Let us welcome the new announcement having " + announcement.id + " as id!");
                if (announcement.id == null) {
                    announcement.id = announcements.get(announcements.size()) + "";
                }
                announcements.add(announcement);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Announcement a = dataSnapshot.getValue(Announcement.class);
                for (Announcement an : announcements) {
                    if (a.id.equalsIgnoreCase(an.id)) {
                        System.out.println("Management has changed announcement!");
                        announcements.remove(an);
                        break;
                    }
                }
                announcements.add(a);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Announcement a = dataSnapshot.getValue(Announcement.class);
                for (Announcement an : announcements) {
                    if (an.id.equalsIgnoreCase(a.id)) {
                        announcements.remove(an);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dRef = database.child("faq");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //faq.clear();
                FAQ faqs = dataSnapshot.getValue(FAQ.class);
                System.out.println("Let us welcome the new FAQ having " + faqs.id + " as id!");
                if (faqs.id == null) {
                    faqs.id = faq.get(faq.size()) + "";
                }
                faq.add(faqs);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                FAQ a = dataSnapshot.getValue(FAQ.class);
                for (FAQ an : faq) {
                    if (a.id.equalsIgnoreCase(an.id)) {
                        System.out.println("Management has changed FAQs!");
                        faq.remove(an);
                        break;
                    }
                }
                faq.add(a);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                FAQ a = dataSnapshot.getValue(FAQ.class);
                for (FAQ an : faq) {
                    if (an.id.equalsIgnoreCase(a.id)) {
                        faq.remove(an);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                    for (int j = 0; j < enc.length(); j++) {

                        if (enc.charAt(j) == ';' || j == enc.length() - 1) {
                            if (j == enc.length() - 1) {
                                muni = muni.concat(enc.charAt(j) + "");
                            }
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

                ObservableList<String> muns = FXCollections.observableArrayList();
                for (Municipality m : municipalities) {
                    if (!muns.contains(m.getTheName())) {
                        muns.add(m.getTheName());
                    }
                }
                cbMunicipalities.setItems(muns);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("Municipality data change detected in Management.");
                Municipality mun = dataSnapshot.getValue(Municipality.class);
                for (Municipality m : municipalities) {
                    if (m.id.equalsIgnoreCase(mun.id)) {
                        m.setFareOrdinary(Integer.parseInt(mun.fare_ordinary));
                        m.setFareAircon(Integer.parseInt(mun.fare_aircon));
                        System.out.println("Municipality data change applied in Management.");
                        break;
                    }
                }
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
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
                    }
                }
                buses.add(bus);
                ObservableList<String> allBusCom = FXCollections.observableArrayList();
                for (Bus b : buses) {
                    if (!allBusCom.contains(b.getBusCompany())) {
                        allBusCom.add(b.getBusCompany());
                    }
                }
                cbBus.setItems(allBusCom);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("Change detected in Management.");
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
                for (Municipality m : municipalities) {
                    if (m.toString().equalsIgnoreCase(bus.destination)) {
                        bus.setFinDestination(m);
                    }
                }
                for (Bus b : buses) {
                    if (bus.bus_id.equalsIgnoreCase(b.bus_id)) {
                        buses.remove(b);
                        System.out.println("Hello hello");
                        break;
                    }
                }
                System.out.println("Replacing bus right now. Bus id: " + bus.bus_id);
                buses.add(bus);

                ObservableList<String> allBusCom = FXCollections.observableArrayList();
                for (Bus b : buses) {
                    if (!allBusCom.contains(b.getBusCompany())) {
                        allBusCom.add(b.getBusCompany());
                    }
                }
                cbBus.setItems(allBusCom);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Bus bus = dataSnapshot.getValue(Bus.class);
                for (Bus b : buses) {
                    if (b.bus_id.equalsIgnoreCase(bus.bus_id)) {
                        System.out.println(b.getBusCompany() + " has been removed from management control.");
                        buses.remove(b);
                        break;
                    }
                }
                ObservableList<String> allBusCom = FXCollections.observableArrayList();
                for (Bus b : buses) {
                    if (!allBusCom.contains(b.getBusCompany())) {
                        allBusCom.add(b.getBusCompany());
                    }
                }
                cbBus.setItems(allBusCom);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @FXML
    private void handleBackClicked() {
        vbEditBuses.setVisible(false);
        vbFares.setVisible(false);
        vbAnnouncements.setVisible(false);
        vbMainPane.setVisible(true);
        cbBus.setValue(cbBus.getPromptText());
        cbBus.setDisable(false);
        cbType.setValue("Type");
        cbType.setDisable(true);
        cbDestination.setValue("Destination");
        cbDestination.setDisable(true);
        tfCompany.setText("");
        tfFare.setText("");
        tfTrips.setText("");
        tfLastDep.setText("");
        tfFirstDep.setText("");
        cbWingArea.setPromptText("Wing Area");
        cbBayNumber.setPromptText("Bay Number");
        cbTypeFin.setPromptText("Bus Type");
        cbDestinationFin.setPromptText("Destination");
        cbMunicipalities.setPromptText("Select one...");
        tfFareOrdinary.setText("");
        tfFareAircon.setText("");
    }

    @FXML
    private void handleEditBusClicked() {
        vbArrival.setVisible(false);
        ivHeader.setImage(editBus);
        lHeader.setText("Edit Bus Information");
        hbEditBus.setVisible(true);
        vbEditBuses.setVisible(true);
        hbDetail.setVisible(true);
        btnSave.setVisible(true);
        vbMainPane.setVisible(false);
        cbBus.setVisible(true);
        cbBus.setDisable(false);
        cbBus.setValue("Bus Company");
        cbType.setValue("Type");
        cbDestination.setValue("Destination");
        cbType.setDisable(true);
        tfCompany.setText("");
        tfFirstDep.setText("");
        tfLastDep.setText("");
        tfTrips.setText("");
        tfBuses.setText("");
        tfFare.setText("");
        btnSave.setText("Save");
        hbDetail.setDisable(true);
        btnSearch.setDisable(true);
        btnDelete.setVisible(true);
        btnDelete.setDisable(true);
        lErrorMessage.setText("");
        hbEditBus.setDisable(false);
    }

    @FXML
    private void handleAddBusClicked() {
        vbArrival.setVisible(false);
        ivHeader.setImage(addBus);
        lHeader.setText("Add Bus");
        vbMainPane.setVisible(false);
        vbEditBuses.setVisible(true);
        hbEditBus.setVisible(false);
        hbDetail.setVisible(true);
        btnSave.setVisible(true);
        hbDetail.setDisable(false);
        btnSave.setText("Add");
        tfCompany.setEditable(true);
        tfCompany.setText("");
        cbBayNumber.setValue("Bay Number");
        cbDestinationFin.setValue("Destination");
        ObservableList<String> allMuns = FXCollections.observableArrayList();
        for (Municipality m : municipalities) {
            allMuns.add(m.toString());
        }
        cbDestinationFin.setItems(allMuns);
        cbTypeFin.setValue("Bus Type");
        tfFirstDep.setText("");
        tfLastDep.setText("");
        tfTrips.setText("");
        tfBuses.setText("");
        tfFare.setText("");
        cbWingArea.setValue("Wing Area");
        busQueriest = null;
        ObservableList<String> types = FXCollections.observableArrayList("Aircon", "Aircon / Ordinary", "Ordinary");
        cbTypeFin.setItems(types);
        ObservableList<String> wingAreas = FXCollections.observableArrayList("Center Wing", "Left Wing", "Right Wing");
        cbWingArea.setItems(wingAreas);
        ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
        cbBayNumber.setItems(bayNumbers);
        cbWingArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    newValue = oldValue;
                }
                ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
                if (newValue.equalsIgnoreCase("Center Wing")) {
                    bayNumbers.addAll("7", "8", "9", "10", "11", "12");
                } else if (newValue.equalsIgnoreCase("Left Wing")) {
                    bayNumbers.remove("1");
                    //cbBayNumber.setPromptText("2");
                    cbBayNumber.setValue("2");
                }
                cbBayNumber.setItems(bayNumbers);
            }
        });
        btnSave.setDisable(false);
        btnDelete.setVisible(false);
    }

    @FXML
    private void handleEditClicked() {
        if (!lHeader.getText().equalsIgnoreCase("Arrivals")) {
            btnSave.setDisable(false);
            btnDelete.setDisable(false);
            tfCompany.setText(cbBus.getValue());
            cbDestinationFin.setPromptText(cbDestination.getValue());
            ObservableList<String> munis = FXCollections.observableArrayList();
            for (Municipality m : municipalities) {
                if (!munis.contains(m.toString())) {
                    munis.add(m.toString());
                }
            }
            cbDestinationFin.setItems(munis);
            ObservableList<String> types = FXCollections.observableArrayList("Aircon", "Aircon / Ordinary", "Ordinary");
            cbTypeFin.setItems(types);
            cbTypeFin.setPromptText(busQueriest.getBusType());
            tfFirstDep.setText(busQueriest.getDeparture());
            tfLastDep.setText(busQueriest.getLastTrip());
            tfTrips.setText(busQueriest.getTrips() + "");
            tfBuses.setText(busQueriest.getBuses() + "");
            tfFare.setText(busQueriest.getFares() + "");
            cbWingArea.setPromptText(busQueriest.getWingArea());
            ObservableList<String> wingAreas = FXCollections.observableArrayList("Center Wing", "Left Wing", "Right Wing");
            cbWingArea.setItems(wingAreas);
            cbBayNumber.setPromptText(busQueriest.getBayNumber() + "");
            ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
            if (busQueriest.getWingArea().equalsIgnoreCase("Center Wing")) {
                bayNumbers.addAll("7", "8", "9", "10", "11", "12");
            } else if (busQueriest.getWingArea().equalsIgnoreCase("Left Wing")) {
                bayNumbers.remove("1");
            }
            cbBayNumber.setItems(bayNumbers);
            cbWingArea.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue == null) {
                        newValue = oldValue;
                    }
                    ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
                    if (newValue.equalsIgnoreCase("Center Wing")) {
                        bayNumbers.addAll("7", "8", "9", "10", "11", "12");
                    } else if (newValue.equalsIgnoreCase("Left Wing")) {
                        bayNumbers.remove("1");
                        cbBayNumber.setPromptText("2");
                        cbBayNumber.setValue("2");
                    }
                    cbBayNumber.setItems(bayNumbers);
                }
            });

            btnSave.setVisible(true);
            hbDetail.setVisible(true);
            hbDetail.setDisable(false);
        } else {
            cbWingArea1.setPromptText(busQueriest.getWingArea());
            ObservableList<String> wingAreas = FXCollections.observableArrayList("Center Wing", "Left Wing", "Right Wing");
            cbWingArea1.setItems(wingAreas);
            cbBayNumber1.setPromptText(busQueriest.getBayNumber() + "");
            ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
            if (busQueriest.getWingArea().equalsIgnoreCase("Center Wing")) {
                bayNumbers.addAll("7", "8", "9", "10", "11", "12");
            } else if (busQueriest.getWingArea().equalsIgnoreCase("Left Wing")) {
                bayNumbers.remove("1");
            }
            cbBayNumber1.setItems(bayNumbers);
            cbWingArea1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (newValue == null) {
                        newValue = oldValue;
                    }
                    ObservableList<String> bayNumbers = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6");
                    if (newValue.equalsIgnoreCase("Center Wing")) {
                        bayNumbers.addAll("7", "8", "9", "10", "11", "12");
                    } else if (newValue.equalsIgnoreCase("Left Wing")) {
                        bayNumbers.remove("1");
                        cbBayNumber1.setPromptText("2");
                        cbBayNumber1.setValue("2");
                    }
                    cbBayNumber1.setItems(bayNumbers);
                }
            });
            cbArrived.setPromptText(busQueriest.arrived);
            cbArrived.setItems(FXCollections.observableArrayList("No", "Yes"));
            btnDelete.setVisible(false);
            btnSave.setDisable(false);
        }
    }

    @FXML
    private void handleEditFaresClicked() {
        vbMainPane.setVisible(false);
        vbFares.setVisible(true);
    }

    @FXML
    private void handleEditFAQClicked() {
        ivAnnQue.setImage(faqBus);
        lAnnQue.setText("Edit FAQs");
        tfQuestion.setVisible(true);
        arrayAnn = 0;
        vbMainPane.setVisible(false);
        vbAnnouncements.setVisible(true);
        ivAnnLeft.setDisable(true);
        if (!faq.isEmpty()) {
            taAnnouncement.setText(faq.get(0).getAnswer());
            tfQuestion.setText(faq.get(0).getQuestion());
            selectedFAQ = faq.get(0);
            arrayAnn = 0;
            ivAnnRight.setDisable(false);
        } else {
            btnAnnDelete.setDisable(true);
            selectedFAQ = null;
            arrayAnn = -1;
            ivAnnRight.setDisable(true);
        }
        btnDelete.setDisable(true);
    }

    @FXML
    private void handleAnnouncementsClicked() {
        ivAnnQue.setImage(annBus);
        lAnnQue.setText("Edit Announcements");
        tfQuestion.setVisible(false);
        arrayAnn = 0;
        vbMainPane.setVisible(false);
        vbAnnouncements.setVisible(true);
        ivAnnLeft.setDisable(true);
        taAnnouncement.setDisable(false);
        btnAnnSave.setDisable(false);
        if (!announcements.isEmpty()) {
            taAnnouncement.setText(announcements.get(0).getAnnouncement());
            selectedAnn = announcements.get(0);
            arrayAnn = 0;
            ivAnnRight.setDisable(false);
        } else {
            taAnnouncement.setText("");
            btnAnnDelete.setDisable(true);
            selectedAnn = null;
            arrayAnn = -1;
            ivAnnRight.setDisable(true);
        }
    }

    @FXML
    private void handleLeftBtnAnnClicked() {
        if (lAnnQue.getText().contains("Announcements")) {
            if (selectedAnn == null) {
                selectedAnn = announcements.get(announcements.size() - 1);
                taAnnouncement.setText(selectedAnn.getAnnouncement());
                ivAnnRight.setDisable(false);
                btnAnnSave.setDisable(false);
                taAnnouncement.setDisable(false);
                btnAnnDelete.setDisable(false);
            } else if (selectedAnn.getTheId() > 0 && arrayAnn - 1 >= Integer.parseInt(announcements.get(0).id) && arrayAnn != 0) {
                selectedAnn = announcements.get(--arrayAnn);
                taAnnouncement.setText(selectedAnn.getAnnouncement());
                ivAnnRight.setDisable(false);
                btnAnnSave.setDisable(false);
                taAnnouncement.setDisable(false);
                btnAnnDelete.setDisable(false);
            } else {
                ivAnnLeft.setDisable(true);
            }
        } else {
            if (selectedFAQ == null) {
                selectedFAQ = faq.get(faq.size() - 1);
                taAnnouncement.setText(selectedFAQ.getAnswer());
                tfQuestion.setText(selectedFAQ.getQuestion());
                ivAnnRight.setDisable(false);
                btnAnnSave.setDisable(false);
                taAnnouncement.setDisable(false);
                tfQuestion.setDisable(false);
                btnAnnDelete.setDisable(false);
            } else if (selectedFAQ.getTheId() > 0 && arrayAnn - 1 > Integer.parseInt(faq.get(0).id) && arrayAnn != 0) {
                selectedFAQ = faq.get(--arrayAnn);
                taAnnouncement.setText(selectedFAQ.getAnswer());
                tfQuestion.setText(selectedFAQ.getQuestion());
                ivAnnRight.setDisable(false);
                btnAnnSave.setDisable(false);
                tfQuestion.setDisable(false);
                taAnnouncement.setDisable(false);
                if (!taAnnouncement.getText().contains("IMAGE")) {
                    btnAnnDelete.setDisable(false);
                } else {
                    btnAnnDelete.setDisable(true);
                }
            } else {
                ivAnnLeft.setDisable(true);
            }
        }
    }

    @FXML
    private void handleRightBtnAnnClicked() {
        if (lAnnQue.getText().contains("Announcements")) {
            if (arrayAnn < announcements.size() - 1) {
                selectedAnn = announcements.get(++arrayAnn);
                taAnnouncement.setText(selectedAnn.getAnnouncement());
                ivAnnLeft.setDisable(false);
                taAnnouncement.setDisable(false);
            } else {
                taAnnouncement.setText("Add announcement.");
                btnAnnDelete.setDisable(true);
                ivAnnRight.setDisable(true);
                ivAnnLeft.setDisable(false);
                selectedAnn = null;
            }
        } else {
            if (arrayAnn < faq.size() - 1) {
                selectedFAQ = faq.get(++arrayAnn);
                taAnnouncement.setText(selectedFAQ.getAnswer());
                tfQuestion.setText(selectedFAQ.getQuestion());
                ivAnnLeft.setDisable(false);
                taAnnouncement.setDisable(false);
                tfQuestion.setDisable(false);
                if (!taAnnouncement.getText().contains("IMAGE")) {
                    btnAnnDelete.setDisable(false);
                } else {
                    btnAnnDelete.setDisable(true);
                }
            } else {
                taAnnouncement.setText("Add answer.");
                tfQuestion.setText("Add question.");
                btnAnnDelete.setDisable(true);
                ivAnnRight.setDisable(true);
                ivAnnLeft.setDisable(false);
                selectedFAQ = null;
            }
        }
    }

    @FXML
    private void handleSaveAnnClicked() {
        if (lAnnQue.getText().contains("Announcements")) {
            int lastAnn = -2;
            if (selectedAnn == null) {
                if (announcements.isEmpty()) {
                    lastAnn = -1;
                } else {
                    for (Announcement a : announcements) {
                        if (a.getTheId() > lastAnn) {
                            lastAnn = a.getTheId();
                        }
                    }
                }
                selectedAnn = new Announcement((lastAnn + 1) + "", "");
                System.out.println("You attempted to occupy " + (lastAnn + 1));
            }
            try {
                if (!selectedAnn.getAnnouncement().equalsIgnoreCase(taAnnouncement.getPromptText()) || taAnnouncement.getText().isEmpty()) {
                    if (taAnnouncement.getText().equalsIgnoreCase(taAnnouncement.getPromptText()) || taAnnouncement.getText().isEmpty()) {
                        throw new Exception("Please enter an announcement.");
                    }
                    dRef = database.child("announcements").child(selectedAnn.id);
                    dRef.child("announcement").setValue(taAnnouncement.getText());
                    if (lastAnn + 1 >= 0) {
                        dRef.child("id").setValue((lastAnn + 1) + "");
                    }
                    btnAnnDelete.setDisable(false);
                    ivAnnRight.setDisable(false);
                    arrayAnn++;
                    selectedAnn.setAnnouncement(taAnnouncement.getText());
                }
            } catch (Exception e) {
                lErrorMessage3.setText(e.getMessage());
                lErrorMessage3.setTextFill(Color.RED);
                selectedAnn = null;
            }
        } else {
            int lastAnn = -2;
            if (selectedFAQ == null) {
                if (faq.isEmpty()) {
                    lastAnn = -1;
                } else {
                    for (FAQ a : faq) {
                        if (a.getTheId() > lastAnn) {
                            lastAnn = a.getTheId();
                        }
                    }
                }
                selectedFAQ= new FAQ((lastAnn + 1) + "", "", "");
                System.out.println("You attempted to occupy " + (lastAnn + 1));
            }
            try {
                if (!selectedFAQ.getAnswer().equalsIgnoreCase("Add answer.") || taAnnouncement.getText().isEmpty() || !selectedFAQ.getQuestion().equalsIgnoreCase(tfQuestion.getPromptText()) || tfQuestion.getText().isEmpty()) {
                    if (taAnnouncement.getText().equalsIgnoreCase("Add answer.") || taAnnouncement.getText().isEmpty()) {
                        throw new Exception("Please enter an answer.");
                    }
                    if (tfQuestion.getText().equalsIgnoreCase(tfQuestion.getPromptText()) || tfQuestion.getText().isEmpty()) {
                        throw new Exception("Please enter a question.");
                    }
                    dRef = database.child("faq").child(selectedFAQ.id);
                    dRef.child("answer").setValue(taAnnouncement.getText());
                    dRef.child("question").setValue(tfQuestion.getText());
                    if (lastAnn + 1 >= 0) {
                        dRef.child("id").setValue((lastAnn + 1) + "");
                    }
                    btnAnnDelete.setDisable(false);
                    ivAnnRight.setDisable(false);
                    arrayAnn++;
                    selectedFAQ.setAnswer(taAnnouncement.getText());
                    selectedFAQ.setQuestion(tfQuestion.getText());
                }
            } catch (Exception e) {
                lErrorMessage3.setText(e.getMessage());
                lErrorMessage3.setTextFill(Color.RED);
                selectedFAQ = null;
            }
        }
    }

    @FXML
    private void handleDeleteAnnClicked() {
        if (lAnnQue.getText().contains("Announcements")) {
            dRef = database.child("announcements").child(selectedAnn.id);
        } else {
            dRef = database.child("faq").child(selectedFAQ.id);
            tfQuestion.setDisable(true);
        }
        dRef.setValue(null);
        taAnnouncement.setDisable(true);
        btnAnnDelete.setDisable(true);
        btnAnnSave.setDisable(true);
        ivAnnLeft.setDisable(false);
    }

    @FXML
    private void handleArrivalsClicked() {
        lHeader.setText("Arrivals");
        ivHeader.setImage(new Image(new File("src/res/arrivals2.png").toURI().toString()));
        vbMainPane.setVisible(false);
        vbEditBuses.setVisible(true);
        hbDetail.setVisible(false);
        vbArrival.setVisible(true);
    }

    @FXML
    private void handleSaveClicked() {
        if (!lHeader.getText().contains("Arrivals")) {
            int highest = 0;
            if (busQueriest == null) {
                for (Bus b : buses) {
                    if (b.getId() > highest) {
                        highest = b.getId();
                    }
                }
                busQueriest = new Bus((highest + 1) + "", "", "", "", "", "", "", "", "", "", "", "");
                dRef = database.child("bus_info").child((highest + 1) + "");
                System.out.println("I can see you're adding at " + (highest + 1));
            } else {
                dRef = database.child("bus_info").child(busQueriest.bus_id);
                System.out.println("Referencing at " + busQueriest.bus_id);
            }
            try {
                if (btnSave.getText().equalsIgnoreCase("Add")) {
                    String _company = tfCompany.getText().trim();
                    if (_company.isEmpty()) {
                        tfCompany.setBackground(error_background);
                        throw new Exception("Please specify name of bus company.");
                    }
                    tfCompany.setBackground(save_background);
                }
                String _bus_type = cbTypeFin.getValue();
                if (_bus_type != null) {
                    if (_bus_type.equalsIgnoreCase(cbTypeFin.getPromptText())) {
                        cbTypeFin.setBackground(error_background);
                        throw new Exception("Please indicate bus type.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("type").setValue(_bus_type);
                    }
                    cbTypeFin.setBackground(save_background);
                }
                String _destination = cbDestinationFin.getValue();
                if (_destination != null) {
                    if (_destination.equalsIgnoreCase(cbDestinationFin.getPromptText())) {
                        cbDestinationFin.setBackground(error_background);
                        throw new Exception("Please specify bus destination");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("destination").setValue(_destination);
                    }
                    cbDestinationFin.setBackground(save_background);
                }
                String _wing_area = cbWingArea.getValue();
                if (_wing_area != null) {
                    if (_wing_area.equalsIgnoreCase(cbWingArea.getPromptText())) {
                        cbWingArea.setBackground(error_background);
                        throw new Exception("Please specify wing area assigned.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("wing_area").setValue(_wing_area);
                    }
                    cbWingArea.setBackground(save_background);
                }
                String _bay_number = cbBayNumber.getValue();
                if (_bay_number != null) {
                    if (_bay_number.equalsIgnoreCase(cbBayNumber.getPromptText())) {
                        cbBayNumber.setBackground(error_background);
                        throw new Exception("Please enter bay number assigned.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("bay_num").setValue(_bay_number);
                    }
                    cbBayNumber.setBackground(save_background);
                }
                String _first_departure = tfFirstDep.getText();
                if (!_first_departure.equals(busQueriest.first_departure) || _first_departure.isEmpty()) {
                    _first_departure = _first_departure.trim();
                    tfFirstDep.setBackground(error_background);
                    inspectTime(_first_departure);
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("first_departure").setValue(_first_departure);
                    }
                    tfFirstDep.setBackground(save_background);
                } else {
                    tfFirstDep.setBackground(save_background);
                }
                String _last_departure = tfLastDep.getText();
                if (!_last_departure.equals(busQueriest.last_trip) || _last_departure.isEmpty()) {
                    _last_departure = _last_departure.trim();
                    tfLastDep.setBackground(error_background);
                    inspectTime(_last_departure);
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("last_departure").setValue(_last_departure);
                    }
                    tfLastDep.setBackground(save_background);
                } else {
                    tfLastDep.setBackground(save_background);
                }
                String _num_trips = tfTrips.getText();
                if (!_num_trips.equalsIgnoreCase(busQueriest.no_of_trips) || _num_trips.isEmpty()) {
                    _num_trips = _num_trips.trim();
                    int t = Integer.parseInt(_num_trips);
                    if (t < 1 || _num_trips.isEmpty()) {
                        tfTrips.setBackground(error_background);
                        throw new Exception("Number of trips must be 1 or more.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("no_of_trips").setValue(_num_trips);
                    }
                    tfTrips.setBackground(save_background);
                } else {
                    tfTrips.setBackground(save_background);
                }
                String _num_buses = tfBuses.getText();
                if (!_num_buses.equalsIgnoreCase(busQueriest.no_of_buses) || _num_buses.isEmpty()) {
                    _num_buses = _num_buses.trim();
                    int t = Integer.parseInt(_num_buses);
                    if (t < 1 || _num_buses.isEmpty()) {
                        tfBuses.setBackground(error_background);
                        throw new Exception("Number of buses must be 1 or more.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("num_of_buses").setValue(_num_buses);
                    }
                    tfBuses.setBackground(save_background);
                } else {
                    tfBuses.setBackground(save_background);
                }
                String _fare = tfFare.getText();
                if (!_fare.equalsIgnoreCase(busQueriest.fare) || _fare.isEmpty()) {
                    _fare = _fare.trim();
                    int t = Integer.parseInt(_fare);
                    if (t < 1 || _fare.isEmpty()) {
                        tfFare.setBackground(error_background);
                        throw new Exception("Fare must be more than 0.");
                    }
                    if (btnSave.getText().equalsIgnoreCase("Save")) {
                        dRef.child("fare").setValue(_fare);
                    }
                    tfFare.setBackground(save_background);
                } else {
                    tfFare.setBackground(save_background);
                }

                lErrorMessage.setText("Saved successfully.");
                lErrorMessage.setTextFill(Color.GREEN);
                if (btnSave.getText().equalsIgnoreCase("Add")) {
                    dRef.child("type").setValue(_bus_type);
                    dRef.child("destination").setValue(_destination);
                    dRef.child("fare").setValue(_fare);
                    dRef.child("no_of_buses").setValue(_num_buses);
                    dRef.child("no_of_trips").setValue(_num_trips);
                    dRef.child("last_departure").setValue(_last_departure);
                    dRef.child("first_departure").setValue(_first_departure);
                    dRef.child("wing_area").setValue(_wing_area);
                    dRef.child("bay_num").setValue(_bay_number);
                    dRef.child("bus_id").setValue((highest + 1) + "");
                    dRef.child("company").setValue(tfCompany.getText().toUpperCase());
                    dRef.child("arrived").setValue("No");
                }
            } catch (Exception e) {
                lErrorMessage.setText(e.getMessage());
                lErrorMessage.setTextFill(Color.RED);
                if (btnSave.getText().equalsIgnoreCase("Add")) {
                    busQueriest = null;
                }
            }
        } else {
            try {
                dRef = database.child("bus_info").child(busQueriest.bus_id);
                String _wing_area = cbWingArea1.getValue();
                if (_wing_area != null) {
                    dRef.child("wing_area").setValue(_wing_area);
                    cbWingArea1.setBackground(save_background);
                }
                String _bay_number = cbBayNumber1.getValue();
                if (_bay_number != null) {
                    dRef.child("bay_num").setValue(_bay_number);
                    cbBayNumber.setBackground(save_background);
                }
                String _arrived = cbArrived.getValue();
                if (_arrived != null) {
                    dRef.child("arrived").setValue(_arrived);
                }
            } catch (Exception e) {
                lErrorMessage.setText(e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteClicked() {
        dRef = database.child("bus_info");
        dRef.child(busQueriest.bus_id).setValue(null);
        lErrorMessage.setText("Delete successful");
        lErrorMessage.setTextFill(Color.DARKGOLDENROD);
        //hbDetail.setDisable(true);
        cbBus.setValue("Bus Company");
        cbBus.setDisable(false);
        cbType.setValue("Type");
        cbType.setDisable(true);
        cbDestination.setValue("Destination");
        cbDestination.setDisable(true);
        btnSearch.setDisable(true);
        hbEditBus.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    private void handleSaveFareClicked() {
        try {
            if (tfFareAircon.getText().isEmpty()) {
                throw new Exception("Please specify fare for Aircon.");
            }
            int aircon = Integer.parseInt(tfFareAircon.getText());
            if (aircon <= 0) {
                throw new Exception("Fares must be above 0");
            }
            if (tfFareOrdinary.getText().isEmpty()) {
                throw new Exception("Please specify fare for Ordinary.");
            }
            int ordinary = Integer.parseInt(tfFareOrdinary.getText());
            if (ordinary <= 0) {
                throw new Exception("Fares must be above 0");
            }

            dRef = database.child("municipality_info").child(selectedFare.id);
            dRef.child("fare_aircon").setValue(tfFareAircon.getText());
            dRef.child("fare_ordinary").setValue(tfFareOrdinary.getText());

            hbFareAir.setDisable(true);
            hbFareOrd.setDisable(true);
        } catch (Exception e) {
            lErrorMessage2.setText(e.getMessage());
        }
    }

    private void inspectTime(String _timeString) throws Exception {
        if (!_timeString.contains(":")) {
            throw new Exception("No colon (please use HH:mm [am/pm] format)");
        }
        if (!_timeString.contains("am") && !_timeString.contains("pm")) {
            throw new Exception("Doesn't end in am or pm");
        }
        int i = 0;
        String hr = "", min = "";
        for (; _timeString.charAt(i) != ':'; i++) {
            if (_timeString.charAt(i) < '0' || _timeString.charAt(i) > '9') {
                throw new Exception("Not a numerical value of hour");
            } else {
                hr = hr.concat(_timeString.charAt(i) + "");
            }
        }
        if (Integer.parseInt(hr) < 1 || Integer.parseInt(hr) > 12) {
            throw new Exception("Hour should be above 0 and below or equal to 12.");
        }
        for(i++; _timeString.charAt(i) != ' '; i++) {
            if (_timeString.charAt(i) < '0' || _timeString.charAt(i) > '9') {
                throw new Exception("Not a numerical value of minute");
            } else {
                min = min.concat(_timeString.charAt(i) + "");
            }
        }
        if (Integer.parseInt(min) >= 60) {
            throw new Exception("Minute should be above or equal to 0 and below 60.");
        }
    }
}
