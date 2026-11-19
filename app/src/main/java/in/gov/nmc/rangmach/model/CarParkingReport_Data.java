package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Generated("jsonschema2pojo")
public class CarParkingReport_Data implements Serializable {

    @SerializedName("Data")
    @Expose
    private List<CarParkingReport> data = new ArrayList<CarParkingReport>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public List<CarParkingReport> getData() {
        return data;
    }

    public void setData(List<CarParkingReport> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}
