package com.rodmat95.trucksgps.Main.Home.Driver;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models.GeneralData;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Models.User;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.Driver;
import com.rodmat95.trucksgps.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewDriverFragment extends Fragment {
    // Declarar las vistas que mostrarán los datos del conductor
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ScrollView scrollView;
    private EditText editTextFullName, editTextIDType, editTextIDNumber;
    private EditText editTextPhoneNumber, editTextDateBirth, editTextResidenceAddress;
    private EditText editTextContactName, editTextContactNumber;
    private EditText editTextLicenseNumber, editTextLicenseClassAndCategory;
    private EditText editTextExpeditionDate, editTextRevalidationDate, editTextSpecialAuthorizations, editTextRestrictions;
    private CardView btnEditDriver, btnSaveDriver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_driver, container, false);

        // Inicializar las vistas
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        scrollView = view.findViewById(R.id.scrollView);

        editTextFullName = view.findViewById(R.id.displayFullName);
        editTextIDType = view.findViewById(R.id.displayIDType);
        editTextIDNumber = view.findViewById(R.id.displayIDNumber);
        editTextPhoneNumber = view.findViewById(R.id.displayPhoneNumber);
        editTextDateBirth = view.findViewById(R.id.displayDateBirth);
        editTextResidenceAddress = view.findViewById(R.id.displayResidenceAddress);
        editTextContactName = view.findViewById(R.id.displayContactName);
        editTextContactNumber = view.findViewById(R.id.displayContactNumber);

        editTextLicenseNumber = view.findViewById(R.id.displayLicenseNumber);
        editTextLicenseClassAndCategory = view.findViewById(R.id.displayLicenseClassAndCategory);
        editTextExpeditionDate = view.findViewById(R.id.displayExpeditionDate);
        editTextRevalidationDate = view.findViewById(R.id.displayRevalidationDate);
        editTextSpecialAuthorizations = view.findViewById(R.id.displaySpecialAuthorizations);
        editTextRestrictions = view.findViewById(R.id.displayRestrictions);

        btnEditDriver = view.findViewById(R.id.cardViewModDriver);
        btnSaveDriver = view.findViewById(R.id.cardViewSaveDriver);

        btnSaveDriver.setVisibility(View.GONE); // Ocultar el botón de guardar al principio

        btnEditDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Habilitar la edición de los EditText
                habilitarEdicion();
                // Cambiar la visibilidad de los botones
                manejarVisibilidadBotones(false);
            }
        });

        btnSaveDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si todos los campos están completos
                if (camposNoVacios()) {
                    // Deshabilitar la edición de los EditText
                    deshabilitarEdicion();
                    // Cambiar la visibilidad de los botones
                    manejarVisibilidadBotones(true);
                    // Llama al método para actualizar los datos del conductor
                    actualizarDatosConductor();
                }
            }
        });

        // Recuperar el objeto Driver pasado desde el fragmento anterior
        Bundle bundle = getArguments();
        if (bundle != null) {
            Driver driver = (Driver) bundle.getSerializable("driver");
            String driverNodePath = bundle.getString("driverNodePath");

            // Verificar si el objeto Driver y la ruta del nodo del conductor son válidos
            if (driver != null && driverNodePath != null) {
                // Obtener la referencia al nodo del conductor en la base de datos
                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child(driverNodePath);

                // Mostrar los datos del conductor en las vistas correspondientes
                editTextFullName.setText(driver.getDatosPersonales().getNombreCompleto());
                editTextIDType.setText(driver.getDatosPersonales().getTipoDocumento());
                editTextIDNumber.setText(driver.getDatosPersonales().getNumeroDocumento());
                editTextPhoneNumber.setText(driver.getDatosPersonales().getNumeroTelefono());
                editTextDateBirth.setText(driver.getDatosPersonales().getFechaNacimiento());
                editTextResidenceAddress.setText(driver.getDatosPersonales().getDireccionResidencia());
                editTextContactName.setText(driver.getDatosPersonales().getNombreContacto());
                editTextContactNumber.setText(driver.getDatosPersonales().getNumeroContacto());

                editTextLicenseNumber.setText(driver.getDocumentacionLegal().getNumeroLicencia());
                editTextLicenseClassAndCategory.setText(driver.getDocumentacionLegal().getClaseLicencia() + " - " + driver.getDocumentacionLegal().getCategoriaLicencia());
                editTextExpeditionDate.setText(driver.getDocumentacionLegal().getFechaExpedicion());
                editTextRevalidationDate.setText(driver.getDocumentacionLegal().getFechaRevalidacion());
                editTextSpecialAuthorizations.setText(driver.getDocumentacionLegal().getAutorizacionesEspeciales());
                editTextRestrictions.setText(driver.getDocumentacionLegal().getRestricciones());
            }
        }

        return view;
    }

    // Método para manejar la visibilidad de los botones
    private void manejarVisibilidadBotones(boolean mostrarEditar) {
        if (mostrarEditar) {
            // Ocultar el botón de guardar y mostrar el botón de editar
            btnSaveDriver.setVisibility(View.GONE);
            btnEditDriver.setVisibility(View.VISIBLE);

            // Restaurar el atributo layout_above del ScrollView
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.cardViewModDriver);
            scrollView.setLayoutParams(params);
        } else {
            // Mostrar el botón de guardar y ocultar el botón de editar
            btnSaveDriver.setVisibility(View.VISIBLE);
            btnEditDriver.setVisibility(View.GONE);

            // Cambiar el atributo layout_above del ScrollView al botón de guardar
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.cardViewSaveDriver);
            scrollView.setLayoutParams(params);
        }
    }

    private void habilitarEdicion() {
        editTextFullName.setEnabled(true);
        editTextIDType.setEnabled(true);
        editTextIDNumber.setEnabled(true);
        editTextPhoneNumber.setEnabled(true);
        editTextDateBirth.setEnabled(true);
        editTextResidenceAddress.setEnabled(true);
        editTextContactName.setEnabled(true);
        editTextContactNumber.setEnabled(true);

        editTextLicenseNumber.setEnabled(true);
        editTextLicenseClassAndCategory.setEnabled(true);
        editTextExpeditionDate.setEnabled(true);
        editTextRevalidationDate.setEnabled(true);
        editTextSpecialAuthorizations.setEnabled(true);
        editTextRestrictions.setEnabled(true);
    }

    private void deshabilitarEdicion() {
        editTextFullName.setEnabled(false);
        editTextIDType.setEnabled(false);
        editTextIDNumber.setEnabled(false);
        editTextPhoneNumber.setEnabled(false);
        editTextDateBirth.setEnabled(false);
        editTextResidenceAddress.setEnabled(false);
        editTextContactName.setEnabled(false);
        editTextContactNumber.setEnabled(false);

        editTextLicenseNumber.setEnabled(false);
        editTextLicenseClassAndCategory.setEnabled(false);
        editTextExpeditionDate.setEnabled(false);
        editTextRevalidationDate.setEnabled(false);
        editTextSpecialAuthorizations.setEnabled(false);
        editTextRestrictions.setEnabled(false);
    }

    // Método para validar que los campos no están vacíos
    private boolean camposNoVacios() {
        // Verificar cada campo individualmente
        if (TextUtils.isEmpty(editTextFullName.getText().toString())
                || TextUtils.isEmpty(editTextIDType.getText().toString())
                || TextUtils.isEmpty(editTextIDNumber.getText().toString())
                || TextUtils.isEmpty(editTextPhoneNumber.getText().toString())
                || TextUtils.isEmpty(editTextDateBirth.getText().toString())
                || TextUtils.isEmpty(editTextResidenceAddress.getText().toString())
                || TextUtils.isEmpty(editTextContactName.getText().toString())
                || TextUtils.isEmpty(editTextContactNumber.getText().toString())

                || TextUtils.isEmpty(editTextLicenseNumber.getText().toString())
                || TextUtils.isEmpty(editTextLicenseClassAndCategory.getText().toString())
                || TextUtils.isEmpty(editTextExpeditionDate.getText().toString())
                || TextUtils.isEmpty(editTextRevalidationDate.getText().toString())
                || TextUtils.isEmpty(editTextSpecialAuthorizations.getText().toString())
                || TextUtils.isEmpty(editTextRestrictions.getText().toString())) {
            // Si algún campo está vacío, mostrar un mensaje de error y devolver false
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para verificar si se realizaron cambios en los datos del conductor
    private boolean cambiosModificados(Driver driver) {
        // Comparar los valores actuales con los originales
        if (!editTextFullName.getText().toString().equals(driver.getDatosPersonales().getNombreCompleto())
                || !editTextIDType.getText().toString().equals(driver.getDatosPersonales().getTipoDocumento())
                || !editTextIDNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroDocumento())
                || !editTextPhoneNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroTelefono())
                || !editTextDateBirth.getText().toString().equals(driver.getDatosPersonales().getFechaNacimiento())
                || !editTextResidenceAddress.getText().toString().equals(driver.getDatosPersonales().getDireccionResidencia())
                || !editTextContactName.getText().toString().equals(driver.getDatosPersonales().getNombreContacto())
                || !editTextContactNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroContacto())

                || !editTextLicenseNumber.getText().toString().equals(driver.getDocumentacionLegal().getNumeroLicencia())
                || !editTextLicenseClassAndCategory.getText().toString().equals(driver.getDocumentacionLegal().getClaseLicencia() + " - " + driver.getDocumentacionLegal().getCategoriaLicencia())
                || !editTextExpeditionDate.getText().toString().equals(driver.getDocumentacionLegal().getFechaExpedicion())
                || !editTextRevalidationDate.getText().toString().equals(driver.getDocumentacionLegal().getFechaRevalidacion())
                || !editTextSpecialAuthorizations.getText().toString().equals(driver.getDocumentacionLegal().getAutorizacionesEspeciales())
                || !editTextRestrictions.getText().toString().equals(driver.getDocumentacionLegal().getRestricciones())
        ) {
            return true;
        } else {
            return false;
        }
    }

    // Actualizar los datos del conductor en la base de datos
    private void actualizarDatosConductor() {
        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Conductores");

        // Obtener el objeto Driver del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            Driver driver = (Driver) bundle.getSerializable("driver");
            if (driver != null) {
                // Obtener el identificador único del conductor
                String uniqueIdentifier = driver.getUniqueIdentifier();
                Log.d(TAG, "Unique Identifier del conductor: " + uniqueIdentifier);

                // Verificar si se realizaron cambios en los datos del camión
                if (cambiosModificados(driver)) {
                    // Crear un mapa para almacenar los datos actualizados
                    Map<String, Object> updates = new HashMap<>();

                    // Agregar los campos que deseas actualizar y sus nuevos valores al mapa
                    String atributo = ""; // Atributo modificado
                    String atributoValorAnterior = ""; // Valor anterior del atributo
                    String atributoValorNuevo = ""; // Nuevo valor del atributo

                    // Dividir el texto del EditText en marca y modelo
                    String brandAndModel = editTextLicenseClassAndCategory.getText().toString();
                    String[] parts = brandAndModel.split(" - ");

                    // Agregar los campos que deseas actualizar y sus nuevos valores al mapa
                    if (!editTextFullName.getText().toString().equals(driver.getDatosPersonales().getNombreCompleto())) {
                        atributo = "Nombre completo";
                        atributoValorAnterior = driver.getDatosPersonales().getNombreCompleto();
                        atributoValorNuevo = editTextFullName.getText().toString();
                        updates.put("Datos personales/Nombre completo", atributoValorNuevo);
                    }
                    if (!editTextIDType.getText().toString().equals(driver.getDatosPersonales().getTipoDocumento())) {
                        atributo = "Tipo de documento";
                        atributoValorAnterior = driver.getDatosPersonales().getTipoDocumento();
                        atributoValorNuevo = editTextIDType.getText().toString();
                        updates.put("Datos personales/Tipo de documento", atributoValorNuevo);
                    }
                    if (!editTextIDNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroDocumento())) {
                        atributo = "Número de documento";
                        atributoValorAnterior = driver.getDatosPersonales().getNumeroDocumento();
                        atributoValorNuevo = editTextIDNumber.getText().toString();
                        updates.put("Datos personales/Número de documento", atributoValorNuevo);
                    }
                    if (!editTextPhoneNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroTelefono())) {
                        atributo = "Número de teléfono";
                        atributoValorAnterior = driver.getDatosPersonales().getNumeroTelefono();
                        atributoValorNuevo = editTextPhoneNumber.getText().toString();
                        updates.put("Datos personales/Número de teléfono", atributoValorNuevo);
                    }
                    if (!editTextDateBirth.getText().toString().equals(driver.getDatosPersonales().getFechaNacimiento())) {
                        atributo = "Fecha de nacimiento";
                        atributoValorAnterior = driver.getDatosPersonales().getFechaNacimiento();
                        atributoValorNuevo = editTextDateBirth.getText().toString();
                        updates.put("Datos personales/Fecha de nacimiento", atributoValorNuevo);
                    }
                    if (!editTextResidenceAddress.getText().toString().equals(driver.getDatosPersonales().getDireccionResidencia())) {
                        atributo = "Dirección de residencia";
                        atributoValorAnterior = driver.getDatosPersonales().getDireccionResidencia();
                        atributoValorNuevo = editTextResidenceAddress.getText().toString();
                        updates.put("Datos personales/Dirección de residencia", atributoValorNuevo);
                    }
                    if (!editTextContactName.getText().toString().equals(driver.getDatosPersonales().getNombreContacto())) {
                        atributo = "Nombre del contacto";
                        atributoValorAnterior = driver.getDatosPersonales().getNombreContacto();
                        atributoValorNuevo = editTextContactName.getText().toString();
                        updates.put("Datos personales/Nombre del contacto", atributoValorNuevo);
                    }
                    if (!editTextContactNumber.getText().toString().equals(driver.getDatosPersonales().getNumeroContacto())) {
                        atributo = "Número del contacto";
                        atributoValorAnterior = driver.getDatosPersonales().getNumeroContacto();
                        atributoValorNuevo = editTextContactNumber.getText().toString();
                        updates.put("Datos personales/Número del contacto", atributoValorNuevo);
                    }

                    if (!editTextLicenseNumber.getText().toString().equals(driver.getDocumentacionLegal().getNumeroLicencia())) {
                        atributo = "Numero de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getNumeroLicencia();
                        atributoValorNuevo = editTextLicenseNumber.getText().toString();
                        updates.put("Documentación legal/Numero de licencia", atributoValorNuevo);
                    }
                    if (!parts[0].equals(driver.getDocumentacionLegal().getClaseLicencia())) {
                        atributo = "Clase de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getClaseLicencia();
                        atributoValorNuevo = parts[0];
                        updates.put("Documentación legal/Clase de licencia", atributoValorNuevo);
                    }
                    if (!parts[1].equals(driver.getDocumentacionLegal().getCategoriaLicencia())) {
                        atributo = "Categoría de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getCategoriaLicencia();
                        atributoValorNuevo = parts[1];
                        updates.put("Documentación legal/Categoría de licencia", atributoValorNuevo);
                    }
                    if (!editTextExpeditionDate.getText().toString().equals(driver.getDocumentacionLegal().getFechaExpedicion())) {
                        atributo = "Fecha de expedición de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getFechaExpedicion();
                        atributoValorNuevo = editTextExpeditionDate.getText().toString();
                        updates.put("Documentación legal/Fecha de expedición de licencia", atributoValorNuevo);
                    }
                    if (!editTextRevalidationDate.getText().toString().equals(driver.getDocumentacionLegal().getFechaRevalidacion())) {
                        atributo = "Fecha de revalidación de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getFechaRevalidacion();
                        atributoValorNuevo = editTextRevalidationDate.getText().toString();
                        updates.put("Documentación legal/Fecha de revalidación de licencia", atributoValorNuevo);
                    }
                    if (!editTextSpecialAuthorizations.getText().toString().equals(driver.getDocumentacionLegal().getAutorizacionesEspeciales())) {
                        atributo = "Autorizaciones especiales de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getAutorizacionesEspeciales();
                        atributoValorNuevo = editTextSpecialAuthorizations.getText().toString();
                        updates.put("Documentación legal/Autorizaciones especiales de licencia", atributoValorNuevo);
                    }
                    if (!editTextRestrictions.getText().toString().equals(driver.getDocumentacionLegal().getRestricciones())) {
                        atributo = "Restricciones de licencia";
                        atributoValorAnterior = driver.getDocumentacionLegal().getRestricciones();
                        atributoValorNuevo = editTextRestrictions.getText().toString();
                        updates.put("Documentación legal/Restricciones de licencia", atributoValorNuevo);
                    }

                    // Actualizar los valores en la base de datos utilizando la referencia del conductor y el mapa de actualización
                    try {
                        final String finalAtributo = atributo;
                        final String finalAtributoValorAnterior = atributoValorAnterior;
                        final String finalAtributoValorNuevo = atributoValorNuevo;

                        databaseReference.child(uniqueIdentifier).updateChildren(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // La actualización se realizó exitosamente
                                        Toast.makeText(getContext(), "Los datos del conductor se han actualizado correctamente", Toast.LENGTH_SHORT).show();

                                        // Generar la notificación
                                        generarNotificacion(driver, finalAtributo, finalAtributoValorAnterior, finalAtributoValorNuevo, uniqueIdentifier);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // La actualización falló
                                        Toast.makeText(getContext(), "Error al actualizar los datos del conductor", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error al actualizar los datos del conductor", e);
                                    }
                                });
                    } catch (Exception e) {
                        // Manejar cualquier excepción que pueda ocurrir durante la actualización de los datos del conductor
                        Toast.makeText(getContext(), "Error al actualizar los datos del conductor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al actualizar los datos del conductor", e);
                    }
                }
            }
        }
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Método para generar una notificación de modificación de conductor
    private void generarNotificacion(Driver driver, String finalAtributo, String finalAtributoValorAnterior, String finalAtributoValorNuevo, String uniqueIdentifier) {
        // Obtener una referencia a la base de datos de Firebase para las notificaciones
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notificaciones");

        // Generar un identificador único para la notificación
        String notificationKey = notificationsRef.push().getKey();

        // Obtiene el usuario actualmente autenticado
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "Usuario actualmente autenticado: " + user);

        if (user != null) {
            // UID del usuario autenticado
            String uid = user.getUid();
            Log.d(TAG, "UID del usuario autenticado: " + uid);

            // Referencia al nodo del usuario en la base de datos
            DatabaseReference usuarioRef = mDatabase.child("usuarios").child(uid);
            Log.d(TAG, "Referencia al nodo del usuario: " + usuarioRef);

            // Escucha los cambios en los datos del usuario
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "DataSnapshot: " + dataSnapshot.exists());

                    if (dataSnapshot.exists()) {
                        // Obtén los datos del usuario y sus datos generales
                        User usuario = dataSnapshot.getValue(User.class);
                        GeneralData datosGenerales = usuario.getDatosGenerales();

                        String usuarioUsuario = datosGenerales.getUsuario();
                        Log.d(TAG, "Usuario: " + usuarioUsuario);

                        // Crear un mapa para almacenar los valores de la notificación
                        Map<String, Object> notificationValues = new HashMap<>();
                        notificationValues.put("Usuario", usuarioUsuario);
                        notificationValues.put("Acción", "modificó");
                        notificationValues.put("Objeto", "Conductor");
                        notificationValues.put("Distintivo", "Nombre completo");
                        notificationValues.put("DistintivoValor", driver.getDatosPersonales().getNombreCompleto());
                        notificationValues.put("Atributo", finalAtributo);
                        notificationValues.put("AtributoValorAnterior", finalAtributoValorAnterior);
                        notificationValues.put("AtributoValorNuevo", finalAtributoValorNuevo);
                        notificationValues.put("Fecha", getCurrentTimestamp());
                        notificationValues.put("IDU", uniqueIdentifier);

                        // Guardar la notificación en la base de datos de Firebase
                        if (notificationKey != null) {
                            notificationsRef.child(notificationKey).setValue(notificationValues)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // La notificación se guardó exitosamente
                                            Log.d(TAG, "Notificación de modificación de conductor guardada exitosamente");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // La notificación falló al gua rdarse
                                            Log.e(TAG, "Error al guardar la notificación de modificación de conductor", e);
                                        }
                                    });
                        }
                    } else {
                        Log.e(TAG, "La instantánea de datos no existe");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error de la base de datos
                    Log.e(TAG, "Error al obtener el usuario de la base de datos", databaseError.toException());
                }
            });
        } else {
            Log.e(TAG, "No hay usuario autenticado actualmente");
        }
    }
}