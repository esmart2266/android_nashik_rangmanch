package in.gov.nmc.rangmach.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import in.gov.nmc.rangmach.FilterActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.adapter.AllEventsAdapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.FragmentNowShowingBinding;
import in.gov.nmc.rangmach.model.NowShowing;
import in.gov.nmc.rangmach.model.NowShowing_Data;
import in.gov.nmc.rangmach.utils.Utils;
import retrofit2.Call;

public class NowShowing_Fragment extends BaseFragment<FragmentNowShowingBinding> {

//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.tv_error)
//    TextView tvError;
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//    @BindView(R.id.ll_main)
//    RelativeLayout ll_main;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.menu_filter)
//    FloatingActionButton menu_filter;

    private AllEventsAdapter allEventsAdapter;
    private List<NowShowing> nowShowings;

    private Set<String> theater = new TreeSet();
    private Set<String> language = new TreeSet();
    private Set<String> category = new TreeSet();

    public static Set<String> selectedtheater = new TreeSet();
    public static Set<String> selectedlanguage = new TreeSet();
    public static Set<String> selectedcategory = new TreeSet();
    private List<NowShowing> nowShowingList = new ArrayList<>();
    private List<NowShowing> MainShowingList = new ArrayList<>();
    private List<NowShowing> MainComingSoonList = new ArrayList<>();
    private List<NowShowing> comingsoon = new ArrayList<>();
    Boolean isNowShowingSelected = true;

    @Override
    protected FragmentNowShowingBinding initializeViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNowShowingBinding.inflate(inflater, container, false);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.fragment_now_showing_;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        int spanCount = 2; // Number of columns in the grid
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, spanCount);
        binding.recyclerView.setLayoutManager(layoutManager);
        allNowShowingShows();
        binding.swipeRefreshLayout.setOnRefreshListener(this::allNowShowingShows);

        binding.menuFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putExtra("theaterSet", new ArrayList<>(theater));
                intent.putExtra("categorySet", new ArrayList<>(category));
                intent.putExtra("languageSet", new ArrayList<>(language));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        ApplyFilter();
    }

    public void allNowShowingShows() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<>();
            map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
            Call<NowShowing_Data> call = app.getApiRequestHelper().getAppService().allevents(map);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                        @Override
                        public void onSuccess(Object object) {
                            if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            if (object != null) {

                                NowShowing_Data allProductsData = (NowShowing_Data) object;
                                Log.e("in", "success");
                                if (allProductsData != null) {
                                    if (allProductsData.getResponsecode() == 200 && allProductsData.getResponsecode() != null && allProductsData.getData().size() > 0) {
                                        nowShowings = allProductsData.getData();

                                        allEventsAdapter = new AllEventsAdapter(mContext, nowShowings);
                                        binding.recyclerView.setAdapter(allEventsAdapter);

//                                    if (allProductsData.getData() != null) {
//                                        MainShowingList.clear();
//                                        nowShowingList.clear();
//                                        MainShowingList = allProductsData.getData();
//                                        nowShowingList.addAll(allProductsData.getData());
//                                    }
//
//                                    if (allProductsData.getUpcoming() != null) {
//                                        comingsoon.clear();
//                                        MainComingSoonList.clear();
//                                        MainComingSoonList = allProductsData.getUpcoming();
//                                        comingsoon.addAll(allProductsData.getUpcoming());
//                                    }

                                        if (allProductsData != null) {
                                            // Handle 'data' list
                                            if (allProductsData.getData() != null) {
                                                MainShowingList.clear();
                                                nowShowingList.clear();
                                                MainShowingList.addAll(allProductsData.getData());
                                                nowShowingList.addAll(allProductsData.getData());
                                            } else {
                                                MainShowingList.clear();
                                                nowShowingList.clear();
                                            }

                                            // Handle 'upcoming' list
                                            if (allProductsData != null && allProductsData.getUpcoming() != null) {
                                                comingsoon.clear();
                                                MainComingSoonList.clear();
                                                List<NowShowing> upcomingList = allProductsData.getUpcoming();
//                                            MainComingSoonList.addAll(allProductsData.getUpcoming());
                                                MainComingSoonList.addAll(upcomingList);
//                                            comingsoon.addAll(allProductsData.getUpcoming());
                                                comingsoon.addAll(upcomingList);
                                            } else {
                                                comingsoon.clear();
                                                MainComingSoonList.clear();
                                            }
                                        } else {
                                            // Clear everything if 'allProductsData' itself is null
                                            MainShowingList.clear();
                                            nowShowingList.clear();
                                            comingsoon.clear();
                                            MainComingSoonList.clear();
                                        }

                                        theater.clear();
                                        category.clear();
                                        language.clear();
                                        selectedcategory.clear();
                                        selectedlanguage.clear();
                                        selectedtheater.clear();
                                        for (NowShowing evt : nowShowingList) {
                                            theater.add(evt.getName());
                                            category.add(evt.getCategory());
                                            language.add(evt.getLanguage());

                                            selectedcategory.add(evt.getCategory());
                                            selectedlanguage.add(evt.getLanguage());
                                            selectedtheater.add(evt.getName());
                                        }

                                        for (NowShowing evt2 : comingsoon) {
                                            theater.add(evt2.getName());
                                            category.add(evt2.getCategory());
                                            language.add(evt2.getLanguage());

                                            selectedcategory.add(evt2.getCategory());
                                            selectedlanguage.add(evt2.getLanguage());
                                            selectedtheater.add(evt2.getName());
                                        }

//                                    setSearchViewData();
                                        binding.recyclerView.setVisibility(View.VISIBLE);
                                        binding.tvError.setVisibility(View.GONE);
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
                            if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            Log.e("in", "error " + apiResponse);
                            Utils.showLongToast(mContext, apiResponse);
                        }
                    }
            );
        }
    }

//    public void ApplyFilter() {
//
//        if (allEventsAdapter == null) {
//            return;
//        }
//        List<NowShowing> nowShowingList = new ArrayList<>();
//        for (NowShowing obj : MainShowingList) {
//            if (selectedtheater.contains(obj.getName()) && selectedcategory.contains(obj.getCategory()) && selectedlanguage.contains(obj.getLanguage())) {
//                nowShowingList.add(obj);
//            }
//        }
//
//        List<NowShowing> comingsoon = new ArrayList<>();
//        for (NowShowing obj : MainComingSoonList) {
//            if (selectedtheater.contains(obj.getName()) && selectedcategory.contains(obj.getCategory()) && selectedlanguage.contains(obj.getLanguage())) {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_add, menu);
//        MenuItem item1 = menu.findItem(R.id.action_share);
//        MenuItem item2 = menu.findItem(R.id.action_filter);
//        MenuItem item = menu.findItem(R.id.action_search);
//        item1.setVisible(false);
//        item.setVisible(false);
//        item2.setVisible(true);
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_filter) {
//
//            Intent intent = new Intent(getActivity(), FilterActivity.class);
//            intent.putExtra("theaterSet", new ArrayList<>(theater));
//            intent.putExtra("categorySet", new ArrayList<>(category));
//            intent.putExtra("languageSet", new ArrayList<>(language));
//            startActivity(intent);
//            getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}