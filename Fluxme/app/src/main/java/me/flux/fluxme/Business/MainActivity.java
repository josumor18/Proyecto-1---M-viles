package me.flux.fluxme.Business;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import me.flux.fluxme.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        Usuario_Singleton u = Usuario_Singleton.getInstance();
        TextView t = findViewById(R.id.textView2);
        t.setText(u.getNombre() + " " + u.getUsername() + "\n" + u.getFoto());
    }
}
