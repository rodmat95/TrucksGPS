package com.rodmat95.trucksgps.Main.Dashboard.SlideBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models.GeneralData;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models.PersonalData;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Adapters.UserAdapter;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models.User;
import com.rodmat95.trucksgps.R;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private static final String TAG = "UsersFragment"; // Etiqueta para identificar los logs de este fragmento

    private DatabaseReference mDatabase;
    private UserAdapter mAdapter;
    private RecyclerView mRecycleView;
    private final ArrayList<User> mUsersList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Obtener las referencias de los elementos del layout
        mRecycleView = view.findViewById(R.id.recyclerViewUser);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Establecer el LayoutManager para el RecyclerView
        mRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configurar eventos de clic
        getUsersFromFirebase();

        return view;
    }

    private void getUsersFromFirebase() {
        // Agrega un listener para escuchar los cambios en los datos de los usuarios en la base de datos.
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica si existen datos en la ubicación "Users" en la base de datos.
                if (dataSnapshot.exists()) {
                    // Borra la lista de usuarios existente para evitar duplicados.
                    mUsersList.clear();

                    // Itera sobre cada snapshot de datos de usuario en la base de datos.
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Obtiene los nodos "Datos personales" y "Documentación legal" de cada snapshot.
                        DataSnapshot generalDataSnapshot = ds.child("Datos generales");
                        DataSnapshot personalDataSnapshot = ds.child("Datos personales");

                        // Obtiene los valores específicos del usuario de cada snapshot.
                        String uniqueIdentifier = ds.getKey();

                        // Obtiene los valores específicos del usuario de cada snapshot.
                        String usuario = generalDataSnapshot.child("Usuario").getValue(String.class);
                        String correoElectronico = generalDataSnapshot.child("Correo electrónico").getValue(String.class);
                        String rol = generalDataSnapshot.child("Rol").getValue(String.class);

                        String nombre = personalDataSnapshot.child("Nombre").getValue(String.class);
                        String apellidoPaterno = personalDataSnapshot.child("Apellido paterno").getValue(String.class);
                        String apellidoMaterno = personalDataSnapshot.child("Apellido materno").getValue(String.class);
                        String numero = personalDataSnapshot.child("Número").getValue(String.class);

                        // Crea un objeto GeneralData con los datos obtenidos.
                        GeneralData generalData = new GeneralData();
                        generalData.setUsuario(usuario);
                        generalData.setCorreo(correoElectronico);
                        generalData.setRol(rol);

                        // Crea un objeto PersonalData con los datos obtenidos.
                        PersonalData personalData = new PersonalData();
                        personalData.setNombre(nombre);
                        personalData.setApellidoPaterno(apellidoPaterno);
                        personalData.setApellidoMaterno(apellidoMaterno);
                        personalData.setNumero(numero);


                        // Crea un objeto User con los datos obtenidos y lo agrega a la lista de usuarios.
                        User user = new User(uniqueIdentifier, generalData, personalData);
                        mUsersList.add(user);

                        // Muestra los datos del usuario en los logs para propósitos de depuración.
                        Log.d(TAG, "Datos del usuario: " + user);
                    }

                    // Crea un nuevo adaptador de usuarios con la lista actualizada y lo establece en el RecyclerView.
                    mAdapter = new UserAdapter(mUsersList, R.layout.list_user);
                    mRecycleView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Muestra un mensaje de error si ocurre un problema al cargar los datos de los usuarios desde la base de datos.
                Log.e(TAG, "Error al cargar los datos de los usuarios", databaseError.toException());
                Toast.makeText(getContext(), "Error al cargar los datos de los usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}