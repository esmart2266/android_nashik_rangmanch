package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class FoodProduct_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("FoodProduct")
    @Expose
    private List<FoodProduct> foodProduct = new ArrayList<FoodProduct>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FoodProduct> getFoodProduct() {
        return foodProduct;
    }

    public void setFoodProduct(List<FoodProduct> foodProduct) {
        this.foodProduct = foodProduct;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}