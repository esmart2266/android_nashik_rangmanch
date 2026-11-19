package in.gov.nmc.rangmach.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public class StringDateComparator implements Comparator<String> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public int compare(String lhs, String rhs) {
        try {
            return dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}