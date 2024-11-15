package ma.tp.retrofitjson;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ma.tp.retrofitjson.api.ApiInterface;
import ma.tp.retrofitjson.beans.Compte;
import ma.tp.retrofitjson.config.CompteAdapter;
import ma.tp.retrofitjson.config.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EDIT_COMPTE = 1; // Request code for edit activity
    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;
    private FloatingActionButton btnAdd;
    private Spinner spinnerFormat;
    private static final String TAG = "MainActivity";
    private String selectedFormat = "application/json"; // Default format

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinnerFormat = findViewById(R.id.spinnerFormat); // Spinner to select format (JSON/XML)
        btnAdd = findViewById(R.id.fab);

        // Set up the Spinner for selecting format (JSON or XML)
        ArrayAdapter<String> adapterFormat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"application/json", "application/xml"});
        adapterFormat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFormat.setAdapter(adapterFormat);

        // Set Spinner item selection listener
        spinnerFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedFormat = (String) parentView.getItemAtPosition(position);
                fetchComptes(selectedFormat); // Fetch data based on selected format
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set the click listener for the FloatingActionButton to add new Compte
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCompteActivity.class);
            intent.putExtra("selectedFormat", selectedFormat);
            startActivity(intent);
        });

        // Fetch and display comptes with default format (JSON)
        fetchComptes(selectedFormat);
    }

    private void fetchComptes(String format) {
        ApiInterface apiService = RetrofitClient.getApi(format); // Pass the format (JSON or XML)
        apiService.getAllComptes().enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Compte> compteList = response.body();
                    compteAdapter = new CompteAdapter(compteList, MainActivity.this, selectedFormat);
                    recyclerView.setAdapter(compteAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                Log.e(TAG, "Error fetching comptes", t);
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_COMPTE && resultCode == RESULT_OK) {
            // Refresh the list after editing
            fetchComptes(selectedFormat);
        }
    }

    private void deleteCompte(long compteId) {
        ApiInterface apiService = RetrofitClient.getApi(selectedFormat); // Use the selected format (JSON or XML)
        apiService.deleteCompte(compteId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Compte deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchComptes(selectedFormat); // Refresh the list after deletion
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
