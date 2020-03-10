package com.example.filmgan2.settings.localization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filmgan2.MainActivity;
import com.example.filmgan2.R;

public class ChangeLanguage extends AppCompatActivity {

    private RadioGroup radioGroupNb;
    private RadioButton radioButtonNb;
    private Button btnChoice;
    private String id_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        radioGroupNb = findViewById(R.id.radio_group);
        btnChoice = findViewById(R.id.btn_change_language);

        addListenerOnButtonChoice();
        setActionBarTitle(R.string.title_language);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    public void addListenerOnButtonChoice() {
        btnChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
    }

    private void setActionBarTitle(int title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle(getString(R.string.title_dialog));

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(getString(R.string.body_dialog))
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ya), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int selecetId = radioGroupNb.getCheckedRadioButtonId();
                        radioButtonNb = findViewById(selecetId);
                        id_language = String.valueOf(radioButtonNb.getContentDescription());
                        LocaleHelper.setLocale(ChangeLanguage.this, id_language);

                        Intent i = new Intent(ChangeLanguage.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
