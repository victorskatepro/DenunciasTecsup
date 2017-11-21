package com.victorsaico.denunciastecsup.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.activities.UbicacionActivity;
import com.victorsaico.denunciastecsup.servicies.ApiService;
import com.victorsaico.denunciastecsup.servicies.ApiServiceGenerator;
import com.victorsaico.denunciastecsup.servicies.ResponseMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddDenunciaFragment extends Fragment {
    private Button btnubicar,btnregistrar,btnfoto;
    private TextView Addres;
    private SharedPreferences sharedPreferences;
    private EditText txttitulo,txtdescripcion;
    private static final String TAG = AddDenunciaFragment.class.getSimpleName();
    private static final int CAPTURE_IMAGE_REQUEST = 300;
    private Uri mediaFileUri;
    private ImageView imgdenuncia;
    private ProgressDialog pDialog;
    public AddDenunciaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_denuncia, container, false);
        btnubicar = (Button)view.findViewById(R.id.btnubicacion);
        Addres = (TextView)view.findViewById(R.id.txtAddres);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        txttitulo = (EditText) view.findViewById(R.id.txtdenunciatitulo);
        txtdescripcion = (EditText) view.findViewById(R.id.descripcion_txt);
        btnregistrar = (Button)view.findViewById(R.id.btnregistrar);
        btnfoto = (Button)view.findViewById(R.id.btncapturefoto);
        imgdenuncia = (ImageView)view.findViewById(R.id.imgdenuncia);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = txttitulo.getText().toString();
                String descripcion = txtdescripcion.getText().toString();
                if(titulo.isEmpty()){
                    txttitulo.setError("Ingrese el titulo");
                }
                if(descripcion.isEmpty()){
                    txtdescripcion.setError("Ingrese la descripcion");
                }
                if(Addres == null){
                    Addres.setError("Seleccione una ubicacion");
                }
                Register(titulo , descripcion);
            }
        });
        btnubicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), UbicacionActivity.class), 200);

            }
        });
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String ubicacion = sharedPreferences.getString("address", null);
            String latitud = sharedPreferences.getString("latitud", null);
            String longitud = sharedPreferences.getString("longitud", null);
            Addres.setText(ubicacion);
        }
        if (requestCode == CAPTURE_IMAGE_REQUEST) {
            // Resultado en la captura de la foto
            if (resultCode == RESULT_OK) {
                try {
                    Log.d(TAG, "ResultCode: RESULT_OK");
                    // Toast.makeText(this, "Image saved to: " + mediaFileUri.getPath(), Toast.LENGTH_LONG).show();
                    Context applicationContext = getActivity().getApplicationContext();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), mediaFileUri);

                    // Reducir la imagen a 800px solo si lo supera
                    bitmap = scaleBitmapDown(bitmap, 800);
                    imgdenuncia.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(getActivity(), "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ResultCode: RESULT_CANCELED");
            } else {
                Log.d(TAG, "ResultCode: " + resultCode);
            }
        }
    }

    public void Register(String titulo, String descripcion){
        String ubicacion = Addres.getText().toString();
        if(ubicacion.isEmpty() || ubicacion.equals("Ubicacion")){
            Toast.makeText(getContext(), "Seleccionar una direccion", Toast.LENGTH_SHORT).show();
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        String latitud = sharedPreferences.getString("latitud", null);
        String longitud = sharedPreferences.getString("longitud", null);
        String id = sharedPreferences.getString("idUsuario", null);
        String username = sharedPreferences.getString("username", null);

        Call<ResponseMessage> call = null;

        pDialog.setMessage("Registro en proceso...");
        showDialog();
        if (mediaFileUri == null) {
            // Si no se incluye imagen hacemos un envío POST simple
            call = service.createDenuncia(id, titulo, username, descripcion, latitud, longitud, ubicacion);
        }else {
            File file = new File(mediaFileUri.getPath());
            Log.d(TAG, "File: " + file.getPath() + " - exists: " + file.exists());

            // Podemos enviar la imagen con el tamaño original, pero lo mejor será comprimila antes de subir (byteArray)
            // RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

            Bitmap bitmap = BitmapFactory.decodeFile(mediaFileUri.getPath());

            // Reducir la imagen a 800px solo si lo supera
            bitmap = scaleBitmapDown(bitmap, 800);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

            RequestBody user_idPart = RequestBody.create(MultipartBody.FORM, id);
            RequestBody tituloPart = RequestBody.create(MultipartBody.FORM, titulo);
            RequestBody user_nombrePart = RequestBody.create(MultipartBody.FORM, username);
            RequestBody descPart = RequestBody.create(MultipartBody.FORM, descripcion);
            RequestBody latitudPart = RequestBody.create(MultipartBody.FORM, latitud);
            RequestBody longitudPart = RequestBody.create(MultipartBody.FORM, longitud);
            RequestBody addressPart = RequestBody.create(MultipartBody.FORM, ubicacion);

            call = service.createDenunciaWithImage(user_idPart, tituloPart, user_nombrePart, descPart, latitudPart, longitudPart, addressPart, imagenPart);

        }
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);

                        Toast.makeText(getActivity(), responseMessage.getMessage(), Toast.LENGTH_LONG).show();

                        hideDialog();

                        // Save to SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("latitud", null);
                        editor.putString("longitud", null);
                        editor.putString("address", null);
                        editor.commit();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        HomeFragment fragment = new HomeFragment();
                        transaction.replace(R.id.main_content, fragment);
                        transaction.commit();

                    } else {

                        hideDialog();

                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        hideDialog();
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }
    //Tomar foto del celular
    public void takePicture() {
        try {

            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LIST, PERMISSIONS_REQUEST);
                return;
            }

            // Creando el directorio de imágenes (si no existe)
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new Exception("Failed to create directory");
                }
            }

            // Definiendo la ruta destino de la captura (Uri)
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
           File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
             mediaFileUri = Uri.fromFile(mediaFile);
            //Uri photoURI = Uri.fromFile(mediaFile());
            //Uri photoURI = FileProvider.getUriForFile(getContext(), context.getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());

            // Iniciando la captura
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(), "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    //<--!! //Registrar los permisos --!>
    private static final int PERMISSIONS_REQUEST = 200;

    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private boolean permissionsGranted() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d(TAG, "" + grantResults[i]);
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), PERMISSIONS_LIST[i] + " permiso rechazado!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(getActivity(), "Permisos concedidos, intente nuevamente.", Toast.LENGTH_LONG).show();
            }
        }
    }
    // Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    //agregar el dialogo y ocultarlo
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
