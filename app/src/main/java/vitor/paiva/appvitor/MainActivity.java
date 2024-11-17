package vitor.paiva.appvitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vitor.paiva.appvitor.adapter.AdapterPassageiros;
import vitor.paiva.appvitor.databinding.ActivityMainBinding;
import vitor.paiva.appvitor.model.PassageiroModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private AdapterPassageiros adapter;
    private List<PassageiroModel> list = new ArrayList<>();
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("users");

    private SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sh = getSharedPreferences("USER", MODE_PRIVATE);
        mainBinding.perfil.setOnClickListener(v -> {
            Intent i = new Intent(this, PerfilActivity.class);
            i.putExtra("id", sh.getString("id", ""));
            startActivity(i);
        });

        mainBinding.rvPassageiros.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.rvPassageiros.setHasFixedSize(true);
        mainBinding.rvPassageiros.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AdapterPassageiros(list, this);
        mainBinding.rvPassageiros.setAdapter(adapter);

        ref_users.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dado : snapshot.getChildren()) {
                        list.add(dado.getValue(PassageiroModel.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}