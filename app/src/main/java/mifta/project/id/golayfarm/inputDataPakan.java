package mifta.project.id.golayfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class inputDataPakan extends AppCompatActivity implements View.OnClickListener {
    EditText idkambing, nama, qty, satuan, tgl, harga;
    TextView admin;
    Button save;
    DatePickerDialog datePickerDialog;
    private Menu action;
    private int idPakan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_pakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idkambing = (EditText)findViewById(R.id.et_idkambingP);
        nama=(EditText)findViewById(R.id.et_namaPakan);
        qty=(EditText)findViewById(R.id.et_qtyPakan);
        satuan=(EditText)findViewById(R.id.et_satuanPakan);
        tgl=(EditText)findViewById(R.id.et_tglPakan);
        harga=(EditText)findViewById(R.id.et_hargapakan);
        admin=(TextView)findViewById(R.id.tv_adminP);
        save=(Button)findViewById(R.id.bt_simpanP);

        save.setOnClickListener(this);

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(inputDataPakan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tgl.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        idPakan = intent.getIntExtra("id_pakan",0);
        setDataFromIntentExtra();
        statusAdmin();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_simpanP:
                simpan();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data_pakan, menu);
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
        if (idPakan != 0) {
            cari();
            getSupportActionBar().setTitle("Edit Data ");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }

    private void simpan(){
        final String idKambing_ = idkambing.getText().toString();
        final String namaPakan_ = nama.getText().toString();
        final String qty_ = qty.getText().toString();
        final String satuan_ = satuan.getText().toString();
        final String harga_ = harga.getText().toString();
        final String tglpakan_ = tgl.getText().toString();
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.simpanPakan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataPakan.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataPakan.this, dataPakan.class));
                }else{
                    Toast.makeText(inputDataPakan.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataPakan.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing,idKambing_);
                params.put(koneksi.key_namaPakan,namaPakan_);
                params.put(koneksi.key_qtyPakan,qty_);
                params.put(koneksi.key_satuanPakan,satuan_);
                params.put(koneksi.key_hargaPakan,harga_);
                params.put(koneksi.key_tglPakan,tglpakan_);
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
                        Toast.makeText(inputDataPakan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id_ = String.valueOf(idPakan);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.cariPakan,
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
                                    nama.setText(c.getString(koneksi.key_namaPakan));
                                    qty.setText(c.getString(koneksi.key_qtyPakan));
                                    satuan.setText(c.getString(koneksi.key_satuanPakan));
                                    tgl.setText(c.getString(koneksi.key_tglPakan));
                                    harga.setText(c.getString(koneksi.key_hargaPakan));
                                    admin.setText(c.getString(koneksi.key_idAdmin));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Data Kurang Tepat", Toast.LENGTH_SHORT).show();
                            Intent failed = new Intent(getApplicationContext(), inputDataPakan.class);
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
                params.put(koneksi.key_idPakan, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void edit(){
        final String id_ = String.valueOf(idPakan);
        final String idkambing_ = idkambing.getText().toString();
        final String nama_ = nama.getText().toString();
        final String qty_ = qty.getText().toString();
        final String satuan_ = satuan.getText().toString();
        final String harga_ = harga.getText().toString();
        final String tgl_ = tgl.getText().toString();
        final String id_admin_ = admin.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editPakan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataPakan.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataPakan.this, dataPakan.class));
                }else{
                    Toast.makeText(inputDataPakan.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataPakan.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idPakan, id_);
                params.put(koneksi.key_idKambing,idkambing_);
                params.put(koneksi.key_namaPakan,nama_);
                params.put(koneksi.key_qtyPakan,qty_);
                params.put(koneksi.key_satuanPakan,satuan_);
                params.put(koneksi.key_hargaPakan,harga_);
                params.put(koneksi.key_tglPakan,tgl_);
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
                                    if(idPakan == 0){
                                        action.findItem(R.id.menu_edit).setVisible(false);
                                        action.findItem(R.id.menu_save).setVisible(true);
                                        txtAdmin();
                                    }else{
                                        if (status_.equals("operator")){
                                            action.findItem(R.id.menu_edit).setVisible(false);
                                            getSupportActionBar().setTitle("Data Pakan");

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
                        Toast.makeText(inputDataPakan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
