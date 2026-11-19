package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Penalty implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("penaulty_for")
    @Expose
    private String penaultyFor;
    @SerializedName("entry_date_time")
    @Expose
    private String entryDateTime;

    @SerializedName("payment_date")
    @Expose
    private String payment_date;
    @SerializedName("payment_mode")
    @Expose
    private String payment_mode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPenaultyFor() {
        return penaultyFor;
    }

    public void setPenaultyFor(String penaultyFor) {
        this.penaultyFor = penaultyFor;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }


    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }


    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

}