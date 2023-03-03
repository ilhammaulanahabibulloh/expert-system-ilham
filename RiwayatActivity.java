package com.e_clinicmelati;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RiwayatActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private final static String url = "https://ilhammaulanahabibulloh.site/android/get_daftar_riwayat.php";
//    private final static String url = "http://10.0.2.2/eclinic_melati/android/get_daftar_riwayat.php";
    private ListView lv;
    private SimpleAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        setTitle("Riwayat Diagnosa");

        SessionHandler session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        lv = findViewById(R.id.list);

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(RiwayatActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
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
                                JSONArray jsonArray = response.getJSONArray("riwayat");
                                ArrayList<HashMap<String, String>> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("nama_penyakit", jsonObject.getString("nama_penyakit"));
                                    map.put("tanggal", jsonObject.getString("tanggal"));
                                    list.add(map);
                                }
                                adapter = new SimpleAdapter(
                                        RiwayatActivity.this,
                                        list,
                                        R.layout.riwayat_list,
                                        new String[]{"nama_penyakit", "tanggal"},
                                        new int[]{R.id.tv_hasil, R.id.tv_tanggal});
                                lv.setAdapter(adapter);
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
