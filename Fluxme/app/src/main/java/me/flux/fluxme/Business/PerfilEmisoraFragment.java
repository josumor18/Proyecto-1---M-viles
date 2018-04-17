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

import org.json.JSONException;

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
    ExecuteSuscription executeSuscription;

    private boolean suscrito=false;
    public PerfilEmisoraFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil_emisora, container, false);

        API_Access api_access = API_Access.getInstance();

        streaming = Streaming.getInstance();
        user = Usuario_Singleton.getInstance();

        nombre = v.findViewById(R.id.txtNombreEmisora);
        TextView descripcion = v.findViewById(R.id.txtDescripcion);
        btnSubscribirse = v.findViewById(R.id.btnSubscribirse);

        for (Emisora emisora:ListaEmisorasActivity.emisoras){
            if(emisora.getId()==Integer.parseInt(streaming.getIdEmisora())){
                nombre.setText(emisora.getNombre());
                if(!(emisora.getDescripcion()==null)){
                    descripcion.setText(emisora.getDescripcion());
                }
                else
                    descripcion.setText("No hay descripcion");

            }
        }



        executeSuscription = new ExecuteSuscription(user.getId(),streaming.getIdEmisora(),user.getAuth_token(),1);
        executeSuscription.execute();

        btnSubscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //btnSubscribirse.setText("Suscrito");  //Quitar esto despues de tener le backend
                executeSuscription = new ExecuteSuscription(user.getId(),streaming.getIdEmisora(),user.getAuth_token(),0);
                executeSuscription.execute();
            }
        });
        return v;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil_emisora, container, false);
    }


    public class ExecuteSuscription extends AsyncTask<String,Void,String> {
        private String idUser;
        private String authToken;
        private String idEmisora;
        private boolean isDone = false;
        private int tipo = 0;

        public ExecuteSuscription(String idUser, String idEmisora,String authToken,int tipo){
            this.idUser = idUser;
            this.idEmisora = idEmisora;
            this.authToken = authToken;
            this.tipo=tipo;

        }

        @Override
        protected String doInBackground(String... strings) {


            API_Access api = API_Access.getInstance();
            if(tipo==0 && !suscrito)
                isDone = api.setSuscription(idUser,idEmisora,authToken);
            else if (tipo ==0 && suscrito)
                isDone = api.deleteSuscription(idUser,idEmisora,authToken);
            else if (tipo ==1)
                isDone = api.isSuscripted(idUser,idEmisora,authToken);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isDone) {
                if(!(tipo==0 && suscrito)) {
                    String token = null;
                    try {
                        token = (API_Access.getInstance().getJsonObjectResponse()).getString("authentication_token");
                        Usuario_Singleton.getInstance().setAuth_token(token);
                        LoginActivity.actualizarAuth_Token(token, getActivity().getApplicationContext());
                        //Si es Activity
                        //LoginActivity.actualizarAuth_Token(token, this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(suscrito){
                    btnSubscribirse.setText("Suscribirse");
                    suscrito = false;
                }
                else {
                    btnSubscribirse.setText("Cancelar Suscripcion");
                    suscrito = true;
                }




            }




        }




    }





}
