package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 9/23/2017.
 */

public class ManagementData {
    String title;
    String due;
    String rent;



    public ManagementData() {

    }

    public ManagementData(String title, String due, String rent) {
        this.title = title;
        this.due = due;
        this.rent = rent;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}
