package me.flux.fluxme.Business;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

public class PerfilActivity extends BaseActivity {

    Usuario_Singleton user;
    EditText edtNombre;
    EditText edtCorreo;
    EditText edtContrasena;
    EditText edtNuevaCon;
    EditText edtConfirmCon;
    ImageView perfilImageView;
    Button btnGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initToolbar();

        user = Usuario_Singleton.getInstance();

        btnGuardar = findViewById(R.id.btn_guardarPerfil);

        edtNombre = findViewById(R.id.PerfilNombreEditText);
        edtNombre.setText(user.getNombre());

        edtCorreo = findViewById(R.id.PerfilCorreoEditText);
        edtCorreo.setText(user.getEmail());

        edtContrasena = findViewById(R.id.PerfilContrasenaEditText);
        edtNuevaCon = findViewById(R.id.PerfilNuevaContrasenaEditText);
        edtConfirmCon = findViewById(R.id.PerfilConfirmConEditText);

        if(LoginActivity.isFacebook){

        }

        perfilImageView = findViewById(R.id.perfilImageView);
        if(!(user.getFoto().isEmpty())){
            HttpGetBitmap request = new HttpGetBitmap();
            Bitmap coverImage = null;
            try {
                coverImage = request.execute(0).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            perfilImageView.setImageBitmap(coverImage);
            edtNombre.setEnabled(false);
            edtCorreo.setEnabled(false);
            edtContrasena.setEnabled(false);
            edtNuevaCon.setEnabled(false);
            edtConfirmCon.setEnabled(false);
            btnGuardar.setEnabled(false);
        }
    }

    public void guardarCambiosClicked(View view){



        if(TextUtils.isEmpty(edtNuevaCon.getText()) && TextUtils.isEmpty((edtConfirmCon.getText()))) {
            if (!TextUtils.isEmpty(edtCorreo.getText()) && !TextUtils.isEmpty(edtNombre.getText())) {

                ExecuteChange executeChange = new ExecuteChange(user.getId(),
                        edtNombre.getText().toString(), edtCorreo.getText().toString(),user.getAuth_token());

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
                        edtNuevaCon.getText().toString(),user.getAuth_token());

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
        private String authToken;
        private boolean isChanged = false;

        public ExecuteChange(String id, String name,String email,String authToken){
            this.name = name;
            this.email = email;
            this.id = id;
            this.authToken = authToken;
            tipoAut = 0;
        }

        public ExecuteChange(String id, String name,String email,String password , String new_password,String authToken){
            this.name = name;
            this.email = email;
            this.id = id;
            this.password = password;
            this.new_password = new_password;
            this.authToken = authToken;
            tipoAut = 1;
        }


        @Override
        protected String doInBackground(String... strings) {


            API_Access api = API_Access.getInstance();
            if(tipoAut==0)
                isChanged=api.change_user(id,name,email,authToken);
            else if(tipoAut==1)
                isChanged=api.change_pass(id,name,email,password,new_password,authToken);





            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(isChanged) {
                String token = null;
                try {
                    token = (API_Access.getInstance().getJsonObjectResponse()).getString("authentication_token");
                    Usuario_Singleton.getInstance().setAuth_token(token);
                    LoginActivity.actualizarAuth_Token(token, getApplicationContext());
                    //Si es Activity
                    //LoginActivity.actualizarAuth_Token(token, this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                user.setEmail(edtCorreo.getText().toString());
                user.setNombre(edtNombre.getText().toString());
                Toast.makeText(PerfilActivity.this, "Cambio exitoso", Toast.LENGTH_SHORT).show();
                //finish();
            }
            else
                Toast.makeText(PerfilActivity.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();

        }




    }


    public class HttpGetBitmap extends AsyncTask<Integer, Void, Bitmap> {
        private static final String REQUEST_METHOD = "GET";
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected Bitmap doInBackground(Integer... index){

            int i = index[0];
            Bitmap cover;

            String address = Usuario_Singleton.getInstance().getFoto();

            try {

                //Create a URL object holding our url
                URL myUrl = new URL(address);

                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.setDoInput(true);
                connection.connect();

                //Create a new InputStream
                InputStream input = connection.getInputStream();
                cover = BitmapFactory.decodeStream(input);

            }
            catch(IOException e){
                e.printStackTrace();
                cover = null;
            }
            return cover;
        }
    }
}
