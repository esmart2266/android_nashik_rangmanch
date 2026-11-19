package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReschedulePojo implements Serializable {

    @SerializedName("event_date")
    @Expose
    private String event_date;
    @SerializedName("slot_id")
    @Expose
    private String slot_id;
    @SerializedName("event_type")
    @Expose
    private String event_type;
    @SerializedName("event_capacity")
    @Expose
    private String event_capacity;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("approved_by")
    @Expose
    private String approved_by;
    @SerializedName("approved_datetime")
    @Expose
    private String approved_datetime;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("event_type_title")
    @Expose
    private String event_type_title;
    @SerializedName("event_capacity_title")
    @Expose
    private String event_capacity_title;
    @SerializedName("slot_title")
    @Expose
    private String slot_title;

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_capacity() {
        return event_capacity;
    }

    public void setEvent_capacity(String event_capacity) {
        this.event_capacity = event_capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(String approved_by) {
        this.approved_by = approved_by;
    }

    public String getApproved_datetime() {
        return approved_datetime;
    }

    public void setApproved_datetime(String approved_datetime) {
        this.approved_datetime = approved_datetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEvent_type_title() {
        return event_type_title;
    }

    public void setEvent_type_title(String event_type_title) {
        this.event_type_title = event_type_title;
    }

    public String getEvent_capacity_title() {
        return event_capacity_title;
    }

    public void setEvent_capacity_title(String event_capacity_title) {
        this.event_capacity_title = event_capacity_title;
    }




    public String getSlot_title() {
        return slot_title;
    }

    public void setSlot_title(String slot_title) {
        this.slot_title = slot_title;
    }


}
