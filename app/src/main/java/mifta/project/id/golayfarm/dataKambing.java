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
import android.widget.ImageView;
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

public class dataKambing extends AppCompatActivity implements View.OnClickListener {
    EditText cariKambing;
    ImageButton btnCariKambing;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapterListKambing adapterListKambing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kambing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Kambing");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dataKambing.this, inputDataKambing.class));

            }
        });

        cariKambing = (EditText)findViewById(R.id.et_cariKambing);
        btnCariKambing = (ImageButton)findViewById(R.id.btn_cariKambing);
        btnCariKambing.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.rv_kambing);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        tampil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cariKambing:
                if (cariKambing.getText().toString().isEmpty()){
                    Toast.makeText(dataKambing.this, "Inputkan Id Kambing di Pencarian", Toast.LENGTH_SHORT).show();
                    tampil();
                } else {
                    tampilCari();
                }
                break;
        }
    }

    private void tampil(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, koneksi.listKambing,
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
                                    String id = c.getString(koneksi.key_id);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String foto_ = c.getString(koneksi.key_foto);
                                    String jenis_kelamin_ = c.getString(koneksi.key_kelamin);
                                    String berat_ = c.getString(koneksi.key_beratAwal);
                                    String harga_ = c.getString(koneksi.key_hargaBeli);
                                    String tgl_ = c.getString(koneksi.key_tgl_masuk);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_id, id);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_foto, foto_);
                                    map.put(koneksi.key_kelamin, jenis_kelamin_);
                                    map.put(koneksi.key_beratAwal, berat_);
                                    map.put(koneksi.key_hargaBeli, harga_);
                                    map.put(koneksi.key_tgl_masuk, tgl_);
                                    tampil.add(map);
                                }
                                adapterListKambing=new adapterListKambing(dataKambing.this,tampil);
                                rv.setAdapter(adapterListKambing);
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
                        Toast.makeText(dataKambing.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id = cariKambing.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistKambing,
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
                                    String id = c.getString(koneksi.key_id);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String foto_ = c.getString(koneksi.key_foto);
                                    String jenis_kelamin_ = c.getString(koneksi.key_kelamin);
                                    String berat_ = c.getString(koneksi.key_beratAwal);
                                    String harga_ = c.getString(koneksi.key_hargaBeli);
                                    String tgl_ = c.getString(koneksi.key_tgl_masuk);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_id, id);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_foto, foto_);
                                    map.put(koneksi.key_kelamin, jenis_kelamin_);
                                    map.put(koneksi.key_beratAwal, berat_);
                                    map.put(koneksi.key_hargaBeli, harga_);
                                    map.put(koneksi.key_tgl_masuk, tgl_);
                                    tampil.add(map);
                                }
                                adapterListKambing=new adapterListKambing(dataKambing.this,tampil);
                                rv.setAdapter(adapterListKambing);
                            }catch (JSONException e){
                                //e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(dataKambing.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
