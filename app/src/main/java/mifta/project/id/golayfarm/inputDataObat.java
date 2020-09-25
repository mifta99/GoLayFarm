package mifta.project.id.golayfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class inputDataObat extends AppCompatActivity implements View.OnClickListener {
    EditText idkambing, jenis, qty, satuan, tgl, harga;
    TextView admin;
    Button save, edit;
    DatePickerDialog datePickerDialog;
    private Menu action;
    private int idObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_obat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idkambing = (EditText)findViewById(R.id.et_idKambingO);
        jenis=(EditText)findViewById(R.id.et_jenisObat);
        qty=(EditText)findViewById(R.id.et_qtyObat);
        satuan=(EditText)findViewById(R.id.et_satuanObat);
        tgl=(EditText)findViewById(R.id.et_tglObat);
        harga=(EditText)findViewById(R.id.et_hargaObat);
        admin=(TextView)findViewById(R.id.tv_adminO);
        save=(Button)findViewById(R.id.bt_simpanO);
        edit=(Button)findViewById(R.id.bt_EditO);

        save.setOnClickListener(this);
        edit.setOnClickListener(this);

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(inputDataObat.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tgl.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        idObat = intent.getIntExtra("id_obat",0);
        setDataFromIntentExtra();
        statusAdmin();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_simpanO:
                simpan();
                break;
            case R.id.bt_EditO:
                //simpan();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data_obat, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_edit) {
            edit();
        }
        if (id == R.id.menu_save) {
            simpan();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDataFromIntentExtra() {
        if (idObat != 0) {
            cari();
            getSupportActionBar().setTitle("Edit Data ");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }

    private void simpan(){
        final String idKambing_ = idkambing.getText().toString();
        final String jenisObat_ = jenis.getText().toString();
        final String qty_ = qty.getText().toString();
        final String satuan_ = satuan.getText().toString();
        final String harga_ = harga.getText().toString();
        final String tglObat_ = tgl.getText().toString();
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.simpanObat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataObat.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataObat.this, dataObat.class));
                }else{
                    Toast.makeText(inputDataObat.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataObat.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing,idKambing_);
                params.put(koneksi.key_jenisObat,jenisObat_);
                params.put(koneksi.key_qtyObat,qty_);
                params.put(koneksi.key_satuan,satuan_);
                params.put(koneksi.key_hargaObat,harga_);
                params.put(koneksi.key_tglObat,tglObat_);
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
                                    admin.setText(c.getString(koneksi.key_nama));
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
                        Toast.makeText(inputDataObat.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id_ = String.valueOf(idObat);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.cariObat,
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
                                    idkambing.setText(c.getString(koneksi.key_idKambing));
                                    jenis.setText(c.getString(koneksi.key_jenisObat));
                                    qty.setText(c.getString(koneksi.key_qtyObat));
                                    satuan.setText(c.getString(koneksi.key_satuan));
                                    tgl.setText(c.getString(koneksi.key_tglObat));
                                    harga.setText(c.getString(koneksi.key_hargaObat));
                                    admin.setText(c.getString(koneksi.key_idAdmin));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Data Kurang Tepat", Toast.LENGTH_SHORT).show();
                            Intent failed = new Intent(getApplicationContext(), inputDataObat.class);
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
                params.put(koneksi.key_idObat, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void edit(){
        final String id_ = String.valueOf(idObat);
        final String idkambing_ = idkambing.getText().toString();
        final String jenis_ = jenis.getText().toString();
        final String qty_ = qty.getText().toString();
        final String satuan_ = satuan.getText().toString();
        final String harga_ = harga.getText().toString();
        final String tgl_ = tgl.getText().toString();
        final String id_admin_ = admin.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editObat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataObat.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataObat.this, dataObat.class));
                }else{
                    Toast.makeText(inputDataObat.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataObat.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idObat, id_);
                params.put(koneksi.key_idKambing,idkambing_);
                params.put(koneksi.key_jenisObat,jenis_);
                params.put(koneksi.key_qtyObat,qty_);
                params.put(koneksi.key_satuan,satuan_);
                params.put(koneksi.key_hargaObat,harga_);
                params.put(koneksi.key_tglObat,tgl_);
                params.put(koneksi.key_idAdmin,id_admin_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                    if(idObat == 0){
                                        action.findItem(R.id.menu_edit).setVisible(false);
                                        action.findItem(R.id.menu_save).setVisible(true);
                                        txtAdmin();
                                    }else{
                                        if (status_.equals("operator")){
                                            action.findItem(R.id.menu_edit).setVisible(false);
                                            getSupportActionBar().setTitle("Data Obat");
                                        }else if (status_.equals("admin")){
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
                        Toast.makeText(inputDataObat.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
