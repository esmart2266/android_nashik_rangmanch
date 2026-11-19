package in.gov.nmc.rangmach.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.MainActivity;
import in.gov.nmc.rangmach.adapter.TheaterBooking_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.FragmentAssetsHallBinding;
import in.gov.nmc.rangmach.model.AllTheater_Data;
import in.gov.nmc.rangmach.model.AllTheaters;
import in.gov.nmc.rangmach.utils.Utils;
import retrofit2.Call;

public class Assets_Hall_Fragment extends BaseFragment<FragmentAssetsHallBinding> {

    private TheaterBooking_Adapter allEventsAdapter;
    private List<AllTheaters> allTheatersList;

    Map<String, String> map = new HashMap<>();

    @Override
    protected FragmentAssetsHallBinding initializeViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAssetsHallBinding.inflate(inflater, container, false);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.fragment_assets__hall_;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//        AllTheaters(map);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Call your function here
                AllTheaters(map);
            }
        }, 2000);

//        binding.searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_hint) + "</font>"));
//        if (binding != null) {
//            binding.swipeRefreshLayout.setOnRefreshListener(this::refreshdata);
//        }

        setSearchView();


        binding.searchView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                Drawable drawableEnd = binding.searchView.getCompoundDrawablesRelative()[2]; // use Relative

                if (drawableEnd != null) {
                    int drawableWidth = drawableEnd.getBounds().width();

                    if (event.getRawX() >= (binding.searchView.getRight() - drawableWidth)) {
                        binding.searchView.setText("");
                        return true;
                    }
                }
            }
            return false;
        });
    }

    public void refreshdata() {
        AllTheaters(map);

    }


    public void AllTheaters(Map<String, String> params1) {
        if (cd.isConnectingToInternet()) {
            Call<AllTheater_Data> call = app.getApiRequestHelper().getAppService().allassets(params1);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                        @Override
                        public void onSuccess(Object object) {
                            if (binding != null && binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            if (object != null) {
                                AllTheater_Data allProductsData = (AllTheater_Data) object;
                                Log.e("in", "success");
                                if (allProductsData != null) {
                                    if (allProductsData.getResponsecode() == 200 && allProductsData.getResponsecode() != null && allProductsData.getData().size() > 0) {
                                        allTheatersList = allProductsData.getData();

                                        allEventsAdapter = new TheaterBooking_Adapter(mContext, allTheatersList);
//                                        binding.recyclerView.setAdapter(allEventsAdapter);
//                                    setSearchViewData();

                                        if (binding != null && binding.recyclerView != null) {
                                            binding.recyclerView.setAdapter(allEventsAdapter);
                                            binding.recyclerView.setVisibility(View.VISIBLE);
                                            binding.tvError.setVisibility(View.GONE);
                                        } else {
                                            Log.e("Assets_Hall_Fragment", "Binding or RecyclerView is null!");
                                        }

                                    } else {
                                        binding.recyclerView.setVisibility(View.GONE);
                                        binding.tvError.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                                }
                            } else {
                                Toast.makeText(mContext, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String apiResponse) {
                            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            Log.e("in", "error " + apiResponse);
                            Utils.showLongToast(mContext, apiResponse);
                        }
                    }
            );
        }
    }

//    private void setSearchView() {
//        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @SuppressLint("SuspiciousIndentation")
//            @Override
//            public boolean onQueryTextSubmit(String queryString) {
//                if (allEventsAdapter != null)
//                    allEventsAdapter.filter(queryString);
//                MainActivity.headerLayout.setVisibility(View.VISIBLE);
//                return true;
//            }
//
//            @SuppressLint("SuspiciousIndentation")
//            @Override
//            public boolean onQueryTextChange(String queryString) {
//                if (allEventsAdapter != null)
//                    allEventsAdapter.filter(queryString);
//                MainActivity.headerLayout.setVisibility(View.GONE);
//
//                return false;
//            }
//        });
//
//        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                MainActivity.headerLayout.setVisibility(View.VISIBLE);
//
//                return false;
//            }
//        });
//    }

    private void setSearchView() {

        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (allEventsAdapter != null) {
                    allEventsAdapter.filter(s.toString());
                }

                if (s.length() > 0) {
                    MainActivity.headerLayout.setVisibility(View.GONE);
                } else {
                    MainActivity.headerLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_add, menu);
//        MenuItem item1 = menu.findItem(R.id.action_share);
//        MenuItem item2 = menu.findItem(R.id.action_filter);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//        item1.setVisible(false);
//        item.setVisible(true);
//        item2.setVisible(false);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_share) {
//            Toast.makeText(mContext, "share", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}