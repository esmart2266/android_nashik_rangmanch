package in.gov.nmc.rangmach;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityRegisterBinding;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    //    @BindView(R.id.et_email)
//    MaterialEditText etEmail;
//    @BindView(R.id.et_password)
//    MaterialEditText etPassword;
//    @BindView(R.id.btn_register)
//    Button btnRegister;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    //    @BindView(R.id.et_firstName)
//    MaterialEditText etFirstName;
//    @BindView(R.id.et_middleName)
//    MaterialEditText etMiddleName;
//    @BindView(R.id.et_lastName)
//    MaterialEditText etLastName;
//    @BindView(R.id.et_mobile)
//    MaterialEditText etMobile;
//    @BindView(R.id.et_address)
//    MaterialEditText etAddress;
//    @BindView(R.id.et_country)
//    MaterialEditText etCountry;
//    @BindView(R.id.et_state)
//    MaterialEditText etState;
//    @BindView(R.id.et_city)
//    MaterialEditText etCity;
//    @BindView(R.id.et_zipcode)
//    MaterialEditText etZipcode;
    String[] cities = {"Amravati", "Aurangabad", "Ahmednagar", "Buldhana", "Beed", "Bhandara", "Chandrapur", "Dhule", "Gadchiroli", "Gondia",
            "Hingoli", "Jalna", "Jalgaon", "Kolhapur", "Latur", "Mumbai", "Mumbai Suburban", "Nanded", "Nagpur", "Nandurbar", "Nashik", "Osmanabad",
            "Parbhani", "Pune", "Palghar", "Raigad", "Ratnagiri", "Sindhudurg", "Satara", "Solapur", "Sangli", "Wardha", "Washim", "Yavatmal"};
//    @BindView(R.id.ll_toolbar)
//    LinearLayout llToolbar;
//    @BindView(R.id.etFirstName)
//    MaterialEditText etFirstName;
//    @BindView(R.id.etLastName)
//    MaterialEditText etLastName;
//    @BindView(R.id.etEmail)
//    MaterialEditText etEmail;
//    @BindView(R.id.etMobile)
//    MaterialEditText etMobile;
//    @BindView(R.id.etBirthDate)
//    MaterialEditText etBirthDate;
//    @BindView(R.id.rrBirthDate)
//    RelativeLayout rrBirthDate;
//    @BindView(R.id.etState)
//    MaterialEditText etState;
//    @BindView(R.id.rrState)
//    RelativeLayout rrState;
//    @BindView(R.id.etCity)
//    MaterialEditText etCity;
//    @BindView(R.id.rrCity)
//    RelativeLayout rrCity;
//    @BindView(R.id.etPassword)
//    MaterialEditText etPassword;
//    @BindView(R.id.etConfPassword)
//    MaterialEditText etConfPassword;
//    @BindView(R.id.etAddress)
//    MaterialEditText etAddress;
//    @BindView(R.id.etPincode)
//    MaterialEditText etPincode;
//    @BindView(R.id.btnSubmit)
//    Button btnSubmit;
    private String dob = "";

    protected ActivityRegisterBinding inflateBinding(LayoutInflater inflater) {
        return ActivityRegisterBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        setSupportActionBar(binding.toolbarContainer.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbarContainer.toolbarTitle.setText("Register");

        binding.etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mMaxDate = Calendar.getInstance();
                int mMaxYear = mMaxDate.get(Calendar.YEAR);
                mMaxYear -= 18;
                mMaxDate.set(Calendar.YEAR, mMaxYear);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view12, year, month, dayOfMonth) -> {
                    String date = "" + dayOfMonth + "/" + (month + 1) + "/" + year;
                    dob = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
                    binding.etBirthDate.setText(date);
                }, mMaxDate.get(Calendar.YEAR), mMaxDate.get(Calendar.MONTH), mMaxDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(mMaxDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        binding.etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cities != null && cities.length > 0) {
                    new MaterialDialog.Builder(mContext)
                            .title("Select City")
                            .items(cities)
                            .itemsCallback((dialog, view1, which, text) ->  binding.etCity.setText(text.toString()))
                            .show();
                }
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName =  binding.etFirstName.getText().toString().trim();
                String lastName =  binding.etLastName.getText().toString().trim();
                String email =  binding.etEmail.getText().toString().trim();
                String mobile =  binding.etMobile.getText().toString().trim();
                String birthdate =  binding.etBirthDate.getText().toString().trim();
                String password =  binding.etPassword.getText().toString().trim();
                String confPassword =  binding.etConfPassword.getText().toString().trim();
                String address =  binding.etAddress.getText().toString().trim();
                String city =  binding.etCity.getText().toString().trim();
                String zipcode =  binding.etPincode.getText().toString().trim();
                if (TextUtils.isEmpty(firstName)) {
                    binding.etFirstName.setError("Enter First Name");
                    binding.etFirstName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    binding.etLastName.setError("Enter Last Name");
                    binding.etLastName.requestFocus();
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
                if (TextUtils.isEmpty(password)) {
                    binding.etPassword.setError("Enter password");
                    binding.etPassword.requestFocus();
                    return;
                }
                if (password.length() < 5) {
                    binding.etPassword.setError("Password must be at least 5 characters of length");
                    binding.etPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(confPassword)) {
                    binding.etConfPassword.setError("Enter password again");
                    binding.etConfPassword.requestFocus();
                    return;
                }
                if (!password.equals(confPassword)) {
                    binding.etPassword.setError("Password does not matches with confirm password");
                    binding.etPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(birthdate)) {
                    binding.etBirthDate.setError("Select BirthDate");
                    binding.etBirthDate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    binding.etCity.setError("Select City");
                    binding.etCity.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    binding.etAddress.setError("Enter Address");
                    binding.etAddress.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(zipcode)) {
                    binding.etPincode.setError("Enter Pin code");
                    binding.etPincode.requestFocus();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("firstname", firstName);
                map.put("lastname", lastName);
                map.put("mobile", mobile);
                map.put("email", email);
                map.put("address", address);
                map.put("city", city);
                map.put("birthdate", dob);
                map.put("state", "Maharashtra");
                map.put("country", "India");
                map.put("pincode", zipcode);
                map.put("password", password);
                register(map);
            }
        });
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_register;
//    }

//    @OnClick({R.id.etBirthDate, R.id.btnSubmit, R.id.etCity})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.etBirthDate: {
//                Calendar mMaxDate = Calendar.getInstance();
//                int mMaxYear = mMaxDate.get(Calendar.YEAR);
//                mMaxYear -= 18;
//                mMaxDate.set(Calendar.YEAR, mMaxYear);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, (view12, year, month, dayOfMonth) -> {
//                    String date = "" + dayOfMonth + "/" + (month + 1) + "/" + year;
//                    dob = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
//                    etBirthDate.setText(date);
//                }, mMaxDate.get(Calendar.YEAR), mMaxDate.get(Calendar.MONTH), mMaxDate.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMaxDate(mMaxDate.getTimeInMillis());
//                datePickerDialog.show();
//                break;
//            }
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
//            case R.id.btnSubmit: {
//                String firstName = etFirstName.getText().toString().trim();
//                String lastName = etLastName.getText().toString().trim();
//                String email = etEmail.getText().toString().trim();
//                String mobile = etMobile.getText().toString().trim();
//                String birthdate = etBirthDate.getText().toString().trim();
//                String password = etPassword.getText().toString().trim();
//                String confPassword = etConfPassword.getText().toString().trim();
//                String address = etAddress.getText().toString().trim();
//                String city = etCity.getText().toString().trim();
//                String zipcode = etPincode.getText().toString().trim();
//                if (TextUtils.isEmpty(firstName)) {
//                    etFirstName.setError("Enter First Name");
//                    etFirstName.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(lastName)) {
//                    etLastName.setError("Enter Last Name");
//                    etLastName.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(email)) {
//                    etEmail.setError("Enter Email");
//                    etEmail.requestFocus();
//                    return;
//                }
//                if (!Utils.isValidEmail(email)) {
//                    etEmail.setError("Enter Valid Email");
//                    etEmail.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(mobile)) {
//                    etMobile.setError("Enter Mobile Number");
//                    etMobile.requestFocus();
//                    return;
//                }
//                if (mobile.length() < 10) {
//                    etMobile.setError("Mobile Number must be 10 digits");
//                    etMobile.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    etPassword.setError("Enter password");
//                    etPassword.requestFocus();
//                    return;
//                }
//                if (password.length() < 5) {
//                    etPassword.setError("Password must be at least 5 characters of length");
//                    etPassword.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(confPassword)) {
//                    etConfPassword.setError("Enter password again");
//                    etConfPassword.requestFocus();
//                    return;
//                }
//                if (!password.equals(confPassword)) {
//                    etPassword.setError("Password does not matches with confirm password");
//                    etPassword.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(birthdate)) {
//                    etBirthDate.setError("Select BirthDate");
//                    etBirthDate.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(city)) {
//                    etCity.setError("Select City");
//                    etCity.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(address)) {
//                    etAddress.setError("Enter Address");
//                    etAddress.requestFocus();
//                    return;
//                }
//                if (TextUtils.isEmpty(zipcode)) {
//                    etPincode.setError("Enter Pin code");
//                    etPincode.requestFocus();
//                    return;
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("firstname", firstName);
//                map.put("lastname", lastName);
//                map.put("mobile", mobile);
//                map.put("email", email);
//                map.put("address", address);
//                map.put("city", city);
//                map.put("birthdate", dob);
//                map.put("state", "Maharashtra");
//                map.put("country", "India");
//                map.put("pincode", zipcode);
//                map.put("password", password);
//                register(map);
//                break;
//            }
//        }
//    }

    private void register(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().userregister(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
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
