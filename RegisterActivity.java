package com.e_clinicmelati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_nama_lengkap;
    private EditText et_username;
    private EditText et_password;
    private String nama_lengkap;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    String url = "https://ilhammaulanahabibulloh.site/android/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        et_nama_lengkap = findViewById(R.id.et_nama_lengkap);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        Button login = findViewById(R.id.btn_login);
        Button register = findViewById(R.id.btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_lengkap = et_nama_lengkap.getText().toString().trim();
                username = et_username.getText().toString().toLowerCase().trim();
                password = et_password.getText().toString().trim();
                if (validateInputs()) {
                    register();
                }
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void register() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
            request.put("password", password);
            request.put("nama_lengkap", nama_lengkap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private boolean validateInputs() {
        if (nama_lengkap.equals("")) {
            et_nama_lengkap.setError("Isi dulu Nama Lengkap");
            et_nama_lengkap.requestFocus();
            return false;
        }
        if (username.equals("")) {
            et_username.setError("Isi dulu Username");
            et_username.requestFocus();
            return false;
        }
        if (password.equals("")) {
            et_password.setError("Isi dulu Password");
            et_password.requestFocus();
            return false;
        }
        return true;
    }
}
