package vitor.paiva.appvitor.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vitor.paiva.appvitor.DetalhePassageiroActivity;
import vitor.paiva.appvitor.R;
import vitor.paiva.appvitor.model.PassageiroModel;

public class AdapterPassageiros extends RecyclerView.Adapter<AdapterPassageiros.MyViewhOlder> {

    List<PassageiroModel> lista;
    Context c;

    public AdapterPassageiros(List<PassageiroModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewhOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewhOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.passageito_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewhOlder holder, int position) {
        StringBuilder info = new StringBuilder();

        info.append("<b>").append("Passageiro: ").append("</b>").append(lista.get(position).getNome());
        holder.info_generica.setText(Html.fromHtml(info.toString()));

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(c, DetalhePassageiroActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("id", lista.get(position).getId());
            c.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewhOlder extends RecyclerView.ViewHolder {

        TextView info_generica;

        public MyViewhOlder(@NonNull View itemView) {
            super(itemView);
            info_generica = itemView.findViewById(R.id.info_generica);
        }
    }
}
