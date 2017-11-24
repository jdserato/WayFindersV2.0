/**
 * Created by Serato, Jay Vince on October 17, 2017.
 */
public class Company {
    private String name;

    Company (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
