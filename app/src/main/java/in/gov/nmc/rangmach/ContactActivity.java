package in.gov.nmc.rangmach;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import in.gov.nmc.rangmach.databinding.ActivityContactUsBinding;

public class ContactActivity extends BaseActivity<ActivityContactUsBinding> {

//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.etSubject)
//    MaterialEditText etSubject;
//    @BindView(R.id.etMessage)
//    MaterialEditText etMessage;
//    @BindView(R.id.btnSubmit)
//    Button btnSubmit;
//
//    @BindView(R.id.tvPhone)
//    TextView tvPhone;
//    @BindView(R.id.tvAddress)
//    TextView tvAddress;
//    @BindView(R.id.tvSite1)
//    TextView tvSite1;
//
//    @BindView(R.id.address_text)
//    TextView address_text;
//    @BindView(R.id.phone_text)
//    TextView phone_text;
//    @BindView(R.id.website_text)
//    TextView website_text;

    String selected_lang;
    public static int typeOfLanguage;

    private static final int CALL_PERMISSION_REQUEST = 102;


    protected ActivityContactUsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityContactUsBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Toolbar toolbar = binding.toolbarContainer.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(ContactActivity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(ContactActivity.this, "mr");
        }
        binding.toolbarContainer.toolbarTitle.setText(GetTranslation("Contact Us", typeOfLanguage));
        binding.addressText.setText(GetTranslation("ADDRESS", typeOfLanguage));
        binding.phoneText.setText(GetTranslation("PHONE (toll free)", typeOfLanguage));
        binding.websiteText.setText(GetTranslation("Website", typeOfLanguage));

//        binding.tvPhone.setOnClickListener(view -> callPhone("0253 - 2575631"));

        binding.tvPhone.setOnClickListener(view -> {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED) {

                callDialer("0253 - 2575631");

            } else {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PERMISSION_REQUEST
                );
            }
        });

        binding.tvAddress.setOnClickListener(view -> {
            String uri = "http://maps.google.co.in/maps?q=" + binding.tvAddress.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
        binding.tvSite1.setOnClickListener(view -> {
            String uri = "https://nmc.gov.in/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = binding.etSubject.getText().toString().trim();
                String message = binding.etMessage.getText().toString().trim();
                if (TextUtils.isEmpty(subject)) {
                    binding.etSubject.setError("Enter Subject");
                    binding.etSubject.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(message)) {
                    binding.etMessage.setError("Enter Message");
                    binding.etMessage.requestFocus();
                    return;
                }
//                Map<String, String> map = new HashMap<>();
//                map.put("customerid", app.getPreferences().getLoggedInUser().getData().getCustomerId());
//                map.put("subject", subject);
//                map.put("message", message);
//                savefeedback(map);
                sendEmail("admin@theecca.com", subject, message);
//                contactus(subject, message);

            }
        });

    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_contact_us;
//    }

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

//    public void contactus(String subject, String message) {
//        Map<String, String> map = new HashMap<>();
//        if (app.getPreferences().getLoggedInUser() != null && app.getPreferences().getLoggedInUser().getData() != null)
//            map.put("customerid", app.getPreferences().getLoggedInUser().getData().getCustomerId());
//        map.put("subject", subject);
//        map.put("message", message);
//        if (cd.isConnectingToInternet()) {
//            CustomProgressDialog cpd = new CustomProgressDialog(mContext);
//            cpd.show();
//            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().contactus(map), new ApiRequestHelper.OnRequestComplete() {
//                @Override
//                public void onSuccess(Object object) {
//                    if (cpd != null && cpd.isShowing()) cpd.dismiss();
//                    UserResponse userData = (UserResponse) object;
//                    if (userData != null) {
//                        if (userData.getResponsecode() == 200) {
//                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
//                                Utils.showShortToast(mContext, userData.getMessage());
//                            etMessage.setText("");
//                            etSubject.setText("");
//                        } else {
//                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
//                                Utils.showLongToast(mContext, userData.getMessage());
//                        }
//                    } else {
//                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
//                    }
//                }
//
//                @Override
//                public void onFailure(String apiResponse) {
//                    if (cpd != null && cpd.isShowing()) cpd.dismiss();
//                }
//            });
//        } else {
//            Utils.alert_dialog(mContext);
//        }
//    }

    private void sendEmail(String email, String subject, String text) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
//        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), 1);
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(emailIntent, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent targetedShare = new Intent(Intent.ACTION_SEND);
//                targetedShare.setType("text/plain"); // put here your mime type
                targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                targetedShareIntents.add(targetedShare);
            }
            // Then show the ACTION_PICK_ACTIVITY to let the user select it
            Intent intentPick = new Intent();
            intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
            // Set the title of the dialog
            intentPick.putExtra(Intent.EXTRA_TITLE, "");
            intentPick.putExtra(Intent.EXTRA_INTENT, emailIntent);
            intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
            // Call StartActivityForResult so we can get the app name selected by the user
            startActivityForResult(intentPick, 111);
        }
    }

    public void callPhone(String phoneNo) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            final MaterialDialog ok = new MaterialDialog.Builder(mContext)
                    .content("Do you want to call this phone number?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .show();
            ok.getActionButton(DialogAction.POSITIVE).setOnClickListener(view -> {
                ok.dismiss();
                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo)));
            });
            ok.getActionButton(DialogAction.NEGATIVE).setOnClickListener(view -> ok.dismiss());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data != null && data.getComponent() != null && !TextUtils.isEmpty(data.getComponent().flattenToShortString())) {
                String appName = data.getComponent().flattenToShortString();
                // Now you know the app being picked.
                // data is a copy of your launchIntent with this important extra info added.

                // Start the selected activity
                startActivity(data);
                binding.etMessage.setText("");
                binding.etSubject.setText("");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                callDialer("0253 - 2575631");

            } else {

                Toast.makeText(this,
                        "Call permission is required to make phone calls.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callDialer(String phoneNumber) {
        String number = phoneNumber.replaceAll("\\s+", "");

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
