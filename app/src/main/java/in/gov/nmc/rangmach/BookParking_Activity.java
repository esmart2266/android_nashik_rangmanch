package in.gov.nmc.rangmach;

import static android.view.View.GONE;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityBookParkingBinding;
import in.gov.nmc.rangmach.model.CarParking;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class BookParking_Activity extends BaseActivity<ActivityBookParkingBinding> {

    CarParking carParking_data;

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.checkBike)
//    CheckBox checkBike;
//    @BindView(R.id.check_car)
//    CheckBox check_car;
//    @BindView(R.id.check_vip)
//    CheckBox check_vip;
//    @BindView(R.id.checkBus)
//    CheckBox checkBus;
//
//    @BindView(R.id.ll_checkbox_bike)
//    LinearLayout ll_checkbox_bike;
//    @BindView(R.id.ll_checkbox_car)
//    LinearLayout ll_checkbox_car;
//    @BindView(R.id.llCheckboxBus)
//    LinearLayout llCheckboxBus;
//    @BindView(R.id.ll_checkbox_vip)
//    LinearLayout ll_checkbox_vip;
//
//    @BindView(R.id.et_bike_qty)
//    MaterialEditText et_bike_qty;
//    @BindView(R.id.et_car_qty)
//    MaterialEditText et_car_qty;
//    @BindView(R.id.etBusQty)
//    MaterialEditText etBusQty;
//    @BindView(R.id.et_vip_qty)
//    MaterialEditText et_vip_qty;
//
//    @BindView(R.id.editTextContainer_bike)
//    LinearLayout editTextContainer_bike;
//    @BindView(R.id.editTextContainer_car)
//    LinearLayout editTextContainer_car;
//    @BindView(R.id.editTextContainer_bus)
//    LinearLayout editTextContainer_bus;
//    @BindView(R.id.editTextContainer_vip)
//    LinearLayout editTextContainer_vip;
//
//    @BindView(R.id.ll_main_bike)
//    LinearLayout ll_main_bike;
//    @BindView(R.id.ll_main_car)
//    LinearLayout ll_main_car;
//    @BindView(R.id.ll_main_bus)
//    LinearLayout ll_main_bus;
//    @BindView(R.id.ll_main_vip)
//    LinearLayout ll_main_vip;
//
//    @BindView(R.id.tv_bike_qty)
//    TextView tv_bike_qty;
//
//    @BindView(R.id.btn_save)
//    AppCompatButton btn_save;
//
//    @BindView(R.id.bottom_relativelayout)
//    RelativeLayout bottom_relativelayout;
//
//    @BindView(R.id.tv_Please_Enter_Your_Bike_Number)
//    TextView tv_Please_Enter_Your_Bike_Number;
//    @BindView(R.id.tv_Please_Enter_Your_Car_Number)
//    TextView tv_Please_Enter_Your_Car_Number;
//    @BindView(R.id.tv_Please_Enter_Your_Vehicle_Number)
//    TextView tv_Please_Enter_Your_Vehicle_Number;
//    @BindView(R.id.tv_Please_Enter_Your_VIP_Number)
//    TextView tv_Please_Enter_Your_VIP_Number;


    int car_ID = 0;
    int bus_ID = 0;
    int vip_ID = 0;

    float totalBillAmt = 0;

    String bike_str = "";
    String car_str = "";
    String bus_str = "";
    String vip_str = "";

    String OrderID = "";

    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_book_parking;
//    }

    protected ActivityBookParkingBinding inflateBinding(LayoutInflater inflater) {
        return ActivityBookParkingBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        binding.toolbarTitle.setText(GetTranslation("Please Select Vehicle Type", typeOfLanguage));
        binding.checkBike.setText(GetTranslation("Bike", typeOfLanguage));
        binding.checkCar.setText(GetTranslation("Car", typeOfLanguage));
        binding.checkBus.setText(GetTranslation("Bus", typeOfLanguage));
        binding.checkVip.setText(GetTranslation("VIP", typeOfLanguage));
        binding.btnSave.setText(GetTranslation("Save", typeOfLanguage));

        binding.etBikeQty.setHint(GetTranslation("Total", typeOfLanguage));
        binding.etCarQty.setHint(GetTranslation("Total", typeOfLanguage));
        binding.etBusQty.setHint(GetTranslation("Total", typeOfLanguage));
        binding.etVipQty.setHint(GetTranslation("Total", typeOfLanguage));

        binding.tvPleaseEnterYourBikeNumber.setText(GetTranslation("Please Enter Your Bike Number", typeOfLanguage));
        binding.tvPleaseEnterYourCarNumber.setText(GetTranslation("Please Enter Your Car Number", typeOfLanguage));
        binding.tvPleaseEnterYourVehicleNumber.setText(GetTranslation("Please Enter Your Vehicle Number", typeOfLanguage));
        binding.tvPleaseEnterYourVIPNumber.setText(GetTranslation("Please Enter Your VIP Number", typeOfLanguage));

        carParking_data = (CarParking) getIntent().getSerializableExtra("data");

        if (carParking_data != null) {

            if (carParking_data.getBikeQty() == 0) {
                binding.llCheckboxBike.setVisibility(GONE);
                binding.checkBike.setVisibility(GONE);
            } else {
                binding.llCheckboxBike.setVisibility(View.VISIBLE);
                binding.checkBike.setVisibility(View.VISIBLE);
            }

            if (carParking_data.getCarQty() == 0) {
                binding.llCheckboxCar.setVisibility(GONE);
                binding.checkCar.setVisibility(GONE);
            } else {
                binding.llCheckboxCar.setVisibility(View.VISIBLE);
                binding.checkCar.setVisibility(View.VISIBLE);
            }

            if (carParking_data.getBusQty() == 0) {
                binding.llCheckboxBus.setVisibility(GONE);
                binding.checkBus.setVisibility(GONE);
            } else {
                binding.llCheckboxBus.setVisibility(View.VISIBLE);
                binding.checkBus.setVisibility(View.VISIBLE);
            }

            if (carParking_data.getVipQty() == 0) {
                binding.llCheckboxVip.setVisibility(GONE);
                binding.checkVip.setVisibility(GONE);
            } else {
                binding.llCheckboxVip.setVisibility(View.VISIBLE);
                binding.checkVip.setVisibility(View.VISIBLE);
            }

            if (carParking_data.getShowVip() == 0) {
                binding.llCheckboxVip.setVisibility(GONE);
                binding.checkVip.setVisibility(GONE);
            } else {
                binding.llCheckboxVip.setVisibility(View.VISIBLE);
                binding.checkVip.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(app, "BookParking_Activity\", \"CarParking data is null", Toast.LENGTH_SHORT).show();
            binding.checkBike.setVisibility(GONE);
            binding.checkCar.setVisibility(GONE);
            binding.checkBus.setVisibility(GONE);
            binding.checkVip.setVisibility(GONE);
        }

        CheckBox.OnCheckedChangeListener listener = new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message = buttonView.getText() + (isChecked ? " checked" : " unchecked");
                Toast.makeText(BookParking_Activity.this, message, Toast.LENGTH_SHORT).show();

                if (binding.checkBike.getId() == R.id.check_bike) {
                    if (binding.checkBike.isChecked()) {
                        binding.etBikeQty.setVisibility(View.VISIBLE);
                        binding.editTextContainerBike.setVisibility(View.VISIBLE);
                        binding.llMainBike.setVisibility(View.VISIBLE);

                    } else {
                        binding.etBikeQty.setText("");
                        binding.etBikeQty.setVisibility(GONE);
                        binding.editTextContainerBike.setVisibility(GONE);
                        binding.llMainBike.setVisibility(GONE);
                    }
                }

                if (binding.checkCar.getId() == R.id.check_car) {
                    if (binding.checkCar.isChecked()) {
                        binding.etCarQty.setVisibility(View.VISIBLE);
                        binding.editTextContainerCar.setVisibility(View.VISIBLE);
                        binding.llMainCar.setVisibility(View.VISIBLE);


                    } else {
                        binding.etCarQty.setText("");
                        binding.etCarQty.setVisibility(GONE);
                        binding.editTextContainerCar.setVisibility(GONE);
                        binding.llMainCar.setVisibility(GONE);

                    }
                }

                if (binding.checkVip.getId() == R.id.check_vip) {
                    if (binding.checkVip.isChecked()) {
                        binding.etVipQty.setVisibility(View.VISIBLE);
                        binding.editTextContainerVip.setVisibility(View.VISIBLE);
                        binding.llMainVip.setVisibility(View.VISIBLE);

                    } else {
                        binding.etVipQty.setText("");
                        binding.etVipQty.setVisibility(GONE);
                        binding.editTextContainerVip.setVisibility(GONE);
                        binding.llMainVip.setVisibility(GONE);

                    }
                }

                if (binding.checkBus.getId() == R.id.check_bus) {
                    if (binding.checkBus.isChecked()) {
                        binding.etBusQty.setVisibility(View.VISIBLE);
                        binding.editTextContainerBus.setVisibility(View.VISIBLE);
                        binding.llMainBus.setVisibility(View.VISIBLE);

                    } else {
                        binding.etBusQty.setText("");
                        binding.etBusQty.setVisibility(GONE);
                        binding.editTextContainerBus.setVisibility(GONE);
                        binding.llMainBus.setVisibility(GONE);

                    }
                    CalculateParking();
                }
            }
        };

        binding.checkBike.setOnCheckedChangeListener(listener);
        binding.checkCar.setOnCheckedChangeListener(listener);
        binding.checkBus.setOnCheckedChangeListener(listener);
        binding.checkVip.setOnCheckedChangeListener(listener);

        binding.etBikeQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                binding.llMainBike.setVisibility(View.VISIBLE);
                int bike_ID = 1;
                binding.editTextContainerBike.removeAllViews();
                String qtyText = binding.etBikeQty.getText().toString();

                try {
                    if (!qtyText.trim().isEmpty()) {
                        int qty = Integer.parseInt(qtyText);
                        for (int i = 0; i < qty; i++) {
                            createNewEditText_for_Bike(bike_ID, i + 1);
                            bike_ID++;
                        }
                    } else {

                    }
                } catch (Exception e) {
                    binding.llMainBike.setVisibility(GONE);
                }

                CalculateParking();
            }
        });

        binding.etCarQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.llMainCar.setVisibility(View.VISIBLE);
                binding.editTextContainerCar.removeAllViews();
                car_ID = 101;
                String qtyText = binding.etCarQty.getText().toString();

                try {
                    if (!qtyText.trim().isEmpty()) {
                        int qty = Integer.parseInt(qtyText);
                        for (int i = 0; i < qty; i++) {
                            createNewEditText_for_Car(car_ID, i + 1);
                            car_ID++;
                        }
                    } else {
                        binding.llMainCar.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    binding.llMainCar.setVisibility(GONE);
                }
                CalculateParking();
            }
        });

        binding.etBusQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                binding.llMainBus.setVisibility(View.VISIBLE);
                binding.editTextContainerBus.removeAllViews();
                bus_ID = 201;
                String qtyText = binding.etBusQty.getText().toString();

                try {
                    if (!qtyText.trim().isEmpty()) {
                        int qty = Integer.parseInt(qtyText);
                        for (int i = 0; i < qty; i++) {
                            createNewEditText_for_Bus(bus_ID, i + 1);
                            bus_ID++;
                        }
                    } else {
                        binding.llMainBus.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    binding.llMainBus.setVisibility(GONE);
                }
                CalculateParking();
            }
        });

        binding.etVipQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                binding.llMainVip.setVisibility(View.VISIBLE);
                binding.editTextContainerVip.removeAllViews();
                vip_ID = 301;
                String qtyText = binding.etVipQty.getText().toString();

                try {
                    if (!qtyText.trim().isEmpty()) {
                        int qty = Integer.parseInt(qtyText);
                        for (int i = 0; i < qty; i++) {
                            createNewEditText_for_Vip(vip_ID, i + 1);
                            vip_ID++;
                        }
                    } else {
                        binding.llMainVip.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    binding.llMainVip.setVisibility(GONE);
                }

                CalculateParking();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveDetails();
            }
        });

    }

    void CalculateParking() {
        String noOfSlotsStr = binding.etBikeQty.getText().toString();
        totalBillAmt = 0;

        float gstPercentage = carParking_data.getGst() / 100f;

        String billString = "";
        if (!noOfSlotsStr.trim().isEmpty()) {
            try {
                // Convert the quantity to a float
                int qty = Integer.parseInt(noOfSlotsStr);
                float price = carParking_data.getBikePrice();
                totalBillAmt += qty * price;
                billString += "Bike slots (₹" + price + " X " + qty + ") = " + "₹ " + String.format("%.2f", (price * qty));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                Toast.makeText(BookParking_Activity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        }


        //CAR SECTION

        noOfSlotsStr = binding.etCarQty.getText().toString();
        if (!noOfSlotsStr.trim().isEmpty()) {
            try {
                // Convert the quantity to a float
                int qty = Integer.parseInt(noOfSlotsStr);
                float price = carParking_data.getCarPrice();
                totalBillAmt += qty * price;
                if (billString.trim().length() > 0) {
                    billString += "\n";
                }
                billString += "Car slots (₹" + price + " X " + qty + ") = " + "₹ " + String.format("%.2f", (price * qty));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
            }
        }


        //bus section

        noOfSlotsStr = binding.etBusQty.getText().toString();
        if (!noOfSlotsStr.trim().isEmpty()) {
            try {
                // Convert the quantity to a float
                int qty = Integer.parseInt(noOfSlotsStr);
                float price = carParking_data.getBusPrice();
                totalBillAmt += qty * price;
                if (billString.trim().length() > 0) {
                    billString += "\n";
                }
                billString += "Bus slots (₹" + price + " X " + qty + ") = " + "₹ " + String.format("%.2f", (price * qty));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
            }
        }


        //VIP section

        noOfSlotsStr = binding.etVipQty.getText().toString();
        if (!noOfSlotsStr.trim().isEmpty()) {
            try {
                // Convert the quantity to a float
                int qty = Integer.parseInt(noOfSlotsStr);
                float price = carParking_data.getVipPrice();
                totalBillAmt += qty * price;
                if (billString.trim().length() > 0) {
                    billString += "\n";
                }
                billString += "VIP slots (₹" + price + " X " + qty + ") = " + "₹ " + String.format("%.2f", (price * qty));
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
            }
        }

        if (totalBillAmt > 0) {
            totalBillAmt = totalBillAmt + (totalBillAmt * gstPercentage);
//            billString += "\nGST Amount = " + (totalBillAmt * gstPercentage);
            billString += "\nGST Amount = " + String.format("%.2f", totalBillAmt * gstPercentage);
            billString += "\nTotal Amount = " + "₹ " + String.format("%.2f", totalBillAmt);
            ;
        }
        binding.tvBikeQty.setText(billString);

    }


    private void SaveDetails() {

        //create a bike numbers strings first
        boolean allCorrectFlag = true;

        //For Bike
        if (binding.checkBike.isChecked()) {
            int qty = Integer.parseInt(binding.etBikeQty.getText().toString());
            if (qty <= 0) {
                Toast.makeText(mContext, "Please enter valid needed slots for bike", Toast.LENGTH_SHORT).show();
                allCorrectFlag = false;
            }
            //now we know how much for loop we much go through
            for (int i = 1; i <= qty; i++) {
                //checking now numbers typed by the user
                EditText temp = (EditText) findViewById(i);
                //check for valid number
                if (temp.getText().toString().length() < 5) {
//                    Toast.makeText(mContext, "Please enter valid number for BIKE vehicle no: " + i, Toast.LENGTH_SHORT).show();
                    showErrorDialog(mContext, "ERROR", "Please enter valid number for BIKE vehicle no: " + i);
                    allCorrectFlag = false;
                    return;
                } else {
                    if (temp.getText().toString().contains(",")) {
                        showErrorDialog(mContext, "ERROR", "Please enter valid number for BIKE vehicle no: " + i);
                        return;
                    }
                    if (bike_str.length() == 0) {
                        bike_str = temp.getText().toString();
                    } else {
                        bike_str += "," + temp.getText().toString();
                    }

                    Toast.makeText(mContext, bike_str, Toast.LENGTH_SHORT).show();
                }
            }
        }
        //For Car
        if (binding.checkCar.isChecked()) {
            int qty = Integer.parseInt(binding.etCarQty.getText().toString());
            if (qty <= 0) {
                Toast.makeText(mContext, "Please enter valid needed slots for car", Toast.LENGTH_SHORT).show();
                allCorrectFlag = false;
            }
            //now we know how much for loop we much go through
            int index = 101;
            for (int i = 1; i <= qty; i++) {
                //checking now numbers typed by the user
                EditText temp = (EditText) findViewById(index);
                //check for valid number
                if (temp.getText().toString().length() < 5) {
//                    Toast.makeText(mContext, "Please enter valid number for Car vehicle no: " + i, Toast.LENGTH_SHORT).show();
                    showErrorDialog(mContext, "ERROR", "Please enter valid number for Car vehicle no: " + i);
                    allCorrectFlag = false;
                    return;
                } else {
                    if (temp.getText().toString().contains(",")) {
                        showErrorDialog(mContext, "ERROR", "Please enter valid number for Car vehicle no: " + i);
                        return;
                    }

                    if (car_str.length() == 0) {
                        car_str = temp.getText().toString();
                    } else {
                        car_str += "," + temp.getText().toString();
                    }
                }
                index++;
            }
        }
        //For Bus
        if (binding.checkBus.isChecked()) {
            int qty = Integer.parseInt(binding.etBusQty.getText().toString());
            if (qty <= 0) {
                Toast.makeText(mContext, "Please enter valid needed slots for Bus", Toast.LENGTH_SHORT).show();
                allCorrectFlag = false;
            }
            //now we know how much for loop we much go through
            int index = 201;
            for (int i = 1; i <= qty; i++) {
                //checking now numbers typed by the user
                EditText temp = (EditText) findViewById(index);
                //check for valid number
                if (temp.getText().toString().length() < 5) {
//                    Toast.makeText(mContext, "Please enter valid number for Bus vehicle no: " + i, Toast.LENGTH_SHORT).show();
                    showErrorDialog(mContext, "ERROR", "Please enter valid number for Bus vehicle no: " + i);
                    allCorrectFlag = false;
                    return;
                } else {
                    if (temp.getText().toString().contains(",")) {
                        showErrorDialog(mContext, "ERROR", "Please enter valid number for Bus vehicle no: " + i);
                        return;
                    }

                    if (bus_str.length() == 0) {
                        bus_str = temp.getText().toString();
                    } else {
                        bus_str += "," + temp.getText().toString();
                    }
                }
                index++;
            }
        }
        //For VIP
        if (binding.checkVip.isChecked()) {
            int qty = Integer.parseInt(binding.etVipQty.getText().toString());
            if (qty <= 0) {
                Toast.makeText(mContext, "Please enter valid needed slots for VIP", Toast.LENGTH_SHORT).show();
                allCorrectFlag = false;
            }
            //now we know how much for loop we much go through
            int index = 301;
            for (int i = 1; i <= qty; i++) {
                //checking now numbers typed by the user
                EditText temp = (EditText) findViewById(index);
                //check for valid number
                if (temp.getText().toString().length() < 5) {
//                    Toast.makeText(mContext, "Please enter valid number for VIP vehicle no: " + i, Toast.LENGTH_SHORT).show();
                    showErrorDialog(mContext, "ERROR", "Please enter valid number for VIP vehicle no: " + i);
                    allCorrectFlag = false;
                    return;
                } else {
                    if (temp.getText().toString().contains(",")) {
                        showErrorDialog(mContext, "ERROR", "Please enter valid number for VIP vehicle no: " + i);
                        return;
                    }

                    if (vip_str.length() == 0) {
                        vip_str = temp.getText().toString();
                    } else {
                        vip_str += "," + temp.getText().toString();
                    }
                }
                index++;
            }
        }

        if (allCorrectFlag == false) {
            return;
        }

        String BikeqtyText = binding.etBikeQty.getText().toString();
        String CarqtyText = binding.etCarQty.getText().toString();
        String BusqtyText = binding.etBusQty.getText().toString();
        String VipqtyText = binding.etVipQty.getText().toString();

        if (BikeqtyText.isEmpty()) {
            BikeqtyText = "0";
        }
        if (CarqtyText.isEmpty()) {
            CarqtyText = "0";
        }
        if (BusqtyText.isEmpty()) {
            BusqtyText = "0";
        }
        if (VipqtyText.isEmpty()) {
            VipqtyText = "0";
        }

        if (totalBillAmt <= 0) {
            showErrorDialog(mContext, GetTranslation("Please Select Atleast One Slot", typeOfLanguage), "");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("parking_id", carParking_data.getId());
        map.put("bike_qty", BikeqtyText);
        map.put("bike_price", carParking_data.getBikePrice() + "");
        map.put("car_qty", CarqtyText);
        map.put("car_price", carParking_data.getCarPrice() + "");
        map.put("bus_qty", BusqtyText);
        map.put("bus_price", carParking_data.getBusPrice() + "");
        map.put("vip_qty", VipqtyText);
        map.put("vip_price", carParking_data.getVipPrice() + "");
        map.put("total", totalBillAmt + "");
        map.put("bike_numbers", bike_str);
        map.put("car_numbers", car_str);
        map.put("bus_numbers", bus_str);
        map.put("vip_numbers", vip_str);
        Book_Parking(map);
    }


    private void createNewEditText_for_Bike(int id, int sequence) {

        EditText newEditTextfor_Bike = new EditText(this);
        newEditTextfor_Bike.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        newEditTextfor_Bike.setHint(GetTranslation("Enter Bike Number", typeOfLanguage) + " " + sequence + "(ex. MH 65 BU 8456 ) ");
        newEditTextfor_Bike.setId(id);  // Generates a unique ID for the EditText

        binding.editTextContainerBike.addView(newEditTextfor_Bike);
    }

    private void createNewEditText_for_Car(int id, int sequence) {
        EditText newEditText = new EditText(this);
        newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newEditText.setHint(GetTranslation("Enter Car Number", typeOfLanguage) + " " + sequence + "(ex. MH 65 BU 8456 ) ");
        newEditText.setId(id);  // Generates a unique ID for the EditText

        binding.editTextContainerCar.addView(newEditText);
    }

    private void createNewEditText_for_Bus(int id, int sequence) {
        EditText newEditText = new EditText(this);
        newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newEditText.setHint(GetTranslation("Enter Vehicle Number", typeOfLanguage) + " " + sequence + "(ex. MH 65 BU 8456 ) ");
        newEditText.setId(id);  // Generates a unique ID for the EditText

        binding.editTextContainerBus.addView(newEditText);
    }

    private void createNewEditText_for_Vip(int id, int sequence) {
        EditText newEditText = new EditText(this);
        newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newEditText.setHint(GetTranslation("Enter Vehicle Number", typeOfLanguage) + " " + sequence + "(ex. MH 65 BU 8456 ) ");
        newEditText.setId(id);  // Generates a unique ID for the EditText

        binding.editTextContainerVip.addView(newEditText);
    }

    public void showErrorDialog(Context context, String title, String message) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle the OK button click
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void Book_Parking(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().book_parking(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {

                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                BaseActivity.ToastDialog(mContext, userData.getMessage());
                            if (pd.isShowing()) pd.dismiss();
                            OrderID = userData.orderId;
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                BaseActivity.ToastDialog(mContext, userData.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
//                    Utils.showLongToast(mContext, apiResponse);
                    BaseActivity.ToastDialog(mContext, apiResponse);
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