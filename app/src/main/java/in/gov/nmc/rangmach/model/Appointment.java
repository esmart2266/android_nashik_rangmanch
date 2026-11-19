package in.gov.nmc.rangmach.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment implements Parcelable {
    @SerializedName("doctorName")
    @Expose
    public String doctorName;
    @SerializedName("scheduledBy")
    @Expose
    public String scheduledBy;
    @SerializedName("confirmedBy")
    @Expose
    public String confirmedBy;
    @SerializedName("appointmentTime")
    @Expose
    public String appointmentTime;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getScheduledBy() {
        return scheduledBy;
    }

    public void setScheduledBy(String scheduledBy) {
        this.scheduledBy = scheduledBy;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.doctorName);
        dest.writeString(this.scheduledBy);
        dest.writeString(this.confirmedBy);
        dest.writeString(this.appointmentTime);
    }

    public Appointment() {
    }

    protected Appointment(Parcel in) {
        this.doctorName = in.readString();
        this.scheduledBy = in.readString();
        this.confirmedBy = in.readString();
        this.appointmentTime = in.readString();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel source) {
            return new Appointment(source);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };
}