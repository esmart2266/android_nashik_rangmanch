package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Theater_Hall_Challan implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("hsn_code")
    @Expose
    private String hsn_code;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;

   public int selectedQty = 0;

   public double total = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String capacity) {
        this.title = capacity;
    }

    public String getSubtitle() {
        return subtitle;
    }
    public void s(String capacity) {
        this.subtitle = capacity;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String capacity) {
        this.price = capacity;
    }

    public String getHsn_code() {
        return hsn_code;
    }
    public void setHsn_code(String capacity) {
        this.hsn_code = capacity;
    }

    public String getCgst() {
        return cgst;
    }
    public void setCgst(String capacity) {
        this.cgst = capacity;
    }


    public String getSgst() {
        return sgst;
    }
    public void setSgst(String capacity) {
        this.sgst = capacity;
    }
}
