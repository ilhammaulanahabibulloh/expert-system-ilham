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

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    String login_url = "https://ilhammaulanahabibulloh.site/android/login.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        session = new SessionHandler(getApplicationContext());

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        Button login = findViewById(R.id.btn_login);
        Button register = findViewById(R.id.btn_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString().toLowerCase().trim();
                password = et_password.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadMainMenu(String level) {
        if (level.equals("User")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        } else if (level.equals("Dokter")) {
            Intent i = new Intent(getApplicationContext(), MainDokterActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
            request.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                session.loginUser(response.getString("id_pengguna"), response.getString("level"));
                                loadMainMenu(response.getString("level"));
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
