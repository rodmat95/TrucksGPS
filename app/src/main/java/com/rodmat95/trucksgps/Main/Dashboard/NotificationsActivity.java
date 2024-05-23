package com.rodmat95.trucksgps.Main.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.Main.Dashboard.Notification.Adapters.NotificationAdapter;
import com.rodmat95.trucksgps.Main.Dashboard.Notification.Models.Notification;
import com.rodmat95.trucksgps.R;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {
    private static final String TAG = "NotificationsActivity"; // Etiqueta para identificar los logs de esta actividad

    private DatabaseReference mDatabase;
    private NotificationAdapter mAdapter;
    private RecyclerView mRecycleView;
    private final ArrayList<Notification> mNotificationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Obtener las referencias de los elementos del layout
        mRecycleView = findViewById(R.id.recyclerViewNotification);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Establecer el LayoutManager para el RecyclerView
        mAdapter = new NotificationAdapter(mNotificationsList, R.layout.list_notification);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        // Asignar el adaptador al RecyclerView
        mRecycleView.setAdapter(mAdapter);

        // Configurar eventos de clic
        getNotificationsFromFirebase();
    }

    private void getNotificationsFromFirebase() {
        // Agrega un listener para escuchar los cambios en los datos de las notificaciones en la base de datos.
        mDatabase.child("Notificaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica si existen datos en la ubicación "Notifications" en la base de datos.
                if (dataSnapshot.exists()) {
                    // Borra la lista de notificaciones existente para evitar duplicados.
                    mNotificationsList.clear();

                    // Itera sobre cada snapshot de datos de notificaciones en la base de datos.
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Obtiene los valores específicos de la notificación de cada snapshot.
                        String uniqueIdentifier = ds.getKey();
                        String usuario = ds.child("Usuario").getValue(String.class);
                        String accion = ds.child("Acción").getValue(String.class);
                        String atributo = ds.child("Atributo").getValue(String.class);
                        String objeto = ds.child("Objeto").getValue(String.class);
                        String distintivo = ds.child("Distintivo").getValue(String.class);
                        String distintivoValor = ds.child("DistintivoValor").getValue(String.class);
                        String atributoValorAnterior = ds.child("AtributoValorAnterior").getValue(String.class);
                        String atributoValorNuevo = ds.child("AtributoValorNuevo").getValue(String.class);
                        String fecha = ds.child("Fecha").getValue(String.class);
                        String idu = ds.child("IDU").getValue(String.class);

                        // Crea un objeto Notification con los datos obtenidos y lo agrega a la lista de notificaciones.
                        Notification notification = new Notification(uniqueIdentifier, usuario, accion, atributo,
                                objeto, distintivo, distintivoValor, atributoValorAnterior, atributoValorNuevo,
                                fecha, idu);
                        mNotificationsList.add(notification);

                        // Muestra los datos de la notificación en los logs para propósitos de depuración.
                        Log.d(TAG, "Datos de la notificación: " + notification);
                    }

                    // Notifica al adaptador de que los datos han cambiado.
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Muestra un mensaje de error si ocurre un problema al cargar los datos de las notificaciones desde la base de datos.
                Log.e(TAG, "Error al cargar los datos de las notificaciones", databaseError.toException());
                Toast.makeText(NotificationsActivity.this, "Error al cargar los datos de las notificaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}