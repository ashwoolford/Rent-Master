package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/12/2017.
 */

public class GetTenantData {

    String address , email , flat , name , project , nid , phone , profileuri;

    public GetTenantData() {

    }

    public GetTenantData(String address, String email, String flat, String name, String project, String nid, String phone, String profileuri) {
        this.address = address;
        this.email = email;
        this.flat = flat;
        this.name = name;
        this.project = project;
        this.nid = nid;
        this.phone = phone;
        this.profileuri = profileuri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileuri() {
        return profileuri;
    }

    public void setProfileuri(String profileuri) {
        this.profileuri = profileuri;
    }
}
