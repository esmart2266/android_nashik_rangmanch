package in.gov.nmc.rangmach.model;

        import java.util.ArrayList;
        import java.util.List;
        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class NowShowing_Data {

    @SerializedName("Data")
    @Expose
    private List<NowShowing> data = new ArrayList<NowShowing>();
    @SerializedName("upcoming")
    @Expose
    private List<NowShowing> upcoming = new ArrayList<NowShowing>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public List<NowShowing> getData() {
        return data;
    }

    public void setData(List<NowShowing> data) {
        this.data = data;
    }

    public List<NowShowing> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<NowShowing> upcoming) {
        this.upcoming = upcoming;
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