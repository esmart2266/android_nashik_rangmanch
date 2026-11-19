package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CarParking_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<CarParking> data = new ArrayList<CarParking>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CarParking> getData() {
        return data;
    }

    public void setData(List<CarParking> data) {
        this.data = data;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}