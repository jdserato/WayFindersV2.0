/**
 * Created by Serato, Jay Vince on October 12, 2017.
 */
public class Bus {
    private int id;
    private String busCompany;
    private String busType;
    private Municipality finDestination;
    private String departure;
    private String lastTrip;

    public String bay_num, bus_id, company, fare, destination, first_departure, last_trip, no_of_buses, no_of_trips, type, wing_area, arrived;

    private String wingArea;
    private int trips, buses, bayNumber, fares;

    public Bus() {}

    public Bus(int bayNumber, String busCompany, String type, Municipality destination, String departure, String lastTrip, int trips, int buses, int fares, String wingArea, int id) {
        this.busCompany = busCompany;
        this.bayNumber = bayNumber;
        this.fares = fares;
        this.wingArea = wingArea;
        this.busType = type;
        this.finDestination = destination;
        this.departure = departure;
        this.lastTrip = lastTrip;
        this.trips = trips;
        this.buses = buses;
        this.id = id;
    }

    public Bus(String bay_num, String company, String type, String destination, String first_departure, String last_trip, String no_of_trips, String no_of_buses, String fare, String wing_area, String bus_id, String arrived) {
        this.company = company;
        this.bay_num = bay_num;
        this.fare = fare;
        this.wing_area = wing_area;
        this.arrived = arrived;
        this.type = type;
        this.destination = destination;
        this.first_departure = first_departure;
        this.last_trip = last_trip;
        this.no_of_trips = no_of_trips;
        this.no_of_buses = no_of_buses;
        this.bus_id = bus_id;
    }

    public String getArrived() {
        return arrived;
    }

    public String getWingArea() {
        return wingArea;
    }

    public void setWingArea(String wingArea) {
        this.wingArea = wingArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBayNumber() {
        return bayNumber;
    }

    public void setBayNumber(int bayNumber) {
        this.bayNumber = bayNumber;
    }

    public int getFares() {
        return fares;
    }

    public void setFares(int fares) {
        this.fares = fares;
    }

    public String getBusCompany() {
        return busCompany;
    }

    public void setBusCompany(String busCompany) {
        this.busCompany = busCompany;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Municipality getFinDestination() {
        return finDestination;
    }

    public void setFinDestination(Municipality finDestination) {
        this.finDestination = finDestination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getLastTrip() {
        return lastTrip;
    }

    public void setLastTrip(String lastTrip) {
        this.lastTrip = lastTrip;
    }

    public int getTrips() {
        return trips;
    }

    public void setTrips(int trips) {
        this.trips = trips;
    }

    public int getBuses() {
        return buses;
    }

    public void setBuses(int buses) {
        this.buses = buses;
    }
}
