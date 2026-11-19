package in.gov.nmc.rangmach.model;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

@Generated("jsonschema2pojo")
public class AllTheaters implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile1")
    @Expose
    private String mobile1;
    @SerializedName("mobile2")
    @Expose
    private String mobile2;
    @SerializedName("landline1")
    @Expose
    private String landline1;
    @SerializedName("landline2")
    @Expose
    private String landline2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("website_link")
    @Expose
    private String websiteLink;
    @SerializedName("google_map")
    @Expose
    private String googleMap;
    @SerializedName("parking_count")
    @Expose
    private String parkingCount;
    @SerializedName("balcony")
    @Expose
    private String balcony;
    @SerializedName("groundfloor")
    @Expose
    private String groundfloor;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("policy_terms")
    @Expose
    private String policy_terms;

    @SerializedName("media")
    @Expose
    private List<Theater_Media> media = new ArrayList<Theater_Media>();
    @SerializedName("halls")
    @Expose
    private List<Theater_Halls> halls = new ArrayList<Theater_Halls>();

    @SerializedName("holidays")
    @Expose
    private List<Theater_Holidays> theaterHolidays = new ArrayList<Theater_Holidays>();

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getLandline1() {
        return landline1;
    }

    public void setLandline1(String landline1) {
        this.landline1 = landline1;
    }

    public String getLandline2() {
        return landline2;
    }

    public void setLandline2(String landline2) {
        this.landline2 = landline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(String googleMap) {
        this.googleMap = googleMap;
    }

    public String getParkingCount() {
        return parkingCount;
    }

    public void setParkingCount(String parkingCount) {
        this.parkingCount = parkingCount;
    }

    public String getBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public String getGroundfloor() {
        return groundfloor;
    }

    public void setGroundfloor(String groundfloor) {
        this.groundfloor = groundfloor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPolicy_terms() {
        return policy_terms;
    }

    public void setPolicy_terms(String policy_terms) {
        this.policy_terms = policy_terms;
    }

    public List<Theater_Media> getMedia() {
        return media;
    }

    public void setMedia(List<Theater_Media> media) {
        this.media = media;
    }

    public List<Theater_Halls> getHalls() {
        return halls;
    }

    public void setHalls(List<Theater_Halls> halls) {
        this.halls = halls;
    }


    public List<Theater_Holidays> getTheaterHolidays() {
        return theaterHolidays;
    }

    public void setTheaterHolidays(List<Theater_Holidays> theaterHolidays) {
        this.theaterHolidays = theaterHolidays;
    }

}