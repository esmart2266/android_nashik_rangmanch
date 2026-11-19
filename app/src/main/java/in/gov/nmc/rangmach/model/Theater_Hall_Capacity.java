package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Theater_Hall_Capacity implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("capacity")
    @Expose
    private String capacity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}

