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

public class dataPenggemukan extends AppCompatActivity implements View.OnClickListener {
    EditText cariBerat;
    ImageButton btnCariBerat;
    RecyclerView rv;
    ArrayList<HashMap<String, String>> tampil = new ArrayList<HashMap<String, String>>();
    adapterListPenggemukan adapterListPenggemukan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penggemukan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Data Penggemukan");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dataPenggemukan.this, inputDataPenggemukan.class));

            }
        });

        cariBerat = (EditText)findViewById(R.id.et_cariGemuk);
        btnCariBerat = (ImageButton)findViewById(R.id.btn_cariGemuk);
        btnCariBerat.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.rv_gemuk);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        tampil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cariGemuk:
                if (cariBerat.getText().toString().isEmpty()){
                    Toast.makeText(dataPenggemukan.this, "Inputkan Id Kambing di Pencarian", Toast.LENGTH_SHORT).show();
                    tampil();
                } else {
                    tampilCari();
                }
                break;
        }
    }
    private void tampil(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, koneksi.listGemuk,
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
                                    String idgemuk_ = c.getString(koneksi.key_idGemuk);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String berat_ = c.getString(koneksi.key_berat);
                                    String tgl_ = c.getString(koneksi.key_tglTimbang);
                                    String kon_ = c.getString(koneksi.key_kondisi);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idGemuk, idgemuk_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_berat, berat_);
                                    map.put(koneksi.key_tglTimbang, tgl_);
                                    map.put(koneksi.key_kondisi, kon_);
                                    tampil.add(map);
                                }
                                adapterListPenggemukan=new adapterListPenggemukan(dataPenggemukan.this,tampil);
                                rv.setAdapter(adapterListPenggemukan);
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
                        Toast.makeText(dataPenggemukan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
        final String id = cariBerat.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.carilistGemuk,
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
                                    String idgemuk_ = c.getString(koneksi.key_idGemuk);
                                    String id_ = c.getString(koneksi.key_idKambing);
                                    String berat_ = c.getString(koneksi.key_berat);
                                    String tgl_ = c.getString(koneksi.key_tglTimbang);
                                    String kon_ = c.getString(koneksi.key_kondisi);

                                    HashMap<String,String> map = new HashMap<String, String>();
                                    map.put(koneksi.key_idGemuk, idgemuk_);
                                    map.put(koneksi.key_idKambing, id_);
                                    map.put(koneksi.key_berat, berat_);
                                    map.put(koneksi.key_tglTimbang, tgl_);
                                    map.put(koneksi.key_kondisi, kon_);
                                    tampil.add(map);
                                }
                                adapterListPenggemukan=new adapterListPenggemukan(dataPenggemukan.this,tampil);
                                rv.setAdapter(adapterListPenggemukan);
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
                        Toast.makeText(dataPenggemukan.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
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
