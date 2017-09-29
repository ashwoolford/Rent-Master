package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/25/2017.
 */

public class GetFlatsData {
    String title;

    public GetFlatsData() {
    }

    public GetFlatsData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
