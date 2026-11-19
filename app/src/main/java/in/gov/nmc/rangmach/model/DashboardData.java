package in.gov.nmc.rangmach.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DashboardData {
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("AllDoctors")
    @Expose
    public ArrayList<Doctor> doctorList;
    @SerializedName("Responsecode")
    @Expose
    public long responsecode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(ArrayList<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public long getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(long responsecode) {
        this.responsecode = responsecode;
    }
}
