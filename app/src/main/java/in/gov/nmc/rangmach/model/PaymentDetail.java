package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class PaymentDetail implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("mode_details")
    @Expose
    private String modeDetails;
    @SerializedName("payment_date")
    @Expose
    private String paymentDatetime;

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getModeDetails() {
        return modeDetails;
    }

    public void setModeDetails(String modeDetails) {
        this.modeDetails = modeDetails;
    }

    public String getPaymentDatetime() {
        return paymentDatetime;
    }

    public void setPaymentDatetime(String paymentDatetime) {
        this.paymentDatetime = paymentDatetime;
    }

}