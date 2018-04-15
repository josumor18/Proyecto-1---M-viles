package me.flux.fluxme.Business;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.stream.Stream;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * Created by JosueAndroid on 27/3/2018.
 */

public class BaseActivity extends AppCompatActivity {

    Menu main_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try{
            Usuario_Singleton user = Usuario_Singleton.getInstance();
            ExecuteDeleteLocations loc = new ExecuteDeleteLocations(user.getId(), user.getAuth_token());
            loc.execute();
        }catch (Exception e){
            e.printStackTrace();
        }*/
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
                Intent intentPerfil = new Intent(getApplicationContext(), PerfilActivity.class);
                startActivity(intentPerfil);

                main_menu.getItem(0).setEnabled(true);
                return true;
            case R.id.item_emisoras:
                Intent intentEmisoras = new Intent(getApplicationContext(), ListaEmisorasActivity.class);
                startActivity(intentEmisoras);

                main_menu.getItem(0).setEnabled(true);
                return true;
            case R.id.item_cerrar_sesion:
                Streaming.pause();
                Streaming.cleanStreaming();

                Usuario_Singleton user = Usuario_Singleton.getInstance();
                ExecuteDeleteLocations loc = new ExecuteDeleteLocations(user.getId(), user.getAuth_token());
                loc.execute();

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
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteDeleteLocations extends AsyncTask<String, Void, String> {
        String id;
        String auth_token;

        public ExecuteDeleteLocations(String id, String auth_token) {
            this.id = id;
            this. auth_token = auth_token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();

            api.deleteLocations(id, auth_token);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
