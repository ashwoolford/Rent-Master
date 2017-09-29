package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/12/2017.
 */

public class TenantsList {

    String name , address , profileuri;

    public TenantsList() {

    }

    public TenantsList(String name, String address, String profileuri) {
        this.name = name;
        this.address = address;
        this.profileuri = profileuri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileuri() {
        return profileuri;
    }

    public void setProfileuri(String profileuri) {
        this.profileuri = profileuri;
    }
}
