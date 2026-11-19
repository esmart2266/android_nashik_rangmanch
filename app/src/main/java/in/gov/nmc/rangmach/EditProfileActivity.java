package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityEditProfileBinding;
import in.gov.nmc.rangmach.model.AllOrganizationsList;
import in.gov.nmc.rangmach.model.AllOrganizationsList_Data;
import in.gov.nmc.rangmach.model.BankDetails;
import in.gov.nmc.rangmach.model.Data;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class EditProfileActivity extends BaseActivity<ActivityEditProfileBinding> {

    private List<AllOrganizationsList> allOrganizationsLists;

    String[] cities = {"Amravati", "Aurangabad", "Ahmednagar", "Buldhana", "Beed", "Bhandara", "Chandrapur", "Dhule", "Gadchiroli", "Gondia", "Hingoli", "Jalna", "Jalgaon", "Kolhapur", "Latur", "Mumbai", "Mumbai Suburban", "Nanded", "Nagpur", "Nandurbar", "Nashik", "Osmanabad", "Parbhani", "Pune", "Palghar", "Raigad", "Ratnagiri", "Sindhudurg", "Satara", "Solapur", "Sangli", "Wardha", "Washim", "Yavatmal"};

    String[] IdProofs = {"Pan Card", "Aadhar Card", "Driving License", "Voter Id", "Passport"};
    String[] AssetsType = {
            "Private Institute / खाजगी संस्था",
            "Theater Institute / नाट्यसंस्था",
            "Traditional Folk Dance Institute / लावणी संस्था",
            "Orchestra Organizer / ऑर्केस्ट्रा आयोजक",
            "Karaoke Organizer / कराओके आयोजक",
            "Social Institution / सामजिक संस्था",
            "Educational Institution / शैक्षणिक संस्था",
            "Political Institution / राजकीय संस्था",
            "Official Institution / शासकीय संस्था",
            "Individual / वैयक्तिक",
            "Others / इतर"
    };


    private Dialog bankDialog;
    private AutoCompleteTextView etBankName;
    private EditText etAccount;
    private EditText etIfsc;
    private AutoCompleteTextView etAccType;
    private EditText etBranch;
    private TextInputLayout tilIfsc, tilBankName, tilAccountNumber, tilAccountType, tilBranch;
    private TextView tvBankNote;
    private Button btnSave, btnCancel;
    private List<String> bankList = new ArrayList<>();
    private ArrayAdapter<String> bankAdapter;

    // Account type list
    private List<String> accountTypeList = new ArrayList<>();
    private ArrayAdapter<String> accountTypeAdapter;


    String selected_lang;
    public static int typeOfLanguage;

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    protected ActivityEditProfileBinding inflateBinding(LayoutInflater inflater) {
        return ActivityEditProfileBinding.inflate(inflater);
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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;
        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(EditProfileActivity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(EditProfileActivity.this, "mr");
        }
        AllOrganizations();

        binding.toolbarContainer.toolbarTitle.setText("My Profile");

        binding.toolbarContainer.toolbarTitle.setText(GetTranslation("My Profile", typeOfLanguage));
        binding.btnSubmit.setText(GetTranslation("Save", typeOfLanguage));
        binding.btnUpdatBankDetails.setText(GetTranslation("Update Bank Details", typeOfLanguage));
        binding.etname.setHint(GetTranslation("Name", typeOfLanguage));
        binding.etMobile.setHint(GetTranslation("Mobile No", typeOfLanguage));
        binding.etEmail.setHint(GetTranslation("Email", typeOfLanguage));
        binding.etCity.setHint(GetTranslation("Select City", typeOfLanguage));
        binding.etAddress.setHint(GetTranslation("Address", typeOfLanguage));
        binding.etGovermentIdtype.setHint(GetTranslation("Goverment Id Type", typeOfLanguage));
        binding.etGovermentId.setHint(GetTranslation("Goverment ID", typeOfLanguage));
        binding.etGstNumber.setHint(GetTranslation("GST No", typeOfLanguage));
        binding.etAssetType.setHint(GetTranslation("Asset Type", typeOfLanguage));
        binding.etOrganization.setHint(GetTranslation("Select Organization title", typeOfLanguage));
        binding.Pleasefillcompleteformforbooking.setText(GetTranslation("Please fill complete form for booking", typeOfLanguage));
        binding.selctOrgNote.setText(GetTranslation("org info label", typeOfLanguage));

        Data data = app.getPreferences().getLoggedInUser().getData();
        if (data != null) {
            binding.etname.setText(Utils.checkNotEmpty(data.getName()));
            binding.etAddress.setText(Utils.checkNotEmpty(data.getAddress()));
            binding.etEmail.setText(Utils.checkNotEmpty(data.getemailid()));
            binding.etMobile.setText(Utils.checkNotEmpty(data.getMobileNo()));
            binding.etCity.setText(Utils.checkNotEmpty(data.getCity()));
            binding.etGovermentId.setText(Utils.checkNotEmpty(data.getGovtId_value()));
            binding.etGovermentIdtype.setText(Utils.checkNotEmpty(data.getGovtId()));
            binding.etGstNumber.setText(Utils.checkNotEmpty(data.getGst_no()));
            binding.etAssetType.setText(Utils.checkNotEmpty(data.getType()));
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.etname.getText().toString().trim();
                String email = binding.etEmail.getText().toString().trim();
                String mobile = binding.etMobile.getText().toString().trim();
                String address = binding.etAddress.getText().toString().trim();
                String city = binding.etCity.getText().toString().trim();
                String IdType = binding.etGovermentIdtype.getText().toString().trim();
                String GovId = binding.etGovermentId.getText().toString().trim();
                String GstNumber = binding.etGstNumber.getText().toString().trim();
                String type = binding.etAssetType.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    binding.etname.setError("Enter Name");
                    binding.etname.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Enter Email");
                    binding.etEmail.requestFocus();
                    return;
                }

                if (!Utils.isValidEmail(email)) {
                    binding.etEmail.setError("Enter Valid Email");
                    binding.etEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mobile)) {
                    binding.etMobile.setError("Enter Mobile Number");
                    binding.etMobile.requestFocus();
                    return;
                }

                if (mobile.length() < 10) {
                    binding.etMobile.setError("Mobile Number must be 10 digits");
                    binding.etMobile.requestFocus();
                    return;
                }

                if (type.length() < 5) {
                    binding.etAssetType.setError("Please Select Type");
                    binding.etAssetType.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(city)) {
                    binding.etCity.setError("Select City");
                    binding.etCity.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(type)) {
                    binding.etCity.setError("Select Type");
                    binding.etCity.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(IdType)) {
                    binding.etGovermentIdtype.setError("Select Id Proof");
                    binding.etGovermentIdtype.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(GovId)) {
                    binding.etGovermentId.setError("Enter Goverment ID");
                    binding.etGovermentId.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    binding.etAddress.setError("Enter Address");
                    binding.etAddress.requestFocus();
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
                map.put("name", firstName);
                map.put("mobile", mobile);
                map.put("email", email);
                map.put("address", address);
                map.put("city", city);
                map.put("govtid", IdType);
                map.put("govtidvalue", GovId);
                map.put("gst_no", GstNumber);
                map.put("type", type);
                updateprofile(map);
            }
        });

        binding.etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cities != null && cities.length > 0) {
                    new MaterialDialog.Builder(mContext).title("Select City").items(cities).itemsCallback((dialog, view1, which, text) -> binding.etCity.setText(text.toString())).show();
                }
            }
        });

        binding.etGovermentIdtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IdProofs != null && IdProofs.length > 0) {
                    new MaterialDialog.Builder(mContext).title("Select ID Proof").items(IdProofs).itemsCallback((dialog, view1, which, text) -> binding.etGovermentIdtype.setText(text.toString())).show();
                }
            }
        });

        binding.etAssetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AssetsType != null && AssetsType.length > 0) {
                    new MaterialDialog.Builder(mContext).title("Select Type").items(AssetsType).itemsCallback((dialog, view1, which, text) -> binding.etAssetType.setText(text.toString())).show();
                }
            }
        });

        binding.ivAddOrganisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, AddOrganization_Details_Activity.class));
            }
        });

        binding.btnUpdatBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBankDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AllOrganizations();
    }

    //    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_edit_profile;
//    }

//    @OnClick({R.id.btnSubmit, R.id.etCity, R.id.etGovermentIdtype})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//
//            case R.id.btnSubmit: {
//                String firstName = etname.getText().toString().trim();
//                String email = etEmail.getText().toString().trim();
//                String mobile = etMobile.getText().toString().trim();
//                String address = etAddress.getText().toString().trim();
//                String city = etCity.getText().toString().trim();
//                String IdType = etGovermentIdtype.getText().toString().trim();
//                String GovId = etGovermentId.getText().toString().trim();
//
//                if (TextUtils.isEmpty(firstName)) {
//                    etname.setError("Enter Name");
//                    etname.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(email)) {
//                    etEmail.setError("Enter Email");
//                    etEmail.requestFocus();
//                    return;
//                }
//
//                if (!Utils.isValidEmail(email)) {
//                    etEmail.setError("Enter Valid Email");
//                    etEmail.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(mobile)) {
//                    etMobile.setError("Enter Mobile Number");
//                    etMobile.requestFocus();
//                    return;
//                }
//
//                if (mobile.length() < 10) {
//                    etMobile.setError("Mobile Number must be 10 digits");
//                    etMobile.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(city)) {
//                    etCity.setError("Select City");
//                    etCity.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(IdType)) {
//                    etGovermentIdtype.setError("Select Id Proof");
//                    etGovermentIdtype.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(GovId)) {
//                    etGovermentId.setError("Enter Goverment ID");
//                    etGovermentId.requestFocus();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(address)) {
//                    etAddress.setError("Enter Address");
//                    etAddress.requestFocus();
//                    return;
//                }
//
//                Map<String, String> map = new HashMap<>();
//                map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//                map.put("name", firstName);
//                map.put("mobile", mobile);
//                map.put("email", email);
//                map.put("address", address);
//                map.put("city", city);
//                map.put("govtid", IdType);
//                map.put("govtidvalue", GovId);
//                updateprofile(map);
//                break;
//            }
//
//            case R.id.etCity: {
//                if (cities != null && cities.length > 0) {
//                    new MaterialDialog.Builder(mContext)
//                            .title("Select City")
//                            .items(cities)
//                            .itemsCallback((dialog, view1, which, text) -> etCity.setText(text.toString()))
//                            .show();
//                }
//                break;
//            }
//
//            case R.id.etGovermentIdtype: {
//                if (IdProofs != null && IdProofs.length > 0) {
//                    new MaterialDialog.Builder(mContext)
//                            .title("Select ID Proof")
//                            .items(IdProofs)
//                            .itemsCallback((dialog, view1, which, text) -> etGovermentIdtype.setText(text.toString()))
//                            .show();
//                }
//                break;
//            }
//        }
//    }

    private void updateprofile(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().updateprofile(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
                            if (userData.getData() != null) {
                                app.getPreferences().setLoggedInUser(userData);
                            }
                            finish();
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showLongToast(mContext, userData.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    public void AllOrganizations() {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().get_org_by_user(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    AllOrganizationsList_Data data = (AllOrganizationsList_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getData() != null && data.getData().size() > 0) {
                        allOrganizationsLists = data.getData();

                        binding.etOrganization.setOnClickListener(v -> {
                            if (allOrganizationsLists != null && !allOrganizationsLists.isEmpty()) {
                                List<String> orgNames = new ArrayList<>();
                                for (AllOrganizationsList model : allOrganizationsLists) {
                                    orgNames.add(model.getName());
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Select Organization");
                                builder.setItems(orgNames.toArray(new String[0]), (dialogInterface, which) -> {
                                    binding.etOrganization.setText(orgNames.get(which));
                                    // Optional: save selected organization ID if needed
//                                    selectedOrgId = allOrganizationsLists.get(which).getId();
                                });
                                builder.show();
                            } else {
                                binding.etOrganization.setText("self");
                                Toast.makeText(mContext, "No organizations available", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Utils.showLongToast(mContext, apiResponse);
//                    ToastDialog(apiResponse);
                }
            }));
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    private void generateEditTexts(LinearLayout container, int quantity) {
        container.removeAllViews(); // Clear previous EditTexts

        for (int i = 0; i < quantity; i++) {
            EditText editText = new EditText(this);

            // Set unique ID
            int uniqueId = generateViewId();
            editText.setId(uniqueId);

            // Set layout parameters if needed
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            editText.setLayoutParams(params);
            editText.setHint("EditText " + (i + 1));

            // Add EditText to the container
            container.addView(editText);
        }
    }

    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1;
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        }
    }


    private void showBankDialog() {
        // Create dialog
        bankDialog = new Dialog(this);
        bankDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bankDialog.setContentView(R.layout.dialog_bank_form);
        bankDialog.setCancelable(true);

        // Set dialog width and height
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(bankDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        bankDialog.getWindow().setAttributes(layoutParams);

        // Initialize dialog views
        initDialogViews();




        // Retrieve bank details from preferences
        BankDetails savedBankDetails = app.getPreferences().getBankDetails();

        if (savedBankDetails != null) {
            // Use the data
            String bankName = savedBankDetails.getBankname();
            String account = savedBankDetails.getAccount();
            String ifsc = savedBankDetails.getIfsc();
            String accType = savedBankDetails.getAcc_type();
            String branch = savedBankDetails.getBranch();
            String lastUpdated = savedBankDetails.getLastupdatedon();

            // You can pre-fill your form with this data
            etBankName.setText(bankName);
            etAccount.setText(account);
            etIfsc.setText(ifsc);
            etAccType.setText(accType);
            etBranch.setText(branch);
        } else {
            // No bank details saved yet
            Toast.makeText(this, "No bank details found", Toast.LENGTH_SHORT).show();
        }

        // Setup bank dropdown
        setupBankDropdown();

        // Setup account type dropdown
        setupAccountTypeDropdown();

        // Set button listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    Map<String, String> bankData = getBankData();
                    UpdateBankDetails(bankData);
                    bankDialog.dismiss();
                }
            }
        });

        // Show dialog
        bankDialog.show();
    }


    // In your dialog or activity
//    private void setupSaveButton() {
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (validateForm()) {
//                    Map<String, String> bankData = getBankData();
//
//                    // Show loading
//                    showProgressDialog();
//
//                    // Call API
//                    UpdateBankDetails(bankData);
//
//                    // Dismiss dialog
//                    if (bankDialog != null && bankDialog.isShowing()) {
//                        bankDialog.dismiss();
//                    }
//                }
//            }
//        });
//    }

    // Method to get form data
    private Map<String, String> getBankData() {
        Map<String, String> params = new HashMap<>();
        params.put("bankname", etBankName.getText().toString().trim());
        params.put("account", etAccount.getText().toString().trim());
        params.put("ifsc", etIfsc.getText().toString().trim());
        params.put("acc_type", etAccType.getText().toString().trim());
        params.put("branch", etBranch.getText().toString().trim());

        // Add user ID if needed
        UserData user = app.getPreferences().getLoggedInUser();
        if (user != null && user.getData().getUserId() != null) {
            params.put("userid", user.getData().getUserId());
        }

        return params;
    }

    /**
     * Initialize dialog views
     */
    private void initDialogViews() {
        etBankName = bankDialog.findViewById(R.id.etBankName);
        etAccount = bankDialog.findViewById(R.id.etAccount);
        etIfsc = bankDialog.findViewById(R.id.etIfsc);
        etAccType = bankDialog.findViewById(R.id.etAccType);
        etBranch = bankDialog.findViewById(R.id.etBranch);
        tilIfsc = bankDialog.findViewById(R.id.tilIfsc);
        btnSave = bankDialog.findViewById(R.id.btnSave11);
        btnCancel = bankDialog.findViewById(R.id.btnCancel11);
        tvBankNote = bankDialog.findViewById(R.id.tvBankNote);

        tilBankName = bankDialog.findViewById(R.id.tilBankName);
        tilAccountNumber = bankDialog.findViewById(R.id.tilAccountNumber);
        tilAccountType = bankDialog.findViewById(R.id.tilAccountType);
        tilBranch = bankDialog.findViewById(R.id.tilBranch);

//        tilBankName.setHint(GetTranslation("SelectBank", typeOfLanguage));
//        tilAccountNumber.setHint(GetTranslation("BankAccNo", typeOfLanguage));
//        tilAccountType.setHint(GetTranslation("BankAccountType", typeOfLanguage));
//        tilBranch.setHint(GetTranslation("BankBranch", typeOfLanguage));
//        tilIfsc.setHint(GetTranslation("IFSC", typeOfLanguage));
//        tvBankNote.setText(GetTranslation("Banknote", typeOfLanguage));
//        btnSave.setText(GetTranslation("Save", typeOfLanguage));
//        btnCancel.setText(GetTranslation("Close", typeOfLanguage));

        String bankNameHint = GetTranslation("SelectBank", typeOfLanguage);
        tilBankName.setHint(!TextUtils.isEmpty(bankNameHint) ? bankNameHint : "Select Bank");

        String accountNoHint = GetTranslation("BankAccNo", typeOfLanguage);
        tilAccountNumber.setHint(!TextUtils.isEmpty(accountNoHint) ? accountNoHint : "Account Number");

        String accountTypeHint = GetTranslation("BankAccountType", typeOfLanguage);
        tilAccountType.setHint(!TextUtils.isEmpty(accountTypeHint) ? accountTypeHint : "Account Type");

        String branchHint = GetTranslation("BankBranch", typeOfLanguage);
        tilBranch.setHint(!TextUtils.isEmpty(branchHint) ? branchHint : "Branch");

        String ifscHint = GetTranslation("IFSC", typeOfLanguage);
        tilIfsc.setHint(!TextUtils.isEmpty(ifscHint) ? ifscHint : "IFSC Code");

        String bankNote = GetTranslation("Banknote", typeOfLanguage);
        tvBankNote.setText(!TextUtils.isEmpty(bankNote) ? bankNote : "Note: You are solely responsible for submitting this details to which payment will be sent by the department");

        String saveText = GetTranslation("Save", typeOfLanguage);
        btnSave.setText(!TextUtils.isEmpty(saveText) ? saveText : "Save");

        String cancelText = GetTranslation("Close", typeOfLanguage);
        btnCancel.setText(!TextUtils.isEmpty(cancelText) ? cancelText : "Close");
    }

    private void setupBankDropdown() {
        // Add all bank names
        List<String> tempList = new ArrayList<>();
        tempList.add("State Bank of India");
        tempList.add("HDFC Bank");
        tempList.add("ICICI Bank");
        tempList.add("Axis Bank");
        tempList.add("Kotak Mahindra Bank");
        tempList.add("Yes Bank");
        tempList.add("Bank of Baroda");
        tempList.add("Punjab National Bank");
        tempList.add("Canara Bank");
        tempList.add("Union Bank of India");
        tempList.add("Indian Bank");
        tempList.add("Bank of India");
        tempList.add("Central Bank of India");
        tempList.add("UCO Bank");
        tempList.add("Bank of Maharashtra");
        tempList.add("Punjab & Sind Bank");
        tempList.add("Indian Overseas Bank");
        tempList.add("IDBI Bank");
        tempList.add("IDFC FIRST Bank");
        tempList.add("IndusInd Bank");
        tempList.add("Federal Bank");
        tempList.add("RBL Bank");
        tempList.add("South Indian Bank");
        tempList.add("Karnataka Bank");
        tempList.add("Karur Vysya Bank");
        tempList.add("City Union Bank");
        tempList.add("Tamilnad Mercantile Bank");
        tempList.add("DCB Bank");
        tempList.add("CSB Bank");
        tempList.add("Jammu & Kashmir Bank");
        tempList.add("Bandhan Bank");
        tempList.add("AU Small Finance Bank");
        tempList.add("Equitas Small Finance Bank");
        tempList.add("Ujjivan Small Finance Bank");
        tempList.add("ESAF Small Finance Bank");
        tempList.add("Suryoday Small Finance Bank");
        tempList.add("Utkarsh Small Finance Bank");
        tempList.add("Jana Small Finance Bank");
        tempList.add("North East Small Finance Bank");
        tempList.add("Shivalik Small Finance Bank");
        tempList.add("Capital Small Finance Bank");
        tempList.add("Fincare Small Finance Bank");
        tempList.add("Unity Small Finance Bank");
        tempList.add("Airtel Payments Bank");
        tempList.add("India Post Payments Bank");
        tempList.add("Fino Payments Bank");
        tempList.add("NSDL Payments Bank");
        tempList.add("Jio Payments Bank");
        tempList.add("Paytm Payments Bank");
        tempList.add("Standard Chartered Bank");
        tempList.add("HSBC India");
        tempList.add("Citibank");
        tempList.add("Deutsche Bank");
        tempList.add("DBS Bank India");
        tempList.add("Barclays Bank");
        tempList.add("Bank of America");
        tempList.add("BNP Paribas");
        tempList.add("JP Morgan Chase Bank");
        tempList.add("MUFG Bank");
        tempList.add("Mizuho Bank");
        tempList.add("Bank of Bahrain and Kuwait");
        tempList.add("Doha Bank");
        tempList.add("Mashreq Bank");
        tempList.add("American Express Banking Corp");
        tempList.add("Societe Generale");
        tempList.add("Credit Agricole Corporate & Investment Bank");
        tempList.add("Industrial and Commercial Bank of China");

        // Remove duplicates from the list
        bankList = removeDuplicates(tempList);

        bankAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bankList);
        etBankName.setAdapter(bankAdapter);
        etBankName.setThreshold(1);
    }

    // Helper method to remove duplicates
    private List<String> removeDuplicates(List<String> list) {
        Set<String> set = new LinkedHashSet<>(list); // LinkedHashSet maintains order
        return new ArrayList<>(set);
    }

    /**
     * Setup account type dropdown with Saving and Current options
     */
    private void setupAccountTypeDropdown() {
        // Clear the list first to prevent duplicates
        accountTypeList.clear();

        // Add account types
        accountTypeList.add("Saving");
        accountTypeList.add("Current");

        accountTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, accountTypeList);
        etAccType.setAdapter(accountTypeAdapter);
        etAccType.setThreshold(1);

        // Optional: Set default value
        // etAccType.setText("Saving", false);
    }

    /**
     * Setup IFSC code validation with auto-uppercase
     */
    /**
     * Validate all form fields
     */
    private boolean validateForm() {
        String bankName = etBankName.getText().toString().trim();
        String account = etAccount.getText().toString().trim();
        String ifsc = etIfsc.getText().toString().trim();
        String accType = etAccType.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();

        // Validate Bank Name
        if (bankName.isEmpty()) {
            etBankName.setError("Please select/enter bank name");
            etBankName.requestFocus();
            return false;
        }

        // Validate Account Number
        if (account.isEmpty()) {
            etAccount.setError("Please enter account number");
            etAccount.requestFocus();
            return false;
        }

        if (account.length() < 9 || account.length() > 18) {
            etAccount.setError("Account number should be 9-18 digits");
            etAccount.requestFocus();
            return false;
        }

        if (ifsc.isEmpty()) {
            tilIfsc.setError("Please Enter IFSC Code (e.g., SBIN0001234)");
            tilIfsc.requestFocus();
            return false;
        }

// Check IFSC length - must be exactly 11 characters
        if (ifsc.length() != 11) {
            tilIfsc.setError("IFSC Code must be exactly 11 characters (e.g., SBIN0001234)");
            tilIfsc.requestFocus();
            return false;
        }

//        Pattern ifscPattern = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");
//        if (!ifscPattern.matcher(ifsc).matches()) {
//            tilIfsc.setError("Invalid IFSC Code format (e.g., SBIN0001234)");
//            tilIfsc.requestFocus();
//            return false;
//        }

        // Validate Account Type
        if (accType.isEmpty()) {
            etAccType.setError("Please select account type");
            etAccType.requestFocus();
            return false;
        }

        // Validate Branch
        if (branch.isEmpty()) {
            etBranch.setError("Please enter branch");
            etBranch.requestFocus();
            return false;
        }

        return true;
    }


//    private void UpdateBankDetails(Map<String, String> params) {
//        if (cd.isConnectingToInternet()) {
//            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
//            cpd.show();
//            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().updatebankdetails(params), new ApiRequestHelper.OnRequestComplete() {
//                @SuppressLint("SuspiciousIndentation")
//                @Override
//                public void onSuccess(Object object) {
//
//                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
//                        cpd.dismiss();
//                    }
//                    UserData response = (UserData) object;
//                    if (response != null) {
//                        if (response.getResponsecode() == 200) {
//                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
//                                Utils.showLongToast(mContext, response.getMessage());
////
//                            BankDetails data = response.getBankDetails();
//                            if (data != null) {
//                                response.setBankDetails(data);
//                                app.getPreferences().setLoggedInUser(response);
//                            }
//                        } else {
//                            if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
//                                Utils.showLongToast(mContext, response.getMessage());
//                        }
//                    } else {
//                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
//                    }
//                }
//
//                @Override
//                public void onFailure(String apiResponse) {
//
//                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
//                        cpd.dismiss();
//                    }
//                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
//                }
//            });
//        } else {
//            Utils.alert_dialog(mContext);
//        }
//    }


private void UpdateBankDetails(Map<String, String> params) {
    if (cd.isConnectingToInternet()) {
        final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
        cpd.show();

        app.getApiRequestHelper().callRetrofit(
                app.getApiRequestHelper().getAppService().updatebankdetails(params),
                new ApiRequestHelper.OnRequestComplete() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onSuccess(Object object) {
                        if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                            cpd.dismiss();
                        }

                        UserData response = (UserData) object;
                        if (response != null) {
                            if (response.getResponsecode() == 200) {
                                // Show success message
                                if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage())) {
                                    Utils.showLongToast(mContext, response.getMessage());
                                }

                                // Get BankDetails from response
                                BankDetails bankDetails = response.getBankDetails(); // Changed from getBankDetails()

                                if (bankDetails != null) {
                                    // Save BankDetails to preferences
                                    app.getPreferences().setBankDetails(bankDetails);

                                    // If you want to update user data as well
                                    UserData currentUser = app.getPreferences().getLoggedInUser();
                                    if (currentUser != null) {
                                        currentUser.setBankDetails(bankDetails);
                                        app.getPreferences().setLoggedInUser(currentUser);
                                    }

                                    // Log saved data for debugging
                                    Log.d("BankDetails", "Saved to SharedPreferences: " +
                                            bankDetails.getBankname() + ", " +
                                            bankDetails.getAccount());
                                }

                            } else {
                                // Show error message from API
                                if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage())) {
                                    Utils.showLongToast(mContext, response.getMessage());
                                } else {
                                    Utils.showLongToast(mContext, "Failed to update bank details");
                                }
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
                        if (!TextUtils.isEmpty(apiResponse)) {
                            Utils.showLongToast(mContext, apiResponse);
                        } else {
                            Utils.showLongToast(mContext, "Network error. Please try again.");
                        }
                    }
                }
        );
    } else {
        Utils.alert_dialog(mContext);
    }
}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Utils.hideSoftKeyboard(this);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
