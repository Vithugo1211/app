package vitor.paiva.appvitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.UUID;

import vitor.paiva.appvitor.databinding.ActivityCadastroBinding;
import vitor.paiva.appvitor.databinding.ActivityDetalhePassageiroBinding;
import vitor.paiva.appvitor.model.PassageiroModel;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding mainBinding;
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("users");
    private SharedPreferences sh;
    private SharedPreferences.Editor ed;

    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        sh = getSharedPreferences("USER", MODE_PRIVATE);
        ed = sh.edit();

        mainBinding.profileImage.setOnClickListener( V -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        mainBinding.salvar.setOnClickListener( v -> {
            PassageiroModel passageiroModel = new PassageiroModel(
                    UUID.randomUUID().toString(),
                    mainBinding.nome.getText().toString(),
                    mainBinding.email.getText().toString(),
                    mainBinding.senha.getText().toString()
            );
            ref_users.child(passageiroModel.getId()).setValue(passageiroModel).addOnCompleteListener( task -> {
                if ( task.isSuccessful() ){
                    Toast.makeText(this, "Criado com Sucesso!", Toast.LENGTH_SHORT).show();
                    ed.putString("id", passageiroModel.getId()).apply();
                    startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(this, "Problema de Conexão", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // Converte a URI da imagem em um Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Exibe o Bitmap no ImageView
                mainBinding.profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}