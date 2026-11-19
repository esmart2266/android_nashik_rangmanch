package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class CarParkingReport implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pid")
    @Expose
    private String pid;
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
    @SerializedName("entry_datetime")
    @Expose
    private String entryDatetime;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("bike_numbers")
    @Expose
    private String bikeNumbers;
    @SerializedName("car_numbers")
    @Expose
    private String carNumbers;
    @SerializedName("bus_numbers")
    @Expose
    private String busNumbers;
    @SerializedName("vip_numbers")
    @Expose
    private String vipNumbers;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("payment_datetime")
    @Expose
    private Object paymentDatetime;
    @SerializedName("payment_mode")
    @Expose
    private Object paymentMode;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(String entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getBikeNumbers() {
        return bikeNumbers;
    }

    public void setBikeNumbers(String bikeNumbers) {
        this.bikeNumbers = bikeNumbers;
    }

    public String getCarNumbers() {
        return carNumbers;
    }

    public void setCarNumbers(String carNumbers) {
        this.carNumbers = carNumbers;
    }

    public String getBusNumbers() {
        return busNumbers;
    }

    public void setBusNumbers(String busNumbers) {
        this.busNumbers = busNumbers;
    }

    public String getVipNumbers() {
        return vipNumbers;
    }

    public void setVipNumbers(String vipNumbers) {
        this.vipNumbers = vipNumbers;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public Object getPaymentDatetime() {
        return paymentDatetime;
    }

    public void setPaymentDatetime(Object paymentDatetime) {
        this.paymentDatetime = paymentDatetime;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}