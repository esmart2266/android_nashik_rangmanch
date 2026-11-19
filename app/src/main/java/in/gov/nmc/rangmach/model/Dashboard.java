package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard implements Parcelable {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("userId")
    @Expose
    public String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Dashboard() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.userId);
    }

    protected Dashboard(Parcel in) {
        this.username = in.readString();
        this.userId = in.readString();
    }

    public static final Creator<Dashboard> CREATOR = new Creator<Dashboard>() {
        @Override
        public Dashboard createFromParcel(Parcel source) {
            return new Dashboard(source);
        }

        @Override
        public Dashboard[] newArray(int size) {
            return new Dashboard[size];
        }
    };
}
