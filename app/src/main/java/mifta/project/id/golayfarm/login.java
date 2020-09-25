package mifta.project.id.golayfarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    Button login;
    TextView signUp;
    private boolean login1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Go Lay Farm");

        username = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.bt_login);
        signUp = (TextView)findViewById(R.id.tv_signUp);

        login.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_signUp:
                startActivity(new Intent(login.this, register.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
        login1 = sharedPreferences.getBoolean("login2", false);
        if (login1) {
            Intent intent = new Intent(login.this, dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    private void login(){
        final String username_ = username.getText().toString();
        final String password_ = password.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, koneksi.login,
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
                                    SharedPreferences sharedPreferences = getSharedPreferences("myproject", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("login2", true);
                                    editor.putString("id_admin", c.getString(koneksi.key_idAdmin));
                                    editor.commit();
                                    startActivity(new Intent(login.this, dashboard.class));
                                    finish();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Email atau Password anda Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login.this, "Tidak Ada Koneksi", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(koneksi.key_username, username_);
                params.put(koneksi.key_password, password_);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
