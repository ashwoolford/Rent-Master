package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/21/2017.
 */

public class GetRentReceiveData {

    String rent , title , due;
    boolean status;

    public GetRentReceiveData() {
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
