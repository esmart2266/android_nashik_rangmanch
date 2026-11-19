package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class CarParking implements Serializable {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("max_per_person_count")
    @Expose
    private String maxPerPersonCount;
    @SerializedName("show_vip")
    @Expose
    private int showVip;
    @SerializedName("bike_qty")
    @Expose
    private int bikeQty;
    @SerializedName("bike_price")
    @Expose
    private float bikePrice;
    @SerializedName("car_qty")
    @Expose
    private int carQty;
    @SerializedName("car_price")
    @Expose
    private float carPrice;
    @SerializedName("bus_qty")
    @Expose
    private int busQty;
    @SerializedName("bus_price")
    @Expose
    private float busPrice;
    @SerializedName("vip_qty")
    @Expose
    private int vipQty;
    @SerializedName("vip_price")
    @Expose
    private float vipPrice;
    @SerializedName("gst")
    @Expose
    private float gst;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMaxPerPersonCount() {
        return maxPerPersonCount;
    }

    public void setMaxPerPersonCount(String maxPerPersonCount) {
        this.maxPerPersonCount = maxPerPersonCount;
    }

    public int getShowVip() {
        return showVip;
    }

    public void setShowVip(int showVip) {
        this.showVip = showVip;
    }

    public int getBikeQty() {
        return bikeQty;
    }

    public void setBikeQty(int bikeQty) {
        this.bikeQty = bikeQty;
    }

    public float getBikePrice() {
        return bikePrice;
    }

    public void setBikePrice(float bikePrice) {
        this.bikePrice = bikePrice;
    }

    public int getCarQty() {
        return carQty;
    }

    public void setCarQty(int carQty) {
        this.carQty = carQty;
    }

    public float getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(float carPrice) {
        this.carPrice = carPrice;
    }

    public int getBusQty() {
        return busQty;
    }

    public void setBusQty(int busQty) {
        this.busQty = busQty;
    }

    public float getBusPrice() {
        return busPrice;
    }

    public void setBusPrice(float busPrice) {
        this.busPrice = busPrice;
    }

    public int getVipQty() {
        return vipQty;
    }

    public void setVipQty(int vipQty) {
        this.vipQty = vipQty;
    }

    public float getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(float vipPrice) {
        this.vipPrice = vipPrice;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}