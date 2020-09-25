package mifta.project.id.golayfarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener {
    EditText nama, username, password;
    Button daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = (EditText) findViewById(R.id.et_namaLengkap);
        username = (EditText) findViewById(R.id.et_inputUser);
        password = (EditText)findViewById(R.id.et_inputPass);
        daftar = (Button) findViewById(R.id.bt_daftar);

        daftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_daftar:
                daftar();
                break;
        }
    }
    private void daftar(){
        final String nama_ = nama.getText().toString();
        final String username_ = username.getText().toString();
        final String pass_ = password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(register.this, "Akun berhasil dibuat, silahkan login.",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(register.this, login.class));
                }else{
                    Toast.makeText(register.this, "Data Gagal Disimpan",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(register.this, "Tidak terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_nama,nama_);
                params.put(koneksi.key_username,username_);
                params.put(koneksi.key_password,pass_);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
