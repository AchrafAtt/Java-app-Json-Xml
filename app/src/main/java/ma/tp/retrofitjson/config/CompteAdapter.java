package ma.tp.retrofitjson.config;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ma.tp.retrofitjson.R;
import ma.tp.retrofitjson.beans.Compte;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> compteList;

    public CompteAdapter(List<Compte> compteList) {
        this.compteList = compteList;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = compteList.get(position);
        holder.tvId.setText("ID: " + compte.getId());
        holder.tvSolde.setText("Solde: " + compte.getSolde());
        holder.tvDateCreation.setText("Date: " + compte.getDateCreation());
        holder.tvType.setText("Type: " + compte.getType());
    }

    @Override
    public int getItemCount() {
        return compteList.size();
    }

    public static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvSolde, tvDateCreation, tvType;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvDateCreation = itemView.findViewById(R.id.tvDateCreation);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}

