package com.victorsaico.denunciastecsup.servicies;

import com.victorsaico.denunciastecsup.models.Denuncia;
import com.victorsaico.denunciastecsup.models.Usuario;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenuncia(@Field("usuario_id") String usuarios_id,
                                         @Field("titulo") String titulo,
                                         @Field("autor") String autor,
                                         @Field("descripcion") String descripcion,
                                         @Field("latitud") String latitud,
                                         @Field("longitud") String longitud,
                                         @Field("direccion") String direccion);

    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenunciaWithImage(
            @Part("usuario_id") RequestBody usuario_id,
            @Part("titulo") RequestBody titulo,
            @Part("autor") RequestBody autor,
            @Part("descripcion") RequestBody descripcion,
            @Part("latitud") RequestBody latitud,
            @Part("longitud") RequestBody longitud,
            @Part("direccion") RequestBody direccion,
            @Part MultipartBody.Part imagen
    );

}
