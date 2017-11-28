/**
 * Created by Serato, Jay Vince on November 01, 2017.
 */
public class Municipality {
    private String theName;
    private Municipality subrouteArea;
    private int fareOrdinary;
    private int fareAircon;
    private Municipality[] encompassingMunicipality;
    private Municipality leftMun;
    private Municipality rightMun;
    private String travelTime;
    private String travelDistance;

    public String encompassed_mun, fare_aircon, fare_ordinary, left_mun, name, right_mun, subroute, travel_distance, travel_time;

    public Municipality() {}

    public Municipality(String theName, int fareOrdinary, int fareAircon, Municipality[] encompassingMunicipality, Municipality leftMun, Municipality rightMun, String travelTime, String travelDistance) {
        this(theName, null, fareOrdinary, fareAircon, encompassingMunicipality, leftMun, rightMun, travelTime, travelDistance);
    }

    public Municipality(String theName, Municipality subroute, int fareOrdinary, int fareAircon, Municipality[] encompassingMunicipality, Municipality leftMun, Municipality rightMun, String travelTime, String travelDistance) {
        this.theName = theName;
        this.subrouteArea = subroute;
        this.fareOrdinary = fareOrdinary;
        this.fareAircon = fareAircon;
        this.encompassingMunicipality = encompassingMunicipality;
        this.leftMun = leftMun;
        this.rightMun = rightMun;
        this.travelDistance = travelDistance;
        this.travelTime = travelTime;
    }

    public Municipality getSubrouteArea() {
        return subrouteArea;
    }

    public void setSubrouteArea(Municipality subrouteArea) {
        this.subrouteArea = subrouteArea;
    }

    public String getTheName() {
        return theName;
    }

    public void setTheName(String theName) {
        this.theName = theName;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(String travelDistance) {
        this.travelDistance = travelDistance;
    }

    public int getFareOrdinary() {
        return fareOrdinary;
    }

    public void setFareOrdinary(int fareOrdinary) {
        this.fareOrdinary = fareOrdinary;
    }

    public int getFareAircon() {
        return fareAircon;
    }

    public void setFareAircon(int fareAircon) {
        this.fareAircon = fareAircon;
    }

    public Municipality[] getEncompassingMunicipality() {
        return encompassingMunicipality;
    }

    public void setEncompassingMunicipality(Municipality[] encompassingMunicipality) {
        this.encompassingMunicipality = encompassingMunicipality;
    }

    public Municipality getLeftMun() {
        return leftMun;
    }

    public void setLeftMun(Municipality leftMun) {
        this.leftMun = leftMun;
    }

    public Municipality getRightMun() {
        return rightMun;
    }

    public void setRightMun(Municipality rightMun) {
        this.rightMun = rightMun;
    }

    public String toString() {
        if (subrouteArea != null) {
            return String.valueOf(theName + " (via " + subrouteArea + ")");
        } else {
            return theName;
        }
    }
}
