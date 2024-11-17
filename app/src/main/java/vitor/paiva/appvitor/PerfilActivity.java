package vitor.paiva.appvitor;

import android.os.Bundle;
import android.text.Html;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vitor.paiva.appvitor.databinding.ActivityMainBinding;
import vitor.paiva.appvitor.databinding.ActivityPerfilBinding;
import vitor.paiva.appvitor.model.PassageiroModel;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding mainBinding;
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mainBinding.infoPerfil.setText("Carregando...");
        ref_users.child(getIntent().getExtras().getString("id", "")).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                PassageiroModel passageiroModel = task.getResult().getValue(PassageiroModel.class);
                StringBuilder info = new StringBuilder();
                info.append("<b>Informação do Perfil: <b>").append("<br>")
                        .append("<b>Nome: </b>").append(passageiroModel.getNome()).append("<br>")
                        .append("<b>Email: </b>").append(passageiroModel.getEmail()).append("<br>")
                        .append("<b>Senha: </b>").append(passageiroModel.getSenha()).append("<br>");

                mainBinding.infoPerfil.setText(Html.fromHtml(info.toString()));
            }
        });
    }
}