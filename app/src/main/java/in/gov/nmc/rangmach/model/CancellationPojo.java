package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancellationPojo implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("request_datetime")
    @Expose
    private String request_datetime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("approved_datetime")
    @Expose
    private String approved_datetime;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequest_datetime() {
        return request_datetime;
    }

    public void setRequest_datetime(String request_datetime) {
        this.request_datetime = request_datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
