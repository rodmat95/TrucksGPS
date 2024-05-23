package com.rodmat95.trucksgps.Main.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.LoginActivity;
import com.rodmat95.trucksgps.R;

public class MyInfoFragment extends Fragment {

    private DatabaseReference mDatabase;
    private TextView textViewUserName;
    private LinearLayout layoutUsuario, layoutNombre, layoutApellidoPaterno, layoutApellidoMaterno, layoutCorreoElectronico,
                        layoutETUsuario, layoutETNombre, layoutETApellidoPaterno, layoutETApellidoMaterno, layoutETCorreoElectronico;
    ImageView buttonUsuario, buttonNombre, buttonApellidoPaterno, buttonApellidoMaterno, buttonCorreoElectronico;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        // Inicializar vistas
        mDatabase = FirebaseDatabase.getInstance().getReference();

        textViewUserName = view.findViewById(R.id.textViewUserName);

        layoutUsuario = view.findViewById(R.id.layoutUsuario);
        layoutNombre = view.findViewById(R.id.layoutNombre);
        layoutApellidoPaterno = view.findViewById(R.id.layoutApellidoPaterno);
        layoutApellidoMaterno = view.findViewById(R.id.layoutApellidoMaterno);
        layoutCorreoElectronico = view.findViewById(R.id.layoutCorreoElectronico);

        buttonUsuario = view.findViewById(R.id.buttonUsuario);
        buttonNombre = view.findViewById(R.id.buttonNombre);
        buttonApellidoPaterno = view.findViewById(R.id.buttonApellidoPaterno);
        buttonApellidoMaterno = view.findViewById(R.id.buttonApellidoMaterno);
        buttonCorreoElectronico = view.findViewById(R.id.buttonCorreoElectronico);

        layoutETUsuario = view.findViewById(R.id.layoutETUsuario);
        layoutETNombre = view.findViewById(R.id.layoutETNombre);
        layoutETApellidoPaterno = view.findViewById(R.id.layoutETApellidoPaterno);
        layoutETApellidoMaterno = view.findViewById(R.id.layoutETApellidoMaterno);
        layoutETCorreoElectronico = view.findViewById(R.id.layoutETCorreoElectronico);

        getUserName();

        // Configurar eventos de clic
        buttonDrop(layoutUsuario, buttonUsuario, layoutETUsuario);
        buttonDrop(layoutNombre, buttonNombre, layoutETNombre);
        buttonDrop(layoutApellidoPaterno, buttonApellidoPaterno, layoutETApellidoPaterno);
        buttonDrop(layoutApellidoMaterno, buttonApellidoMaterno, layoutETApellidoMaterno);
        buttonDrop(layoutCorreoElectronico, buttonCorreoElectronico, layoutETCorreoElectronico);

        return view;
    }

    private void getUserName() {
        // Obtener el usuario actualmente autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    private void buttonDrop(View layout, ImageView button, View layoutET) {
        layout.setOnClickListener(v -> {
            if (layoutET.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition((ViewGroup) getView(), new AutoTransition());
                layoutET.setVisibility(View.GONE);
                button.setImageResource(R.drawable.ic_arrow_next_gray_24dp);
            } else {
                TransitionManager.beginDelayedTransition((ViewGroup) getView(), new AutoTransition());
                layoutET.setVisibility(View.VISIBLE);
                button.setImageResource(R.drawable.ic_arrow_back_gray_24dp);
            }
        });
    }
}