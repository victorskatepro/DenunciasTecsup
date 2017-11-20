package com.victorsaico.denunciastecsup.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.servicies.ApiService;


public class PerfilFragment extends Fragment {
private SharedPreferences sharedPreferences;
private TextView perfilcorreo,perfilusername;
private ImageView perfilImagen;
    public PerfilFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        perfilcorreo = (TextView) view.findViewById(R.id.texto_email);
        perfilusername = (TextView) view.findViewById(R.id.texto_nombre);
        perfilImagen = (ImageView) view.findViewById(R.id.perfilimagen);
        imprimirDatos();
        return view;
    }

    public void imprimirDatos(){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = sharedPreferences.getString("username", null);
            String correo = sharedPreferences.getString("correo", null);
            String photo = sharedPreferences.getString("photo", null);
            String url = ApiService.API_BASE_URL + "/usuarios/" + photo;
            Picasso.with(getContext()).load(url).into(perfilImagen);
            perfilusername.setText(username);
            perfilcorreo.setText(correo);
    }

}
