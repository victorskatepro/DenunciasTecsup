package com.victorsaico.denunciastecsup.servicies;

import com.victorsaico.denunciastecsup.models.Denuncia;
import com.victorsaico.denunciastecsup.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by JARVIS on 13/11/2017.
 */

public interface ApiService {
    String API_BASE_URL = "https://denuncias-api-alejovictor.c9users.io";

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> loginUsuario(@Field("username") String username,
                                     @Field("password")String password);

    @FormUrlEncoded
    @POST("api/v1/registrar")
    Call<ResponseMessage> registerUsuario(@Field("username") String username,
                                          @Field("password") String password,
                                          @Field("email") String email);
    @GET("api/v1/denuncias")
    Call<List<Denuncia>> getDenuncias();

}
