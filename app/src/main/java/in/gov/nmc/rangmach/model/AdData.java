package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdData {
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Data")
    @Expose
    public List<Ad> data = null;

    @SerializedName("Centers")
    @Expose
    public List<Centers> centersList = null;

    @SerializedName("Responsecode")
    @Expose
    public long responsecode;
    @SerializedName("ShowParking_Menu")
    @Expose
    public String showparking_menu;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        this.data = data;
    }

    public List<Centers> getCentersList() {
        return centersList;
    }

    public void setCentersList(List<Centers> centersList) {
        this.centersList = centersList;
    }

    public long getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(long responsecode) {
        this.responsecode = responsecode;
    }

    public String getShowparking_menu() {
        return showparking_menu;
    }

    public void setShowparking_menu(String showparking_menu) {
        this.showparking_menu = showparking_menu;
    }
}
