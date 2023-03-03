package com.e_clinicmelati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PenyakitActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private final static String url = "https://ilhammaulanahabibulloh.site/android/get_daftar_penyakit.php";
//    private final static String url = "http://10.0.2.2/eclinic_melati/android/get_daftar_penyakit.php";
    private ListView lv;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyakit);
        setTitle("Daftar Penyakit");

        lv = findViewById(R.id.list);

        getData();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(PenyakitActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getData() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                JSONArray jsonArray = response.getJSONArray("penyakit");
                                ArrayList<HashMap<String, String>> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id_penyakit", jsonObject.getString("id_penyakit"));
                                    map.put("nama_penyakit", jsonObject.getString("nama_penyakit"));
                                    list.add(map);
                                }
                                adapter = new SimpleAdapter(
                                        PenyakitActivity.this,
                                        list,
                                        R.layout.penyakit_list,
                                        new String[]{"id_penyakit", "nama_penyakit"},
                                        new int[]{R.id.menu_id, R.id.menu_text});
                                lv.setAdapter(adapter);

                                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
                                        RelativeLayout layout = (RelativeLayout) container;
                                        TextView tvId = (TextView) layout.getChildAt(0);
                                        Intent intent = new Intent(PenyakitActivity.this, PenyakitDetailActivity.class);
                                        intent.putExtra("ID_PENYAKIT", tvId.getText().toString());
                                        startActivity(intent);
                                    }
                                };

                                lv.setOnItemClickListener(itemClickListener);
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
