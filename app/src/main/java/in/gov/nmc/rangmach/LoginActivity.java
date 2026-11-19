package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.CustomSpinnerAdapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityLoginBinding;
import in.gov.nmc.rangmach.model.BankDetails;
import in.gov.nmc.rangmach.model.Data;
import in.gov.nmc.rangmach.model.LanguagePOJO;
import in.gov.nmc.rangmach.model.LanguagePOJO_Data;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.NetworkUtil;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    public static int typeOfLanguage;
    public static LoginActivity loginActivity;
    BottomSheetDialog bottomSheetDialog;
    private Handler handler;
    private Runnable checkConnectionRunnable;
    String selected_lang;
    public static List<LanguagePOJO> languagePOJOS;

    //    public final static String[] languages = {"Select Language", "English", "Hindi", "Marathi"};
    //hindi is currently close
    public final static String[] languages = {"Select Language", "English", "Marathi"};

    private static final long TIMER_DURATION = 1 * 60 * 1000;
    private CountDownTimer countDownTimer;
    public static boolean isLangaugeApiCall;
    private AppCompatTextView tvTimer;
    private AppCompatTextView tvOtp_number;
    AppCompatTextView tvResend;
    Dialog dialog;
    String mobileNo;
    Map<String, String> params = new HashMap<>();
    String temp = "";

    protected ActivityLoginBinding inflateBinding(LayoutInflater inflater) {
        return ActivityLoginBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;
        // Check internet connectivity
        if (isInternetAvailable()) {
            // Internet is available
            binding.ivLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    languagedialog();
                }
            });
            //Language Change
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String selected_lang = sharedPreferences.getString("languagekey", "English");
            typeOfLanguage = 0;

            if (selected_lang.equals("Hindi")) {
                typeOfLanguage = 2;
//                setLocal(LoginActivity.this, "hi");
            } else if (selected_lang.equals("Marathi")) {
                typeOfLanguage = 1;
//                setLocal(LoginActivity.this, "mr");
            }
            binding.etMobileNo.setHint(GetTranslation("Enter Mobile Number", typeOfLanguage));
            binding.btnLogin.setText(GetTranslation("Login", typeOfLanguage));

            binding.btnHowtouse.setText(GetTranslation("How to Use App", typeOfLanguage));
            binding.tvTitle.setText(GetTranslation("Rang Manch", typeOfLanguage));
            binding.tvNMC.setText(GetTranslation("Nashik Municipal Corporation", typeOfLanguage));
            binding.tvTermsConditions.setText(GetTranslation("Terms and Conditions", typeOfLanguage));

            temp = "Nashik Municipal Corporation(NMC) is committed to protecting your privacy and providing you with a positive experience on our websites and in using our products and services.\n" + "\n" + "This Privacy Statement applies to NMC mobile applications ANDROID and IOS respectively that reference this Statement and describes how we handle personal information and the choices available to you regarding collection, use, access, and how to update and correct your personal information. Additional information on our personal information practices may be provided in offer descriptions, supplemental privacy statements, or notices provided prior to or at the time of data collection.\n" + "\n" + "\n" + "Collection of Your Personal Information\n" + "We may collect data, including personal information, about you solely for application management and software handling. \"Personal information\" is any information that can be used to identify an individual, and may include name, address, email address, phone number, login information, (account number, password), or photo.\n" + "\n" + " \n" + "\n" + "However, we collect personal information for a variety of reasons, such as:\n" + "\n" + "\n" + "\n" + "Processing your order, including payment transactions.\n" + "\n" + "Providing you with a newsletter subscription.\n" + "\n" + "Sending marketing communications.\n" + "\n" + "Creating an account.\n" + "\n" + "Enabling the use of certain features of our Solutions.\n" + "\n" + "Personalising your experience.\n" + "\n" + "Providing customer service.\n" + "\n" + "\n" + "\n" + "This helps us improve its overall accuracy and completeness and also helps us better tailor our interactions with you.\n" + "\n" + "If you choose to provide NMC with a third party's personal information (such as name, email, and phone number), you represent that you have the third party's permission to do so. Examples include forwarding reference or marketing material to a friend or sending job referrals. Third parties may unsubscribe from any future communication following the link provided in the initial message or contacting prasad@esmartsolution.in. This information is used to better understand and improve the usability, performance, and effectiveness of the website and to help tailor content or offers for you.\n" + "\n" + "\n" + "\n" + "\n" + "Permissions asked in the application\n" + "\n" + "Push notification (For alerts and messages)\n" + "\n" + "Phone (For quick dialling customer support)\n" + "\n" + "Camera (For uploading photos like profile photo, document uploads for app functionality)\n" + "\n" + "\n" + "\n" + "Security of Your Personal Information\n" + "We intend to protect the personal information entrusted to us and treat it securely in accordance with this Privacy Statement. NMC implements physical, administrative, and technical safeguards designed to protect your personal information from unauthorised access, use, or disclosure. We also contractually require that our suppliers protect such information from unauthorised access, use, and disclosure. The Internet, however, cannot be guaranteed to be 100% secure, and we cannot ensure or warrant the security of any personal information you provide to us.\n" + "\n" + "\n" + "\n" + "Retention of Personal Information\n" + "We will retain your personal information as needed to fulfil the purposes for which it was collected. We will retain and use your personal information as necessary to comply with our business requirements, legal obligations, resolve disputes, protect our assets, and enforce our agreements.\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "Children's Privacy\n" + "eSmart encourages parents and guardians to take an active role in their children's online activities. NMC does not knowingly collect personal information from children without appropriate parental or guardian consent. If you believe that we may have collected personal information from someone under the applicable age of consent in your country without proper consent, please let us know using the methods described in the Contact Us section and we will take appropriate measures to investigate and address the issue promptly.\n" + "\n" + "\n" + "\n" + "Your Internet Protocol (IP) address. This is a number automatically assigned to your computer or device whenever you connect to the Internet. It is a unique address assigned by your Internet service provider or IT department on a TCP/IP network. Among other things, the IP address allows web servers to locate and identify your device.\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "Updates to this NMC  Privacy Statement\n" + "We may update this Privacy Statement from time to time. If we modify our Privacy Statement, we will post the revised version here, with an updated revision date. You agree to visit these pages periodically to be aware of and review any such revisions. If we make material changes to our Privacy Statement, we may also notify you by other means prior to the changes taking effect, such as by posting a notice on our websites or sending you a notification. By continuing to use our mobile application after such revisions are in effect, you accept and agree to the revisions and to abide by them.\n" + "\n" + "\n" + "\n" + "The NMC Privacy Statement was revised and posted as of January 1, 2024.\n" + "\n" + "\n" + "How to Contact Us\n" + "Email : prasad@eSmartsolution.in";

            // Check if the app is launched for the first time
            if (!(isFirstTimeLaunch())) {
                // Perform actions for the first time launch
                languagedialog();
                // For example, show an introduction screen, tutorial, or perform initial setup
                // Once completed, set the flag to indicate that the app has been launched before
//            setFirstTimeLaunch(false);
            } else {
                // Perform actions if the app has been launched before
                // For example, navigate to the main activity
            }

            binding.llWebsite.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://nmc.gov.in/"));
                startActivity(intent);
            });

            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this, s -> {
                if (!TextUtils.isEmpty(s)) app.getPreferences().setToken(s);
                Log.e("Token", s);
            });

            try {
                FirebaseMessaging.getInstance().subscribeToTopic("nashik_all")
                        .addOnCompleteListener(task -> {
                            String msg = task.isSuccessful() ? "Subscribed" : "Subscription failed";
                            Log.d("FCM", msg);
                        });
            } catch (Exception e) {

            }

            //Android 8.0
            createNotificationChannelForOreo();
            LanguageAPI();

//            binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(mContext, RegisterActivity.class));
//                }
//            });

            binding.btnHowtouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String videoId = GetTranslation("introvideo", typeOfLanguage); // Replace with your YouTube video ID
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

//            binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(mContext, ForgotPasswordActivity.class));
//                }
//            });

            binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobileNo = binding.etMobileNo.getText().toString().trim();

                    if (mobileNo.trim().length() != 10) {
                        Toast.makeText(LoginActivity.this, "Please enter valid Mobile Number(10 digits)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    params.put("usrname", mobileNo);
                    params.put("devicetype", "android");
                    params.put("uuid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    params.put("mobile_model", Utils.getDeviceName());
                    params.put("appversion", String.valueOf(BuildConfig.VERSION_CODE));
                    login(params);
                }
            });

            binding.termsConditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TermsCondition_Dialog();
                }
            });

        } else {
            // Internet is not available
//            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this).setTitle("NO INTERNET").setMessage("Check your internet connection and try again.").setCancelable(false).setPositiveButton("Mobile Data", new BottomSheetMaterialDialog.OnClickListener() {
//                @Override
//                public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
//                    // Open the mobile data settings
//                    Intent intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
//                    startActivity(intent);
//                }
//            }).setNegativeButton("Cancel", new BottomSheetMaterialDialog.OnClickListener() {
//                @Override
//                public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
//                    dialogInterface.dismiss();
//                }
//            }).build();l
//
//            Show Dialog
//            mBottomSheetDialog.show();
//
//            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            // Initialize the handler
            handler = new Handler(Looper.getMainLooper());
            checkInternetAndShowDialog();
        }
    }

    private void checkInternetAndShowDialog() {
        if (!NetworkUtil.isConnected(this)) {
            showInternetCheckBottomSheet();
        }
    }

    private void showInternetCheckBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_internet_check, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView tvTitle = bottomSheetView.findViewById(R.id.tvTitle);
        TextView tvMessage = bottomSheetView.findViewById(R.id.tvMessage);
        Button btnRetry = bottomSheetView.findViewById(R.id.btnRetry);

        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        // Set click listener for retry button
        btnRetry.setOnClickListener(v -> {
//            bottomSheetDialog.dismiss();
//            if (!NetworkUtil.isConnected(this)) {
//                Toast.makeText(this, "Still no connection", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Connection Restored", Toast.LENGTH_SHORT).show();
//            }
            // Set click listener for retry button
            Toast.makeText(this, "Checking connection...", Toast.LENGTH_SHORT).show();
            // Start periodic connection checks
            startConnectionCheck();
            bottomSheetDialog.show();
        });
        bottomSheetDialog.show();
    }

    private void startConnectionCheck() {
        checkConnectionRunnable = new Runnable() {
            @Override
            public void run() {
                if (NetworkUtil.isConnected(LoginActivity.this)) {
                    // If the internet is connected, dismiss the dialog
                    Toast.makeText(LoginActivity.this, "Connection Restored", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    handler.removeCallbacks(checkConnectionRunnable); // Stop checking once connected


                    //if internet available direct go to homepage if user is loging user
                    if (app.getPreferences().getLoggedInUser() != null) {
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }
                } else {
                    // If still no internet, continue checking after 1 second
                    handler.postDelayed(checkConnectionRunnable, 1000);
                }
            }
        };
        // Start the first check
        handler.post(checkConnectionRunnable);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_login;
//    }

    private void login(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
            cpd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().login(params), new ApiRequestHelper.OnRequestComplete() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onSuccess(Object object) {

                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    UserData response = (UserData) object;
                    if (response != null) {
                        if (response.getResponsecode() == 200) {
                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
                                Utils.showLongToast(mContext, response.getMessage());
//                            if (response.getData() != null)

                            String mobileNo = binding.etMobileNo.getText().toString().trim();
                            Data data = response.getData();
                            BankDetails bankDetails = response.getBankDetails();

                            if (data != null) {
                                data.setMobileNo(mobileNo);
                                response.setData(data);
                                app.getPreferences().setLoggedInUser(response);
                                regPushNotification(response);
                            } else {
                                //show toast as failed login
                                Toast.makeText(LoginActivity.this, "Login failed. Please contact support", Toast.LENGTH_SHORT).show();
                            }

                            if (bankDetails != null) {
                                response.setBankDetails(bankDetails);
                                app.getPreferences().setBankDetails(bankDetails);
                            }

//                            if (bankDetails != null) {
//                                response.setBankDetails(bankDetails);
//                                app.getPreferences().setBankDetails(bankDetails);
//                            }


                        } else if (response.getResponsecode() == 500) {
                            // Show an alert dialog for server errors (500)
                            showServerErrorDialog();
                        } else if (response.getResponsecode() == 201) {
                            // Show an alert dialog for server errors (500)
                            VerifyOTP_Dialog();
                        } else {
                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
                                ;
//                                Utils.showLongToast(mContext, response.getMessage());

                            //show dialog
                            showAlertMessage("Alert", response.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }

                    // Handle specific 500 response code
                    if (!TextUtils.isEmpty(apiResponse) && apiResponse.contains("500")) {
                        showServerErrorDialog();
                    } else {
                        Utils.showLongToast(mContext, apiResponse);
                    }
//                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }


    private void showAlertMessage(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Create and show the alert dialog
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showServerErrorDialog() {

        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.dialog_app_maintenance);
        Button btnsubmit = dialog.findViewById(R.id.btnClose);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void VerifyOTP_Dialog() {
        dialog = Utils.showCustomAlertDialog(mContext, R.layout.verify_otp_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        MaterialEditText myotp = dialog.findViewById(R.id.etotp);
        tvResend = dialog.findViewById(R.id.tvResend);
        tvResend.setVisibility(View.GONE);
//
//        try {
//            requestSMSPermission();
//            new OTP_Receiver().setEditText(myotp);
//        } catch (Exception e) {
//
//        }

        tvOtp_number = dialog.findViewById(R.id.tvOtp_number);
        tvOtp_number.setText("Enter OTP Sent to your mobile number " + mobileNo);

        tvTimer = dialog.findViewById(R.id.tvTimer);
        startTimer();

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        btnSave.setOnClickListener(view1 -> {
            String otp = myotp.getText().toString().trim();

            if (otp.trim().length() != 4) {
                Toast.makeText(LoginActivity.this, "Please enter Valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> map = new HashMap<>();
            map.put("usrname", mobileNo);
            map.put("otp", otp);
            map.put("uuid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

            VerifyOTP(map);

            dialog.dismiss();
        });
        dialog.show();
    }

    public void TermsCondition_Dialog() {

        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.terms_and_condition_dialog);
        Button btnsubmit = dialog.findViewById(R.id.btnsubmit);
        AppCompatTextView tvterms_conditions = dialog.findViewById(R.id.tvterms_conditions);

        tvterms_conditions.setText(temp);

        CheckBox checkboxAccept = dialog.findViewById(R.id.checkbox_accept);

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkboxAccept.isChecked()) {
                    Toast.makeText(LoginActivity.this, "Privacy policy accepted!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Please accept the privacy policy.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    //Get Auto OTP

//    private void requestSMSPermission() {
//        String permission = Manifest.permission.RECEIVE_SMS;
//
//        int grant = ContextCompat.checkSelfPermission(this, permission);
//        if (grant != PackageManager.PERMISSION_GRANTED) {
//            String[] permission_list = new String[1];
//            permission_list[0] = permission;
//
//            ActivityCompat.requestPermissions(this, permission_list, 1);
//        }
//    }

    // Get Auto OTP  end

    //Timer Countown start
    private void startTimer() {
        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                tvResend.setVisibility(View.VISIBLE);
                tvResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login(params);
                    }
                });
                tvTimer.setText("Timer Finished!");
            }
        };
        countDownTimer.start();
    }

    private void updateTimerText(long millisUntilFinished) {
        long minutes = millisUntilFinished / 1000 / 60;
        long seconds = (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText("Resend OTP in " + timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Remove callbacks when the activity is destroyed to avoid memory leaks
        if (handler != null && checkConnectionRunnable != null) {
            handler.removeCallbacks(checkConnectionRunnable);
        }
    }

    private void VerifyOTP(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
            cpd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().verifydevice(params), new ApiRequestHelper.OnRequestComplete() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onSuccess(Object object) {

                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    UserData response = (UserData) object;
                    if (response != null) {
                        if (response.getResponsecode() == 200) {
                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
                                Utils.showLongToast(mContext, response.getMessage());
//                            if (response.getData() != null) {
                            regPushNotification(response);
                            String mobileNo = binding.etMobileNo.getText().toString().trim();
                            Data data = response.getData();
                            BankDetails bankDetails = response.getBankDetails();

                            if (data != null) {
                                data.setMobileNo(mobileNo);
                                response.setData(data);
                                app.getPreferences().setLoggedInUser(response);
                            }

                            if (bankDetails != null) {
                                response.setBankDetails(bankDetails);
                                app.getPreferences().setBankDetails(bankDetails);
                            }
                        } else {
                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
                                Utils.showLongToast(mContext, response.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {

                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void regPushNotification(final UserData response) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", response.getData().getUserId() + "");
        if (!TextUtils.isEmpty(app.getPreferences().getToken()))
            params.put("deviceid", app.getPreferences().getToken());
        params.put("ostype", "Android");
        params.put("appversion", BuildConfig.VERSION_NAME);
        if (cd.isConnectingToInternet()) {
            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
            cpd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().regpushnotification(params), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }

//                    String mobileNo = response.getData().getMobileNo();
//                    Data data = app.getPreferences().getLoggedInUser().getData();
//                    if (data != null) {
//                        data.setMobileNo(mobileNo);
//                        response.setData(data);
//                        app.getPreferences().setLoggedInUser(response);
//                    }
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void LanguageAPI() {
        if (cd.isConnectingToInternet()) {
            //loading
            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
            cpd.show();

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().languageAPI(), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    LanguagePOJO_Data data = (LanguagePOJO_Data) object;
                    if (data != null && data.getData() != null) {
                        languagePOJOS = data.getData();
                        isLangaugeApiCall = true;
                        //Toast.makeText(mContext, "Langiages Fetches Success", Toast.LENGTH_SHORT).show();
                        // Default hidden
                        binding.btnHowtouse.setVisibility(View.GONE);

                        String videoId = GetTranslation("introvideo", typeOfLanguage);

                        Log.d("INTRO_VIDEO", "videoId = [" + videoId + "]");

                        if (videoId != null &&
                                videoId.trim().length() > 0 &&
                                !videoId.equalsIgnoreCase("null") &&
                                !videoId.equals("-") &&
                                !videoId.equalsIgnoreCase("NA")) {

                            binding.btnHowtouse.setVisibility(View.VISIBLE);
                        } else {
                            binding.btnHowtouse.setVisibility(View.GONE);
                        }

                        if (app.getPreferences().getLoggedInUser() != null) {
                            startActivity(new Intent(mContext, MainActivity.class));
                            finish();
                        } else {
                            //Toast.makeText(mContext, "Auto login failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isLangaugeApiCall = false;
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    isLangaugeApiCall = false;
                    Utils.showLongToast(mContext, "something went wromg");
                }
            }));
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void createNotificationChannelForOreo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "rang_manch_app";
            // The user-visible name of the channel.
            CharSequence name = "App Updates";
            // The user-visible description of the channel.
            String description = "Notifications regarding our app";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }

    public void languagedialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builder.setTitle(R.string.ChangeLanguage)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Handle OK button click
//                        }
//                    })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Cancel button click
                        dialog.dismiss();
                    }
                });


        // Inflate the dialog layout XML
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.lang_dialog, null);
        builder.setView(dialogView);

        Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
//             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
//             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//             spinner.setAdapter(adapter);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item, languages);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String selectlang = adapterView.getItemAtPosition(position).toString();

                if (selectlang.equals("English")) {
//                    setLocal(LoginActivity.this, "en");
                    setFirstTimeLaunch(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("languagekey", selectlang);
                    editor.apply();

                    finish();
                    startActivity(getIntent());

                } else if (selectlang.equals("Hindi")) {
//                    setLocal(LoginActivity.this, "hi");
                    setFirstTimeLaunch(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("languagekey", selectlang);
                    editor.apply();

                    finish();
                    startActivity(getIntent());

                } else if (selectlang.equals("Marathi")) {
//                    setLocal(LoginActivity.this, "mr");
                    setFirstTimeLaunch(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("languagekey", selectlang);
                    editor.apply();

                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to check if the app is launched for the first time
    private boolean isFirstTimeLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("isFirstTimeLaunch")) {
            return true;
        } else {
            return false;
        }
    }

    // Method to set the flag indicating the app has been launched before
    private void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTimeLaunch", isFirstTime);
        editor.apply();
    }

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // For devices below API level 23
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    return activeNetworkInfo.isConnected();
                }
            }
        }

        return false;
    }
}
