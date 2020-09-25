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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class inputDataPenggemukan extends AppCompatActivity implements View.OnClickListener {
    EditText idkambing, berat, tgl;
    TextView admin;
    Button save;
    DatePickerDialog datePickerDialog;
    RadioGroup kondisi;
    RadioButton sehat,sakit,mati;
    String kon;
    private Menu action;
    private int idGemuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_penggemukan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idkambing = (EditText)findViewById(R.id.et_idkambingG);
        berat=(EditText)findViewById(R.id.et_beratG);
        tgl=(EditText)findViewById(R.id.et_tglGemuk);
        admin=(TextView)findViewById(R.id.tv_adminG);
        save=(Button)findViewById(R.id.bt_simpanG);
        kondisi=(RadioGroup)findViewById(R.id.rg_kondisi);
        sehat=(RadioButton)findViewById(R.id.rb_sehat);
        sakit=(RadioButton)findViewById(R.id.rb_sakit);
        mati=(RadioButton)findViewById(R.id.rb_mati);

        save.setOnClickListener(this);

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(inputDataPenggemukan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tgl.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        kondisi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_sehat){
                    kon = String.valueOf("Sehat");
                }else if (checkedId == R.id.rb_sakit){
                    kon = String.valueOf("Sakit");
                }else if (checkedId == R.id.rb_mati){
                    kon = String.valueOf("Mati");
                }
            }
        });

        Intent intent = getIntent();
        idGemuk = intent.getIntExtra("id_gemuk",0);
        setDataFromIntentExtra();
        statusAdmin();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_simpanG:
                simpan();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data_penggemukan, menu);
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
        if (idGemuk != 0) {
            cari();
            getSupportActionBar().setTitle("Edit Data ");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }
    private void simpan(){
        final String idKambing_ = idkambing.getText().toString();
        final String berat_ = berat.getText().toString();
        final String tgl_ = tgl.getText().toString();
        final String kon_ = kon.toString();
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.simpanGemuk, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataPenggemukan.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataPenggemukan.this, dataPenggemukan.class));
                }else{
                    Toast.makeText(inputDataPenggemukan.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataPenggemukan.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing,idKambing_);
                params.put(koneksi.key_berat,berat_);
                params.put(koneksi.key_tglTimbang,tgl_);
                params.put(koneksi.key_kondisi,kon_);
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
                        Toast.makeText(inputDataPenggemukan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id_ = String.valueOf(idGemuk);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.cariGemuk,
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
                                    berat.setText(c.getString(koneksi.key_berat));
                                    tgl.setText(c.getString(koneksi.key_tglTimbang));
                                    admin.setText(c.getString(koneksi.key_idAdmin));
                                    String kondisi_ = c.getString(koneksi.key_kondisi);
                                    if (kondisi_.toString().equals("Sehat")){
                                        sehat.setChecked(true);
                                    }else if (kondisi_.toString().equals("Sakit")){
                                        sakit.setChecked(true);
                                    }else if (kondisi_.toString().equals("Mati")){
                                        mati.setChecked(true);
                                    }

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
                params.put(koneksi.key_idGemuk, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void edit(){
        final String id_ = String.valueOf(idGemuk);
        final String idkambing_ = idkambing.getText().toString();
        final String berat_ = berat.getText().toString();
        final String tgl_ = tgl.getText().toString();
        final String id_admin_ = admin.getText().toString();
        final String kon_ = kon.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editGemuk, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataPenggemukan.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataPenggemukan.this, dataPenggemukan.class));
                }else{
                    Toast.makeText(inputDataPenggemukan.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataPenggemukan.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idGemuk, id_);
                params.put(koneksi.key_idKambing,idkambing_);
                params.put(koneksi.key_berat,berat_);
                params.put(koneksi.key_tglTimbang,tgl_);
                params.put(koneksi.key_kondisi,kon_);
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
                                    if(idGemuk == 0){
                                        action.findItem(R.id.menu_edit).setVisible(false);
                                        action.findItem(R.id.menu_save).setVisible(true);
                                        txtAdmin();
                                    }else{
                                        if (status_.equals("operator")){
                                            action.findItem(R.id.menu_edit).setVisible(false);
                                            getSupportActionBar().setTitle("Data Penggemukan");

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
                        Toast.makeText(inputDataPenggemukan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
