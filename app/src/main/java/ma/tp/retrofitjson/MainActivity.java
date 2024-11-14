package ma.tp.retrofitjson;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
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
    private RecyclerView recyclerView;
    private CompteAdapter compteAdapter;
    private FloatingActionButton btnAdd;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.fab);
        btnAdd.setOnClickListener(v -> {
            // Open AddCompteActivity to add a new compte
            Intent intent = new Intent(MainActivity.this, AddCompteActivity.class);
            startActivity(intent);
        });

        // Fetch and display comptes
        fetchComptes();
    }

    private void fetchComptes() {
        ApiInterface apiService = RetrofitClient.getApi();
        apiService.getAllComptes().enqueue(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Compte> compteList = response.body();
                    compteAdapter = new CompteAdapter(compteList);
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
    protected void onResume() {
        super.onResume();
        fetchComptes(); // Refresh data each time MainActivity is resumed
    }
}
