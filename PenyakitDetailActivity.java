package com.e_clinicmelati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PenyakitDetailActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private final static String url = "http://ilhammaulanahabibulloh.site/android/get_penyakit.php";
//    private final static String url = "http://10.0.2.2/eclinic_melati/android/get_penyakit.php";
    private TextView nama_penyakit;
    private TextView detail_penyakit;
    private String id_penyakit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit_detail);
        setTitle("Informasi Penyakit");

        nama_penyakit = findViewById(R.id.nama_penyakit);
        detail_penyakit = findViewById(R.id.detail_penyakit);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            id_penyakit = extras.getString("ID_PENYAKIT");

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenyakitDetailActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
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
                                nama_penyakit.setText(response.getString("nama_penyakit"));
                                detail_penyakit.setText(response.getString("deskripsi") + "\n\nSolusi\n" + response.getString("solusi"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
