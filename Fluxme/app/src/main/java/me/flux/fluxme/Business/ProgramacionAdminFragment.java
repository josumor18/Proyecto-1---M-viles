package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramacionAdminFragment extends Fragment {

    private ArrayList<Tendencia> tendencias = new ArrayList<Tendencia>();

    private  ArrayList<EditText> nombresCanciones = new ArrayList<EditText>();
    private  ArrayList<EditText> nombresArtistas = new ArrayList<EditText>();
    private  ArrayList<EditText> linksImgsCanciones = new ArrayList<EditText>();

    private RelativeLayout rlProgSemAdmin;
    private RelativeLayout rlTendAdmin;
    ListView lvTendAdmin;
    Button btnSaveTendencias;
    RadioButton rbProgramacion;
    RadioButton rbTendencias;
    RadioGroup rgrp_Opcion;
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(rbProgramacion.isChecked()){
                rlProgSemAdmin.setVisibility(View.VISIBLE);
                rlTendAdmin.setVisibility(View.INVISIBLE);
            }else if(rbTendencias.isChecked()){
                ExecuteAddTrending trend = new ExecuteAddTrending();
                trend.execute();

                rlTendAdmin.setVisibility(View.VISIBLE);
                rlProgSemAdmin.setVisibility(View.INVISIBLE);
            }
        }
    };

    public ProgramacionAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_programacion_admin, container, false);
        rlProgSemAdmin = view.findViewById(R.id.rlProgSemAdmin);
        rlTendAdmin = view.findViewById(R.id.rlTendAdmin);

        btnSaveTendencias = view.findViewById(R.id.btnSaveTendencias);
        btnSaveTendencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarClicked();
            }
        });

        rbProgramacion = view.findViewById(R.id.rbProgramacion);
        rbTendencias = view.findViewById(R.id.rbTendencias);
        rgrp_Opcion = view.findViewById(R.id.rgrp_Opcion);
        rgrp_Opcion.setOnCheckedChangeListener(checkedChangeListener);

        initTextBoxes(view);



        return view;
    }

    public void guardarClicked(){
        String jsonObject = "{\"Lista_Canciones\":" + listaCancionesToJsonString() + "}";

        ExecuteAddTrending trend = new ExecuteAddTrending(jsonObject);
        trend.execute();
    }

    private String listaCancionesToJsonString(){
        String jsonArray = "[";
        for (int i = 0; i < 10; i++){
            try {
                jsonArray+= new JSONObject()
                        .put("posicion", i+1)
                        .put("cancion", nombresCanciones.get(i).getText())
                        .put("artista", nombresArtistas.get(i).getText())
                        .put("imagen", linksImgsCanciones.get(i).getText()).toString();
                if(i<9){
                    jsonArray+= ",";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonArray+= "]";
        return jsonArray;
    }

   private void initTextBoxes(View view){
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion1));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista1));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion1));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion2));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista2));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion2));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion3));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista3));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion3));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion4));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista4));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion4));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion5));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista5));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion5));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion6));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista6));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion6));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion7));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista7));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion7));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion8));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista8));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion8));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion9));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista9));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion9));
       nombresCanciones.add((EditText) view.findViewById(R.id.edtxtCancion10));
       nombresArtistas.add((EditText) view.findViewById(R.id.edtxtArtista10));
       linksImgsCanciones.add((EditText) view.findViewById(R.id.edtxtLinkImgCancion10));
   }

   private void llenarTextBoxex(){
       for (int i = 0; i < 10; i++){
           Tendencia tend = getTendeciaByPosicion(i+1);
           nombresCanciones.get(i).setText(tend.cancion);
           nombresArtistas.get(i).setText(tend.artista);
           linksImgsCanciones.get(i).setText(tend.link);
       }
   }

   private Tendencia getTendeciaByPosicion(int pos){
       for(Tendencia t:tendencias){
           if(t.posicion == pos){
               return t;
           }
       }
       return new Tendencia(pos, "", "", "");
   }

    private void cargarTendencias(JSONObject jsonResult){

        try {
            tendencias.clear();

            JSONArray jsonTendencias = jsonResult.getJSONArray("trending");
            for(int i = 0; i < jsonTendencias.length(); i++) {
                JSONObject tendencia = (JSONObject) jsonTendencias.get(i);
                tendencias.add(new Tendencia(tendencia.getInt("posicion"), tendencia.getString("cancion"), tendencia.getString("artista"), tendencia.getString("imagen")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        llenarTextBoxex();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    public class ExecuteAddTrending extends AsyncTask<String, Void, String> {
        boolean isOk = false;
        String tendencias = "";
        int tipo = 0;

        public ExecuteAddTrending() {
            this.tipo = 1;
        }

        public ExecuteAddTrending(String tendencias) {
            this.tendencias = tendencias;
            this.tipo = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            Usuario_Singleton user = Usuario_Singleton.getInstance();

            if(tipo == 0){
                isOk = api.addTrending(user.getId(), user.getAuth_token(), Streaming.getIdEmisora(), tendencias);
            }else{
                isOk = api.getTrending(user.getId(), user.getAuth_token(), Streaming.getIdEmisora());
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isOk){
                String token = null;
                try {
                    JSONObject result = API_Access.getInstance().getJsonObjectResponse();
                    token = (result).getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());

                    if(tipo == 1){
                        cargarTendencias(result);
                        Toast.makeText(getActivity(), "Lista obtenida", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Lista agregada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class Tendencia{
       int posicion;
       String cancion;
       String artista;
       String link;

        public Tendencia(int posicion, String cancion, String artista, String link) {
            this.posicion = posicion;
            this.cancion = cancion;
            this.artista = artista;
            this.link = link;
        }
    }
}
