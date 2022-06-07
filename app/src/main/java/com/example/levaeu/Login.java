package com.example.levaeu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Activity {
    Button cadastrar_login;
    Button entrar;
    EditText login_usuario,login_senha;
    String[] msg = {"Prencha todos os campos","Login efetuado com sucesso"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cadastrar_login = findViewById(R.id.cadastrar);
        entrar = findViewById(R.id.acessar);
        login_usuario = findViewById(R.id.usuario);
        login_senha = findViewById(R.id.senha);
    }
    public void OnClickCadastrar(View view){
        Intent intent = new Intent(this, Tela_cad.class);
        startActivity(intent);
    }
    public void onClickAcessar(View view){
        String usuario = login_usuario.getText().toString();
        String senha = login_senha.getText().toString();
        if(usuario.isEmpty() || senha.isEmpty()){
            Snackbar snackbar = Snackbar.make(view, msg[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else{
            autenticar();
        }
    }
    private void autenticar(){
        String usuario = login_usuario.getText().toString();
        String senha = login_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}