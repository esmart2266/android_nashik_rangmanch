package in.gov.nmc.rangmach.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import in.gov.nmc.rangmach.App;
import in.gov.nmc.rangmach.utils.ConnectionDetector;

//public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {
public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    //    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
//    private Unbinder unbinder;
    public Context mContext;
    public App app;
    public ConnectionDetector cd;
    HashMap<DatabaseReference, ValueEventListener> refValEventListHashMap;
    HashMap<DatabaseReference, ChildEventListener> refChildEventListHashMap;

    protected T binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View inflate = inflater.inflate(getActivityLayout(), container, false);
//        unbinder = ButterKnife.bind(this, inflate);

        binding = initializeViewBinding(inflater, container);

        mContext = getActivity();
        app = (App) getActivity().getApplication();
        cd = new ConnectionDetector(mContext);
        refValEventListHashMap = new HashMap<>();
        refChildEventListHashMap = new HashMap<>();

        return binding != null ? binding.getRoot() : null;
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//    @Override
//    public LifecycleRegistry getLifecycle() {
//        return lifecycleRegistry;
//    }

    protected abstract T initializeViewBinding(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        if (refValEventListHashMap != null && refValEventListHashMap.size() > 0) {
            for (Map.Entry<DatabaseReference, ValueEventListener> entry : refValEventListHashMap.entrySet()) {
                DatabaseReference ref = entry.getKey();
                ValueEventListener listener = entry.getValue();
                if (listener != null && ref != null) ref.removeEventListener(listener);
            }
        }
        if (refChildEventListHashMap != null && refChildEventListHashMap.size() > 0) {
            for (Map.Entry<DatabaseReference, ChildEventListener> entry : refChildEventListHashMap.entrySet()) {
                DatabaseReference ref = entry.getKey();
                ChildEventListener listener = entry.getValue();
                if (listener != null && ref != null) ref.removeEventListener(listener);
            }
        }

        binding = null;
    }

//    protected abstract int getActivityLayout();

    public abstract boolean onCreateOptionsMenu(Menu menu);
}