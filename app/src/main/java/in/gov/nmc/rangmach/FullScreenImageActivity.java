package in.gov.nmc.rangmach;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

//import org.apache.commons.logging.LogFactory;

import in.gov.nmc.rangmach.databinding.ActivityFullScreenImageBinding;

public class FullScreenImageActivity extends BaseActivity<ActivityFullScreenImageBinding> {
//    private static final org.apache.commons.logging.Log log = LogFactory.getLog(FullScreenImageActivity.class);
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.imageView)
//    PhotoView mImageView;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;


    protected ActivityFullScreenImageBinding inflateBinding(LayoutInflater inflater) {
        return ActivityFullScreenImageBinding.inflate(inflater);
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        String title = getIntent().getStringExtra("title");
//        String urlPhotoClick = getIntent().getStringExtra("urlPhotoClick");
//        binding.toolbarContainer.toolbarTitle.setText(title);
//
//        Glide.with(this).load(urlPhotoClick)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .placeholder(R.drawable.placeholder)
//                .thumbnail(0.1f)
//                .error(R.drawable.no_image)
//                .into(binding.imageView);
        setValues();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_full_screen_image;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setValues() {


        Glide.get(mContext).clearMemory();  // Clears Glide's memory cache
        new Thread(() -> Glide.get(mContext).clearDiskCache()).start();


        String title = getIntent().getStringExtra("title");
        String urlPhotoClick = getIntent().getStringExtra("urlPhotoClick");
        binding.toolbarContainer.toolbarTitle.setText(title);

        Glide.with(this)
                .load(urlPhotoClick)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.placeholder)
                .thumbnail(0.1f)
                .error(R.drawable.no_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Log the error
                        if (e != null) {
                            Log.e("Glide Error", "Error loading image: " + e.getMessage());
                        }
                        return false; // Allow Glide's error handling to take place (optional)
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false; // No need to handle success
                    }
                })
                .into(binding.imageView);

    }
}

