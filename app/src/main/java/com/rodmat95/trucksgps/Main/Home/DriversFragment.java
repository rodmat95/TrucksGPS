package com.rodmat95.trucksgps.Main.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.Main.Home.Driver.AddDriverFragment;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.LegalDocumentation;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.PersonalData;
import com.rodmat95.trucksgps.Main.Home.Driver.Adapters.DriverAdapter;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.Driver;
import com.rodmat95.trucksgps.R;

import java.util.ArrayList;

public class DriversFragment extends Fragment {
    private static final String TAG = "DriversFragment"; // Etiqueta para identificar los logs de este fragmento

    private CardView cardViewAddDriver;
    private DatabaseReference mDatabase;
    private DriverAdapter mAdapter;
    private RecyclerView mRecycleView;
    private final ArrayList<Driver> mDriversList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drivers, container, false);

        // Obtener las referencias de los elementos del layout
        cardViewAddDriver = view.findViewById(R.id.cardViewAddDriver);
        mRecycleView = view.findViewById(R.id.recyclerViewDriver);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Establecer el LayoutManager para el RecyclerView
        mRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configurar eventos de clic
        getDriversFromFirebase();
        addDriver();

        return view;
    }

    private void getDriversFromFirebase() {
        // Agrega un listener para escuchar los cambios en los datos de los conductores en la base de datos.
        mDatabase.child("Conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica si existen datos en la ubicación "Drivers" en la base de datos.
                if (dataSnapshot.exists()) {
                    // Borra la lista de conductores existente para evitar duplicados.
                    mDriversList.clear();

                    // Itera sobre cada snapshot de datos de conductor en la base de datos.
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Obtiene los nodos "Datos personales" y "Documentación legal" de cada snapshot.
                        DataSnapshot personalDataSnapshot = ds.child("Datos personales");
                        DataSnapshot legalDocumentationSnapshot = ds.child("Documentación legal");

                        // Obtiene los valores específicos del conductor de cada snapshot.
                        String uniqueIdentifier = ds.getKey();

                        // Obtiene los valores específicos del conductor de cada snapshot.
                        String nombreCompleto = personalDataSnapshot.child("Nombre completo").getValue(String.class);
                        String tipoDocumento = personalDataSnapshot.child("Tipo de documento").getValue(String.class);
                        String numeroDocumento = personalDataSnapshot.child("Número de documento").getValue(String.class);
                        String numeroTelefono = personalDataSnapshot.child("Número de teléfono").getValue(String.class);
                        String fechaNacimiento = personalDataSnapshot.child("Fecha de nacimiento").getValue(String.class);
                        String direccionResidencia = personalDataSnapshot.child("Dirección de residencia").getValue(String.class);
                        String nombreContacto = personalDataSnapshot.child("Nombre del contacto").getValue(String.class);
                        String numeroContacto = personalDataSnapshot.child("Número del contacto").getValue(String.class);

                        String numeroLicencia = legalDocumentationSnapshot.child("Numero de licencia").getValue(String.class);
                        String claseLicencia = legalDocumentationSnapshot.child("Clase de licencia").getValue(String.class);
                        String categoriaLicencia = legalDocumentationSnapshot.child("Categoría de licencia").getValue(String.class);
                        String fechaExpedicion = legalDocumentationSnapshot.child("Fecha de expedición de licencia").getValue(String.class);
                        String fechaRevalidacion = legalDocumentationSnapshot.child("Fecha de revalidación de licencia").getValue(String.class);
                        String autorizacionesEspeciales = legalDocumentationSnapshot.child("Autorizaciones especiales de licencia").getValue(String.class);
                        String restricciones = legalDocumentationSnapshot.child("Restricciones de licencia").getValue(String.class);

                        // Crea un objeto PersonalData con los datos obtenidos.
                        PersonalData personalData = new PersonalData();
                        LegalDocumentation legalDocumentation = new LegalDocumentation();

                        personalData.setNombreCompleto(nombreCompleto);
                        personalData.setTipoDocumento(tipoDocumento);
                        personalData.setNumeroDocumento(numeroDocumento);
                        personalData.setNumeroTelefono(numeroTelefono);
                        personalData.setFechaNacimiento(fechaNacimiento);
                        personalData.setDireccionResidencia(direccionResidencia);
                        personalData.setNombreContacto(nombreContacto);
                        personalData.setNumeroContacto(numeroContacto);

                        legalDocumentation.setNumeroLicencia(numeroLicencia);
                        legalDocumentation.setClaseLicencia(claseLicencia);
                        legalDocumentation.setCategoriaLicencia(categoriaLicencia);
                        legalDocumentation.setFechaExpedicion(fechaExpedicion);
                        legalDocumentation.setFechaRevalidacion(fechaRevalidacion);
                        legalDocumentation.setAutorizacionesEspeciales(autorizacionesEspeciales);
                        legalDocumentation.setRestricciones(restricciones);

                        // Crea un objeto Driver con los datos obtenidos y lo agrega a la lista de conductores.
                        Driver driver = new Driver(uniqueIdentifier, personalData, legalDocumentation);
                        mDriversList.add(driver);

                        // Muestra los datos del conductor en los logs para propósitos de depuración.
                        Log.d(TAG, "Datos del conductor: " + driver);
                    }

                    // Crea un nuevo adaptador de conductores con la lista actualizada y lo establece en el RecyclerView.
                    mAdapter = new DriverAdapter(mDriversList, R.layout.list_driver);
                    mRecycleView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Muestra un mensaje de error si ocurre un problema al cargar los datos de los conductores desde la base de datos.
                Log.e(TAG, "Error al cargar los datos de los conductores", databaseError.toException());
                Toast.makeText(getContext(), "Error al cargar los datos de los conductores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDriver() {
        cardViewAddDriver.setOnClickListener(v -> {
            // Crear e inicializar el fragmento AddDriverFragment
            AddDriverFragment addDriverFragment = new AddDriverFragment();

            // Utilizar FragmentTransaction para reemplazar el fragmento actual con AddDriverFragment
            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, addDriverFragment).addToBackStack(null).commit();
        });
    }
}