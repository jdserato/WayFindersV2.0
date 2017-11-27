import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String nextTime;
    private Date[] setOfTimes;

    public String bay_num, bus_id, company, fare, destination, first_departure, last_trip, no_of_buses, no_of_trips, times, type, wing_area;

    private String wingArea;
    private int trips, buses, bayNumber, fares;

    public Bus() {}

    public Bus(int bayNumber, String busCompany, String type, Municipality destination, String departure, String lastTrip, int trips, int buses, int fares, String wingArea, Date[] times, int id) {
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
        this.setOfTimes = times;
        this.id = id;
    }

    public String getNextTime() {
        Calendar cal = Calendar.getInstance();
        double currTime = cal.getTime().getHours() + ((double) cal.getTime().getMinutes() / 100 * 1.67);
        DateFormat df = new SimpleDateFormat("HH:mm");
        for (Date timeOfBus : getSetOfTimes()) {
            if (timeOfBus == null) {
                break;
            }
            double timeBay = timeOfBus.getHours() + ((double) timeOfBus.getMinutes() / 100 * 1.67);
            if (currTime - 0.5 <= timeBay) {
                if (currTime >= timeBay) {
                    return "Arrived";
                } else {
                    return df.format(timeOfBus);
                }
            }
        }
        return df.format(getSetOfTimes()[0]);
    }

    public double nearestTime() {
        double currTime = Calendar.getInstance().getTime().getHours() + ((double) Calendar.getInstance().getTime().getMinutes() / 100 * 1.67);
        for (Date timeOfBus : getSetOfTimes()) {
            if (timeOfBus == null) {
                break;
            }
            double timeBay = timeOfBus.getHours() + ((double) timeOfBus.getMinutes() / 100 * 1.67);
            if (currTime <= timeBay) {
                return timeBay;
            }
        }
        return 0.0;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getWingArea() {
        return wingArea;
    }

    public void setWingArea(String wingArea) {
        this.wingArea = wingArea;
    }


    public Date[] getSetOfTimes() {
        return setOfTimes;
    }

    public void setSetOfTimes(Date[] setOfTimes) {
        this.setOfTimes = setOfTimes;
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
