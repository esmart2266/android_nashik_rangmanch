
package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Booking_Refund implements Serializable {

    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("mode_details")
    @Expose
    private String modeDetails;
    @SerializedName("entry_date")
    @Expose
    private String entryDate;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("isConfirmedByCustomer")
    @Expose
    private String isConfirmedByCustomer;


    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }


    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsConfirmedByCustomer() {
        return isConfirmedByCustomer;
    }

    public void setIsConfirmedByCustomer(String isConfirmedByCustomer) {
        this.isConfirmedByCustomer = isConfirmedByCustomer;
    }

}