package com.e_clinicmelati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainDokterActivity extends AppCompatActivity {

    private String[] itemname = {
            "Diagnosa",
            "Daftar Penyakit",
            "Bantuan",
            "About",
            "Logout"
    };
    private Integer[] imgid = {
            R.drawable.stethoscope,
            R.drawable.list,
            R.drawable.question,
            R.drawable.info,
            R.drawable.logout
    };
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dokter);
        setTitle("Menu Utama Dokter");

        session = new SessionHandler(getApplicationContext());

        CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid);
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent(MainDokterActivity.this, DiagnosaActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainDokterActivity.this, PenyakitActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainDokterActivity.this, BantuanDokterActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainDokterActivity.this, AboutActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    logout();
                }
            }
        });
    }

    //    jika tombol back ditekan
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    fungsi untuk menampilkan alert jika tombol back ditekan
    protected void exitByBackKey() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Anda yakin mau keluar ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    protected void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Anda yakin mau logout ?")
                .setPositiveButton("Ya, Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        session.logoutUser();
                        Intent intent = new Intent(MainDokterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}
