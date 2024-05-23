package com.rodmat95.trucksgps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.rodmat95.trucksgps.Providers.AuthProvider;
import com.rodmat95.trucksgps.Providers.NetworkProvider;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private CheckBox checkBoxRemember;
    private TextView textViewForgotPassword;
    private Button buttonLogin;
    private LinearLayout progressBarLayout;

    private AuthProvider mAuthProvider;
    private NetworkProvider mNetworkProvider;

    private SharedPreferences sharedPreferences;
    private static final String PREF_EMAIL = "pref_email";
    private static final String PREF_PASSWORD = "pref_password";
    private static final String PREF_CHECKBOX = "pref_checkbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        buttonLogin = findViewById(R.id.buttonLogIn);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        mAuthProvider = new AuthProvider();
        mNetworkProvider = new NetworkProvider(this);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Recuperar estado del CheckBox y credenciales guardadas
        checkBoxRemember.setChecked(sharedPreferences.getBoolean(PREF_CHECKBOX, false));
        if (checkBoxRemember.isChecked()) {
            editTextEmail.setText(sharedPreferences.getString(PREF_EMAIL, ""));
            editTextPassword.setText(sharedPreferences.getString(PREF_PASSWORD, ""));
        }

        // Configurar eventos de clic
        logIn();
        forgotPassword();
    }

    private void logIn() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    if (mNetworkProvider.isNetworkAvailable()) {
                        progressBarLayout.setVisibility(View.VISIBLE);

                        savePreferences(email, password, checkBoxRemember.isChecked());
                        mAuthProvider.login(email, password)
                                .addOnCompleteListener(LoginActivity.this, task -> {
                                    progressBarLayout.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuthProvider.getCurrentUser();
                                        if (user.isEmailVerified()) {
                                            // El usuario ha verificado su correo electrónico, mostrar actividad principal y finalizar la actividad actual
                                            showMain(email, MainActivity.ProviderType.BASIC);
                                            finish();
                                        } else {
                                            // El usuario no ha verificado su correo electrónico, mostrar un mensaje de alerta
                                            showEmailVerificationAlert();
                                            // También se puede enviar nuevamente el correo de verificación si se desea, utilizando user.sendEmailVerification()
                                        }
                                    } else {
                                        // Mostrar alerta si falla el inicio de sesión
                                        showLoginErrorAlert();
                                    }
                                });
                    } else {
                        mNetworkProvider.showNoInternetConnectionAlert();
                    }
                }
            }
        });
    }

    private void forgotPassword() {
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePreferences(String email, String password, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_PASSWORD, password);
        editor.putBoolean(PREF_CHECKBOX, isChecked);
        editor.apply();
    }

    private void showMain(String email, MainActivity.ProviderType provider) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("email", email);
        mainIntent.putExtra("provider", provider.name());
        startActivity(mainIntent);
    }

    private void showEmailVerificationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor, verifica tu correo electrónico para iniciar sesión.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLoginErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Correo electrónico o contraseña incorrectos.")
                .setPositiveButton("OK", null)
                .show();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (mAuthProvider.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }*/
}