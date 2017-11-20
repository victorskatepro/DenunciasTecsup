package com.victorsaico.denunciastecsup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.activities.UbicacionActivity;

public class AddDenunciaFragment extends Fragment {
    private Button btnubicar;
    private TextView Addres;
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

        btnubicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), UbicacionActivity.class), 200);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            Bundle MBuddle = data.getExtras();
            String calle = MBuddle.getString("Ubicacion");
            if(calle.isEmpty()){
                Toast.makeText(getContext(), "nada", Toast.LENGTH_SHORT).show();
            }
            Addres.setText(calle);

        }
    }

}
