package mifta.project.id.golayfarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class inputDataKambing extends AppCompatActivity implements View.OnClickListener {
    ImageView foto;
    RadioGroup kelamin,dara;
    RadioButton jantan,betina,iya,tidak;
    EditText beratAwal, hargaBeli, tglMasuk, id_kambing;
    LinearLayout layDara;
    TextView idAdmin;
    Button save;
    Bitmap bitmap = null;
    FloatingActionButton fab, editfab;
    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    String jk,dr;
    private static final int PICK_Camera_IMAGE = 2;
    DatePickerDialog datePickerDialog;
    private double berat = Double.NaN, hasil;
    private Menu action;
    private int idKambing, id;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_kambing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.bt_foto);
        editfab = findViewById(R.id.bt_editfoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFoto();
            }
        });

        foto=(ImageView) findViewById(R.id.img_foto);
        kelamin=(RadioGroup)findViewById(R.id.rg_kelamin);
        dara=(RadioGroup)findViewById(R.id.rg_dara);
        jantan=(RadioButton)findViewById(R.id.rb_jantan);
        betina=(RadioButton)findViewById(R.id.rb_betina);
        iya=(RadioButton)findViewById(R.id.rb_iya);
        tidak=(RadioButton)findViewById(R.id.rb_tidak);
        beratAwal=(EditText)findViewById(R.id.et_beratAwal);
        id_kambing=(EditText)findViewById(R.id.et_idkambing);
        hargaBeli=(EditText)findViewById(R.id.et_hargaBeli);
        tglMasuk=(EditText)findViewById(R.id.et_tglMasuk);
        idAdmin=(TextView) findViewById(R.id.tv_adminK);
        save = (Button)findViewById(R.id.bt_simpanK);
        layDara=(LinearLayout)findViewById(R.id.layout_dara);

        save.setOnClickListener(this);
        hargaBeli.setOnClickListener(this);

        kelamin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_jantan){
                    jk = String.valueOf("Jantan");
                    layDara.setVisibility(View.GONE);
                    layDara.setActivated(false);
                }else if (checkedId == R.id.rb_betina){
                    jk = String.valueOf("Betina");
                    layDara.setVisibility(View.VISIBLE);
                    layDara.setActivated(true);
                }
            }
        });

        dara.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_iya){
                    dr = String.valueOf("Iya");
                }else if (checkedId == R.id.rb_tidak){
                    dr = String.valueOf("Tidak");
                }
            }
        });

        tglMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(inputDataKambing.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tglMasuk.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        idKambing = intent.getIntExtra("idKambing",0);
        perizinan();
        setDataFromIntentExtra();

        statusAdmin();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data_kambing, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        action.findItem(R.id.menu_edit).setVisible(true);
        editfab.setVisibility(View.VISIBLE);

        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        id = item.getItemId();

        if (id == R.id.menu_edit) {
            edit();
        }
        if (id == R.id.menu_save) {
            simpan();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDataFromIntentExtra() {
        if (idKambing != 0) {
            cari();
            getSupportActionBar().setTitle("Edit Data ");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_hargaBeli:
                //harga();
                break;
        }
    }

    private void perizinan(){
        ActivityCompat.requestPermissions(inputDataKambing.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                99);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    perizinan();
                }
                return;
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Ambil Foto", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                inputDataKambing.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil Foto")) {
                    String fileName = "new-photo-name.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, PICK_Camera_IMAGE);
                } else if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE);
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = imageUri;
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                String filemanagerstring = selectedImageUri.getPath();
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(inputDataKambing.this, "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(inputDataKambing.this, "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        foto.setImageBitmap(bitmap);

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void simpan(){
        final String id_kambing_ = id_kambing.getText().toString();
        final String kelamin_ = jk.toString();
        final String berat_ = beratAwal.getText().toString();
        final String hargaBeli_ = hargaBeli.getText().toString();
        final String tglMasuk_ = tglMasuk.getText().toString();
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.simpanKambing, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataKambing.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataKambing.this, dataKambing.class));
                }else{
                    Toast.makeText(inputDataKambing.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataKambing.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing,id_kambing_);
                if (kelamin_.equals("Betina")){
                    final String dara_ = dr.toString();
                    params.put(koneksi.key_dara,dara_);
                }
                params.put(koneksi.key_foto,getStringImage(bitmap));
                params.put(koneksi.key_kelamin,kelamin_);
                params.put(koneksi.key_beratAwal,berat_);
                params.put(koneksi.key_hargaBeli,hargaBeli_);
                params.put(koneksi.key_tgl_masuk,tglMasuk_);
                params.put(koneksi.key_idAdmin,id_admin_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                    idAdmin.setText(c.getString(koneksi.key_nama));
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "data tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(inputDataKambing.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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

    private void cari() {
        final String id_ = String.valueOf(idKambing);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.cariKambing,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("result");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    Glide.with(inputDataKambing.this)
                                            .load(c.get(koneksi.key_foto))
                                            .into(foto);
                                    String jenis_kelamin_ = c.getString(koneksi.key_kelamin);
                                    if (jenis_kelamin_.toString().equals("Jantan")){
                                        jantan.setChecked(true);
                                        layDara.setVisibility(View.GONE);
                                        layDara.setActivated(false);
                                    }else if (jenis_kelamin_.toString().equals("Betina")){
                                        betina.setChecked(true);
                                        layDara.setVisibility(View.VISIBLE);
                                        layDara.setActivated(true);
                                    }
                                    String dara_ = c.getString(koneksi.key_dara);
                                    if (dara_.toString().equals("Iya")){
                                        iya.setChecked(true);
                                    }else if (dara_.toString().equals("Tidak")){
                                        tidak.setChecked(true);
                                    }
                                    id_kambing.setText(c.getString(koneksi.key_idKambing));
                                    beratAwal.setText(c.getString(koneksi.key_beratAwal));
                                    hargaBeli.setText(c.getString(koneksi.key_hargaBeli));
                                    tglMasuk.setText(c.getString(koneksi.key_tgl_masuk));
                                    idAdmin.setText(c.getString(koneksi.key_idAdmin));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Data Kurang Tepat", Toast.LENGTH_SHORT).show();
                            Intent failed = new Intent(getApplicationContext(), inputDataKambing.class);
                            startActivity(failed);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_id, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void edit(){
        final String id_ = String.valueOf(idKambing);
        final String id_kambing_ = id_kambing.getText().toString();
        final String kelamin_ = jk.toString();
        final String berat_ = beratAwal.getText().toString();
        final String hargaBeli_ = hargaBeli.getText().toString();
        final String tglMasuk_ = tglMasuk.getText().toString();
        final String id_admin_ = idAdmin.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editKambing, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataKambing.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataKambing.this, dataKambing.class));
                }else{
                    Toast.makeText(inputDataKambing.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataKambing.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_id, id_);
                params.put(koneksi.key_idKambing, id_kambing_);
                params.put(koneksi.key_kelamin,kelamin_);
                if (kelamin_.equals("Betina")){
                    final String dara_ = dr.toString();
                    params.put(koneksi.key_dara,dara_);
                }
                params.put(koneksi.key_beratAwal,berat_);
                params.put(koneksi.key_hargaBeli,hargaBeli_);
                params.put(koneksi.key_tgl_masuk,tglMasuk_);
                params.put(koneksi.key_idAdmin,id_admin_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void editFoto(){
        final String id_ = String.valueOf(idKambing);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editFoto, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataKambing.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(inputDataKambing.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataKambing.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_id, id_);
                params.put(koneksi.key_foto,getStringImage(bitmap));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void harga(){
        berat = Double.parseDouble(beratAwal.getText().toString());
        hasil = berat*32000;

        hargaBeli.setText(String.valueOf(hasil));
    }

    private void statusAdmin(){
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.dataAdmin,
                new Response.Listener<String>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")){
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("result");
                                for (int i=0; i<result.length(); i++){
                                    JSONObject c = result.getJSONObject(i);
                                    getMenuInflater().inflate(R.menu.menu_input_data_kambing, action);
                                    final String status_ = c.getString(koneksi.key_statusAdmin);
                                    if(idKambing == 0){
                                        action.findItem(R.id.menu_edit).setVisible(false);
                                        action.findItem(R.id.menu_save).setVisible(true);
                                        editfab.setVisibility(View.GONE);
                                        fab.setVisibility(View.VISIBLE);
                                        txtAdmin();
                                    }else{
                                        if (status_.equals("operator")){
                                            fab.setVisibility(View.GONE);
                                            editfab.setVisibility(View.GONE);
                                            action.findItem(R.id.menu_edit).setVisible(false);
                                            getSupportActionBar().setTitle("Data Kambing");

                                        }else if (status_.equals("admin")){
                                            fab.setVisibility(View.VISIBLE);
                                            editfab.setVisibility(View.VISIBLE);
                                            action.findItem(R.id.menu_edit).setVisible(true);
                                        }
                                    }
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "data tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(inputDataKambing.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
