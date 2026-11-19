package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import in.gov.nmc.rangmach.adapter.AllEventsAdapter;
import in.gov.nmc.rangmach.adapter.CustomPagerAdapter;
import in.gov.nmc.rangmach.adapter.CustomSpinnerAdapter;
import in.gov.nmc.rangmach.adapter.MyViewPagerAdapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;

import in.gov.nmc.rangmach.databinding.ActivityMainBinding;
import in.gov.nmc.rangmach.model.AdData;
import in.gov.nmc.rangmach.model.BankDetails;
import in.gov.nmc.rangmach.model.Centers;
import in.gov.nmc.rangmach.model.Data;
import in.gov.nmc.rangmach.model.LanguagePOJO;
import in.gov.nmc.rangmach.model.LanguagePOJO_Data;
import in.gov.nmc.rangmach.model.NowShowing;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    public static LinearLayout headerLayout;
    private ArrayList<Centers> centersList;
    MyViewPagerAdapter myViewPagerAdapter;
    public static MainActivity activity;
    private static final int MESSAGE_SCROLL = 123;
    private String currUserId;
    private ImageView ivProfile;
    private EasyImage easyImage;
    private String imagePath = "";

    public static List<LanguagePOJO> languagePOJOS;

    private AllEventsAdapter allEventsAdapter;
    private List<NowShowing> nowShowingList = new ArrayList<>();
    private List<NowShowing> MainShowingList = new ArrayList<>();
    private List<NowShowing> MainComingSoonList = new ArrayList<>();
    private List<NowShowing> comingsoon = new ArrayList<>();

    private Set<String> theater = new TreeSet();
    private Set<String> language = new TreeSet();
    private Set<String> category = new TreeSet();

    public static Set<String> selectedtheater = new TreeSet();
    public static Set<String> selectedlanguage = new TreeSet();
    public static Set<String> selectedcategory = new TreeSet();

    String selected_lang;
    public static int typeOfLanguage;
    //    public final static String[] languages = {"Select Language", "English", "Hindi", "Marathi"};
    //hindi is currently close
    public final static String[] languages = {"Select Language", "English", "Marathi"};

    Boolean isNowShowingSelected = true;

    protected ActivityMainBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            );
//        }

        activity = this;
        easyImage = new EasyImage.Builder(mContext).allowMultiple(false).build();
        headerLayout = findViewById(R.id.headerLayout);

//        // Create tabs and set their text
//        TabLayout.Tab tab1 = tabLayout.newTab().setText("Tab 1");
//        TabLayout.Tab tab2 = tabLayout.newTab().setText("Tab 2");
//       // Add tabs to the TabLayout
//        tabLayout.addTab(tab1);
//        tabLayout.addTab(tab2);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selected_lang = sharedPreferences.getString("languagekey", "English");

        Menu menu = binding.navView.getMenu();

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }


        TabLayout.Tab tab1 = binding.tabLayout.newTab().setText(GetTranslation("Hall/Assets", typeOfLanguage));
//        TabLayout.Tab tab2 = binding.tabLayout.newTab().setText(GetTranslation("Now Showing", typeOfLanguage));

        binding.tabLayout.addTab(tab1);
//        binding.tabLayout.addTab(tab2);

        binding.textFood.setText(GetTranslation("Food", typeOfLanguage));
        binding.textParking.setText(GetTranslation("Parking", typeOfLanguage));

        menu.findItem(R.id.nav_home).setTitle(GetTranslation("Home", typeOfLanguage));
        menu.findItem(R.id.nav_my_orders).setTitle(GetTranslation("My Bookings", typeOfLanguage));
        menu.findItem(R.id.nav_my_profile).setTitle(GetTranslation("My Profile", typeOfLanguage));
        menu.findItem(R.id.nav_contact_us).setTitle(GetTranslation("Contact us", typeOfLanguage));

        menu.findItem(R.id.nav_my_refund).setTitle(GetTranslation("My Refund", typeOfLanguage));
        menu.findItem(R.id.nav_my_penalty).setTitle(GetTranslation("My Penalty", typeOfLanguage));
        menu.findItem(R.id.nav_my_payments).setTitle(GetTranslation("My Payments", typeOfLanguage));
        menu.findItem(R.id.nav_my_FoodOrders).setTitle(GetTranslation("My Food Orders", typeOfLanguage));
        menu.findItem(R.id.nav_delete_myaccount).setTitle(GetTranslation("Delete My Account", typeOfLanguage));
        menu.findItem(R.id.nav_Car_Parking_Report).setTitle(GetTranslation("Car Parking Report", typeOfLanguage));

        menu.findItem(R.id.nav_feedback).setTitle(GetTranslation("Feedback", typeOfLanguage));
        menu.findItem(R.id.nav_logout).setTitle(GetTranslation("LogOut", typeOfLanguage));
        menu.findItem(R.id.nav_change_language).setTitle(GetTranslation("Change Language", typeOfLanguage));

        binding.llCarparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CarParking_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        binding.llFoodorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, FoodOrder_Activity.class)
//                        .putExtra("event", (Parcelable) centersList));

                Intent intent = new Intent(MainActivity.this, FoodOrder_Activity.class);
                intent.putParcelableArrayListExtra("data", centersList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    new MaterialDialog.Builder(MainActivity.this)
                            .content("Are you sure you want to exit?")
                            .positiveText("Yes")
                            .negativeText("No")
                            .onPositive((dialog, which) -> {
                                dialog.dismiss();
                                finish(); // Close the activity
                            })
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .show();
                }
            }
        });

           /* if (selected_lang.equals("Hindi")) {
            setLocal(MainActivity.this, "hi");

            toolbarTitle.setText("होम");

            TabLayout.Tab tab1 = tabLayout.newTab().setText("हॉल/एसेट्स");
            TabLayout.Tab tab2 = tabLayout.newTab().setText("अभी प्रदर्शित है");
            tabLayout.addTab(tab1);
            tabLayout.addTab(tab2);

            menu.findItem(R.id.nav_home).setTitle("होम");
            menu.findItem(R.id.nav_my_orders).setTitle("मेरी बुकिंग्ज");
            menu.findItem(R.id.nav_my_profile).setTitle("मेरी प्रोफाइल");
            menu.findItem(R.id.nav_my_refund).setTitle("वापसी");
            menu.findItem(R.id.nav_my_penalty).setTitle("दंड");
            menu.findItem(R.id.nav_my_payments).setTitle("भुगतान");
            menu.findItem(R.id.nav_contact_us).setTitle("हमसे संपर्क करें");
            menu.findItem(R.id.nav_feedback).setTitle("प्रतिक्रिया");
            menu.findItem(R.id.nav_logout).setTitle("लॉग आउट");
            menu.findItem(R.id.nav_change_language).setTitle("भाषा बदलें");

        } else if (selected_lang.equals("Marathi")) {
            setLocal(MainActivity.this, "mr");

            toolbarTitle.setText("होम");
            TabLayout.Tab tab1 = tabLayout.newTab().setText("हॉल/असेट्स");
            TabLayout.Tab tab2 = tabLayout.newTab().setText("आता प्रदर्शित");
            tabLayout.addTab(tab1);
            tabLayout.addTab(tab2);

            menu.findItem(R.id.nav_home).setTitle("होम");
            menu.findItem(R.id.nav_my_orders).setTitle("माझ्या बुकिंग्ज");
            menu.findItem(R.id.nav_my_profile).setTitle("माझी प्रोफाइल");
            menu.findItem(R.id.nav_my_refund).setTitle("परतावा");
            menu.findItem(R.id.nav_my_penalty).setTitle("दंड");
            menu.findItem(R.id.nav_my_payments).setTitle("पेमेंट्स");
            menu.findItem(R.id.nav_contact_us).setTitle("आम्हाला संपर्क साधा");
            menu.findItem(R.id.nav_feedback).setTitle("अभिप्रेती");
            menu.findItem(R.id.nav_logout).setTitle("लॉग आउट");
            menu.findItem(R.id.nav_change_language).setTitle("भाषा बदला");

        } else {
            setLocal(MainActivity.this, "en");

            toolbarTitle.setText("Home");
            TabLayout.Tab tab1 = tabLayout.newTab().setText("Hall/Assets");
            TabLayout.Tab tab2 = tabLayout.newTab().setText("Now Showing");
            tabLayout.addTab(tab1);
            tabLayout.addTab(tab2);

            menu.findItem(R.id.nav_home).setTitle("Home");
            menu.findItem(R.id.nav_my_orders).setTitle("My Bookings");
            menu.findItem(R.id.nav_my_profile).setTitle("My Profile");
            menu.findItem(R.id.nav_contact_us).setTitle("Contact Us");

            menu.findItem(R.id.nav_my_refund).setTitle("My Refund");
            menu.findItem(R.id.nav_my_penalty).setTitle("My Penalty");
            menu.findItem(R.id.nav_my_payments).setTitle("My Payments");

            menu.findItem(R.id.nav_feedback).setTitle("Feedback");
            menu.findItem(R.id.nav_logout).setTitle("Log Out");
            menu.findItem(R.id.nav_change_language).setTitle("Change Language");
        }

            */

//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

//        int spanCount = 2; // Number of columns in the grid
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, spanCount);
//        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setItemIconTintList(null);

        myViewPagerAdapter = new MyViewPagerAdapter(this);
        binding.mainViewPager.setAdapter(myViewPagerAdapter);

        binding.mainViewPager.setUserInputEnabled(false);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.getTabAt(position).select();
            }
        });

        login();
        if (LoginActivity.isLangaugeApiCall == false) {
            LanguageAPI();
        }

        //allNowShowingShows(params);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            // Call both methods when swipe-to-refresh is triggered
            try {
                if (app.getPreferences().getLoggedInUser().getData() != null) {
                    Map<String, String> tempparams = new HashMap<>();
                    tempparams.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
                    alladvertisements(tempparams);
                    tempparams = null;
                } else {
                    Log.e("MainActivity", "Logged-in user or user data is null!");
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            } catch (Exception e) {
            }
        });

//        nowshowing_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                nowShowingList = MainShowingList;
////                allEventsAdapter.addData(MainShowingList);
////                allEventsAdapter.notifyDataSetChanged();
//                isNowShowingSelected = true;
//                ApplyFilter();
//            }
//        });

//        upcoming_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            nowShowingList = comingsoon;
//             allEventsAdapter.addData(comingsoon);
//               allEventsAdapter.notifyDataSetChanged();
//                isNowShowingSelected = false;
//                ApplyFilter();
//            }
//        });
        updateData();
        try {
//            ivProfile = navigationView.getHeaderView(0).findViewById(R.id.ivProfile);
            TextView tvName = binding.navView.getHeaderView(0).findViewById(R.id.tvName);
//            GlideApp.with(mContext).load(storageReference.child("ProfileImages/" + currUserId + ".png"))
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .error(R.drawable.user_icon)
//                    .into(ivProfile);
            String name = app.getPreferences().getLoggedInUser().getData().getName();
            tvName.setText(name);
//            ivProfile.setOnClickListener(v -> set_image());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void updateData() {
        try {
            currUserId = app.getPreferences().getLoggedInUser().getData().getUserId();
            ivProfile = binding.navView.getHeaderView(0).findViewById(R.id.ivProfile);
            TextView tvName = binding.navView.getHeaderView(0).findViewById(R.id.tvName);
            ImageView ivrightarrow = binding.navView.getHeaderView(0).findViewById(R.id.ivrightarrow);
            Glide.with(mContext).load(BuildConfig.BASE_URL + BuildConfig.API_PATH + "patientpics/" + currUserId + ".jpg").skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.user_icon).into(ivProfile);
            tvName.setText(app.getPreferences().getLoggedInUser().getData().getName());
            ivProfile.setOnClickListener(v -> set_image());
            ivrightarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }, 250);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void set_image() {
        String[] items = new String[]{"View Image"};
        new AlertDialog.Builder(mContext).setTitle("Select Picture").setItems(items, (dialog, item) -> {
            switch (items[item]) {
                case "View Image":
                    Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                    intent.putExtra("title", "Preview");
                    intent.putExtra("urlPhotoClick", BuildConfig.BASE_URL + BuildConfig.API_PATH + "patientpics/" + currUserId + ".jpg");
                    mContext.startActivity(intent);
                    break;
                case "Camera":
                    easyImage.openCameraForImage(this);
                    break;
                case "Gallery":
                    easyImage.openGallery(this);
                    break;
                case "Cancel":
                    dialog.dismiss();
                    break;
            }
        }).show();
    }

//    private void uploadImage() {
//        String strFile = "";
//
//        try {
//            Bitmap bm = new Compressor(mContext).compressToBitmap(new File(imagePath));
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            if (bm != null) {
//                bm.compress(Bitmap.CompressFormat.JPEG, 40, baos); //bm is the bitmap object
//                byte[] b = baos.toByteArray();
//                strFile = Base64.encodeToString(b, Base64.DEFAULT);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Map<String, RequestBody> map = new HashMap<>();
//
//        map.put("imageName", createPartFromString("patientpics/" + currUserId + ".jpg"));
//        map.put("angle", createPartFromString("0"));
//        map.put("imageData", createPartFromString(!TextUtils.isEmpty(strFile) ? strFile : ""));
//        if (cd.isConnectingToInternet()) {
//            CustomProgressDialog pd = new CustomProgressDialog(mContext);
//            pd.show();
//            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().uploadimage(map), new ApiRequestHelper.OnRequestComplete() {
//                @Override
//                public void onSuccess(Object object) {
//                    if (pd != null && pd.isShowing()) pd.dismiss();
//                    UserResponse response = (UserResponse) object;
//                    if (response.getMessage() != null && !TextUtils.isEmpty(response.getMessage()))
//                        Utils.showShortToast(mContext, response.getMessage());
//                }
//
//                @Override
//                public void onFailure(String apiResponse) {
//                    if (pd != null && pd.isShowing()) pd.dismiss();
//                }
//            });
//        } else {
//            Utils.alert_dialog(mContext);
//        }
//    }

    public void LogoutAlertDialog() {
        new AlertDialog.Builder(mContext).setTitle(GetTranslation("Are you sure?", typeOfLanguage)).setMessage(GetTranslation("Do you want to logout?", typeOfLanguage)).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        logout();
                        Logout_from_GCM();
                    }
                })
                .setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    private void logout() {
        app.getPreferences().logOutUser();
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    private void Logout_from_GCM() {
        if (cd.isConnectingToInternet()) {
            final CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Map<String, String> params = new HashMap<>();
            params.put("userid", app.getPreferences().getLoggedInUser().getData().userId);
            if (!TextUtils.isEmpty(app.getPreferences().getToken()))
                params.put("deviceId", app.getPreferences().getToken());
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().logout(params), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd != null && pd.isShowing()) pd.dismiss();
                    UserData response = (UserData) object;
                    if (response != null) {
                        if (response.getResponsecode() == 200) {
                            logout();
                        } else {
                            logout();
//                            Utils.showLongToast(mContext, response.message);
                        }
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd != null && pd.isShowing()) pd.dismiss();
                    if (!TextUtils.isEmpty(apiResponse)) {
                        Utils.showLongToast(mContext, apiResponse);
                    }
                    logout();
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                    imagePath = imageFiles[0].getFile().getAbsolutePath();
//                    uploadImage();
                }

                @Override
                public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                    //Some error handling
                    error.printStackTrace();
                }

                @Override
                public void onCanceled(@NonNull MediaSource source) {
                    //Not necessary to remove any files manually anymore
                }
            });
        }
    }

    private void login() {
        if (app.getPreferences().getLoggedInUser() != null && app.getPreferences().getLoggedInUser().getData() != null && !TextUtils.isEmpty(app.getPreferences().getLoggedInUser().getData().getMobileNo())) {
            if (cd.isConnectingToInternet()) {
                final CustomProgressDialog pd = new CustomProgressDialog(mContext);
                pd.show();
                Map<String, String> params = new HashMap<>();
                params.put("usrname", app.getPreferences().getLoggedInUser().getData().getMobileNo());
                params.put("devicetype", "android");
                params.put("uuid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("appversion", String.valueOf(BuildConfig.VERSION_CODE));
                params.put("mobile_model", Utils.getDeviceName());
                app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().login(params), new ApiRequestHelper.OnRequestComplete() {
                    @Override
                    public void onSuccess(Object object) {
                        if (pd != null && pd.isShowing()) pd.dismiss();
                        UserData response = (UserData) object;
                        if (response != null) {
                            try {
                                if (response.getResponsecode() != 200) {
                                    logout();
                                    showAlertMessage("Alert", response.getMessage());
                                } else {
                                    if (response.getData() != null) {
                                        Data data = response.getData();
                                        BankDetails bankDetails = response.getBankDetails();

                                        data.setMobileNo(app.getPreferences().getLoggedInUser().getData().getMobileNo());
                                        response.setData(data);
                                        app.getPreferences().setLoggedInUser(response);

                                        if (bankDetails != null) {
                                            response.setBankDetails(bankDetails);
                                            app.getPreferences().setBankDetails(bankDetails);
                                        }

                                        Map<String, String> params = new HashMap<>();
                                        if (app.getPreferences().getLoggedInUser().getData() != null) {
                                            params.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());

                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Call your function here
                                                    alladvertisements(params);
                                                }
                                            }, 2000);

                                        } else {
                                            Log.e("MainActivity", "Logged-in user or user data is null!");
                                        }

                                        binding.toolbarTitle.setText(GetTranslation("Welcome", typeOfLanguage));
                                        try {
                                            String LoginUserName = app.getPreferences().getLoggedInUser().getData().getName();
                                            binding.toolbarUserName.setText(LoginUserName);
                                        } catch (Exception e) {
                                            String LoginUserName = app.getPreferences().getLoggedInUser().getData().getName();
                                            binding.toolbarUserName.setText("Welcome");
                                        }


                                        // if (response.getData().getIsActive().equalsIgnoreCase("0"))
                                        //     logout();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace(); // Log the exception for debugging purposes
                                // Optionally, handle specific exceptions here
                                // For example: if (e instanceof NullPointerException) { ... }
                                // You can also display a user-friendly message

                                showToast(mContext, "An error occurred while processing the response.", 2);
                            }
                        } else {
                            // logout();
                            showToast(mContext, "An error occurred, please reload the application.", 2);
                        }
                    }

                    @Override
                    public void onFailure(String apiResponse) {
                        if (pd != null && pd.isShowing()) pd.dismiss();
                        if (!TextUtils.isEmpty(apiResponse)) {
                            Utils.showLongToast(mContext, apiResponse);
                        }
                        showToast(mContext, "An error occurred, please reload the application.", 2);
                    }
                });
            } else {
                Utils.alert_dialog(mContext);
            }
        } else {
            logout();
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
//                        Toast.makeText(mContext, "Language Call DOne", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (cpd != null && cpd.isShowing() && !isFinishing() && !isDestroyed()) {
                        cpd.dismiss();
                    }
                    Utils.showLongToast(mContext, "something went wromg");
                }
            }));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.ss
        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }

        } else if (id == R.id.nav_my_orders) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MyAssetsOder_Activity.class);
                intent.putExtra("centerid", centersList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);


        } else if (id == R.id.nav_my_refund) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MyRefund_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);


        } else if (id == R.id.nav_my_FoodOrders) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MyFoodOrders_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_Car_Parking_Report) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, CarParking_Reports_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_my_penalty) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MyPenalty_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_my_payments) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MyPayments_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        }
//       else if (id == R.id.nav_book_theater) {

//        Intent intent = new Intent(MainActivity.this, TheaterBooking_Activity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//       }
        else if (id == R.id.nav_my_profile) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_contact_us) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_feedback) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, FeedBack_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }, 250);

        } else if (id == R.id.nav_change_language) {
            language_dialog();
        } else if (id == R.id.nav_delete_myaccount) {
//            logout();
//            LogoutAlertDialog();
            DeleteMyAccount_Dialog();
        } else if (id == R.id.nav_logout) {
//            logout();
            LogoutAlertDialog();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void alladvertisements(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<AdData> call = app.getApiRequestHelper().getAppService().alladvertisements(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (pd.isShowing()) pd.dismiss();
                    AdData adData = (AdData) object;
                    Log.e("in", "success");
                    if (adData != null && adData.getResponsecode() == 200) {
                        if (adData.getData() != null && adData.getData().size() > 0) {

                            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(mContext, adData.getData());
                            binding.loopViewPager.setAdapter(customPagerAdapter);

                            centersList = (ArrayList<Centers>) adData.getCentersList();
//                                circlePageIndicator.setViewPager(viewpager);
                            if (adData.getData().size() > 1) {
                                set_slider_animation();
                            } else {
                                try {
                                    stopAutoPlay();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    Toast.makeText(mContext, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                            binding.loopViewPager.setVisibility(View.VISIBLE);
                        } else {
                            binding.loopViewPager.setVisibility(View.GONE);
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void DeleteMyAccount_Dialog() {
        new AlertDialog.Builder(MainActivity.this)  // replace YourActivity with the actual activity class
                .setTitle("Request subnitted")
                .setMessage("Your request for account deletion will be reviewed. We will get back to you soon on email/phone")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform delete account action here
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void set_slider_animation() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            int animationDuration = 1000;
            FixedSpeedScroller scroller = new FixedSpeedScroller(binding.loopViewPager.getContext(), new AccelerateDecelerateInterpolator(), animationDuration);
            mScroller.set(binding.loopViewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (binding.loopViewPager != null && binding.loopViewPager.getAdapter() != null && binding.loopViewPager.getAdapter().getCount() > 1)
            startAutoPlay();
        else stopAutoPlay();
        binding.loopViewPager.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (binding.loopViewPager != null && binding.loopViewPager.getAdapter() != null && binding.loopViewPager.getAdapter().getCount() > 1)
                        startAutoPlay();
                    else stopAutoPlay();
                    break;

            }
            return false;
        });
    }

    public class FixedSpeedScroller extends Scroller {
        int duration;

        public FixedSpeedScroller(Context context, int duration) {
            super(context);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel, int duration) {
            super(context, interpolator, flywheel);
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SCROLL) {
                if (binding.loopViewPager != null) {
                    binding.loopViewPager.setCurrentItem(binding.loopViewPager.getCurrentItem() + 1);
                    startAutoPlay();
                }
            }
        }
    };

    public void stopAutoPlay() {
        handler.removeMessages(MESSAGE_SCROLL);
    }

    public void startAutoPlay() {
        stopAutoPlay();
        int homeColumnScrollInterval = 5;
        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL, homeColumnScrollInterval * 1000);
    }

    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_mark_visit, menu);
//        return true;
//    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }


    public void language_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
//                    setLocal(MainActivity.this, "en");

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("languagekey", selectlang);
                    editor.apply();

                    finish();
                    startActivity(getIntent());

                } else if (selectlang.equals("Hindi")) {
//                    setLocal(MainActivity.this, "hi");

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("languagekey", selectlang);
                    editor.apply();

                    finish();
                    startActivity(getIntent());

                } else if (selectlang.equals("Marathi")) {
//                    setLocal(MainActivity.this, "mr");

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

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        ApplyFilter();
        login();
    }

//    public void Theatercount(int position) {
////        Toast.makeText(mContext, "ROSHAN BHAMARE"+position, Toast.LENGTH_SHORT).show();
////        ApplyFilter();
//    }

//    public void ApplyFilter() {
//
//        if(allEventsAdapter == null) {
//            return;
//        }
//        List<NowShowing> nowShowingList = new ArrayList<>();
//        for (NowShowing obj : MainShowingList) {
//            if(selectedtheater.contains(obj.getName()) && selectedcategory.contains(obj.getCategory()) && selectedlanguage.contains(obj.getLanguage())) {
//                nowShowingList.add(obj);
//            }
//        }
//
//        List<NowShowing> comingsoon = new ArrayList<>();
//        for (NowShowing obj : MainComingSoonList) {
//            if(selectedtheater.contains(obj.getName()) && selectedcategory.contains(obj.getCategory()) && selectedlanguage.contains(obj.getLanguage())) {
//                comingsoon.add(obj);
//            }
//        }
//
//        if (isNowShowingSelected) {
//            allEventsAdapter.addData(nowShowingList);
//            allEventsAdapter.notifyDataSetChanged();
//        } else {
//            allEventsAdapter.addData(comingsoon);
//            allEventsAdapter.notifyDataSetChanged();
//        }
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_add, menu);
//
//        MenuItem item1 = menu.findItem(R.id.action_share);
//        MenuItem item2 = menu.findItem(R.id.action_filter);
//        MenuItem item = menu.findItem(R.id.action_search);
//        item1.setVisible(false);
//        item.setVisible(true);
//        item2.setVisible(false);
//        this.invalidateOptionsMenu();
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_filter: {
//
//                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
//                intent.putExtra("theaterSet", new ArrayList<>(theater));
//                intent.putExtra("categorySet", new ArrayList<>(category));
//                intent.putExtra("languageSet", new ArrayList<>(language));
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
//
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    public void FilterDialog() {
////        theater    language     category
//        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.row_event_filter);
//
//        final String[] filterTheater =theater.toArray(new String[theater.size()]);
//        boolean[] checkedtheaters=new boolean[theater.size()];
//
//        final String[] filterLangauge =language.toArray(new String[language.size()]);
//        boolean[] checkedlanguage=new boolean[language.size()];
//
//        final String[] filterCategory =category.toArray(new String[category.size()]);
//        boolean[] checkedcategory=new boolean[category.size()];
//
//
//        Button btnClose = dialog.findViewById(R.id.btnFilterClose);
//        Button btnSave = dialog.findViewById(R.id.btnApplyFilter);
//
//        AppCompatTextView tv_language = dialog.findViewById(R.id.tv_language);
//        AppCompatTextView tv_category = dialog.findViewById(R.id.tv_category);
//        AppCompatTextView tv_theatername = dialog.findViewById(R.id.tv_theatername);
//
//        AppCompatCheckBox language_checkbox = dialog.findViewById(R.id.language_checkbox);
//        AppCompatCheckBox category_checkbox = dialog.findViewById(R.id.category_checkbox);
//        AppCompatCheckBox theater_checkbox = dialog.findViewById(R.id.theater_checkbox);
//
//        btnClose.setOnClickListener(view1 -> dialog.dismiss());
//
//        btnSave.setOnClickListener(view1 -> {
//            dialog.dismiss();
//        });
//        dialog.show();
//    }


}
