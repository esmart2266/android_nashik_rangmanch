package in.gov.nmc.rangmach;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import in.gov.nmc.rangmach.databinding.ActivityPdfviewWebViewBinding;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class PDFView_WebView_Activity extends BaseActivity<ActivityPdfviewWebViewBinding> {

    protected ActivityPdfviewWebViewBinding inflateBinding(LayoutInflater inflater) {
        return ActivityPdfviewWebViewBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_pdfview_web_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        CustomProgressDialog pd = new CustomProgressDialog(mContext);
        pd.show();
        pd.setTitle("Loading...!");

        String weburl = (String) getIntent().getSerializableExtra("website");

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


        // Enable JavaScript
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true); // Enable zoom controls
        webSettings.setDisplayZoomControls(false);

        // Ensure links open within WebView and not in an external browser
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.setWebChromeClient(new WebChromeClient());

        // Set WebViewClient to handle page loading events
        binding.webView.setWebViewClient(new WebViewClient() {
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


        // Load the PDF using Google Docs Viewer
        String googleDocsViewerUrl = "https://docs.google.com/viewer?url=" + weburl;
        binding.webView.loadUrl(googleDocsViewerUrl);
    }
}