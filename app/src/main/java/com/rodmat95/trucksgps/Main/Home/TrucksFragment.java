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
import com.rodmat95.trucksgps.Main.Home.Truck.Adapters.TruckAdapter;
import com.rodmat95.trucksgps.Main.Home.Truck.AddTruckFragment;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Details;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Information;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.LegalDocumentation;
import com.rodmat95.trucksgps.R;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Truck;

import java.util.ArrayList;

public class TrucksFragment extends Fragment {
    private static final String TAG = "TrucksFragment"; // Etiqueta para identificar los logs de este fragmento

    private CardView cardViewAddTruck;
    private DatabaseReference mDatabase;
    private TruckAdapter mAdapter;
    private RecyclerView mRecycleView;
    private final ArrayList<Truck> mTrucksList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trucks, container, false);

        // Obtener las referencias de los elementos del layout
        cardViewAddTruck = view.findViewById(R.id.cardViewAddTruck);
        mRecycleView = view.findViewById(R.id.recyclerViewTruck);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Establecer el LayoutManager para el RecyclerView
        mRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Configurar eventos de clic
        getTrucksFromFirebase();
        addTruck();

        return view;
    }

    private void getTrucksFromFirebase() {
        // Agrega un listener para escuchar los cambios en los datos de los camiones en la base de datos.
        mDatabase.child("Camiones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica si existen datos en la ubicación "Trucks" en la base de datos.
                if (dataSnapshot.exists()) {
                    // Borra la lista de camiones existente para evitar duplicados.
                    mTrucksList.clear();

                    // Itera sobre cada snapshot de datos de camión en la base de datos.
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Obtiene el nodo "Información", "Documentación Legal" y "Detalles" de cada snapshot.
                        DataSnapshot informationSnapshot = ds.child("Información");
                        DataSnapshot legalDocumentationSnapshot = ds.child("Documentación legal");
                        DataSnapshot detailsSnapshot = ds.child("Detalles");

                        // Obtiene los valores específicos del camión de cada snapshot.
                        String uniqueIdentifier = ds.getKey();

                        String numeroPlaca = informationSnapshot.child("Número de placa").getValue(String.class);
                        String marca = informationSnapshot.child("Marca").getValue(String.class);
                        String modelo = informationSnapshot.child("Modelo").getValue(String.class);
                        String anioProduccion = informationSnapshot.child("Año de producción").getValue(String.class);
                        String numeroMotor = informationSnapshot.child("Número de motor").getValue(String.class);
                        String serieChasis = informationSnapshot.child("Serie del chasis").getValue(String.class);
                        String tipoCombustible = informationSnapshot.child("Tipo de combustible").getValue(String.class);

                        String companiaAseguradora = legalDocumentationSnapshot.child("Compañía aseguradora").getValue(String.class);
                        String numeroSOAT = legalDocumentationSnapshot.child("Número de SOAT").getValue(String.class);
                        String fechaEmisionSOAT = legalDocumentationSnapshot.child("Fecha de emisión del SOAT").getValue(String.class);
                        String fechaVencimientoSOAT = legalDocumentationSnapshot.child("Fecha de vencimiento del SOAT").getValue(String.class);
                        String titularSeguro = legalDocumentationSnapshot.child("Titular del seguro").getValue(String.class);
                        String dniTitular = legalDocumentationSnapshot.child("DNI del titular").getValue(String.class);
                        String numeroRevisionTecnica = legalDocumentationSnapshot.child("Número de Revisión Técnica").getValue(String.class);
                        String fechaEmisionRevisionTecnica = legalDocumentationSnapshot.child("Fecha de emisión de la revisión técnica").getValue(String.class);
                        String fechaVencimientoRevisionTecnica = legalDocumentationSnapshot.child("Fecha de vencimiento de la revisión técnica").getValue(String.class);

                        String fechaCompraVehiculo = detailsSnapshot.child("Fecha de compra del vehículo").getValue(String.class);
                        String valorCompraVehiculo = detailsSnapshot.child("Valor de compra del vehículo").getValue(String.class);
                        String colorVehiculo = detailsSnapshot.child("Color del vehículo").getValue(String.class);
                        String pesoVehiculo = detailsSnapshot.child("Peso del vehículo").getValue(String.class);
                        String longitudVehiculo = detailsSnapshot.child("Longitud del vehículo").getValue(String.class);
                        String anchuraVehiculo = detailsSnapshot.child("Anchura del vehículo").getValue(String.class);
                        String alturaVehiculo = detailsSnapshot.child("Altura del vehículo").getValue(String.class);

                        // Crea un objeto Information con los datos obtenidos.
                        Information information = new Information();
                        LegalDocumentation legalDocumentation = new LegalDocumentation();
                        Details details = new Details();

                        information.setNumeroPlaca(numeroPlaca);
                        information.setMarca(marca);
                        information.setModelo(modelo);
                        information.setAnioProduccion(anioProduccion);
                        information.setNumeroMotor(numeroMotor);
                        information.setSerieChasis(serieChasis);
                        information.setTipoCombustible(tipoCombustible);

                        legalDocumentation.setCompaniaAseguradora(companiaAseguradora);
                        legalDocumentation.setNumeroSOAT(numeroSOAT);
                        legalDocumentation.setFechaEmisionSOAT(fechaEmisionSOAT);
                        legalDocumentation.setFechaVencimientoSOAT(fechaVencimientoSOAT);
                        legalDocumentation.setTitularSeguro(titularSeguro);
                        legalDocumentation.setDniTitular(dniTitular);
                        legalDocumentation.setNumeroRevisionTecnica(numeroRevisionTecnica);
                        legalDocumentation.setFechaEmisionRevisionTecnica(fechaEmisionRevisionTecnica);
                        legalDocumentation.setFechaVencimientoRevisionTecnica(fechaVencimientoRevisionTecnica);

                        details.setFechaCompraVehiculo(fechaCompraVehiculo);
                        details.setValorCompraVehiculo(valorCompraVehiculo);
                        details.setColorVehiculo(colorVehiculo);
                        details.setPesoVehiculo(pesoVehiculo);
                        details.setLongitudVehiculo(longitudVehiculo);
                        details.setAnchuraVehiculo(anchuraVehiculo);
                        details.setAlturaVehiculo(alturaVehiculo);

                        // Crea un objeto Truck con los datos obtenidos y lo agrega a la lista de camiones.
                        Truck truck = new Truck(uniqueIdentifier, information, legalDocumentation, details);
                        mTrucksList.add(truck);

                        // Muestra los datos del camión en los logs para propósitos de depuración.
                        Log.d(TAG, "Datos del camión: " + truck);
                    }

                    // Crea un nuevo adaptador de camiones con la lista actualizada y lo establece en el RecyclerView.
                    mAdapter = new TruckAdapter(mTrucksList, R.layout.list_truck, mDatabase);
                    mRecycleView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Muestra un mensaje de error si ocurre un problema al cargar los datos de los camiones desde la base de datos.
                Log.e(TAG, "Error al cargar los datos de los camiones", databaseError.toException());
                Toast.makeText(getContext(), "Error al cargar los datos de los camiones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTruck() {
        cardViewAddTruck.setOnClickListener(v -> {
            // Crear e inicializar el fragmento AddTruckFragment
            AddTruckFragment addTruckFragment = new AddTruckFragment();

            // Utilizar FragmentTransaction para reemplazar el fragmento actual con AddTruckFragment
            FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, addTruckFragment).addToBackStack(null).commit();
        });
    }
}