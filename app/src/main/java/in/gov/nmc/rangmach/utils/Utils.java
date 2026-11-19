package in.gov.nmc.rangmach.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    //    public static String tag = "MyFragment";
    public static final String OTP_DELIMITER = ":";
    public static String NO_INTERNET_MSG = "You don't have internet connection.Please connect to internet";
    public static String NO_INTERNET_TITLE = "No Internet Connection";
    public static String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
    public static int MAX_PENDING_MINUTE = 5;
    public static int MOBILE_NO_LENGTH = 10;
    public static int MAX_MINUTE = 61;
    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String DATE_FORMAT_DMY = "dd-MM-yyyy";
    public static String UNPROPER_RESPONSE = "Unable to process your request. Please, Try again later.";
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    private static String tag = "MyFragment";

    public static List<Date> getDates(Date date1, Date date2) {
        ArrayList<Date> dates = new ArrayList<Date>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Date getYmdDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYmdString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    //1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400
    public static long printDifference(Date startDate, Date endDate) {
//        Date startDate = null;
//        Date endDate = null;
//        try {
//            startDate = simpleDateFormat.parse(date1);
//            endDate = simpleDateFormat.parse(date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        if (endDate != null && startDate != null) {
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
//            different = different % daysInMilli;
//
//            long elapsedHours = different / hoursInMilli;
//            different = different % hoursInMilli;
//
//            long elapsedMinutes = different / minutesInMilli;
//            different = different % minutesInMilli;
//
//            long elapsedSeconds = different / secondsInMilli;
//
//            System.out.printf(
//                    "%d days, %d hours, %d minutes, %d seconds%n",
//                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            return elapsedDays;
        }
        return 0;
    }

    public static boolean isAfterToday(String date) {
        int year, month, day;
        String[] split = date.split("-");
        year = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]);
        day = Integer.parseInt(split[2]);
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month, day);

        return !myDate.before(today);
    }

    public static String addDays(String dateString, int period) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, period);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        return sdf1.format(c.getTime());
    }

    public static String secondsToHms(long totalSeconds) {
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static void openYoutubeUrl(Context mContext, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // youtube is not installed.Will be opened in other available apps
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + url));
            mContext.startActivity(i);
        }
    }

    public static String getFormattedDuration(int totalSeconds) {
        StringBuilder mFormatBuilder = new StringBuilder();
        mFormatBuilder.setLength(0);
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (totalSeconds < 60) {
            return mFormatter.format("%02d sec", totalSeconds).toString();
        }
        int minutes = totalSeconds / 60;
        return mFormatter.format("%02d min", minutes).toString();
    }

    public static double round(double number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_DOWN);
        return bd.doubleValue();
    }

    public static String format2Dec(double x) {
        return Math.floor(x * 100) / 100 + "";
    }

    public static String decodeBase64(String coded) {
        byte[] valueDecoded = new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(valueDecoded);
    }

    public static boolean checkBetween(String dateToCheck, String startDate, String endDate) {
        boolean res = false;
        SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd"); //2013-05-20
        try {
            Date fromDate = fmt1.parse(startDate);
            Date requestDate = fmt1.parse(dateToCheck);
            Date toDate = fmt1.parse(endDate);
            res = requestDate.compareTo(fromDate) >= 0 && requestDate.compareTo(toDate) <= 0;
        } catch (ParseException pex) {
            pex.printStackTrace();
        }
        return res;
    }

    public static void callPhone(String mobile, Context mContext) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            MaterialDialog ok = new MaterialDialog.Builder(mContext)
                    .content("Do you want to call this phone number?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .show();
            ok.getActionButton(DialogAction.POSITIVE).setOnClickListener(view -> {
                ok.dismiss();
                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile)));
            });
            ok.getActionButton(DialogAction.NEGATIVE).setOnClickListener(view -> ok.dismiss());
        }
    }

    public static String formatTimeToAgo(String strDate) {
        String formattedTime = "";
        if (strDate != null && !TextUtils.isEmpty(strDate) && !strDate.equals("0000-00-00 00:00:00")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            format.setTimeZone(TimeZone.getTimeZone("US/Central"));
            Date past = null;
            try {
                past = format.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            Date currDate = new Date();
            Date now = new Date();
//            Date now = convertTimeZone(currDate, TimeZone.getDefault(), TimeZone.getTimeZone("US/Central"));
            long seconds = 0;
            if (past != null) {
                seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

                if (seconds < 60) {
                    String strSeconds = seconds > 1 ? "seconds ago" : "second ago";
                    formattedTime = seconds + " s";
                    if (formattedTime.contains("-")) {
                        formattedTime = formattedTime.replace("-", "");
                    }
                } else if (minutes < 60) {
                    String strMinutes = minutes > 1 ? "minutes ago" : "minute ago";
                    formattedTime = minutes + " m";
                } else if (hours < 24) {
                    String strHour = hours > 1 ? "hours ago" : "hour ago";
                    formattedTime = hours + " h";
                } else {
                    String strDay = days > 1 ? "days ago" : "day ago";
                    formattedTime = days + " d";
                }
            }
        }
        return formattedTime;
    }

    public static void showDialog(final Context mContext, String content, boolean isNegativeEnabled,
                                  MaterialDialog.SingleButtonCallback positiveButtonCallback) {
        if (mContext == null) return;
        new MaterialDialog.Builder(mContext)
                .content(content)
                .positiveText("Ok")
                .negativeText(isNegativeEnabled ? "Cancel" : "")
                .cancelable(false)
                .onPositive(positiveButtonCallback)
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    public static Dialog showCustomAlertDialog(Context mContext, int layoutResID) {
        if (mContext == null) return null;
        Dialog mDialog = new Dialog(mContext);
        mDialog.setContentView(layoutResID);
        if (mDialog.getWindow() != null) {
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        return mDialog;
    }

//    /**
//     * Returns the unique identifier for the device
//     *
//     * @return unique identifier for the device
//     */
//    @SuppressLint({"HardwareIds", "MissingPermission"})
//    public static String getDeviceIMEI(Context mContext) {
//        if (mContext == null) return "";
//        String deviceUniqueIdentifier = "";
//        try {
//            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//            if (null != tm) {
//                deviceUniqueIdentifier = tm.getDeviceId();
//            }
//            if (null == deviceUniqueIdentifier || deviceUniqueIdentifier.isEmpty()) {
//                deviceUniqueIdentifier = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return deviceUniqueIdentifier;
//    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static void openBrowser(Context context, String url) {
        if (context == null) return;
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static String toBase64Encode(String string) {
        String base64 = "";
        byte[] data;
        try {
            data = string.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.format(c.getTime());
    }

    public static String dmyTod(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdTod(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdHmsTodmyHms(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("EEE dd MMM yyyy hh:mm aa");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdHmsToHms(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm aa");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdTodmy(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdToedmy(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, dd MMM yy");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdTodMy(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yy");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static void alert_dialog(final Context mContext) {
        if (mContext == null) return;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle("Internet Connection Error");
        builder1.setMessage("Please connect to working Internet connection");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Go To Settings",
                (dialog, which) -> mContext.startActivity(new Intent(Settings.ACTION_SETTINGS)));
        builder1.setNegativeButton("Cancel",
                (dialog, which) -> dialog.dismiss());
        builder1.show();
    }

    public static String replaceUnwantedChars(String s) {
        if (s.contains("\n")) {
            s = s.replaceAll("\n", "");
        }
        if (s.contains("\r")) {
            s = s.replaceAll("\r", "");
        }
        return s;
    }

    public static void showLongToast(Context context, String message) {
        if (!TextUtils.isEmpty(message) && context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String message) {
        if (!TextUtils.isEmpty(message) && context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // validating email id
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null && activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static String checkNotEmpty(String s) {
        if (s != null && !TextUtils.isEmpty(s) && !s.equalsIgnoreCase("null")) {
            return s;
        }
        return "";
    }

    public static String checkForNull(String s) {
        if (s != null && !TextUtils.isEmpty(s) && !s.equalsIgnoreCase("NA") && !s.equalsIgnoreCase("null")) {
            if (s.contains("\n")) {
                s = s.replaceAll("\n", "");
            }
            if (s.contains("\r")) {
                s = s.replaceAll("\r", "");
            }
            return s;
        }
        return "";
    }

    public static String format(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);
        return format.format(n);
    }

    public static String ymdHmsToEdmyHms(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("EEE dd MMM yyyy hh:mm aa");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }

    public static String ymdTodMyyyy(String str_date) {
        if (!TextUtils.isEmpty(str_date)) {
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = null;
            try {
                date = originalFormat.parse(str_date); //2017-07-27
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return targetFormat.format(date);
        }
        return "";
    }
}
