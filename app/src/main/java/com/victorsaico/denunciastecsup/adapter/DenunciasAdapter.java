package com.victorsaico.denunciastecsup.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.victorsaico.denunciastecsup.R;
import com.victorsaico.denunciastecsup.models.Denuncia;
import com.victorsaico.denunciastecsup.servicies.ApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JARVIS on 15/11/2017.
 */

public class DenunciasAdapter extends RecyclerView.Adapter<DenunciasAdapter.ViewHolder> {

    private List<Denuncia> denuncias;

    public DenunciasAdapter(){
        this.denuncias = new ArrayList<>();
    }

    public void setDenuncias(List<Denuncia> denuncias){
        this.denuncias = denuncias;
    }


   public class ViewHolder extends RecyclerView.ViewHolder {

       public ImageView fotoImage;
       public TextView tituloText;
       public TextView denunciaText;
       public TextView ubicacion;
       public TextView descripcion;

       public ViewHolder(View itemView) {
           super(itemView);
           fotoImage = (ImageView)itemView.findViewById(R.id.imgdenuncia);
           tituloText = (TextView) itemView.findViewById(R.id.txttitulo);
           denunciaText = (TextView) itemView.findViewById(R.id.txtauto);
           ubicacion = (TextView)itemView.findViewById(R.id.txtlugar);
           descripcion = (TextView)itemView.findViewById(R.id.txtdescripcion);
       }
   }
    @Override
    public DenunciasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.denuncia_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DenunciasAdapter.ViewHolder holder, int position) {

       Denuncia denuncia = this.denuncias.get(position);

       holder.tituloText.setText(denuncia.getTitulo());
       holder.denunciaText.setText("Autor :"+denuncia.getAutor());
       holder.ubicacion.setText("Direccion :"+denuncia.getDireccion());
        String url = ApiService.API_BASE_URL + "/images/" + denuncia.getImagen();
        Picasso.with(holder.itemView.getContext()).load(url).into(holder.fotoImage);
        holder.descripcion.setText("Direccion :"+denuncia.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return this.denuncias.size();
    }
}
