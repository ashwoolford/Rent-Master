package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/17/2017.
 */

public class FlatRentDataR {

    String title;
    String rent;

    public FlatRentDataR() {
    }

    public FlatRentDataR(String title, String rent) {
        this.title = title;
        this.rent = rent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String  getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}
