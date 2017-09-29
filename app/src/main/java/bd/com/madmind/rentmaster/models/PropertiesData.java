package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/11/2017.
 */

public class PropertiesData{
    String title , address , url;

    public PropertiesData() {

    }

    public PropertiesData(String title, String address, String url) {
        this.title = title;
        this.address = address;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
