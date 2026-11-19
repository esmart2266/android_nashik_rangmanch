package in.gov.nmc.rangmach.model;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

@Generated("jsonschema2pojo")
public class MyAssetsOrder_Data implements Serializable {

    @SerializedName("Data")
    @Expose
    private List<MyAssetsOrder> data = new ArrayList<MyAssetsOrder>();
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public List<MyAssetsOrder> getData() {
        return data;
    }

    public void setData(List<MyAssetsOrder> data) {
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