package com.e_clinicmelati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosaHasilActivity extends AppCompatActivity {

    private String hasil;
    private ProgressDialog pDialog;
    private static final String url = "https://ilhammaulanahabibulloh.site/android/get_hasil_diagnosa.php";
//    private static final String url = "http://10.0.2.2/eclinic_melati/android/get_hasil_diagnosa.php";
    private Button btn_solusi;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_hasil);
        setTitle("Hasil Diagnosa");

        SessionHandler session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasil = extras.getString("HASIL");
        }

        btn_solusi = findViewById(R.id.btn_solusi);

        getHasilDiagnosa();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaHasilActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getHasilDiagnosa() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("hasil", hasil);
            request.put("id_pengguna", user.getIdPengguna());
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
                                if (response.getString("id_penyakit").equals("")) {
                                    TextView tv_title = findViewById(R.id.hasil);
                                    tv_title.setText(response.getString("nama_penyakit"));
                                    btn_solusi.setVisibility(View.GONE);
                                } else {
                                    final String id_penyakit = response.getString("id_penyakit");
                                    TextView tv_title = findViewById(R.id.hasil);
                                    tv_title.setText("Berdasarkan gejala yang dipilih, anda kemungkinan terkena " + response.getString("nama_penyakit"));
                                    btn_solusi.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent myIntent = new Intent(v.getContext(), PenyakitDetailActivity.class);
                                            myIntent.putExtra("ID_PENYAKIT", id_penyakit);
                                            startActivity(myIntent);
                                        }
                                    });
                                }
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

}
