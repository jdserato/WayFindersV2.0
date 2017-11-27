/**
 * Created by Serato, Jay Vince on November 01, 2017.
 */
public class Municipality {
    private String name;
    private Municipality subroute;
    private int fareOrdinary;
    private int fareAircon;
    private Municipality[] encompassingMunicipality;
    private Municipality leftMun, rightMun;
    private String travelTime, travelDistance;

    public Municipality() {}

    public Municipality(String name, int fareOrdinary, int fareAircon, Municipality[] encompassingMunicipality, Municipality leftMun, Municipality rightMun, String travelTime, String travelDistance) {
        this(name, null, fareOrdinary, fareAircon, encompassingMunicipality, leftMun, rightMun, travelTime, travelDistance);
    }

    public Municipality(String name, Municipality subroute, int fareOrdinary, int fareAircon, Municipality[] encompassingMunicipality, Municipality leftMun, Municipality rightMun, String travelTime, String travelDistance) {
        this.name = name;
        this.subroute = subroute;
        this.fareOrdinary = fareOrdinary;
        this.fareAircon = fareAircon;
        this.encompassingMunicipality = encompassingMunicipality;
        this.leftMun = leftMun;
        this.rightMun = rightMun;
        this.travelDistance = travelDistance;
        this.travelTime = travelTime;
    }

    public Municipality getSubroute() {
        return subroute;
    }

    public void setSubroute(Municipality subroute) {
        this.subroute = subroute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (subroute != null) {
            return String.valueOf(name + " (via " + subroute + ")");
        } else {
            return name;
        }
    }
}
