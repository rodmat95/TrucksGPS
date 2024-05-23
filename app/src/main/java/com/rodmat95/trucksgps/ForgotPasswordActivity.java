package com.rodmat95.trucksgps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private Button buttonResetPasword;
    private Button buttonBack;
    private FirebaseAuth mAuth;
    private String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Inicializar vistas
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonResetPasword = findViewById(R.id.buttonResetPasword);
        buttonBack = findViewById(R.id.buttonBack);

        mAuth = FirebaseAuth.getInstance();

        // Configurar evento de clic para el botón "Restablecer"
        buttonResetPasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = editTextEmail.getText().toString().trim();
                if (!TextUtils.isEmpty(strEmail)) {
                    ResetPassword();
                } else {
                    editTextEmail.setError("El campo de correo electrónico no puede estar vacío");
                }
            }
        });


        // Configurar evento de clic para el botón "Regresar"
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad anterior
                finish();
            }
        });
    }

    private void ResetPassword() {
        mAuth.sendPasswordResetEmail(strEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPasswordActivity.this, "El enlace para restablecer contraseña se ha enviado a su correo electrónico registrado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}