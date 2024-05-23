package com.rodmat95.trucksgps.Main.Dashboard.SlideBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodmat95.trucksgps.Providers.AuthProvider;
import com.rodmat95.trucksgps.Providers.NetworkProvider;
import com.rodmat95.trucksgps.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText editTextName;
    private TextInputEditText editTextEmail;
    private CheckBox checkBoxAdmin;
    private Button buttonRegister;

    private FirebaseAuth mAuth;
    private AuthProvider mAuthProvider;
    private NetworkProvider mNetworkProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthProvider = new AuthProvider();
        mNetworkProvider = new NetworkProvider(this);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        checkBoxAdmin = findViewById(R.id.checkBoxAdmin);
        buttonRegister = findViewById(R.id.buttonRegister);

        clickButtonRegister();
    }

    private void clickButtonRegister() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();

                if (!name.isEmpty() && !email.isEmpty()) {
                    if (mNetworkProvider.isNetworkAvailable()) {
                        // Verificar si el correo electrónico ya está registrado antes de continuar con el registro
                        mAuth.fetchSignInMethodsForEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().getSignInMethods().isEmpty()) {
                                            // El correo electrónico no está registrado, proceder con el registro del usuario
                                            // Obtener el estado del CheckBox
                                            boolean isAdmin = checkBoxAdmin.isChecked();
                                            registerUser(name, email, isAdmin);
                                        } else {
                                            // El correo electrónico ya está registrado, mostrar mensaje de error
                                            Toast.makeText(RegisterActivity.this, "El correo electrónico ingresado ya está registrado", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Error al verificar si el correo electrónico está registrado
                                        Log.e("TAG", "Error al verificar correo electrónico", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Error al verificar correo electrónico", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        mNetworkProvider.showNoInternetConnectionAlert();
                    }
                } else {
                    // Mostrar mensaje de error si algún campo está vacío
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String name, String email, boolean isAdmin) {
        // Generar contraseña temporal
        String tempPassword = UUID.randomUUID().toString().substring(0, 8); // Generar una cadena aleatoria de 8 caracteres

        mAuthProvider.register(email, tempPassword)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Enviar correo electrónico de verificación
                        mAuthProvider.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Éxito al enviar el correo electrónico de verificación
                                        Toast.makeText(RegisterActivity.this, "Usuario registrado. Se ha enviado un correo electrónico para la validación.", Toast.LENGTH_SHORT).show();

                                        // Guardar detalles del usuario en la base de datos
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                                        String id = mAuthProvider.getUserUid(); // Obtener el UID del usuario
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("Datos generales", new HashMap<String, Object>() {{
                                            put("Usuario", name);
                                            put("Correo electrónico", email);
                                            put("Rol", isAdmin ? "Administrador" : "Usuario");
                                            put("Contraseña", tempPassword);
                                        }});
                                        userData.put("Datos personales", new HashMap<String, Object>() {{
                                            put("Nombre", name.split(" ")[0]);
                                            put("Apellido paterno", name.split(" ").length > 1 ? name.split(" ")[1] : "");
                                            put("Apellido materno", "");
                                            put("Número", "");
                                        }});

                                        mDatabase.child("Usuarios").child(id).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task2) {
                                                if (task2.isSuccessful()) {
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Los datos no se pudieron crear correctamente", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        // Error al enviar el correo electrónico de verificación
                                        Log.e("TAG", "Error al enviar correo de verificación", task1.getException());
                                        Toast.makeText(RegisterActivity.this, "Error al enviar correo de verificación", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Mostrar alerta si falla el registro
                        showAlert();
                    }
                });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}