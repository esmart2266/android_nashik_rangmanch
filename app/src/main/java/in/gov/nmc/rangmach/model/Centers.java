package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Centers implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
    }

    protected Centers(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();

    }

    public static final Creator<Centers> CREATOR = new Creator<Centers>() {
        @Override
        public Centers createFromParcel(Parcel source) {
            return new Centers(source);
        }

        @Override
        public Centers[] newArray(int size) {
            return new Centers[size];
        }
    };
}