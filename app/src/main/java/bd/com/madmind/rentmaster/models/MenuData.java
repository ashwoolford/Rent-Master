package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/6/2017.
 */

public class MenuData {
    int id;
    int imageUrl;
    String title;

    public MenuData(int id, int imageUrl, String title) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}