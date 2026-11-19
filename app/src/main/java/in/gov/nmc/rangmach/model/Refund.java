package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Refund implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("mode_details")
    @Expose
    private String modeDetails;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("entry_date_time")
    @Expose
    private String entryDateTime;

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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getModeDetails() {
        return modeDetails;
    }

    public void setModeDetails(String modeDetails) {
        this.modeDetails = modeDetails;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

}