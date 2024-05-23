package com.rodmat95.trucksgps.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.LoginActivity;
import com.rodmat95.trucksgps.Main.User.MyInfoFragment;
import com.rodmat95.trucksgps.Providers.AuthProvider;
import com.rodmat95.trucksgps.Providers.GeofireProvider;
import com.rodmat95.trucksgps.R;

public class UserFragment extends Fragment {
    private DatabaseReference usuariosRef;
    private AuthProvider mAuthProvider;
    private TextView textViewUserName;
    private ImageView buttonMyInfo, buttonChangePassword, buttonHelpAndSupport, buttonSettings, buttonAboutTheApp, buttonLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mAuthProvider = new AuthProvider();
        usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios activos");

        // Inicializar vistas
        textViewUserName = view.findViewById(R.id.textViewUserName);
        buttonMyInfo = view.findViewById(R.id.buttonMyInfo);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonHelpAndSupport = view.findViewById(R.id.buttonHelpAndSupport);
        buttonSettings = view.findViewById(R.id.buttonSettings);
        buttonAboutTheApp = view.findViewById(R.id.buttonAboutTheApp);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);

        // Configurar eventos de clic
        getUserName();
        myInfo();
        changePassword();
        helpAndSupport();
        settings();
        aboutTheApp();
        logOut();

        return view;
    }


    // Inicializar el fragmento DashboardFragment
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getUserName() {
        // Obtener el usuario actualmente autenticado
        FirebaseUser user = mAuthProvider.getCurrentUser();

        if (user != null) {
            // Si el usuario está autenticado, obtener su UID
            String uid = user.getUid();

            // Obtener una referencia a la base de datos de Firebase
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            // Crear una referencia al nodo del usuario en la base de datos
            DatabaseReference userRef = mDatabase.child("Usuarios").child(uid);

            // Escuchar cambios en los datos del usuario
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Verificar si existe el nodo del usuario
                    if (dataSnapshot.exists()) {
                        // Obtener los datos generales del usuario desde la base de datos
                        DataSnapshot datosGeneralesSnapshot = dataSnapshot.child("Datos generales");
                        if (datosGeneralesSnapshot.exists()) {
                            // Obtener el nombre del usuario desde los datos generales
                            String userName = datosGeneralesSnapshot.child("Usuario").getValue(String.class);

                            // Verificar si el nombre del usuario está disponible
                            if (userName != null && !userName.isEmpty()) {
                                // Establecer el nombre de usuario en el TextView
                                textViewUserName.setText(userName);
                            } else {
                                // Si el nombre de usuario está vacío, mostrar un mensaje alternativo
                                textViewUserName.setText("Nombre de usuario no disponible");
                            }
                        } else {
                            // Si no existen datos generales, mostrar un mensaje alternativo
                            textViewUserName.setText("Datos generales del usuario no encontrados en la base de datos");
                        }
                    } else {
                        // Si no existe el nodo del usuario, mostrar un mensaje alternativo
                        textViewUserName.setText("Nombre de usuario no encontrado en la base de datos");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar cualquier error en la lectura de datos de la base de datos
                    Log.e("UserFragment", "Error al leer datos de la base de datos: " + databaseError.getMessage());
                }
            });
        } else {
            // Si no hay ningún usuario autenticado, redirigir al usuario a la pantalla de inicio de sesión
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        }
    }

    private void myInfo() {
        buttonMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear e inicializar el fragmento UsersFragment
                MyInfoFragment myInfoFragment = new MyInfoFragment();

                // Utilizar FragmentTransaction para reemplazar el fragmento actual con MyInfoFragment
                FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, myInfoFragment).addToBackStack(null).commit();
            }
        });
    }

    private void changePassword() {
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void helpAndSupport() {
        buttonHelpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void settings() {
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void aboutTheApp() {
        buttonAboutTheApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void disconnect() {
        FirebaseUser currentUser = mAuthProvider.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            usuariosRef.child(userUid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("UserFragment", "Usuario desconectado y eliminado de usuarios_activos");
                    } else {
                        Log.w("UserFragment", "Error al desconectar al usuario", task.getException());
                    }
                }
            });
        } else {
            Log.w("UserFragment", "No hay usuario autenticado.");
        }
    }

    private void logOut() {
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
                mAuthProvider.logOut();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}