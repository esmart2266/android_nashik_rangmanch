package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import in.gov.nmc.rangmach.databinding.ActivityWebviewBinding;
import in.gov.nmc.rangmach.utility.AvenuesParams;
import in.gov.nmc.rangmach.utility.Constants;
import in.gov.nmc.rangmach.utility.LoadingDialog;
import in.gov.nmc.rangmach.utility.RSAUtility;
import in.gov.nmc.rangmach.utility.ServiceUtility;

public class Payment_WebActivity extends BaseActivity<ActivityWebviewBinding> {
    Intent mainIntent;
    String encVal;
    String vResponse;
    Context mContext;
    App app;
    
//    private Button button;

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;


    protected ActivityWebviewBinding inflateBinding(LayoutInflater inflater) {
        return ActivityWebviewBinding.inflate(inflater);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        setContentView(R.layout.activity_webview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        mContext = Payment_WebActivity.this;
        app = (App) getApplication();
        mainIntent = getIntent();

        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });

        //get rsa key method
//        get_RSA_key(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(Payment_WebActivity.this, "Loading...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html source code to get final status of transaction
                    Log.e("html", html);
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    } else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
//                    intent.putExtra("transStatus", status);
//                    startActivity(intent);
                    Intent intent = getIntent();
                    intent.putExtra("status", status);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    Log.e("in", "onPageFinished:" + url);
                    if (url.indexOf("/ccavResponseHandler.php") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        String str = "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');";
                        Log.e("str", str);
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(Payment_WebActivity.this, "Loading...");
                }
            });

            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" +
                        AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" +
                        AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" +
                        AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" +
                        AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_COUNTRY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY), "UTF-8") + "&" +
                        AvenuesParams.BILLING_STATE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_STATE), "UTF-8") + "&" +
                        AvenuesParams.BILLING_CITY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_CITY), "UTF-8") + "&" +
                        AvenuesParams.BILLING_ZIP + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP), "UTF-8") + "&" +
                        AvenuesParams.BILLING_NAME + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_NAME), "UTF-8") + "&" +
                        AvenuesParams.BILLING_ADDRESS + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS), "UTF-8") + "&" +
                        AvenuesParams.BILLING_TEL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_TEL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_EMAIL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_NAME + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_ADDRESS + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_CITY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_STATE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_ZIP + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_COUNTRY + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY), "UTF-8") + "&" +
                        AvenuesParams.DELIVERY_TEL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL), "UTF-8") + "&" +
                        AvenuesParams.BILLING_NOTES + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.BILLING_NOTES), "UTF-8") + "&" +
                        AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

//    public void get_RSA_key(final String ac, final String od) {
//        LoadingDialog.showLoadingDialog(Payment_WebActivity.this, "Loading...");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //Toast.makeText(Payment_WebActivity.this,response,Toast.LENGTH_LONG).show();
//                LoadingDialog.cancelLoading();
//                if (response != null && !response.equals("")) {
//                    vResponse = response;     ///save retrived rsa key
//                    Log.e("response", response);
//                    if (vResponse.contains("!ERROR!")) {
//                        show_alert(vResponse);
//                    } else {
//                        new RenderView().execute();   // Calling async task to get display content
//                    }
//                } else {
//                    show_alert("No response");
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                LoadingDialog.cancelLoading();
//                //Toast.makeText(Payment_WebActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put(AvenuesParams.ACCESS_CODE, ac);
//                params.put(AvenuesParams.ORDER_ID, od);
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(Payment_WebActivity.this).create();
        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");
        alertDialog.setMessage(msg);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }
}