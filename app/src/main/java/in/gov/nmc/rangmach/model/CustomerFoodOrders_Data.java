package in.gov.nmc.rangmach.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CustomerFoodOrders_Data implements Serializable {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("FoodProduct")
    @Expose
    private List<CustomerFoodOrders> foodProduct = new ArrayList<CustomerFoodOrders>();
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomerFoodOrders> getFoodProduct() {
        return foodProduct;
    }

    public void setFoodProduct(List<CustomerFoodOrders> foodProduct) {
        this.foodProduct = foodProduct;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}