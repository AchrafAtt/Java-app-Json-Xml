package ma.tp.retrofitjson;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ma.tp.retrofitjson.api.ApiInterface;
import ma.tp.retrofitjson.beans.Compte;
import ma.tp.retrofitjson.config.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class EditCompteActivity extends AppCompatActivity {
    private EditText etSolde;
    private Button btnSave;


    private Spinner spinnerType;
    private String selectedFormat; // Format passed from MainActivity
    private long compteId; // ID of the selected Compte to edit
    private Compte existingCompte; // To hold the current account data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_compte);
        etSolde = findViewById(R.id.etSolde);
        spinnerType = findViewById(R.id.spinnerType);
        btnSave = findViewById(R.id.btnSave);

        ; // Retrieve the Compte ID
        compteId = getIntent().getLongExtra("compteId", 0);

        // Get selected format and compte ID passed from MainActivity
        selectedFormat = getIntent().getStringExtra("selectedFormat");

        Log.d(TAG, "Received compteId in EditCompteActivity: " + compteId);
        Log.d(TAG, "Received selectedFormat in EditCompteActivity: " + selectedFormat);

        if (compteId == 0) {
            Toast.makeText(this, "Invalid account ID", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if compteId is invalid
            return;
        }

        // Set up the Spinner with types COURANT and EPARGNE
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"COURANT", "EPARGNE"});
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);

        // Fetch the account details from the server using the ID
        fetchCompteDetails(compteId);

        btnSave.setOnClickListener(view -> updateCompte());
    }

    private void fetchCompteDetails(long compteId) {
        ApiInterface apiService = RetrofitClient.getApi(selectedFormat); // Use the selected format (JSON or XML)
        apiService.getCompteById(compteId).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    existingCompte = response.body();
                    etSolde.setText(String.valueOf(existingCompte.getSolde()));
                    spinnerType.setSelection(existingCompte.getType().equals("COURANT") ? 0 : 1);
                } else {
                    Toast.makeText(EditCompteActivity.this, "Failed to fetch compte details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(EditCompteActivity.this, "Error fetching compte details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCompte() {
        String soldeText = etSolde.getText().toString();
        if (soldeText.isEmpty()) {
            Toast.makeText(this, "Solde is required", Toast.LENGTH_SHORT).show();
            return;
        }

        double solde;
        try {
            solde = Double.parseDouble(soldeText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid solde value", Toast.LENGTH_SHORT).show();
            return;
        }

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String selectedType = spinnerType.getSelectedItem().toString();

        // Update the existing compte object with new values
        existingCompte.setSolde(solde);
        existingCompte.setType(selectedType);
        existingCompte.setDateCreation(todayDate);

        ApiInterface apiService = RetrofitClient.getApi(selectedFormat); // Use the selected format (JSON or XML)
        apiService.updateCompte(compteId, existingCompte).enqueue(new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditCompteActivity.this, "Compte updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Set the result as OK
                    finish(); // Close the activity and return to MainActivity
                } else {
                    Toast.makeText(EditCompteActivity.this, "Failed to update compte", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                Toast.makeText(EditCompteActivity.this, "Error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

