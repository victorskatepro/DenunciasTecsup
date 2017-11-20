package com.victorsaico.denunciastecsup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.activities.RegistrarDenunciaActivity;
import com.victorsaico.denunciastecsup.adapter.DenunciasAdapter;
import com.victorsaico.denunciastecsup.models.Denuncia;
import com.victorsaico.denunciastecsup.servicies.ApiService;
import com.victorsaico.denunciastecsup.servicies.ApiServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private RecyclerView denunciasList;
    private FloatingActionButton btnRegistrar;
    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        denunciasList = (RecyclerView) view.findViewById(R.id.recyclerview);
        denunciasList.setLayoutManager(new LinearLayoutManager(getContext()));
        denunciasList.setAdapter(new DenunciasAdapter());
        initialize();

        return view;
    }

    private void initialize(){
        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<List<Denuncia>> call = service.getDenuncias();
        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        Log.d(TAG, "productos: " + denuncias);

                        DenunciasAdapter adapter = (DenunciasAdapter) denunciasList.getAdapter();
                        adapter.setDenuncias(denuncias);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }
            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void goRegisterDenuncia()
    {
        Intent intent = new Intent(getContext(), RegistrarDenunciaActivity.class);
        startActivity(intent);
    }
}
