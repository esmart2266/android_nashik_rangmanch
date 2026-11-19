package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("jsonschema2pojo")
public class Theater_Booking_Documents implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("document_id")
    @Expose
    private String document_id;
    @SerializedName("item_id")
    @Expose
    private String item_id;


    @SerializedName("filePath")
    @Expose
    private String filePath;

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public String getItem_id() {
        return id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTiming() {
        return path;
    }

    public void setTiming(String path) {
        this.path = path;
    }

    public String getSelectedTiming() {
        return isApproved;
    }

    public void setSelectedTiming(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsActive() {
        return remarks;
    }

    public void setIsActive(String remarks) {
        this.remarks = remarks;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}