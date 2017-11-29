import com.google.firebase.database.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Serato, Jay Vince on November 07, 2017.
 */
public class Management_Controller implements Initializable{
    private DatabaseReference database;
    private DatabaseReference dRef;

    public ComboBox<String> cbBus, cbDestination, cbType;
    public Button btnSearch, btnSave, btnBack;
    public ImageView ivAddBus;
    public TextField tfCompany, tfType, tfDestination, tfFirstDep, tfLastDep, tfTrips, tfBuses, tfFare, tfWingArea, tfBayNumber;
    public VBox vbMainPane, vbEditBuses, vbDetail1_1, vbDetail1_2, vbDetail2_1, vbDetail2_2;
    public HBox hbDetail, hbEditBus;
    public Label lMaxFare;
    public ComboBox<String> cbTypeFin, cbDestinationFin, cbWingArea, cbBayNumber;

    private ObservableList<Bus> buses = FXCollections.observableArrayList();
    private ObservableList<Municipality> municipalities = FXCollections.observableArrayList();
    private ObservableList<Announcement> announcements = FXCollections.observableArrayList();
    private ObservableList<FAQ> faq = FXCollections.observableArrayList();
    private Bus busQueriest;

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
                cbDestination.setDisable(false);
                cbType.setDisable(true);
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
                announcements.add(announcement);
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

        dRef = database.child("faq");
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //faq.clear();
                FAQ faqs = dataSnapshot.getValue(FAQ.class);
                faq.add(faqs);
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
                        System.out.println("HELLO");
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
    }

    @FXML
    private void handleBackClicked() {
        vbEditBuses.setVisible(false);
        vbMainPane.setVisible(true);
        for (Node n : vbDetail2_2.getChildren()) {
            n.setVisible(true);
        }
        for (Node n : vbDetail2_1.getChildren()) {
            n.setVisible(true);
        }
        vbDetail1_2.setVisible(true);
        vbDetail1_1.setVisible(true);
        for (Node n : hbEditBus.getChildren()) {
            n.setVisible(true);
        }
    }

    @FXML
    private void handleEditBusClicked() {
        hbEditBus.setVisible(true);
        vbEditBuses.setVisible(true);
        hbDetail.setVisible(true);
        btnSave.setVisible(true);
        vbMainPane.setVisible(false);
        tfCompany.setText("");
        tfFirstDep.setText("");
        tfLastDep.setText("");
        tfTrips.setText("");
        tfBuses.setText("");
        tfFare.setText("");
        btnSave.setText("Save");
        hbDetail.setDisable(true);
    }

    @FXML
    private void handleAddBusClicked() {
        vbMainPane.setVisible(false);
        vbEditBuses.setVisible(true);
        hbEditBus.setVisible(false);
        hbDetail.setVisible(true);
        btnSave.setVisible(true);
        btnSave.setText("Add");
        tfCompany.setEditable(true);
        tfCompany.setText("");
        tfBayNumber.setText("");
        tfDestination.setText("");
        tfType.setText("");
        tfFirstDep.setText("");
        tfLastDep.setText("");
        tfTrips.setText("");
        tfBuses.setText("");
        tfFare.setText("");
        tfWingArea.setText("");
    }

    @FXML
    private void handleAddClicked() {
        String query = "INSERT INTO bus_info(destination, type, first_departure, last_trip, no_of_trips, no_of_buses, fare, wing_area, bay_num, company) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = this.connect()){
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, tfDestination.getText());
            pstmt.setString(2, tfType.getText());
            pstmt.setString(3, tfFirstDep.getText());
            pstmt.setString(4, tfLastDep.getText());
            pstmt.setInt(5, Integer.parseInt(tfTrips.getText()));
            pstmt.setInt(6, Integer.parseInt(tfBuses.getText()));
            pstmt.setInt(7, Integer.parseInt(tfFare.getText()));
            pstmt.setString(8, tfWingArea.getText());
            pstmt.setInt(9, Integer.parseInt(tfBayNumber.getText()));
            pstmt.setString(10, tfCompany.getText());

            if (pstmt.executeUpdate() == 1) {
                Municipality sel = null;
                for (Municipality m : municipalities) {
                    if (tfDestination.getText().equalsIgnoreCase(m.toString())) {
                        sel = m;
                        break;
                    }
                }

                //buses.add(new Bus(Integer.parseInt(tfBayNumber.getText()),tfCompany.getText(), tfType.getText(), sel, tfFirstDep.getText(), tfLastDep.getText(), Integer.parseInt(tfTrips.getText()), Integer.parseInt(tfBuses.getText()), Integer.parseInt(tfFare.getText()), tfWingArea.getText() , new List<Date>(){}, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditClicked() {
        if (!btnSave.getText().equalsIgnoreCase("Save Fares")) {
            tfCompany.setText(cbBus.getValue());
            cbDestinationFin.setPromptText(cbDestination.getValue());
            ObservableList<String> munis = FXCollections.observableArrayList();
            for (Municipality m : municipalities) {
                if (!munis.contains(m.toString())) {
                    munis.add(m.toString());
                }
            }
            cbDestinationFin.setItems(munis);
            ObservableList<String> types = FXCollections.observableArrayList("Aircon", "Ordinary / Aircon", "Ordinary");
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

        } else {
            //dRef = database.child("bus_info").child(id);

        }
        btnSave.setVisible(true);
        hbDetail.setVisible(true);
        if (!btnSave.getText().equalsIgnoreCase("Delete")) {
            hbDetail.setDisable(false);
        }
    }

    @FXML
    private void handleEditFaresClicked() {
        vbMainPane.setVisible(false);
        vbEditBuses.setVisible(true);
        hbEditBus.setVisible(true);
        hbDetail.setDisable(true);
        cbBus.setVisible(false);
        cbDestination.setDisable(false);
        cbType.setDisable(true);
        btnSearch.setDisable(true);
        btnSave.setText("Save Fares");
        ObservableList<String> municipalityWO = FXCollections.observableArrayList();
        ObservableList<String> type = FXCollections.observableArrayList("Aircon", "Ordinary");
        vbDetail1_1.setVisible(false);
        vbDetail1_2.setVisible(false);
        for (Node n : vbDetail2_1.getChildren()) {
            n.setVisible(false);
        }
        for (Node n : vbDetail2_2.getChildren()) {
            n.setVisible(false);
        }
        tfFare.setVisible(true);
        lMaxFare.setVisible(true);

        for (Municipality m : municipalities) {
            if (!municipalityWO.contains(m.toString())) {
                municipalityWO.add(m.toString());
            }
        }
        cbDestination.setItems(municipalityWO);
        cbType.setItems(type);
    }

    @FXML
    private void handleDeleteBusClicked() {
        this.handleEditBusClicked();
        btnSave.setText("Delete");
    }

    @FXML
    private void handleSaveClicked() {
        String _bus_type = cbTypeFin.getValue();
        if (_bus_type == null) {
            _bus_type = cbTypeFin.getPromptText();
        }
        //String

        dRef = database.child("bus_info").child(busQueriest.bus_id);
        dRef.child("type").setValue(_bus_type);
        Municipality d = null;
        for (Municipality m : municipalities) {
            if (cbDestinationFin.getPromptText().equalsIgnoreCase(m.toString())) {
                d = m;
                break;
            }
        }
        //dRef.setValue(null);
        Bus newBus = new Bus(cbBayNumber.getPromptText(),tfCompany.getText(), _bus_type, d.toString(), tfFirstDep.getText(), tfLastDep.getText(), tfTrips.getText(), tfBuses.getText(), tfFare.getText(), cbWingArea.getPromptText(), busQueriest.bus_id);
        //dRef.setValue(newBus);
        //handleSaveClicked(3);
    }

    private void handleSaveClicked(int times) {
        String _bus_type = cbTypeFin.getValue();
        if (_bus_type == null) {
            _bus_type = cbTypeFin.getPromptText();
        }
        System.out.println("Bus type is " + _bus_type);

        dRef = database.child("bus_info").child(busQueriest.bus_id);
        dRef.child("type").setValue(_bus_type);
        Municipality d = null;
        for (Municipality m : municipalities) {
            if (cbDestinationFin.getPromptText().equalsIgnoreCase(m.toString())) {
                d = m;
                break;
            }
        }
        //dRef.setValue(null);
        Bus newBus = new Bus(cbBayNumber.getPromptText(),tfCompany.getText(), _bus_type, d.toString(), tfFirstDep.getText(), tfLastDep.getText(), tfTrips.getText(), tfBuses.getText(), tfFare.getText(), cbWingArea.getPromptText(), busQueriest.bus_id);
        //dRef.setValue(newBus);
        if (times > 0) {
            handleSaveClicked(--times);
        }
    }

    private Connection connect() {
        String url = "jdbc:sqlite:src/sample/bus.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }
}
