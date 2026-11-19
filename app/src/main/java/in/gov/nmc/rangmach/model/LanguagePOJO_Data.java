package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("jsonschema2pojo")
public class LanguagePOJO_Data {

    @SerializedName("data")
    @Expose
    private List<LanguagePOJO> data = new ArrayList<LanguagePOJO>();

    public List<LanguagePOJO> getData() {
        return data;
    }

    public void setData(List<LanguagePOJO> data) {
        this.data = data;
    }

}
