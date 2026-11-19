package in.gov.nmc.rangmach.model;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.ArrayList;
        import java.util.List;

@Generated("jsonschema2pojo")
public class LoadSitingData {

    @SerializedName("booked")
    @Expose
    private List<String> booked = new ArrayList<String>();

    @SerializedName("blocked")
    @Expose
    private List<String> blocked = new ArrayList<String>();

    @SerializedName("rows")
    @Expose
    private List<Row> rows;
    @SerializedName("columns")
    @Expose
    private List<Column> columns;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Responsecode")
    @Expose
    private Integer responsecode;


    public List<String> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<String> blocked) {
        this.blocked = blocked;
    }

    public List<String> getBooked() {
        return booked;
    }

    public void setBooked(List<String> booked) {
        this.booked = booked;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

}