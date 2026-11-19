package in.gov.nmc.rangmach;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import in.gov.nmc.rangmach.adapter.DocumentUpload_Adapter;
import in.gov.nmc.rangmach.databinding.ActivityUploadDocumentsBinding;
import in.gov.nmc.rangmach.model.DocumentsPOJO;
import in.gov.nmc.rangmach.model.MyAssetsOrder;
import in.gov.nmc.rangmach.utils.Utils;
import jodd.net.MimeTypes;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class UploadDocuments_Activity extends BaseActivity<ActivityUploadDocumentsBinding> {

    MyAssetsOrder items;

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;f
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.ll_items)
//    LinearLayout ll_items;
//    @BindView(R.id.tv_error)
//    TextView tv_error;
//    @BindView(R.id.etFile)
//    MaterialEditText etFile;
//    @BindView(R.id.ivDocument)
//    ImageView ivDocument;

    ProgressBar customProgressDialog;

    String documentId;
    String bookingId;
    private EasyImage easyImage;
    int PICK_PDF_FILE = 2;
    private Uri fileUri;
    private String fileExtension = "";
    private static int REQUEST_AVATAR_CODE = 2345;
    private File file;
    private String patientDocumentId = "", imagePath = "";
    int myadapterposition;
    DocumentUpload_Adapter dataAdapter;

    protected ActivityUploadDocumentsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityUploadDocumentsBinding.inflate(inflater);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_upload_documents;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        easyImage = new EasyImage.Builder(mContext).allowMultiple(false).build();


        items = (MyAssetsOrder) getIntent().getSerializableExtra("data");

        binding.toolbarTitle.setText("Upload Documents");

        if (items.getDocumentsArrays() != null) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            dataAdapter = new DocumentUpload_Adapter(mContext, items.getDocumentsArrays(), items.getId());
            binding.recyclerView.setAdapter(dataAdapter);

            binding.tvError.setVisibility(View.GONE);
            binding.llItems.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.VISIBLE);

        } else {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.llItems.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    boolean isCropEnabled = false;

    public void openfile(int adapterPosition, String bookId, String docId) {
        bookingId = bookId;
        documentId = docId;

        myadapterposition = adapterPosition;
        String[] items = new String[]{"Capture", "Select File"};
        new androidx.appcompat.app.AlertDialog.Builder(mContext).setTitle("File")
                .setItems(items, (dialog, item) -> {
                    switch (items[item]) {
//                        case "Capture Photo":
//                            dialog.dismiss();
//                            isCropEnabled = false;
//                            easyImage.openCameraForImage(this);
//                            break;
                        case "Capture":
                            dialog.dismiss();
                            isCropEnabled = false;
                            easyImage.openCameraForImage(this);
                            break;
                        case "Select File":
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            startActivityForResult(intent, PICK_PDF_FILE);
                            break;
                        case "Cancel":
                            dialog.dismiss();
                            break;
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PDF_FILE && resultCode == Activity.RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        Log.e("uti", uri.toString());
                        String mimeType = getContentResolver().getType(uri);
                        Log.e("mimeType", mimeType);
                        fileExtension = MimeTypes.findExtensionsByMimeTypes(mimeType, false)[0];
                        Log.e("fileExtension", fileExtension);
                        String path = createCopyAndReturnRealPath(mContext, uri, fileExtension);
                        if (path != null) {
                            Log.e("path", path);
                            if (fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png") ||
                                    fileExtension.equalsIgnoreCase("bmp") || fileExtension.equalsIgnoreCase("tiff"))
                                ;
                        }
                        imagePath = path;
                        Thread t = new Thread(() -> {
                            try {
                                UploadFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        );
                        t.start();

                    } else {
                        Log.e("uti", "null");
                    }
                    fileUri = uri;
                }
            } else {
                easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                    @Override
                    public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                        if (isCropEnabled) {
                            String dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                            SimpleDateFormat dateFormatForPhoto = new SimpleDateFormat("HHmmSS", Locale.ENGLISH);
                            String fileName = dateFormatForPhoto.format(System.currentTimeMillis()) + ".jpg";
                            File image = new File(dir + "/" + fileName);

                            imagePath = image.getAbsolutePath();

                            Uri cropUri = Uri.fromFile(image);

                            if (cropUri != null) {
                                Log.e("cropUri", String.valueOf(cropUri));
                            }
                            if (!TextUtils.isEmpty(imagePath)) {
                                fileExtension = imagePath.substring(imagePath.lastIndexOf("."));
                                fileExtension = fileExtension.replace(".", "");
                                Log.e("extension", fileExtension);
                            }
                            showCropAvatarFragment(Uri.fromFile(imageFiles[0].getFile()), cropUri);
                        } else {
                            imagePath = imageFiles[0].getFile().getAbsolutePath();
                            Log.e("imagePath", imagePath);

                            Thread t = new Thread(() -> {
                                try {
                                    UploadFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            );
                            t.start();

                            Uri uri = Uri.fromFile(imageFiles[0].getFile());
                            if (uri != null) {
                                Log.e("uri", String.valueOf(uri));
                            }
                            if (!TextUtils.isEmpty(imagePath)) {
                                fileExtension = imagePath.substring(imagePath.lastIndexOf("."));
                                fileExtension = fileExtension.replace(".", "");
                                Log.e("extension", fileExtension);
                            }
                        }
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

                if (requestCode == REQUEST_AVATAR_CODE) {
                    if (data == null) {
                        Toast.makeText(this, "Failed to crop image. Please, try again later", Toast.LENGTH_SHORT).show();
                        ;
                        return;
                    }

                    Uri resultUri = null;
                    try {
                        resultUri = UCrop.getOutput(data);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }

                    if (resultUri != null) {
//                        Glide.with(mContext).load(imagePath).into(ivDocument);
//                        etFile.setText(imagePath);
                    }
                }
            }
        }
    }

    private void showCropAvatarFragment(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(false);
        options.setShowCropFrame(true);
        options.setShowCropGrid(false);
        options.setToolbarTitle("Crop Photo");
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
//        options.withAspectRatio(1f, 1f);
//        options.withMaxResultSize(512, 512);
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this, REQUEST_AVATAR_CODE);
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @Nullable
    public String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri, String fileExtension) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
            filePath = context.getApplicationInfo().dataDir + File.separator
                    + System.currentTimeMillis() + "." + fileExtension;
        }

        file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }

    void UploadFile() throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("bookingid", bookingId);
        map.put("documentid", documentId);
        map.put("path", "photos/bookingdocumentphotos/" + System.currentTimeMillis() + ".jpg");
//        map.put("image",imagePath);
        Log.e("map", map.toString());

        //CI Function
//      multipartRequest("https://smartleaders.in/pmc-psrs/MobileApi/bookingdocumentupload", map, imagePath);

        multipartRequest(BuildConfig.BASE_URL + BuildConfig.PhotoPath + "bookingdocumentupload", map, imagePath);
//        multipartRequest("https://cultural.pmc.gov.in/cultural/MobileApi/bookingdocumentupload", map, imagePath);
    }

//    void multipartRequest(String urlTo, Map<String, String> parmas, String filepath) throws Exception {
//
//        String tempfilepath2 = "";
//        //compress photo
//        if (filepath != null && filepath.trim().length() > 5) {
//            File file2 = new Compressor(mContext)
//                    .setQuality(90) // Set quality to 60%
//                    .compressToFile(new File(filepath));
//            tempfilepath2 = file2.getAbsolutePath();
//        }
//
//        HttpURLConnection connection = null;
//        DataOutputStream outputStream = null;
//        InputStream inputStream = null;
//        String twoHyphens = "--";
//        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
//        String lineEnd = "\r\n";
//        String result = "";
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        String[] q = filepath.split("/");
//        int idx = q.length - 1;
//
//        try {
//
//            URL url = new URL(urlTo);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setUseCaches(false);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Connection", "Keep-Alive");
//            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//            outputStream = new DataOutputStream(connection.getOutputStream());
//
//            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//            //outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "image" + lineEnd);
//            outputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + System.currentTimeMillis() + "\"" + lineEnd);
//
//
//            // outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
//            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
//            outputStream.writeBytes(lineEnd);
//            //outputStream.write(loadFile(new File(filepath)));
//            outputStream.write(loadFile(new File(tempfilepath2)));
//            outputStream.writeBytes(lineEnd);
//
//            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            // Upload POST Data
//            Iterator<String> keys = parmas.keySet().iterator();
//            while (keys.hasNext()) {
//                String key = keys.next();
//                Object value = parmas.get(key);
//
//                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
//                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
//                outputStream.writeBytes(lineEnd);
//                outputStream.writeBytes(value.toString());
//                outputStream.writeBytes(lineEnd);
//                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            }
//
//            Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//            String responseStr = "";
//            for (int c; (c = in.read()) >= 0; ) {
//                responseStr += (char) c;
//                //   System.out.print((char) c);
//            }
//            inputStream = connection.getInputStream();
//            result = convertStreamToString(inputStream);
//            System.out.println(result);
//            inputStream.close();
//            outputStream.flush();
//            outputStream.close();
//
//
//            try {
//                JSONObject jsonObject = new JSONObject(responseStr);
//
//                String message = jsonObject.getString("Message");
//                runOnUiThread(() -> {
//
//                    MyAssetsOder_Activity.refreshpage = true;
//                    ToastDialog(mContext, message);
//                    DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
//                    String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_FOLDER + parmas.get("path");
//                    temp.setPath(updatedPhotoPath);
//                    this.dataAdapter.notifyItemChanged(myadapterposition);
//                });
//
//            } catch (JSONException e) {
//                runOnUiThread(() -> {
//
//                    MyAssetsOder_Activity.refreshpage = false;
//                    ToastDialog(mContext, "Failed to upload");
//                    DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
//                    String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_FOLDER + parmas.get("path");
//                    temp.setPath(updatedPhotoPath);
//                    this.dataAdapter.notifyItemChanged(myadapterposition);
//                });
//                e.printStackTrace();
//            }
//
//
//        } catch (Exception e) {
//
//            runOnUiThread(() -> {
//
//                MyAssetsOder_Activity.refreshpage = false;
//                ToastDialog(mContext, e.getLocalizedMessage());
//                DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
//                String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_FOLDER + parmas.get("path");
//                temp.setPath(updatedPhotoPath);
//                this.dataAdapter.notifyItemChanged(myadapterposition);
//            });
//          // System.err.println(e.toString());
//            //e.printStackTrace();
//        }
//    }


    void multipartRequest(String urlTo, Map<String, String> params, String filepath) {


        runOnUiThread(() -> binding.progressBar.setVisibility(View.VISIBLE));

        new Thread(() -> {
            try {
                String tempFilePath = "";

                // Step 1: Compress Image
                if (filepath != null && filepath.trim().length() > 5) {
                    File file = new Compressor(mContext)
                            .setQuality(90)
                            .compressToFile(new File(filepath));

                    tempFilePath = file.getAbsolutePath();
                }

                // Step 2: Create Multipart Body
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);

                // Add Image
                if (tempFilePath != null && tempFilePath.length() > 5) {
                    File file = new File(tempFilePath);

                    if (file.exists()) {
                        builder.addFormDataPart(
                                "image",
                                System.currentTimeMillis() + ".jpg",
                                RequestBody.create(file, MediaType.parse("image/jpeg"))
                        );
                    }
                }

                // Add Params
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }

                RequestBody requestBody = builder.build();

                // Step 3: Create Request
                Request request = new Request.Builder()
                        .url(urlTo)
                        .post(requestBody)
                        .build();

                // Step 4: OkHttp Client
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();

                Response response = client.newCall(request).execute();

                String responseStr = response.body() != null ? response.body().string() : "";

                Log.e("API_RESPONSE", responseStr);

                // Step 5: Parse JSON
                try {
                    JSONObject jsonObject = new JSONObject(responseStr);
                    String message = jsonObject.optString("Message");

                    runOnUiThread(() -> {

                        runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

                        MyAssetsOder_Activity.refreshpage = true;
                        ToastDialog(mContext, message);

                        DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
                        String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_PATH + params.get("path");
                        temp.setPath(updatedPhotoPath);

                        dataAdapter.notifyItemChanged(myadapterposition);
                    });

                } catch (JSONException e) {
                    runOnUiThread(() -> {

                        runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

                        MyAssetsOder_Activity.refreshpage = false;
                        ToastDialog(mContext, "Failed to upload");

                        DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
                        String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_PATH + params.get("path");
                        temp.setPath(updatedPhotoPath);

                        dataAdapter.notifyItemChanged(myadapterposition);
                    });

                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();

                runOnUiThread(() -> {

                    MyAssetsOder_Activity.refreshpage = false;
                    ToastDialog(mContext, e.getLocalizedMessage());

                    DocumentsPOJO temp = items.getDocumentsArrays().get(myadapterposition);
                    String updatedPhotoPath = BuildConfig.BASE_URL + BuildConfig.API_PATH + params.get("path");
                    temp.setPath(updatedPhotoPath);

                    dataAdapter.notifyItemChanged(myadapterposition);
                });
            }

        }).start();
    }

    static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
//            JOptionPane.showMessageDialog(null, "Could not completely read file " + file.getName());
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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