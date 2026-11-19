package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class BankDetails implements Parcelable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("bankname")
    @Expose
    public String bankname;
    @SerializedName("account")
    @Expose
    public String account;
    @SerializedName("ifsc")
    @Expose
    public String ifsc;
    @SerializedName("branch")
    @Expose
    public String branch;
    @SerializedName("acc_type")
    @Expose
    public String acc_type;

    @SerializedName("lastupdatedon")
    @Expose
    public String lastupdatedon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getLastupdatedon() {
        return lastupdatedon;
    }

    public void setLastupdatedon(String lastupdatedon) {
        this.lastupdatedon = lastupdatedon;
    }


    public BankDetails() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.bankname);
        dest.writeString(this.account);
        dest.writeString(this.ifsc);
        dest.writeString(this.branch);
        dest.writeString(this.acc_type);
        dest.writeString(this.lastupdatedon);
    }

    protected BankDetails(Parcel in) {
        this.id = in.readString();
        this.bankname = in.readString();
        this.account = in.readString();
        this.ifsc = in.readString();
        this.branch = in.readString();
        this.acc_type = in.readString();
        this.lastupdatedon = in.readString();

    }

    public static final Creator<BankDetails> CREATOR = new Creator<BankDetails>() {
        @Override
        public BankDetails createFromParcel(Parcel source) {
            return new BankDetails(source);
        }

        @Override
        public BankDetails[] newArray(int size) {
            return new BankDetails[size];
        }
    };
}
