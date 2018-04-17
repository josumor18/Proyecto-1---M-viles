package me.flux.fluxme.Business;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilEmisoraAdminFragment extends Fragment {

    Streaming streaming;
    Usuario_Singleton user;
    EditText nombre;
    Button btnGuardar;

    public PerfilEmisoraAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_perfil_emisora_admin, container, false);

        streaming = Streaming.getInstance();
        user = Usuario_Singleton.getInstance();

        nombre = v.findViewById(R.id.edtNombreEmisora);
        final EditText descripcion = v.findViewById(R.id.edtDescripcion);
        btnGuardar = v.findViewById(R.id.btn_guardarCambios);

        for (Emisora emisora:ListaEmisorasActivity.emisoras){
            if(emisora.getId()==Integer.parseInt(streaming.getIdEmisora())){
                nombre.setText(emisora.getNombre());
                if(!emisora.getDescripcion().equals(null)){
                    descripcion.setText(emisora.getDescripcion());
                }

            }
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecuteChange executeChange = new ExecuteChange(user.getId(),user.getAuth_token(),streaming.getIdEmisora()
                        ,nombre.getText().toString(),
                        descripcion.getText().toString());
                executeChange.execute();
            }
        });
        return v;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_perfil_emisora_admin, container, false);
    }

    public class ExecuteChange extends AsyncTask<String,Void,String> {
        private String id;
        private String id_user;
        private String authToken;
        private String nombre;
        private String descripcion;

        private boolean isDone = false;

        public ExecuteChange(String id_user,String authToken,String id,String nombre, String descripcion){
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.id=id;
            this.id_user=id_user;
            this.authToken=authToken;

        }

        @Override
        protected String doInBackground(String... strings) {


            API_Access api = API_Access.getInstance();
            isDone = api.change_emisora(id_user,authToken,id,nombre,descripcion);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isDone) {
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

                Toast.makeText(getActivity().getApplicationContext(), "Cambio exitoso", Toast.LENGTH_SHORT).show();
            }


        }




    }

}
