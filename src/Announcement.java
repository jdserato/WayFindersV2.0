/**
 * Created by Serato, Jay Vince on November 27, 2017.
 */
public class Announcement {
    private int theId;
    public String id;
    private String announcement;

    public Announcement(String id, String announcement) {
        this.id = id;
        this.announcement = announcement;
    }

    public Announcement() {
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public int getTheId() {
        return Integer.parseInt(id);
    }

    public void setTheId(int theId) {
        this.theId = theId;
    }
}
