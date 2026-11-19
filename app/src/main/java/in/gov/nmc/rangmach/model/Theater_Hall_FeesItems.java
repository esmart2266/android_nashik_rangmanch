package in.gov.nmc.rangmach.model;

import java.io.Serializable;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Theater_Hall_FeesItems implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("odd_day_rent")
    @Expose
    private String oddDayRent;
    @SerializedName("holiday_rent")
    @Expose
    private String holidayRent;
    @SerializedName("deposit")
    @Expose
    private String deposit;
    @SerializedName("isActive")
    @Expose
    private String isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOddDayRent() {
        return oddDayRent;
    }

    public void setOddDayRent(String oddDayRent) {
        this.oddDayRent = oddDayRent;
    }

    public String getHolidayRent() {
        return holidayRent;
    }

    public void setHolidayRent(String holidayRent) {
        this.holidayRent = holidayRent;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}