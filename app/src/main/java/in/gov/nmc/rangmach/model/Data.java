package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Data implements Parcelable {
    @SerializedName("id")
    @Expose
    public String userId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("mobile")
    @Expose
    public String mobileNo;
    @SerializedName("emailid")
    @Expose
    public String emailid;
    @SerializedName("isActive")
    @Expose
    public String isActive;
    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("govtId")
    @Expose
    public String govtId;
    @SerializedName("govtId_value")
    @Expose
    public String govtId_value;
    @SerializedName("gst_no")
    @Expose
    public String gst_no;
    @SerializedName("type")
    @Expose
    public String type;



    public String getemailid() {
        return emailid;
    }

    public void setemailid(String emailid) {
        this.emailid = emailid;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getGovtId() {
        return govtId;
    }

    public void setGovtId(String govtId) {
        this.govtId = govtId;
    }

    public String getGovtId_value() {
        return govtId_value;
    }

    public void setGovtId_value(String govtId_value) {
        this.govtId_value = govtId_value;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public Data() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.mobileNo);
        dest.writeString(this.emailid);
        dest.writeString(this.isActive);
        dest.writeString(this.address);
        dest.writeString(this.govtId);
        dest.writeString(this.govtId_value);
        dest.writeString(this.city);
        dest.writeString(this.gst_no);
        dest.writeString(this.type);
    }

    protected Data(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.mobileNo = in.readString();
        this.emailid = in.readString();
        this.isActive = in.readString();
        this.address = in.readString();
        this.govtId = in.readString();
        this.govtId_value = in.readString();
        this.city = in.readString();
        this.gst_no = in.readString();
        this.type = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };
}
