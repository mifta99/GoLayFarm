package mifta.project.id.golayfarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;

public class dataPakan extends AppCompatActivity implements View.OnClickListener {
    EditText cariPakan;
    ImageButton btnCariPakan;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapterListPakan adapterListPakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pakan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Pakan");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dataPakan.this, inputDataPakan.class));

            }
        });

        cariPakan = (EditText)findViewById(R.id.et_cariPakan);
        btnCariPakan = (ImageButton)findViewById(R.id.btn_cariPakan);
        btnCariPakan.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.rv_pakan);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        tampil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cariPakan:
                if (cariPakan.getText().toString().isEmpty()){
                    Toast.makeText(dataPakan.this, "Inputkan Id Kambing di Pencarian", Toast.LENGTH_SHORT).show();
                    tampil();
                } else {
                    tampilCari();
                }
                break;
        }
    }

    private void tampilCari(){
        final String id = cariPakan.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistPakan,
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
                                    String idPakan_ = c.getString(koneksi.key_idPakan);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String nama_ = c.getString(koneksi.key_namaPakan);
                                    String qty_ = c.getString(koneksi.key_qtyPakan);
                                    String sat_ = c.getString(koneksi.key_satuanPakan);
                                    String harga_ = c.getString(koneksi.key_hargaPakan);
                                    String tgl_ = c.getString(koneksi.key_tglPakan);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idPakan, idPakan_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_namaPakan, nama_);
                                    map.put(koneksi.key_qtyPakan, qty_);
                                    map.put(koneksi.key_satuanPakan, sat_);
                                    map.put(koneksi.key_hargaPakan, harga_);
                                    map.put(koneksi.key_tglPakan, tgl_);
                                    tampil.add(map);
                                }
                                adapterListPakan=new adapterListPakan(dataPakan.this,tampil);
                                rv.setAdapter(adapterListPakan);
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
                        Toast.makeText(dataPakan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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

    private void tampil(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, koneksi.listPakan,
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
                                    String idPakan_ = c.getString(koneksi.key_idPakan);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String nama_ = c.getString(koneksi.key_namaPakan);
                                    String qty_ = c.getString(koneksi.key_qtyPakan);
                                    String sat_ = c.getString(koneksi.key_satuanPakan);
                                    String harga_ = c.getString(koneksi.key_hargaPakan);
                                    String tgl_ = c.getString(koneksi.key_tglPakan);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idPakan, idPakan_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_namaPakan, nama_);
                                    map.put(koneksi.key_qtyPakan, qty_);
                                    map.put(koneksi.key_satuanPakan, sat_);
                                    map.put(koneksi.key_hargaPakan, harga_);
                                    map.put(koneksi.key_tglPakan, tgl_);
                                    tampil.add(map);
                                }
                                adapterListPakan=new adapterListPakan(dataPakan.this,tampil);
                                rv.setAdapter(adapterListPakan);
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
                        Toast.makeText(dataPakan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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

}
