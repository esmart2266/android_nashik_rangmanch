package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mayur on 3/19/2017.
 */

public class VideoData {
    @SerializedName("Data")
    @Expose
    private List<Video> data = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private long responsecode;

    public List<Video> getData() {
        return data;
    }

    public void setData(List<Video> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(long responsecode) {
        this.responsecode = responsecode;
    }
}
