package me.flux.fluxme.Business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

/**
 * Created by alvaroramirez on 4/13/18.
 */

public class RegistrarActivity extends AppCompatActivity{


    EditText edtUsernameRegis;
    EditText edtEmailRegis;
    EditText edtPasswordRegis;
    EditText edtConfirmPasswordRegis;
    TextView txtInvalidRegis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        edtEmailRegis = findViewById(R.id.edtEmailRegistrar);
        edtUsernameRegis = findViewById(R.id.edtUsernameRegistrar);
        edtPasswordRegis = findViewById(R.id.edtPasswordRegistrar);
        edtConfirmPasswordRegis = findViewById(R.id.edtConfirmarPassRegistrar);



        txtInvalidRegis = findViewById(R.id.txtInvalidoRegistrar);
    }

    public void registerClicked (View view){

        String pass1 =edtPasswordRegis.getText().toString();
        String pass2 = edtConfirmPasswordRegis.getText().toString();


        if(!pass1.equals(pass2)){

            //txtInvalidRegis.setVisibility(1);
            Toast.makeText(RegistrarActivity.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            edtPasswordRegis.setText("");
            edtConfirmPasswordRegis.setText("");



        }
        else{

            // Metodo login para guardar en shared preference
            Toast.makeText(RegistrarActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

            ExecuteRegister register = new ExecuteRegister(edtUsernameRegis.getText().toString()
                                                            ,edtEmailRegis.getText().toString()
                                                            ,edtPasswordRegis.getText().toString());
            register.execute();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public class ExecuteRegister extends AsyncTask<String,Void,String>{
        private String name;
        private String email;
        private String password;

        public ExecuteRegister(String name,String email, String password){
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            api.register(name,email,password);

            return null;
        }
    }
}
