package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blog implements Parcelable {
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("details")
    @Expose
    public String details;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("photolink")
    @Expose
    public String photolink;

    public String getPhotolink() {
        return photolink;
    }

    public void setPhotolink(String photolink) {
        this.photolink = photolink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.details);
        dest.writeString(this.url);
        dest.writeString(this.photolink);
    }

    public Blog() {
    }

    protected Blog(Parcel in) {
        this.title = in.readString();
        this.details = in.readString();
        this.url = in.readString();
        this.photolink = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel source) {
            return new Blog(source);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };
}
