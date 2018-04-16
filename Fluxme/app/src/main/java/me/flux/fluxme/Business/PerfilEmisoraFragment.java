package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilEmisoraFragment extends Fragment {

    Streaming streaming;
    Usuario_Singleton user;
    TextView nombre;
    Button btnSubscribirse;
    public PerfilEmisoraFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil_emisora, container, false);

        streaming = Streaming.getInstance();
        user = Usuario_Singleton.getInstance();

        nombre = v.findViewById(R.id.txtNombreEmisora);
        TextView descripcion = v.findViewById(R.id.txtDescripcion);
        btnSubscribirse = v.findViewById(R.id.btnSubscribirse);

        for (Emisora emisora:ListaEmisorasActivity.emisoras){
            if(emisora.getId()==Integer.parseInt(streaming.getIdEmisora())){
                nombre.setText(emisora.getNombre());
                if(!emisora.getDescripcion().equals(null)){
                    descripcion.setText(emisora.getDescripcion());
                }

            }
        }

        btnSubscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubscribirse.setText("Suscrito");  //Quitar esto despues de tener le backend
                /*ExecuteSuscription executeSuscription = new ExecuteSuscription(user.getId(),streaming.getIdEmisora());
                executeSuscription.execute();*/
            }
        });
        return v;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil_emisora, container, false);
    }


    public class ExecuteSuscription extends AsyncTask<String,Void,String> {
        private String idUser;
        private String idEmisora;

        private boolean isDone = false;

        public ExecuteSuscription(String idUser, String idEmisora){
            this.idUser = idUser;
            this.idEmisora = idEmisora;

        }

        @Override
        protected String doInBackground(String... strings) {


            API_Access api = API_Access.getInstance();
            isDone = api.setSuscription(idUser,idEmisora);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isDone) {
                btnSubscribirse.setText("Suscrito");
            }


        }




    }





}
