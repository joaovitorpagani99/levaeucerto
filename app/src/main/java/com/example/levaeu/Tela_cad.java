package com.example.levaeu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class Tela_cad extends AppCompatActivity {
    Button cadastrar;
    RadioGroup grupo;
    private EditText cad_nome,cad_sobrenome,cad_usuario,cad_senha;
    String[] msg = {"Prencha todos os campos","cadastro realizado com sucesso"};
    String usuarioId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cad);
        cadastrar = findViewById(R.id.cad_pass_btn);
        cad_nome = findViewById(R.id.cad_nome);
        cad_sobrenome = findViewById(R.id.cad_sobrenome);
        cad_usuario = findViewById(R.id.cad_usuario);
        cad_senha = findViewById(R.id.cad_senha);
        grupo = findViewById(R.id.radioGrupo);

    }

    public void radioButtonOnClick(View view) {
        int id = grupo.getCheckedRadioButtonId();
        switch (id) {
            case R.id.radioNao:
                cadastrar.setText("Cadastrar");
                break;
            case R.id.radioSim:
                cadastrar.setText("Prosseguir");
                break;
        }
    }
    public void onClickCadastrar(View view){
        String nome = cad_nome.getText().toString();
        String sobrenome = cad_sobrenome.getText().toString();
        String usuario = cad_usuario.getText().toString();
        String senha = cad_senha.getText().toString();
        if(cadastrar.getText().equals("Cadastrar")) {
            if (nome.isEmpty() || sobrenome.isEmpty() || usuario.isEmpty() || senha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, msg[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }else{
                cadastrarUsuario(view);
            }
        }
        if(cadastrar.getText().equals("Prosseguir")){
            Bundle bundle = new Bundle();
            bundle.putString("nome",nome);
            bundle.putString("sobrenome",sobrenome);
            bundle.putString("usuario",usuario);
            bundle.putString("senha",senha);
            Intent intent = new Intent(getApplicationContext(), Cad_carro.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    private void cadastrarUsuario(View view){
        String usuario = cad_usuario.getText().toString();
        String senha = cad_senha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                    salvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(view, msg[1], Snackbar.LENGTH_SHORT);
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
    private void salvarDadosUsuario(){
        String nome = cad_nome.getText().toString();
        String sobrenome = cad_sobrenome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> usuario = new HashMap<>();
        usuario.put("nome",nome);
        usuario.put("sobrenome",sobrenome);

        usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Cadastro").document(usuarioId);
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


    }

}