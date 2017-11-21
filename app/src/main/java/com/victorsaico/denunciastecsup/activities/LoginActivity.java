package com.victorsaico.denunciastecsup.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.models.Usuario;
import com.victorsaico.denunciastecsup.servicies.ApiService;
import com.victorsaico.denunciastecsup.servicies.ApiServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private EditText edtpassword, edtusername;
    private Button btnlogin,btnregistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtpassword = (EditText) findViewById(R.id.password);
        edtusername = (EditText) findViewById(R.id.username);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnregistrar = (Button) findViewById(R.id.btnregistrar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getBoolean("islogged", false)){
            Log.d(TAG,"al main");
            goMain();
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    public void login(){
        String username = edtusername.getText().toString();
        String password = edtpassword.getText().toString();
        if(username.isEmpty()){
            edtusername.setError("Se te olvido el email :)");
            return;
        }else if (password.isEmpty()){
            edtpassword.setError("Oops se te olvido");
            return;
        }
        initialize(username, password);
    }
    public void initialize(String username, String password){
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Usuario> call = null;
        call = service.loginUsuario(username, password);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {
                        assert response != null;
                        Usuario usuarios = response.body();
                        guardarShared(usuarios);
                        goMain();
                        Log.e(TAG, "usuarios"+usuarios);
                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Usuario no autorizado");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public void register(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void guardarShared(Usuario usuarios){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean success = editor
                .putString("username", usuarios.getUsername())
                .putString("correo", usuarios.getCorreo())
                .putString("photo", usuarios.getImagen())
                .putString("idUsuario", Integer.toString(usuarios.getId()))
                .putBoolean("islogged", true)
                .commit();
        goMain();

    }
    public void goMain(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
