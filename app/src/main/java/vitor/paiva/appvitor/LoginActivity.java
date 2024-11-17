package vitor.paiva.appvitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vitor.paiva.appvitor.databinding.ActivityLoginBinding;
import vitor.paiva.appvitor.databinding.ActivityPerfilBinding;
import vitor.paiva.appvitor.model.PassageiroModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mainBinding;
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("users");

    private SharedPreferences sh;
    private SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        sh = getSharedPreferences("USER", MODE_PRIVATE);
        ed = sh.edit();


        mainBinding.cadastrar.setOnClickListener(v -> startActivity(new Intent(this, CadastroActivity.class)));

        mainBinding.entrar.setOnClickListener(v -> {
            ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    boolean achou = false;

                    for (DataSnapshot dado : snapshot.getChildren()) {
                        PassageiroModel passageiroModel = dado.getValue(PassageiroModel.class);
                        if (passageiroModel.getEmail().equals(mainBinding.email.getText().toString())) {
                            if (passageiroModel.getSenha().equals(mainBinding.senha.getText().toString())) {
                                finish();
                                ed.putString("id", passageiroModel.getId()).apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                achou = true;
                            }
                        }
                    }
                    if (!achou)
                        Toast.makeText(LoginActivity.this, "Email e Senha incorretos!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }
}