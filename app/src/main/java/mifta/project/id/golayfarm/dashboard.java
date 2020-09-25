package mifta.project.id.golayfarm;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    CardView kambing,obat,pakan,gemuk,jual;
    ImageView logout;
    TextView txtAdmin;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = dashboard.class.getSimpleName();
    private String url, nama_file;
    private Menu action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Go Lay Farm");

        kambing =(CardView)findViewById(R.id.crd_kambing);
        obat = (CardView)findViewById(R.id.crd_obat);
        pakan = (CardView)findViewById(R.id.crd_pakan);
        gemuk=(CardView)findViewById(R.id.crd_gemuk);
        jual=(CardView)findViewById(R.id.crd_jual);
        logout=(ImageView)findViewById(R.id.bt_logout);
        txtAdmin=(TextView) findViewById(R.id.tv_admin);

        kambing.setOnClickListener(this);
        obat.setOnClickListener(this);
        pakan.setOnClickListener(this);
        gemuk.setOnClickListener(this);
        jual.setOnClickListener(this);
        logout.setOnClickListener(this);
        txtAdmin();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crd_kambing:
                startActivity(new Intent(dashboard.this, dataKambing.class));
                break;
            case R.id.crd_obat:
                startActivity(new Intent(dashboard.this, dataObat.class));
                break;
            case R.id.crd_pakan:
                startActivity(new Intent(dashboard.this, dataPakan.class));
                break;
            case R.id.crd_gemuk:
                startActivity(new Intent(dashboard.this, dataPenggemukan.class));
                break;
            case R.id.crd_jual:
                startActivity(new Intent(dashboard.this, dataPenjualan.class));
                break;
            case R.id.bt_logout:
                logout();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        action = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.ex_kambing) {
            if (CheckForSDCard.isSDCardPresent()){
                if (EasyPermissions.hasPermissions(dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    url = koneksi.excelKambing;
                    nama_file = "Data Kambing";
                    new dashboard.DownloadFile().execute(url);
                }else {
                    EasyPermissions.requestPermissions( dashboard.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                Toast.makeText(getApplicationContext(), "SD Card Not Found", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.ex_obat) {
            if (CheckForSDCard.isSDCardPresent()){
                if (EasyPermissions.hasPermissions(dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    url = koneksi.excelObat;
                    nama_file = "Data Pengobatan";
                    new dashboard.DownloadFile().execute(url);
                }else {
                    EasyPermissions.requestPermissions( dashboard.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                Toast.makeText(getApplicationContext(), "SD Card Not Found", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.ex_pakan) {
            if (CheckForSDCard.isSDCardPresent()){
                if (EasyPermissions.hasPermissions(dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    url = koneksi.excelPakan;
                    nama_file = "Data Pakan";
                    new dashboard.DownloadFile().execute(url);
                }else {
                    EasyPermissions.requestPermissions( dashboard.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                Toast.makeText(getApplicationContext(), "SD Card Not Found", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.ex_gemuk) {
            if (CheckForSDCard.isSDCardPresent()){
                if (EasyPermissions.hasPermissions(dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    url = koneksi.excelGemuk;
                    nama_file = "Data Penggemukan";
                    new dashboard.DownloadFile().execute(url);
                }else {
                    EasyPermissions.requestPermissions( dashboard.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                Toast.makeText(getApplicationContext(), "SD Card Not Found", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.ex_jual) {
            if (CheckForSDCard.isSDCardPresent()){
                if (EasyPermissions.hasPermissions(dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    url = koneksi.excelJual;
                    nama_file = "Data Penjualan";
                    new dashboard.DownloadFile().execute(url);
                }else {
                    EasyPermissions.requestPermissions( dashboard.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else {
                Toast.makeText(getApplicationContext(), "SD Card Not Found", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, dashboard.this);
    }

//    //@Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        //Download the file once permission is granted
//        url = "http://tazzakha.com/golay/excel.php";
//        new DownloadFile().execute(url);
//    }
//
//    //@Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        Log.d(TAG, "Permission has been denied");
//    }


    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(dashboard.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + "_" + nama_file +".xls";

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "Golay Farm/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }

    private void logout() {
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        SharedPreferences.Editor akses = sharedPreferences.edit();
        akses.clear();
        akses.commit();
        startActivity(new Intent(dashboard.this, login.class));
        finish();
    }

    private void txtAdmin(){
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.dataAdmin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")){
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("result");
                                for (int i=0; i<result.length(); i++){
                                    JSONObject c = result.getJSONObject(i);
                                    txtAdmin.setText("Welcome "+c.getString(koneksi.key_nama));
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Email atau Password anda Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(dashboard.this, "Tidak Ada Koneksi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idAdmin, id_admin_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
