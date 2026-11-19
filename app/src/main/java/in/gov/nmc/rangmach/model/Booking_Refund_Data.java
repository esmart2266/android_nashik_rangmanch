package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Booking_Refund_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("allRefunds")
    @Expose
    private List<Booking_Refund> allRefunds = new ArrayList<Booking_Refund>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking_Refund> getAllRefunds() {
        return allRefunds;
    }

    public void setAllRefunds(List<Booking_Refund> allRefunds) {
        this.allRefunds = allRefunds;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}