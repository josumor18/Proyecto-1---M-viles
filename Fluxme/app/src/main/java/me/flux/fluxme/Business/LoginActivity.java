package me.flux.fluxme.Business;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import me.flux.fluxme.Data.API_Access;
import me.flux.fluxme.R;

public class LoginActivity extends AppCompatActivity {

    private static final String USER_PREFERENCES = "user.preferences.fluxme";
    private static final String PREFERENCE_EMAIL = "string.email.sesion";
    private static final String PREFERENCE_AUTH_TOKEN = "string.token.sesion";
    private static final String PREFERENCE_FOTO_TOKEN = "string.foto.sesion";
    private static final String PREFERENCE_SESION_ACTIVA = "boolean.sesion.isActiva";

    private static String email = "";
    private static String nombre_completo = "";
    private static String token = "";
    private static String foto = "";

    RelativeLayout rlLogin, rlLoader;
    ProgressBar progressBarLogin;
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

        rlLogin = findViewById(R.id.rlLogin);
        rlLoader = findViewById(R.id.rlLoader);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        if(getEstadoSesion()){
            String[] userData = getUsuarioSesion();
            email = userData[0];
            token = userData[1];
            foto = userData[2];
            nombre_completo = "Usuario App";//Se obtiene de BD por medio del username
            //foto = "Fotoooo";//Se obtiene de BD por medio del username
            ///IR AL ASYNKTASK pero hacer un metodo para iniciar sesion solo con token
            ExecuteLogin login = new ExecuteLogin(email, token);
            login.setAuth_Token(email, token);
            login.execute();
        }else{
            LoginManager.getInstance().logOut();

            SharedPreferences preferences = this.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
            preferences.edit().putString(PREFERENCE_EMAIL, "").apply();
            preferences.edit().putString(PREFERENCE_AUTH_TOKEN, "").apply();
            preferences.edit().putString(PREFERENCE_FOTO_TOKEN, "").apply();
            preferences.edit().putBoolean(PREFERENCE_SESION_ACTIVA, false).apply();
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
            public void onSuccess(final LoginResult loginResult) {
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

                token = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email = object.getString("email");
                                    nombre_completo = object.getString("name");
                                    foto = Profile.getCurrentProfile().getProfilePictureUri(200,200).toString();
                                    //guardarUsuarioSesion();
                                    token = loginResult.getAccessToken().getToken();
                                    ExecuteLogin login = new ExecuteLogin(nombre_completo, email, token);
                                    login.execute();
                                    ///////iniciarSesion();
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
        email = edtUsernameLogin.getText().toString();
        String pass = edtPasswordLogin.getText().toString();
        //Validar si las credenciales son correctas
        ExecuteLogin login = new ExecuteLogin(email, pass);
        login.execute();
    }


    public void iniciarSesion(JSONObject response){
        Usuario_Singleton user = Usuario_Singleton.getInstance();

        try {
            response = response.getJSONObject("data");
            user.setId(response.getString("id"));
            user.setNombre(response.getString("name"));
            user.setEmail(response.getString("email"));
            user.setFoto(foto);
            user.setAuth_token(response.getString("authentication_token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Si son correctas...hacer:
        if(chckSesionActiva.isChecked()){
            guardarUsuarioSesion(user.getEmail(), user.getAuth_token());
        }

        Streaming streaming = Streaming.getInstance();
        streaming.setMediaPlayer(new MediaPlayer());
        streaming.setAudioManager((AudioManager)getSystemService(Context.AUDIO_SERVICE));
        streaming.initMediaPlayer();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void signinClicked(View view){
        //Aqui llama a la activity de registrar
        Intent intent = new Intent(getApplicationContext(), RegistrarActivity.class);
        startActivity(intent);
    }

    //------------------------------------------------------------------------------------------------------//
    //----------------------------- Obtiene/Guarda las preferencias de sesion ------------------------------//
    //------------------------------------------------------------------------------------------------------//
    public void guardarUsuarioSesion(String correo, String auth_token){
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        //Esto es para probar unicamente...después habría que ver si lo que se guarda son todos los datos del usuario o que...
        preferences.edit().putString(PREFERENCE_EMAIL, correo).apply();
        preferences.edit().putString(PREFERENCE_AUTH_TOKEN, auth_token).apply();
        preferences.edit().putString(PREFERENCE_FOTO_TOKEN, foto).apply();
        preferences.edit().putBoolean(PREFERENCE_SESION_ACTIVA, chckSesionActiva.isChecked()).apply();
    }

    public static void actualizarAuth_Token(String auth_token, Context c){
        SharedPreferences preferences = c.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);

        preferences.edit().putString(PREFERENCE_AUTH_TOKEN, auth_token).apply();
        //preferences.edit().putString("tokenAux", auth_token).apply();
    }

    public static void cerrarSesion(Context c){
        foto = "";
        Usuario_Singleton user = Usuario_Singleton.getInstance();
        user.setId("");
        user.setNombre("");
        user.setEmail("");
        user.setFoto("");
        user.setAuth_token("");

        //Streaming.pause();
        //Streaming.cleanStreaming();

        LoginManager.getInstance().logOut();
        if(perfil != null){
            perfil.stopTracking();
            perfil = null;
        }
        email = "";

        SharedPreferences preferences = c.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);

        preferences.edit().putString(PREFERENCE_EMAIL, email).apply();
        preferences.edit().putString(PREFERENCE_AUTH_TOKEN, "").apply();
        preferences.edit().putString(PREFERENCE_FOTO_TOKEN, "").apply();
        preferences.edit().putBoolean(PREFERENCE_SESION_ACTIVA, false).apply();
    }

    public String[] getUsuarioSesion(){
        String[] userData = new String[3];
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        //Esto es para probar unicamente...después habría que ver si lo que se obtiene son todos los datos del usuario o que (segun lo que se haya guardado)...
        userData[0] = preferences.getString(PREFERENCE_EMAIL, "");
        userData[1] = preferences.getString(PREFERENCE_AUTH_TOKEN, "");
        userData[2] = preferences.getString(PREFERENCE_FOTO_TOKEN, "");
        return userData;
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

    //------------------------------------------------------------------------------------------------------//
    public class ExecuteLogin extends AsyncTask<String, Void, String>{
        private String name;
        private String email;
        private String password;
        private String auth_token;
        int tipoAutenticacion = 0;// 0-Formulario, 1-Facebook, 2-Authentication Token(sesion abierta)
        private boolean isLogged = false;

        //Login con los campos de email y contraseña
        public ExecuteLogin(String email, String password){
            this.email = email;
            this.password = password;
            tipoAutenticacion = 0;
        }

        //Login con facebook
        public ExecuteLogin(String name, String email, String auth_token){
            this.name = name;
            this.email = email;
            this.password = "";
            this.auth_token = auth_token.substring(0, 10);
            tipoAutenticacion = 1;
        }

        //set Authentication Token
        public void setAuth_Token(String email, String auth_token){
            this.email = email;
            this.auth_token = auth_token;
            tipoAutenticacion = 2;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rlLoader.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            API_Access api = API_Access.getInstance();
            if(tipoAutenticacion == 2){
                //login con sesion ya abierta de antes
                isLogged = api.login_token(email, auth_token);
            }else if(tipoAutenticacion == 1){
                //login con facebook
                isLogged = api.login_facebook(name, email, auth_token);
            }else if (tipoAutenticacion == 0){
                //login con los campos del formulario (email, password)
                isLogged = api.login(email, password);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isLogged){
                iniciarSesion(API_Access.getInstance().getJsonObjectResponse());
            }else{
                String mensaje = "Error al iniciar sesión";
                try {
                    mensaje = (API_Access.getInstance().getJsonObjectResponse()).getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                rlLoader.setVisibility(View.INVISIBLE);
                rlLogin.setVisibility(View.VISIBLE);
            }
        }
    }
}
