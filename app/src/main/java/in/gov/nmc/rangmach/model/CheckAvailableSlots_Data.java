package in.gov.nmc.rangmach.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CheckAvailableSlots_Data {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("documentlist")
    @Expose
    private List<Theater_Booking_Documents> documentlist;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Theater_Booking_Documents> getDocumentlist() {
        return documentlist;
    }

    public void setDocumentlist(List<Theater_Booking_Documents> documentlist) {
        this.documentlist = documentlist;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}