package in.gov.nmc.rangmach.model;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Theater_Hall_Timing implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("center_hall_id")
    @Expose
    private String centerHallId;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("selected_timing")
    @Expose
    private String selectedTiming;
    @SerializedName("isActive")
    @Expose
    private String isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterHallId() {
        return centerHallId;
    }

    public void setCenterHallId(String centerHallId) {
        this.centerHallId = centerHallId;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSelectedTiming() {
        return selectedTiming;
    }

    public void setSelectedTiming(String selectedTiming) {
        this.selectedTiming = selectedTiming;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}