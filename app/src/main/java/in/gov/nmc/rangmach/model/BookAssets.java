package in.gov.nmc.rangmach.model;

  import javax.annotation.Generated;
  import com.google.gson.annotations.Expose;
  import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class BookAssets {

    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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