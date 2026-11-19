package in.gov.nmc.rangmach.model;
        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

@Generated("jsonschema2pojo")
public class NowShowing implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("about_details")
    @Expose
    private String aboutDetails;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("age_group")
    @Expose
    private String ageGroup;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("center_id")
    @Expose
    private String centerId;
    @SerializedName("center_hall_id")
    @Expose
    private String center_hall_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("google_map")
    @Expose
    private String google_map;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("actor")
    @Expose
    private List<NowShowing_Actor> actor = new ArrayList<NowShowing_Actor>();
    @SerializedName("media")
    @Expose
    private List<NowShowing_Media> media = new ArrayList<NowShowing_Media>();
    @SerializedName("timings")
    @Expose
    private List<NowShowing_Timing> timings = new ArrayList<NowShowing_Timing>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogle_map() {
        return google_map;
    }

    public void setGoogle_map(String google_map) {
        this.google_map = google_map;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAboutDetails() {
        return aboutDetails;
    }

    public void setAboutDetails(String aboutDetails) {
        this.aboutDetails = aboutDetails;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenter_hall_id() {
        return center_hall_id;
    }

    public void setCenter_hall_id(String center_hall_id) {
        this.center_hall_id = center_hall_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<NowShowing_Actor> getActor() {
        return actor;
    }

    public void setActor(List<NowShowing_Actor> actor) {
        this.actor = actor;
    }

    public List<NowShowing_Media> getMedia() {
        return media;
    }

    public void setMedia(List<NowShowing_Media> media) {
        this.media = media;
    }



    public List<NowShowing_Timing> getTimings() {
        return timings;
    }

    public void setTimings(List<NowShowing_Timing> timings) {
        this.timings = timings;
    }

}