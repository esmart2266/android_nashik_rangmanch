package in.gov.nmc.rangmach;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.gov.nmc.rangmach.databinding.ActivityWebviewBinding;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class WebviewActivity extends BaseActivity<ActivityWebviewBinding> {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//    Context mContext;
//    @BindView(R.id.webview)
//    WebView webview;
//    @BindView(R.id.progress_bar)
//    ProgressBar progressBar;


    protected ActivityWebviewBinding inflateBinding(LayoutInflater inflater) {
        return ActivityWebviewBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_webview);
//        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }


        CustomProgressDialog pd = new CustomProgressDialog(mContext);
        pd.show();
        pd.setTitle("Website Details Loading...!");
        try {
            mContext = WebviewActivity.this;
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            binding.toolbar.setTitle(getIntent().getStringExtra("title"));

            String weburl = (String) getIntent().getSerializableExtra("website");
            String toolbartitle = (String) getIntent().getSerializableExtra("name");
            binding.toolbarTitle.setText(toolbartitle);

            binding.toolbar.setTitleTextColor(Color.WHITE);
//            progressBar.setVisibility(View.VISIBLE);
//            webview.setWebViewClient(new MyBrowser());
            String url = getIntent().getStringExtra("url");
            binding.webview.getSettings().setLoadsImagesAutomatically(true);
            binding.webview.getSettings().setJavaScriptEnabled(true);
            binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            binding.webview.getSettings().setLoadsImagesAutomatically(true);
            binding.webview.getSettings().setAllowFileAccess(true);
            binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            binding.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            binding.webview.getSettings().setPluginState(WebSettings.PluginState.ON);
            binding.webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
            binding.webview.getSettings().setDomStorageEnabled(true);
//            binding.webview.getSettings().setAppCacheMaxSize(1024 * 8);
            binding.webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            binding.webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            binding.webview.requestFocus(View.FOCUS_DOWN);
//            binding.webview.getSettings().setAppCacheEnabled(true);
            binding.webview.clearCache(true);
//            binding.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            binding.webview.loadUrl(weburl);
            binding.webview.setWebChromeClient(new WebChromeClient());

            binding.btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.hideSoftKeyboard(WebviewActivity.this);
                    finish();
                }
            });

            // Set WebViewClient to handle page loading events
            binding.webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
//                    ProgressDialogUtil.showProgressDialog(WebviewActivity.this);
//                    pd.show();
                    if (pd.isShowing()) pd.dismiss();
                }

                @Override
                public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Alert!");
                    builder.setMessage("You are visiting the PMC site. Do you want to continue anyway with SSL?");
                    builder.setPositiveButton("Continue", (dialog, which) -> handler.proceed());
                    builder.setNegativeButton("Cancel", (dialog, which) -> handler.cancel());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
//                    ProgressDialogUtil.dismissProgressDialog();
                    if (pd.isShowing()) pd.dismiss();
                }

            });


//            webview.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//                    progressBar.setVisibility(View.VISIBLE);
//                    webview.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onPageCommitVisible(WebView view, String url) {
//                    super.onPageCommitVisible(view, url);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    progressBar.setVisibility(View.GONE);
//                    webview.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                    super.onReceivedError(view, request, error);
//                    Log.e("onReceivedError", error.toString());
//                }
//
//                @Override
//                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                    super.onReceivedSslError(view, handler, error);
//                    Log.e("onReceivedSslError", error.toString());
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.hideSoftKeyboard(WebviewActivity.this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}