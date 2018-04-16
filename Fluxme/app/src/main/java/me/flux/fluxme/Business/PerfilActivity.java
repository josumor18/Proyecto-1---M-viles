package me.flux.fluxme.Business;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

public class PerfilActivity extends BaseActivity {

    Usuario_Singleton user;
    EditText edtNombre;
    EditText edtCorreo;
    EditText edtContrasena;
    EditText edtNuevaCon;
    EditText edtConfirmCon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initToolbar();

        user = Usuario_Singleton.getInstance();

        //Falta foto

        edtNombre = findViewById(R.id.PerfilNombreEditText);
        edtNombre.setText(user.getNombre());

        edtCorreo = findViewById(R.id.PerfilCorreoEditText);
        edtCorreo.setText(user.getEmail());

        edtContrasena = findViewById(R.id.PerfilContrasenaEditText);
        edtNuevaCon = findViewById(R.id.PerfilNuevaContrasenaEditText);
        edtConfirmCon = findViewById(R.id.PerfilConfirmConEditText);
    }

    public void guardarCambiosClicked(View view){



        if(TextUtils.isEmpty(edtNuevaCon.getText()) && TextUtils.isEmpty((edtConfirmCon.getText()))) {
            if (!TextUtils.isEmpty(edtCorreo.getText()) && !TextUtils.isEmpty(edtNombre.getText())) {

                ExecuteChange executeChange = new ExecuteChange(user.getId(),
                        edtNombre.getText().toString(), edtCorreo.getText().toString());

                executeChange.execute();
            }
            else {
                edtNombre.setError("Campo Requerido");
                edtCorreo.setError("Campo Requerido");
            }

        }
        else {

            String nuevaCont = edtNuevaCon.getText().toString();
            boolean val1 = true;
            boolean val2 = true;

            if (nuevaCont.length() < 6 || TextUtils.isEmpty(nuevaCont)) {
                val1 = false;
                edtNuevaCon.setError("Min. 6 caracteres");
                edtNuevaCon.setText("");
                edtConfirmCon.setText("");
            }

            if (!nuevaCont.equals(edtConfirmCon.getText().toString())) {
                val2 = false;
                edtConfirmCon.setError("Contraseñas no coinciden");

            }

            if (val1 && val2) {
                ExecuteChange exChange = new ExecuteChange(user.getId(),edtNombre.getText().toString(),
                        edtCorreo.getText().toString(),edtContrasena.getText().toString(),
                        edtNuevaCon.getText().toString());

                exChange.execute();
            }
        }

    }

    public class ExecuteChange extends AsyncTask<String,Void,String> {
        private String id;
        private String email;
        private String name;
        private String password;
        private String new_password;
        private int tipoAut=0;
        private boolean isChanged = false;

        public ExecuteChange(String id, String name,String email){
            this.name = name;
            this.email = email;
            this.id = id;
            tipoAut = 0;
        }

        public ExecuteChange(String id, String name,String email,String password , String new_password){
            this.name = name;
            this.email = email;
            this.id = id;
            this.password = password;
            this.new_password = new_password;
            tipoAut = 1;
        }


        @Override
        protected String doInBackground(String... strings) {


            API_Access api = API_Access.getInstance();
            if(tipoAut==0)
                isChanged=api.change_user(id,name,email);
            else if(tipoAut==1)
                isChanged=api.change_pass(id,name,email,password,new_password);





            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isChanged) {
                user.setEmail(edtCorreo.getText().toString());
                user.setNombre(edtNombre.getText().toString());
                Toast.makeText(PerfilActivity.this, "Cambio exitoso", Toast.LENGTH_SHORT).show();
                //finish();
            }
            else
                Toast.makeText(PerfilActivity.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();

        }




    }
}
