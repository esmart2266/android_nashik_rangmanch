package in.gov.nmc.rangmach.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Generated("jsonschema2pojo")
public class MyAssetsOrder implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("center_hall_id")
    @Expose
    private String center_hall_id;
    @SerializedName("center_id")
    @Expose
    private String center_id;
    @SerializedName("event_type")
    @Expose
    private String event_type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("requestDateTime")
    @Expose
    private String requestDateTime;
    @SerializedName("eventDate")
    @Expose
    private String eventDate;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_Status")
    @Expose
    private String paymentStatus;
    @SerializedName("refund_amount")
    @Expose
    private String refundAmount;
    @SerializedName("refund_reason")
    @Expose
    private String refundReason;
    @SerializedName("refund_datetime")
    @Expose
    private String refundDatetime;
    @SerializedName("items")
    @Expose
    private List<ListItems> items;

    @SerializedName("payment_details")
    @Expose
    private List<PaymentDetail> paymentDetails;
    @SerializedName("refund")
    @Expose
    private List<Refund> refund;
    @SerializedName("penalty")
    @Expose
    private List<Penalty> penalty;
    @SerializedName("documentsArray")
    @Expose
    private List<DocumentsPOJO> documentsArrays;
    @SerializedName("Cancellation")
    @Expose
    private CancellationPojo cancellation;
    @SerializedName("Reschedule")
    @Expose
    private ReschedulePojo reschedulePojo;
    @SerializedName("capacityArray")
    @Expose
    private List<Theater_Hall_Capacity> eventCapacityPojo = new ArrayList<Theater_Hall_Capacity>();
    @SerializedName("eventType")
    @Expose
    private List<Theater_Hall_EventType> eventTypePojo = new ArrayList<Theater_Hall_EventType>();
    @SerializedName("timings")
    @Expose
    private List<Theater_Hall_Timing> timings = new ArrayList<Theater_Hall_Timing>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCenter_hall_id() {
        return center_hall_id;
    }

    public void setCenter_hall_id(String center_hall_id) {
        this.center_hall_id = center_hall_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundDatetime() {
        return refundDatetime;
    }

    public void setRefundDatetime(String refundDatetime) {
        this.refundDatetime = refundDatetime;
    }

    public List<ListItems> getItems() {
        return items;
    }

    public void setItems(List<ListItems> items) {
        this.items = items;
    }

    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public List<Refund> getRefund() {
        return refund;
    }

    public void setRefund(List<Refund> refund) {
        this.refund = refund;
    }

    public List<Penalty> getPenalty() {
        return penalty;
    }

    public void setPenalty(List<Penalty> penalty) {
        this.penalty = penalty;
    }

    public List<DocumentsPOJO> getDocumentsArrays() {
        return documentsArrays;
    }

    public void setDocumentsArrays(List<DocumentsPOJO> documentsArrays) {
        this.documentsArrays = documentsArrays;
    }

    public CancellationPojo getCancellation() {
        return cancellation;
    }

    public void setCancellation(CancellationPojo cancellation) {
        this.cancellation = cancellation;
    }

    public ReschedulePojo getReschedulePojo() {
        return reschedulePojo;
    }

    public void setReschedulePojo(ReschedulePojo reschedulePojo) {
        this.reschedulePojo = reschedulePojo;
    }

    public List<Theater_Hall_Timing> getTimings() {
        return timings;
    }

    public void setTimings(List<Theater_Hall_Timing> timings) {
        this.timings = timings;
    }

    public List<Theater_Hall_Capacity> getCapacityArray() {
        return eventCapacityPojo;
    }

    public void setCapacityArray(List<Theater_Hall_Capacity> eventType) {
        this.eventCapacityPojo = eventType;
    }


    public List<Theater_Hall_EventType> getEventTypePojo() {
        return eventTypePojo;
    }

    public void setEventTypePojo(List<Theater_Hall_EventType> eventType) {
        this.eventTypePojo = eventType;
    }
}
