package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/26/2017.
 */

public class GetAletsData {

    String msg , uri , period;

    public GetAletsData() {
    }

    public GetAletsData(String msg, String uri , String period) {
        this.msg = msg;
        this.uri = uri;
        this.period = period;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
