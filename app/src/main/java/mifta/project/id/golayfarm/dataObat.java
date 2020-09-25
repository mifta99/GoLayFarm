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

public class dataObat extends AppCompatActivity implements View.OnClickListener {
    EditText cariObat;
    ImageButton btnCariObat;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapterListObat adapterListObat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_obat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Obat");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dataObat.this, inputDataObat.class));

            }
        });

        cariObat = (EditText)findViewById(R.id.et_cariObat);
        btnCariObat = (ImageButton)findViewById(R.id.btn_cariObat);
        btnCariObat.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.rv_obat);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        tampil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cariObat:
                if (cariObat.getText().toString().isEmpty()){
                    Toast.makeText(dataObat.this, "Inputkan Id Kambing di Pencarian", Toast.LENGTH_SHORT).show();
                    tampil();
                } else {
                    tampilCari();
                }
                break;
        }
    }

    private void tampil(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, koneksi.listObat,
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
                                    String idObat_ = c.getString(koneksi.key_idObat);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String jenis_ = c.getString(koneksi.key_jenisObat);
                                    String qty_ = c.getString(koneksi.key_qtyObat);
                                    String sat_ = c.getString(koneksi.key_satuan);
                                    String harga_ = c.getString(koneksi.key_hargaObat);
                                    String tgl_ = c.getString(koneksi.key_tglObat);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idObat, idObat_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_jenisObat, jenis_);
                                    map.put(koneksi.key_qtyObat, qty_);
                                    map.put(koneksi.key_satuan, sat_);
                                    map.put(koneksi.key_hargaObat, harga_);
                                    map.put(koneksi.key_tglObat, tgl_);
                                    tampil.add(map);
                                }
                                adapterListObat=new adapterListObat(dataObat.this,tampil);
                                rv.setAdapter(adapterListObat);
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
                        Toast.makeText(dataObat.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id = cariObat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistObat,
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
                                    String idObat_ = c.getString(koneksi.key_idObat);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String jenis_ = c.getString(koneksi.key_jenisObat);
                                    String qty_ = c.getString(koneksi.key_qtyObat);
                                    String sat_ = c.getString(koneksi.key_satuan);
                                    String harga_ = c.getString(koneksi.key_hargaObat);
                                    String tgl_ = c.getString(koneksi.key_tglObat);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idObat, idObat_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_jenisObat, jenis_);
                                    map.put(koneksi.key_qtyObat, qty_);
                                    map.put(koneksi.key_satuan, sat_);
                                    map.put(koneksi.key_hargaObat, harga_);
                                    map.put(koneksi.key_tglObat, tgl_);
                                    tampil.add(map);
                                }
                                adapterListObat=new adapterListObat(dataObat.this,tampil);
                                rv.setAdapter(adapterListObat);
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
                        Toast.makeText(dataObat.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
