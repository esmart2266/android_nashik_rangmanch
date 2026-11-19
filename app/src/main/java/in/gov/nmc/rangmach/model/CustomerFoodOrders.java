package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Generated("jsonschema2pojo")
public class CustomerFoodOrders implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("center_name")
    @Expose
    private String centerName;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("ispaid")
    @Expose
    private String ispaid;
    @SerializedName("order_date_time")
    @Expose
    private String orderDateTime;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_date_time")
    @Expose
    private String paymentDateTime;
    @SerializedName("products")
    @Expose
    private List<FoodOrder_Products> products = new ArrayList<FoodOrder_Products>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIspaid() {
        return ispaid;
    }

    public void setIspaid(String ispaid) {
        this.ispaid = ispaid;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(String paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public List<FoodOrder_Products> getProducts() {
        return products;
    }

    public void setProducts(List<FoodOrder_Products> products) {
        this.products = products;
    }

}