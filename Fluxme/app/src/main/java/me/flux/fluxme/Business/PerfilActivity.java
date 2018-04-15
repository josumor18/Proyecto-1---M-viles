package me.flux.fluxme.Business;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import me.flux.fluxme.R;

public class PerfilActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initToolbar();

        Usuario_Singleton u = Usuario_Singleton.getInstance();
        EditText nombrePerfilEdit = findViewById(R.id.PerfilNombreEditText);
        nombrePerfilEdit.setText(u.getNombre());

        EditText usuarioPerfilEdit = findViewById(R.id.PerfilUsuarioEditText);
        usuarioPerfilEdit.setText(u.getAuth_token());
    }
}
