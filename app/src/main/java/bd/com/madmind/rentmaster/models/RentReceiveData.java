package bd.com.madmind.rentmaster.models;

/**
 * Created by ash on 8/17/2017.
 */

public class RentReceiveData {

    String curntMonth ,due , additionalB ;

    public RentReceiveData(String curntMonth, String due, String additionalB) {
        this.curntMonth = curntMonth;
        this.due = due;
        this.additionalB = additionalB;
    }

    public String getCurntMonth() {
        return curntMonth;
    }

    public void setCurntMonth(String curntMonth) {
        this.curntMonth = curntMonth;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getAdditionalB() {
        return additionalB;
    }

    public void setAdditionalB(String additionalB) {
        this.additionalB = additionalB;
    }
}
