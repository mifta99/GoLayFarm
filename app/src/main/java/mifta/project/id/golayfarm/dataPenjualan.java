package mifta.project.id.golayfarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class dataPenjualan extends AppCompatActivity implements View.OnClickListener {
    EditText cariJual;
    ImageButton btnCariJual;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapterListPenjualan adapterListPenjualan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penjualan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Penjualan");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dataPenjualan.this, inputDataHasil.class));

            }
        });

        cariJual = (EditText)findViewById(R.id.et_cariJual);
        btnCariJual = (ImageButton)findViewById(R.id.btn_cariJual);
        btnCariJual.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.rv_jual);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        tampil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cariJual:
                if (cariJual.getText().toString().isEmpty()){
                    Toast.makeText(dataPenjualan.this, "Inputkan Id Kambing di Pencarian", Toast.LENGTH_SHORT).show();
                    tampil();
                } else {
                    tampilCari();
                }
                break;
        }
    }

    private void tampil(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, koneksi.listJual,
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
                                    String idJual_ = c.getString(koneksi.key_idHasil);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String status_ = c.getString(koneksi.key_status);
                                    String berat_ = c.getString(koneksi.key_beratAkhir);
                                    String harga_ = c.getString(koneksi.key_hargaJual);
                                    String tgl_ = c.getString(koneksi.key_tglKeluar);
                                    String sj = c.getString(koneksi.key_status_jual);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idHasil, idJual_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_status, status_);
                                    map.put(koneksi.key_beratAkhir, berat_);
                                    map.put(koneksi.key_hargaJual, harga_);
                                    map.put(koneksi.key_tglKeluar, tgl_);
                                    map.put(koneksi.key_status_jual, sj);
                                    tampil.add(map);
                                }
                                adapterListPenjualan=new adapterListPenjualan(dataPenjualan.this,tampil);
                                rv.setAdapter(adapterListPenjualan);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(dataPenjualan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void tampilCari(){
        final String id = cariJual.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistJual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")){
                            try {
                                JSONObject jsonObject;
                                jsonObject = new JSONObject(response);
                                JSONArray result = jsonObject.getJSONArray("result");
                                tampil.clear();
                                for (int i=0; i<result.length(); i++){
                                    JSONObject c = result.getJSONObject(i);
                                    String idJual_ = c.getString(koneksi.key_idHasil);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String status_ = c.getString(koneksi.key_status);
                                    String berat_ = c.getString(koneksi.key_beratAkhir);
                                    String harga_ = c.getString(koneksi.key_hargaJual);
                                    String tgl_ = c.getString(koneksi.key_tglKeluar);
                                    String sj = c.getString(koneksi.key_status_jual);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idHasil, idJual_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_status, status_);
                                    map.put(koneksi.key_beratAkhir, berat_);
                                    map.put(koneksi.key_hargaJual, harga_);
                                    map.put(koneksi.key_tglKeluar, tgl_);
                                    map.put(koneksi.key_status_jual, sj);
                                    tampil.add(map);
                                }
                                adapterListPenjualan=new adapterListPenjualan(dataPenjualan.this,tampil);
                                rv.setAdapter(adapterListPenjualan);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(dataPenjualan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_idKambing, id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
