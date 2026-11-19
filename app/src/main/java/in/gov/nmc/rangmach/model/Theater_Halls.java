package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Theater_Halls implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("center_id")
    @Expose
    private String centerId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("totalcapacity")
    @Expose
    private String totalcapacity;
    @SerializedName("balcony")
    @Expose
    private String balcony;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("ground_floor")
    @Expose
    private String groundFloor;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("creation_date_time")
    @Expose
    private String creationDateTime;
    @SerializedName("timings")
    @Expose
    private List<Theater_Hall_Timing> timings = new ArrayList<Theater_Hall_Timing>();
    @SerializedName("feesItems")
    @Expose
    private List<Theater_Hall_FeesItems> feesItems = new ArrayList<Theater_Hall_FeesItems>();
    @SerializedName("capacityArray")
    @Expose
    private List<Theater_Hall_Capacity> capacityArray = new ArrayList<Theater_Hall_Capacity>();
    @SerializedName("challan")
    @Expose
    private List<Theater_Hall_Challan> challan = new ArrayList<Theater_Hall_Challan>();
    @SerializedName("eventType")
    @Expose
    private List<Theater_Hall_EventType> eventType = new ArrayList<Theater_Hall_EventType>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTotalcapacity() {
        return totalcapacity;
    }

    public void setTotalcapacity(String totalcapacity) {
        this.totalcapacity = totalcapacity;
    }

    public String getBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGroundFloor() {
        return groundFloor;
    }

    public void setGroundFloor(String groundFloor) {
        this.groundFloor = groundFloor;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public List<Theater_Hall_Timing> getTimings() {
        return timings;
    }

    public void setTimings(List<Theater_Hall_Timing> timings) {
        this.timings = timings;
    }

    public List<Theater_Hall_FeesItems> getFeesItems() {
        return feesItems;
    }

    public void setFeesItems(List<Theater_Hall_FeesItems> feesItems) {
        this.feesItems = feesItems;
    }

    public List<Theater_Hall_Capacity> getCapacityArray() {
        return capacityArray;
    }

    public void setCapacityArray(List<Theater_Hall_Capacity> capacityArray) {
        this.capacityArray = capacityArray;
    }

    public List<Theater_Hall_Challan> getChallan() {
        return challan;
    }

    public void setChallan(List<Theater_Hall_Challan> capacityArray) {
        this.challan = capacityArray;
    }

    public List<Theater_Hall_EventType> getEventType() {
        return eventType;
    }

    public void setEventType(List<Theater_Hall_EventType> eventType) {
        this.eventType = eventType;
    }

}