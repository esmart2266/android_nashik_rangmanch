package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Booking_Payment_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("allPayments")
    @Expose
    private List<Booking_Payment> allPayments = new ArrayList<Booking_Payment>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Booking_Payment> getAllPayments() {
        return allPayments;
    }

    public void setAllPayments(List<Booking_Payment> allPayments) {
        this.allPayments = allPayments;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}