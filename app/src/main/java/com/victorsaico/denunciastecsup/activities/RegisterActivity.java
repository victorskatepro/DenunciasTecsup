package com.victorsaico.denunciastecsup.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.victorsaico.denunciastecsup.servicies.ApiService;
import com.victorsaico.denunciastecsup.servicies.ApiServiceGenerator;
import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.servicies.ResponseMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

private EditText edtcorreo,edtusername,edtpassword;
private Button btnRegistrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtcorreo = (EditText) findViewById(R.id.email);
        edtusername = (EditText) findViewById(R.id.username);
        edtpassword = (EditText)findViewById(R.id.password);
        btnRegistrar = (Button) findViewById(R.id.btnregistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }
    public void registrar (){
        String username = edtusername.getText().toString();
        String correo = edtcorreo.getText().toString();
        String password = edtpassword.getText().toString();
        if(username.isEmpty()){
            edtusername.setError("Username necesario");
            return;
        }
        if(correo.isEmpty()){
            edtcorreo.setError("Correo necesario");
            return;
        }
        if(password.isEmpty()){
            edtpassword.setError("Password necesario");
            return;
        }

        initialize(username, correo, password);
    }
    public void initialize(String username, String correo, String password){
        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<ResponseMessage> call = null;
        call = service.registerUsuario(username,password,correo);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try{
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: "+ statusCode);
                    if(response.isSuccessful()){
                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage" + responseMessage);
                        Toast.makeText(RegisterActivity.this,responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        ResponseMessage responseMessage = response.body();
                        Log.e(TAG, "on Error" + response.errorBody().string());

                        Toast.makeText(RegisterActivity.this, responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Throwable t){
                    try {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }catch (Throwable x){
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure" + t.toString());
                Toast.makeText(RegisterActivity.this, "Error en el servicio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
