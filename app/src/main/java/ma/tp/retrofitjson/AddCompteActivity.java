package ma.tp.retrofitjson;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ma.tp.retrofitjson.beans.Compte;
import ma.tp.retrofitjson.api.ApiInterface;
import ma.tp.retrofitjson.config.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AddCompteActivity extends AppCompatActivity {
    private EditText etSolde;
    private Button btnSave;
    private Spinner spinnerType;
    private static final String TAG = "AddCompteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_compte);

        etSolde = findViewById(R.id.etSolde);
        spinnerType = findViewById(R.id.spinnerType);
        btnSave = findViewById(R.id.btnSave);

        // Set up the Spinner with the types COURANT and EPARGNE
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                new String[]{"COURANT", "EPARGNE"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        btnSave.setOnClickListener(view -> createCompte());
    }

    private void createCompte() {
        // Check if solde input is empty
        if (etSolde.getText().toString().isEmpty()) {
            Toast.makeText(this, "Solde is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get today's date in "yyyy-MM-dd" format
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());

        // Get selected type from spinner
        String selectedType = spinnerType.getSelectedItem().toString();

        // Create a new Compte object
        Compte compte = new Compte();
        compte.setSolde(Double.parseDouble(etSolde.getText().toString()));
        compte.setType(selectedType);
        compte.setDateCreation(todayDate);

        // Call the API to create a new Compte
        ApiInterface apiService = RetrofitClient.getApi();
        apiService.createCompte(compte).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCompteActivity.this, "Compte added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close this activity and return to MainActivity
                } else {
                    Toast.makeText(AddCompteActivity.this, "Failed to add compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(AddCompteActivity.this, "Error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
