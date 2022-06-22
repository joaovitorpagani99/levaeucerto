package com.example.levaeu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.BaseOnChangeListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cad_carro extends AppCompatActivity {
    private EditText cad_carro,cad_placa,cad_cor;
    private String carro,placa,cor,nome,sobrenome,usuario,senha;
    String usuarioId;
    private Boolean carroConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_carro);
        carroConfirm = true;
        cad_carro = findViewById(R.id.cad_carro);
        cad_placa = findViewById(R.id.cad_placa);
        cad_cor = findViewById(R.id.cad_cor);
        Bundle dados = new Bundle();
        dados = getIntent().getExtras();
        if(dados != null){
            nome = dados.getString("nome");
            sobrenome = dados.getString("sobrenome");
            usuario = dados.getString("usuario");
            senha = dados.getString("senha");
        }
    }

    public void onClickCadastrar(View view){
        carro = cad_carro.getText().toString();
        cor = cad_cor.getText().toString();
        placa = cad_placa.getText().toString();
        if(carro.isEmpty() || cor.isEmpty() || placa.isEmpty()){
            Snackbar snackbar = Snackbar.make(view, "preencha os campos corretamente!", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else{
            cadastrar(view);
        }


    }
    private void cadastrar(View view){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    salvar();
                    Snackbar snackbar = Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "digite senha com no minimimo 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "esta conta ja esta cadastrada";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "e-mail invalido";
                    }catch (Exception e){
                        erro = "erro ao cadastrar usuario";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }
    private void salvar(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> usuario = new HashMap<>();
        usuario.put("nome",nome);
        usuario.put("sobrenome",sobrenome);
        usuario.put("carro modelo",carro);
        usuario.put("cor",cor);
        usuario.put("placa",placa);
        usuario.put("possui carro",carroConfirm);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Cadastro motorista").document(usuarioId);
        documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_erro","error!"+e.toString());
            }
        });
        if(isChild()){

        }

    }
}