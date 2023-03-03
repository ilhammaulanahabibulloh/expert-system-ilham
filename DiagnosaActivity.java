package com.e_clinicmelati;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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

public class DiagnosaActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://ilhammaulanahabibulloh.site/android/get_daftar_gejala.php";
    private MyCustomAdapter dataAdapter = null;
    private ArrayList<Gejala> gejalaList = new ArrayList<Gejala>();
    private Gejala gejala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa);
        setTitle("Pilih Gejala");

        getDaftarGejala();

        Button btn_diagnosa = findViewById(R.id.btn_diagnosa);
        btn_diagnosa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                if (!gejalaList.isEmpty()) {
                    ArrayList<Gejala> gejalaList2 = dataAdapter.gejalaList;
                    for (int i = 0; i < gejalaList2.size(); i++) {
                        Gejala gejala = gejalaList2.get(i);
                        if (gejala.isSelected()) {
                            responseText.append(gejala.getName() + "#");
                        }
                    }
                }

                if (responseText.toString().equals("")) {
                    Toast.makeText(DiagnosaActivity.this, "Pilih dulu gejala !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(v.getContext(), DiagnosaHasilActivity.class);
                    myIntent.putExtra("HASIL", responseText.toString());
                    startActivity(myIntent);
                }
            }
        });
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void displayGejalaView() {
        dataAdapter = new MyCustomAdapter(this, R.layout.diagnosa_list, gejalaList);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(dataAdapter);
    }

    private void getDaftarGejala() {
        displayLoader();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            if (response.getInt("status") == 0) {
                                gejalaList = new ArrayList<Gejala>();
                                JSONArray jsonArray = response.getJSONArray("gejala");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("nama_gejala");
                                    gejala = new Gejala(name, false);
                                    gejalaList.add(gejala);
                                }
                                displayGejalaView();
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

    private class MyCustomAdapter extends ArrayAdapter<Gejala> {

        private ArrayList<Gejala> gejalaList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Gejala> gejalaList) {
            super(context, textViewResourceId, gejalaList);
            this.gejalaList = new ArrayList<Gejala>();
            this.gejalaList.addAll(gejalaList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.diagnosa_list, null);

                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.chk_gejala);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Gejala gejala = (Gejala) cb.getTag();
                        gejala.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Gejala gejala = gejalaList.get(position);
            holder.name.setText(gejala.getName());
            holder.name.setChecked(gejala.isSelected());
            holder.name.setTag(gejala);

            return convertView;
        }
    }
}