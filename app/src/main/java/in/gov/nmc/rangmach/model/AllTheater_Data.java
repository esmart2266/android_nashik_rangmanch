package in.gov.nmc.rangmach.model;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;
        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AllTheater_Data implements Serializable {

    @SerializedName("Data")
    @Expose
    private List<AllTheaters> data = new ArrayList<AllTheaters>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public List<AllTheaters> getData() {
        return data;
    }

    public void setData(List<AllTheaters> data) {
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