package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Generated("jsonschema2pojo")
public class Booking_Penalty_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("allPenalty")
    @Expose
    private List<Booking_Penalty> allPanelty = new ArrayList<Booking_Penalty>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking_Penalty> getAllPanelty() {
        return allPanelty;
    }

    public void setAllPanelty(List<Booking_Penalty> allPanelty) {
        this.allPanelty = allPanelty;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}