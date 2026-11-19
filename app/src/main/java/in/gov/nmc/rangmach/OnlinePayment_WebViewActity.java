package in.gov.nmc.rangmach;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.nio.charset.StandardCharsets;

import in.gov.nmc.rangmach.databinding.ActivityOnlinePaymentWebViewActityBinding;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.webkit.CookieManager;

public class OnlinePayment_WebViewActity extends BaseActivity<ActivityOnlinePaymentWebViewActityBinding> {
    WebView webView;

    protected ActivityOnlinePaymentWebViewActityBinding inflateBinding(LayoutInflater inflater) {
        return ActivityOnlinePaymentWebViewActityBinding.inflate(inflater);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        webView = binding.webView;

        mContext = OnlinePayment_WebViewActity.this;

        CustomProgressDialog pd = new CustomProgressDialog(mContext);

        pd.setTitle("Loading...!");

        String booking_id = (String) getIntent().getSerializableExtra("bookingid");
        String amt = (String) getIntent().getSerializableExtra("amount");

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.clearCache(true);
        webView.setWebChromeClient(new WebChromeClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }

        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view,
                                          boolean isDialog,
                                          boolean isUserGesture,
                                          Message resultMsg) {

                WebView newWebView = new WebView(OnlinePayment_WebViewActity.this);

                newWebView.getSettings().setJavaScriptEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    newWebView.setWebViewClient(webView.getWebViewClient());
                }

                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.getSettings().setSupportMultipleWindows(true);
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

                WebView.WebViewTransport transport =
                        (WebView.WebViewTransport) resultMsg.obj;

                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                return true;
            }
        });

        String weburl = BuildConfig.BASE_URL + BuildConfig.PaymentApi + "paymentinterface";

        String bookingid = booking_id;  // Example values
        String amount = amt;
//        String amount = "1";
        String name = app.getPreferences().getLoggedInUser().getData().getName();
        String mobile = app.getPreferences().getLoggedInUser().getData().getMobileNo();
        String gst = app.getPreferences().getLoggedInUser().getData().getGst_no();
        String address = app.getPreferences().getLoggedInUser().getData().getAddress();
        String email = app.getPreferences().getLoggedInUser().getData().getemailid();

        // Prepare POST data
        String postData = "bookingid=" + bookingid + "&amount=" + amount + "&name=" + name + "&mobile=" + mobile + "&gst=" + gst + "&address=" + address + "&email=" + email;

        // Convert postData to bytes
        byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

        // Set WebViewClient to handle page loading events
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle errors here
                Log.e("WebViewError", "Error: " + description);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    WebResourceRequest request) {
                return handleUrl(view, request.getUrl().toString());
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Alert!");
                builder.setMessage("You are visiting the NMC site. Do you want to continue anyway with SSL?");
                builder.setPositiveButton("Continue", (dialog, which) -> handler.proceed());
                builder.setNegativeButton("Cancel", (dialog, which) -> handler.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pd.dismiss();
            }
        });

        // Use postUrl to load the WebView with the POST body
        webView.postUrl(weburl, postDataBytes);
//      webView.postUrl("https://cultural.pmc.gov.in/cultural/PaymentApi/paymentinterface", postDataBytes);
    }

    private boolean handleUrl(WebView view, String url) {

        String lower = url.toLowerCase();

        // Success / Failed callback
        if (lower.contains("success") || lower.contains("failed")) {
            MyAssetsOder_Activity.refreshpage = true;
            finish();
            return true;
        }

        // Direct UPI
        if (lower.startsWith("upi://pay")) {
            openUpiIntent(url);
            return true;
        }

        // intent://
        if (lower.startsWith("intent://")) {

            try {

                Intent intent =
                        Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                String scheme = intent.getScheme();

                if (scheme != null &&
                        scheme.equalsIgnoreCase("upi")) {

                    String upiUri = intent.getDataString();

                    if (upiUri == null)
                        upiUri = url;

                    openUpiIntent(upiUri);

                    return true;
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    return true;
                }

                String pkg = intent.getPackage();

                if (pkg != null && !pkg.isEmpty()) {
                    openPlayStore(pkg);
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // phonepe:// gpay:// paytmmp:// etc
        boolean isHttp =
                lower.startsWith("http://") ||
                        lower.startsWith("https://");

        if (!isHttp) {

            try {

                Intent i =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                startActivity(i);
                return true;

            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                return true;
            }
        }

        // Normal URL
        view.loadUrl(url);
        return true;
    }

    private void openUpiIntent(String upiUrl) {

        try {

            Uri uri = Uri.parse(upiUrl);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            Intent chooser =
                    Intent.createChooser(intent, "Pay with");

            startActivity(chooser);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openPlayStore(String packageName) {

        try {

            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)));

        } catch (Exception e) {

            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }
}


