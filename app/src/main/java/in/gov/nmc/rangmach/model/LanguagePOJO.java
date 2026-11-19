package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LanguagePOJO {

    @SerializedName("English")
    @Expose
    private String english;
    @SerializedName("Marathi")
    @Expose
    private String marathi;
    @SerializedName("Hindi")
    @Expose
    private String hindi;
    @SerializedName("Key")
    @Expose
    private String key;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getMarathi() {
        return marathi;
    }

    public void setMarathi(String marathi) {
        this.marathi = marathi;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}