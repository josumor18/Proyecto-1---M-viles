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
        menu.getItem(0).setTitle("");
        menu.getItem(0).setEnabled(false);
        main_menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_play:
                if(Streaming.isIsPlaying()){
                    item.setIcon(R.drawable.play_button_fluxme);
                    Streaming.pause();
                }else{
                    if(Streaming.isPrepared()){
                        item.setIcon(R.drawable.stop_button_fluxme);
                        Streaming.setStream(Streaming.getStream());
                        //Streaming.play();
                    }else{
                        //ir a activity de emisoras favoritas
                    }
                }
                return true;
            case R.id.item_emisoras:
                //Streaming.cleanStreaming();//LINEA DE PRUEBA. HAY QUE QUITARLA
                Streaming.setIdEmisora("0");//LINEA DE PRUEBA. HAY QUE QUITARLA
                Streaming.setEmisora_name("RadioPrueba");//LINEA DE PRUEBA. HAY QUE QUITARLA
                Streaming.setStream("http://s41.myradiostream.com:35530/");//LINEA DE PRUEBA. HAY QUE QUITARLA
                main_menu.getItem(0).setTitle(Streaming.getEmisora_name());//LINEA DE PRUEBA. HAY QUE QUITARLA
                main_menu.getItem(0).setEnabled(Streaming.isPrepared());//LINEA DE PRUEBA. HAY QUE QUITARLA
                main_menu.getItem(1).setIcon(R.drawable.stop_button_fluxme);//LINEA DE PRUEBA. HAY QUE QUITARLA
                return true;
            case R.id.item_cerrar_sesion:
                LoginActivity.cerrarSesion(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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
