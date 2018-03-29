package me.flux.fluxme.Business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import me.flux.fluxme.R;

public class LoginActivity extends AppCompatActivity {

    private static final String USER_PREFERENCES = "user.preferences.fluxme";
    private static final String PREFERENCE_USERNAME = "string.username.sesion";
    private static final String PREFERENCE_SESION_ACTIVA = "boolean.sesion.isActiva";

    private static String username = "";
    private static String nombre_completo = "";
    private static String foto = "";
    private static boolean isAdmin = false;

    EditText edtUsernameLogin;
    EditText edtPasswordLogin;
    CheckBox chckSesionActiva;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private static ProfileTracker perfil;
    TextView txtRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getEstadoSesion()){
            username = getUsuarioSesion();
            nombre_completo = "Usuario App";//Se obtiene de BD por medio del username
            foto = "Fotoooo";//Se obtiene de BD por medio del username
            iniciarSesion();
        }

        txtRegistrar = findViewById(R.id.txtRegistrar);
        txtRegistrar.setText(Html.fromHtml(getResources().getString(R.string.strRegistrateAqui)));

        edtUsernameLogin = findViewById(R.id.edtUsernameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        chckSesionActiva = findViewById(R.id.chckSesionActiva);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                perfil = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        if(currentProfile != null){
                            foto = currentProfile.getProfilePictureUri(200, 200).toString();
                            //guardarUsuarioSesion();
                            //iniciarSesion();
                        }

                    }
                };
                perfil.startTracking();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    username = object.getString("id");
                                    nombre_completo = object.getString("name");
                                    foto = Profile.getCurrentProfile().getProfilePictureUri(200,200).toString();
                                    guardarUsuarioSesion();
                                    iniciarSesion();
                                    //String birthday = object.getString("birthday"); // 01/31/1980 format
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();



                //Verificar si existe el usuario en la base de datos. Si no es asi, se registra con los datos de facebook...
                ////////OBTENER SI EL USUARIO ES ADMIN O NO Y GUARDARLO EN LA VARIABLE BOOLEANA isAdmin;

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "Login con Facebook ha fallado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginClicked(View view){
        username = edtUsernameLogin.getText().toString();
        String pass = edtPasswordLogin.getText().toString();
        //Validar si las credenciales son correctas
        ////////OBTENER SI EL USUARIO ES ADMIN O NO Y GUARDARLO EN LA VARIABLE BOOLEANA isAdmin;
        //Si son correctas...hacer:
        if(chckSesionActiva.isChecked()){
            guardarUsuarioSesion();
        }
        iniciarSesion();
    }

    public void iniciarSesion(){
        Usuario_Singleton user = Usuario_Singleton.getInstance();
        user.setNombre(nombre_completo);
        user.setUsername(username);
        user.setFoto(foto);
        user.setAdmin(isAdmin);

        Streaming streaming = Streaming.getInstance();
        streaming.setMediaPlayer(new MediaPlayer());
        streaming.setAudioManager((AudioManager)getSystemService(Context.AUDIO_SERVICE));
        streaming.initMediaPlayer();
        /*streaming.setIdEmisora("0");//LINEA DE PRUEBA. HAY QUE QUITARLA
        streaming.setEmisora_name("RadioPrueba");//LINEA DE PRUEBA. HAY QUE QUITARLA
        streaming.setStream("http://s41.myradiostream.com:35530/");//LINEA DE PRUEBA. HAY QUE QUITARLA
        */

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signinClicked(View view){
        //Aqui llama a la activity de registrar
        /*Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(intent);*/
        Toast.makeText(this, "Llamar a la activity Signin", Toast.LENGTH_SHORT).show();
    }

    //------------------------------------------------------------------------------------------------------//
    //----------------------------- Obtiene/Guarda las preferencias de sesion ------------------------------//
    //------------------------------------------------------------------------------------------------------//
    public void guardarUsuarioSesion(){
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        //Esto es para probar unicamente...después habría que ver si lo que se guarda son todos los datos del usuario o que...
        preferences.edit().putString(PREFERENCE_USERNAME, username).apply();
        preferences.edit().putBoolean(PREFERENCE_SESION_ACTIVA, chckSesionActiva.isChecked()).apply();
    }

    public static void cerrarSesion(Context c){
        Usuario_Singleton user = Usuario_Singleton.getInstance();
        user.setNombre("");
        user.setUsername("");
        user.setFoto("");
        user.setAdmin(false);

        //Streaming.pause();
        //Streaming.cleanStreaming();

        LoginManager.getInstance().logOut();
        if(perfil != null){
            perfil.stopTracking();
            perfil = null;
        }
        username = "";

        SharedPreferences preferences = c.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        //Esto es para probar unicamente...después habría que ver si lo que se guarda son todos los datos del usuario o que...
        preferences.edit().putString(PREFERENCE_USERNAME, username).apply();
        preferences.edit().putBoolean(PREFERENCE_SESION_ACTIVA, false).apply();
    }

    public String getUsuarioSesion(){
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        //Esto es para probar unicamente...después habría que ver si lo que se obtiene son todos los datos del usuario o que (segun lo que se haya guardado)...
        return preferences.getString(PREFERENCE_USERNAME, "");
    }

    public boolean getEstadoSesion(){
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_SESION_ACTIVA, false);
    }
    //------------------------------------------------------------------------------------------------------//
    //------------------------------------------------------------------------------------------------------//
    //------------------------------------------------------------------------------------------------------//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
