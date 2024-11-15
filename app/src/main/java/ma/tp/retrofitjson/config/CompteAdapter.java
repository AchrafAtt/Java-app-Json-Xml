package ma.tp.retrofitjson.config;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ma.tp.retrofitjson.EditCompteActivity;
import ma.tp.retrofitjson.R;
import ma.tp.retrofitjson.api.ApiInterface;
import ma.tp.retrofitjson.beans.Compte;
import ma.tp.retrofitjson.config.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {

    private List<Compte> compteList;
    private Context context;
    private String selectedFormat;

    // Constructor for the adapter
    public CompteAdapter(List<Compte> compteList, Context context, String selectedFormat) {
        this.compteList = compteList;
        this.context = context;
        this.selectedFormat = selectedFormat;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view); // Return the ViewHolder with the inflated layout
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        // Bind the data from the Compte object to the views
        Compte compte = compteList.get(position);
        holder.tvId.setText("ID: " + compte.getId());
        holder.tvSolde.setText("Solde: " + compte.getSolde());
        holder.tvDateCreation.setText("Date: " + compte.getDateCreation());
        holder.tvType.setText("Type: " + compte.getType());

        long compteId = compte.getId();

        // Set up the click listener for the Edit button
        holder.fabEdit.setOnClickListener(view -> {
            Log.d(TAG, "Edit button clicked for compteId: " + compteId);
            Log.d(TAG, "Selected format: " + selectedFormat);

            Intent intent = new Intent(context, EditCompteActivity.class);
            intent.putExtra("compteId", compteId);  // Pass the Compte ID to Edit activity
            intent.putExtra("selectedFormat", selectedFormat); // Pass format

            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, 1); // Request code 1 for editing
            } else {
                context.startActivity(intent);
            }
        });

        // Set up the click listener for the Delete button
        holder.fabDelete.setOnClickListener(view -> {
            Log.d(TAG, "Delete button clicked for compteId: " + compteId);
            deleteCompte(compteId, position);
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the list
        return compteList.size();
    }

    // Method to handle deletion of a Compte
    private void deleteCompte(long compteId, int position) {
        ApiInterface apiService = RetrofitClient.getApi(selectedFormat); // Use the selected format (JSON or XML)
        apiService.deleteCompte(compteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Remove the item from the list and notify the adapter
                    compteList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Compte deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class CompteViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvSolde, tvDateCreation, tvType;
        FloatingActionButton fabEdit, fabDelete;

        // ViewHolder constructor
        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views by finding them from the layout
            tvId = itemView.findViewById(R.id.tvId);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvDateCreation = itemView.findViewById(R.id.tvDateCreation);
            tvType = itemView.findViewById(R.id.tvType);
            fabEdit = itemView.findViewById(R.id.fabEdit); // Initialize the Edit button
            fabDelete = itemView.findViewById(R.id.fabDelete); // Initialize the Delete button
        }
    }
}
