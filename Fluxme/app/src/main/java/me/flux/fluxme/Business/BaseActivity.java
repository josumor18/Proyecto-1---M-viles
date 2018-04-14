package me.flux.fluxme.Business;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.stream.Stream;

import me.flux.fluxme.R;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class BaseActivity extends AppCompatActivity {

    Menu main_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Streaming s = Streaming.getInstance();
        //s.initMediaPlayer();
        ///s.setStream("http://s41.myradiostream.com:35530/");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_appbar, menu);
        if(!Streaming.getIdEmisora().isEmpty()){
            menu.getItem(0).setTitle(Streaming.getEmisora_name());
            menu.getItem(0).setEnabled(true);
        }
        if(Streaming.isIsPlaying()){
            menu.getItem(1).setIcon(R.drawable.stop_button_fluxme);
        }else{
            menu.getItem(1).setIcon(R.drawable.play_button_fluxme);
        }
        if(this instanceof EmisoraActivity){
            menu.getItem(0).setEnabled(false);
        }else{
            menu.getItem(0).setEnabled(true);
        }
        main_menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_emisora:
                Intent intentEmisora = new Intent(getApplicationContext(), EmisoraActivity.class);
                startActivity(intentEmisora);
                //item.setEnabled(false);
                return true;
            case R.id.item_play:
                if(Streaming.isIsPlaying()){
                    item.setIcon(R.drawable.play_button_fluxme);
                    Streaming.pause();
                }else{
                    if(Streaming.isPrepared()){
                        //Streaming.setStream(Streaming.getStream());
                        Streaming.play();
                        if(Streaming.isIsPlaying()){
                            item.setIcon(R.drawable.stop_button_fluxme);
                        }
                    }else{
                        if(!(this instanceof ListaEmisorasActivity) && Streaming.getStream().isEmpty()) {
                            Intent intentEmisoras = new Intent(getApplicationContext(), ListaEmisorasActivity.class);
                            startActivity(intentEmisoras);
                        }
                    }
                }
                return true;
            case R.id.item_inicio:
                return true;
            case R.id.item_perfil:

                return true;
            case R.id.item_emisoras:
                Intent intentEmisoras = new Intent(getApplicationContext(), ListaEmisorasActivity.class);
                startActivity(intentEmisoras);

                main_menu.getItem(0).setEnabled(true);
                return true;
            case R.id.item_cerrar_sesion:
                Streaming.pause();
                Streaming.cleanStreaming();

                LoginActivity.cerrarSesion(getApplicationContext());
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                main_menu.getItem(0).setEnabled(true);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initToolbar(){
        Toolbar toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        //toolbar.setSubtitle("welcome");
    }
}
