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

public class inputDataHasil extends AppCompatActivity implements View.OnClickListener {
    EditText idKambing, beratAkhir, biayaOp, tgl, lamaFat, ethargaJual;
    TextView hargaJual, totalObat, totalPakan,  totalBiaya, laba, status, admin, hrgBeli, brtAwal, adg, beratTambah, beratPanen,
            tvBiayaOp, kelamin;
    Button cek, save, cekLaba;
    DatePickerDialog datePickerDialog;
    private double harga= Double.NaN, totalHarga, hitungAdg, fat=Double.NaN, brt=Double.NaN, hitungBerat,
            bAw = Double.NaN, bAk =Double.NaN, pembelian=Double.NaN, pengobatan=Double.NaN, pakan=Double.NaN,
            operasional=Double.NaN, total, hrgJual=Double.NaN, biaya=Double.NaN, hitunglaba;
    private Menu action;
    private int idHasil ;
    private String kambingId;
    RadioGroup sj;
    RadioButton terjual,belum;
    String sj_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_hasil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        idKambing=(EditText)findViewById(R.id.et_idkambingJ);
        beratAkhir=(EditText)findViewById(R.id.et_beratAkhir);
        tgl=(EditText)findViewById(R.id.et_tglKeluar);
        lamaFat=(EditText)findViewById(R.id.et_lamaFat);
        ethargaJual=(EditText)findViewById(R.id.et_hargaJual);
        hargaJual=(TextView)findViewById(R.id.tv_hargaJual);
        totalObat=(TextView)findViewById(R.id.tv_totalObat);
        totalPakan=(TextView)findViewById(R.id.tv_totalPakan);
        hrgBeli=(TextView)findViewById(R.id.tv_hargaBeli);
        biayaOp=(EditText) findViewById(R.id.et_biayaOp);
        totalBiaya=(TextView)findViewById(R.id.tv_totalBiaya);
        laba=(TextView)findViewById(R.id.tv_laba);
        kelamin=(TextView)findViewById(R.id.tv_kelamin);
        brtAwal=(TextView)findViewById(R.id.tv_beratawal);
        adg=(TextView)findViewById(R.id.tv_adg);
        beratTambah=(TextView)findViewById(R.id.tv_beratTambah);
        beratPanen=(TextView)findViewById(R.id.tv_beratPanen);
        tvBiayaOp=(TextView)findViewById(R.id.tv_biayaOp);
        status=(TextView)findViewById(R.id.tv_status);
        admin=(TextView)findViewById(R.id.tv_adminJ);
        cek=(Button) findViewById(R.id.bt_check);
        save=(Button)findViewById(R.id.bt_simpanJ);
        cekLaba=(Button)findViewById(R.id.bt_hitungLaba);
        sj=(RadioGroup)findViewById(R.id.rg_statusJual);
        terjual=(RadioButton)findViewById(R.id.rb_terjual);
        belum=(RadioButton)findViewById(R.id.rb_belum);

        sj.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_terjual){
                    sj_ = String.valueOf("Terjual");
                }else if (checkedId == R.id.rb_belum){
                    sj_ = String.valueOf("Belum Terjual");
                }
            }
        });

        cek.setOnClickListener(this);
        save.setOnClickListener(this);
        cekLaba.setOnClickListener(this);
        beratAkhir.setOnClickListener(this);
        biayaOp.setOnClickListener(this);
        lamaFat.setOnClickListener(this);
        tgl.setOnClickListener(this);

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(inputDataHasil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tgl.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });



        Intent intent = getIntent();
        idHasil = intent.getIntExtra("id_jual",0);
        kambingId = intent.getStringExtra("id_kambing");
        setDataFromIntentExtra();
        statusAdmin();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_beratAkhir:
                cari();
                cariPakan();
                cariHargaBeli();
                break;
            case R.id.et_biayaOp:
                //hitungHarga();
                hitungTambahBerat();
                beratPanen.setText(beratAkhir.getText().toString());
                break;
            case R.id.et_lamaFat:
                tvBiayaOp.setText(biayaOp.getText().toString());
                break;
            case R.id.bt_check:
                hitungTotalBiaya();
                hitungAdg();
                hargaJual.setText(ethargaJual.getText().toString());
                break;
            case R.id.bt_hitungLaba:
                hitungLaba();
                cekStatus();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_data_hasil, menu);
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
        if (idHasil != 0) {
            caridata();
            cariTambahan();
            getSupportActionBar().setTitle("Edit Data ");
        } else {
            getSupportActionBar().setTitle("Tambah Data");
        }
    }
    private void hitungHarga(){
        harga = Double.parseDouble(beratAkhir.getText().toString());
        totalHarga = harga*35000;

        hargaJual.setText(String.valueOf(totalHarga));

    }

    private void hitungTambahBerat(){
        bAw = Double.parseDouble(brtAwal.getText().toString());
        bAk = Double.parseDouble(beratAkhir.getText().toString());
        hitungBerat = bAk - bAw;
        beratTambah.setText(String.valueOf(hitungBerat));

    }

    private void hitungAdg(){
        fat = Double.parseDouble(lamaFat.getText().toString());
        brt = Double.parseDouble(beratTambah.getText().toString());
        hitungAdg = brt/fat;

        adg.setText(String.valueOf(hitungAdg));

    }

    private void hitungTotalBiaya(){
        pembelian = Double.parseDouble(hrgBeli.getText().toString());
        pengobatan = Double.parseDouble(totalObat.getText().toString());
        pakan = Double.parseDouble(totalPakan.getText().toString());
        operasional = Double.parseDouble(tvBiayaOp.getText().toString());
        total = pembelian+pengobatan+pakan+operasional;

        totalBiaya.setText(String.valueOf(total));

    }

    private void cekStatus(){
        final String kelamin_ = kelamin.getText().toString();
        final double tambahBerat = Double.parseDouble(beratTambah.getText().toString());

        if (kelamin_.equals("Jantan")){
            if (tambahBerat >= 4.2){
                status.setText("Target Panen Tercapai");
            }else {
                status.setText("Target Panen Tidak Tercapai");
            }
        }else if (kelamin_.equals("Betina")){
            if (tambahBerat >= 3.5){
                status.setText("Target Panen Tercapai");
            }else {
                status.setText("Target Panen Tidak Tercapai");
            }
        }

    }
    private void hitungLaba(){
        hrgJual = Double.parseDouble(hargaJual.getText().toString());
        biaya = Double.parseDouble(totalBiaya.getText().toString());
        hitunglaba = hrgJual-biaya;

        laba.setText(String.valueOf(hitunglaba));

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
                        Toast.makeText(inputDataHasil.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id_ = idKambing.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.hasil,
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
                                    totalObat.setText(c.getString(koneksi.key_totalObat));
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
                params.put(koneksi.key_idKambing, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cariPakan() {
        final String id_ = idKambing.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.hasilPakan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("resultPakan");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject c = result.getJSONObject(i);
                                    totalPakan.setText(c.getString(koneksi.key_totalPakan));
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
                params.put(koneksi.key_idKambing, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cariHargaBeli() {
        final String id_ = idKambing.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistKambing,
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
                                    hrgBeli.setText(c.getString(koneksi.key_hargaBeli));
                                    brtAwal.setText(c.getString(koneksi.key_beratAwal));
                                    kelamin.setText(c.getString(koneksi.key_kelamin));
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
                params.put(koneksi.key_idKambing, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void simpan(){
        final String idKambing_ = idKambing.getText().toString();
        final String beratAkhir_ = beratAkhir.getText().toString();
        final String fat_ = lamaFat.getText().toString();
        final String adg_ = adg.getText().toString();
        final String tambahBerat_ = beratTambah.getText().toString();
        final String hargaJual_ = ethargaJual.getText().toString();
        final String totalObat_ = totalObat.getText().toString();
        final String totalPakan_ = totalPakan.getText().toString();
        final String biayaOp_ = biayaOp.getText().toString();
        final String totalbiaya_ = totalBiaya.getText().toString();
        final String laba_ = laba.getText().toString();
        final String status_ = status.getText().toString();
        final String tglKeluar_ = tgl.getText().toString();
        final String statusJual = sj_.toString();
        final SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        final String id_admin_ = sharedPreferences.getString("id_admin", "0");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.simpanJual, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataHasil.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataHasil.this, dataPenjualan.class));
                }else{
                    Toast.makeText(inputDataHasil.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataHasil.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing,idKambing_);
                params.put(koneksi.key_beratAkhir,beratAkhir_);
                params.put(koneksi.key_lamaFat,fat_);
                params.put(koneksi.key_adg,adg_);
                params.put(koneksi.key_tambahBerat,tambahBerat_);
                params.put(koneksi.key_hargaJual,hargaJual_);
                params.put(koneksi.key_totalObat,totalObat_);
                params.put(koneksi.key_totalPakan,totalPakan_);
                params.put(koneksi.key_biayaop,biayaOp_);
                params.put(koneksi.key_totalBiaya,totalbiaya_);
                params.put(koneksi.key_laba,laba_);
                params.put(koneksi.key_status,status_);
                params.put(koneksi.key_tglKeluar,tglKeluar_);
                params.put(koneksi.key_status_jual,statusJual);
                params.put(koneksi.key_idAdmin,id_admin_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void caridata() {
        final String id_ = String.valueOf(idHasil);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.cariJual,
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
                                    idKambing.setText(c.getString(koneksi.key_idKambing));
                                    beratAkhir.setText(c.getString(koneksi.key_beratAkhir));
                                    lamaFat.setText(c.getString(koneksi.key_lamaFat));
                                    biayaOp.setText(c.getString(koneksi.key_biayaop));
                                    tgl.setText(c.getString(koneksi.key_tglKeluar));
                                    adg.setText(c.getString(koneksi.key_adg));
                                    beratTambah.setText(c.getString(koneksi.key_tambahBerat));
                                    beratPanen.setText(c.getString(koneksi.key_beratAkhir));
                                    hargaJual.setText(c.getString(koneksi.key_hargaJual));
                                    ethargaJual.setText(c.getString(koneksi.key_hargaJual));
                                    totalObat.setText(c.getString(koneksi.key_totalObat));
                                    totalPakan.setText(c.getString(koneksi.key_totalPakan));
                                    status.setText(c.getString(koneksi.key_status));
                                    totalBiaya.setText(c.getString(koneksi.key_totalBiaya));
                                    tvBiayaOp.setText(c.getString(koneksi.key_biayaop));
                                    laba.setText(c.getString(koneksi.key_laba));
                                    admin.setText(c.getString(koneksi.key_idAdmin));
                                    String sj_ = c.getString(koneksi.key_status_jual);
                                    if (sj_.toString().equals("Terjual")){
                                        terjual.setChecked(true);
                                    }else if (sj_.toString().equals("Belum Terjual")){
                                        belum.setChecked(true);
                                    }
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
                params.put(koneksi.key_idHasil, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void cariTambahan() {
        final String id_ = kambingId.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistKambing,
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
                                    hrgBeli.setText(c.getString(koneksi.key_hargaBeli));
                                    brtAwal.setText(c.getString(koneksi.key_beratAwal));
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
                params.put(koneksi.key_idKambing, id_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void edit(){
        final String id_ = String.valueOf(idHasil);
        final String idKambing_ = idKambing.getText().toString();
        final String beratAkhir_ = beratAkhir.getText().toString();
        final String fat_ = lamaFat.getText().toString();
        final String adg_ = adg.getText().toString();
        final String tambahBerat_ = beratTambah.getText().toString();
        final String hargaJual_ = ethargaJual.getText().toString();
        final String totalObat_ = totalObat.getText().toString();
        final String totalPakan_ = totalPakan.getText().toString();
        final String biayaOp_ = biayaOp.getText().toString();
        final String totalbiaya_ = totalBiaya.getText().toString();
        final String laba_ = laba.getText().toString();
        final String status_ = status.getText().toString();
        final String tglKeluar_ = tgl.getText().toString();
        final String statusJual = sj_.toString();
        final String id_admin_ = admin.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.editJual, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(inputDataHasil.this, "Data Berhasil Disimpan",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(inputDataHasil.this, dataPenjualan.class));
                }else{
                    Toast.makeText(inputDataHasil.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inputDataHasil.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idHasil, id_);
                params.put(koneksi.key_idKambing,idKambing_);
                params.put(koneksi.key_beratAkhir,beratAkhir_);
                params.put(koneksi.key_lamaFat,fat_);
                params.put(koneksi.key_adg,adg_);
                params.put(koneksi.key_tambahBerat,tambahBerat_);
                params.put(koneksi.key_hargaJual,hargaJual_);
                params.put(koneksi.key_totalObat,totalObat_);
                params.put(koneksi.key_totalPakan,totalPakan_);
                params.put(koneksi.key_biayaop,biayaOp_);
                params.put(koneksi.key_totalBiaya,totalbiaya_);
                params.put(koneksi.key_laba,laba_);
                params.put(koneksi.key_status,status_);
                params.put(koneksi.key_tglKeluar,tglKeluar_);
                params.put(koneksi.key_status_jual,statusJual);
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
                                    if(idHasil == 0){
                                        action.findItem(R.id.menu_edit).setVisible(false);
                                        action.findItem(R.id.menu_save).setVisible(true);
                                        txtAdmin();
                                    }else{
                                        if (status_.equals("operator")){
                                            action.findItem(R.id.menu_edit).setVisible(false);
                                            getSupportActionBar().setTitle("Data Penjualan");

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
                        Toast.makeText(inputDataHasil.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
