package com.rodmat95.trucksgps.Main.Home.Truck;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.R.attr;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodmat95.trucksgps.R;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Truck;

import java.util.HashMap;
import java.util.Map;

public class ViewTruckFragment extends Fragment {
    // Declarar las vistas que mostrarán los datos del camión
    private int colorOnPrimary, colorOnSecondary;
    private ScrollView scrollView;
    private EditText editTextBrandAndModel, editTextPlateNumber;
    private EditText editTextYearProduction, editTextEngineNumber, editTextChassisSerial;
    private EditText editTextFuelType, editTextInsuranceCompany, editTextInsuranceHolder;
    private EditText editTextHolderDNI, editTextSOATNumber, editTextSOATIssueDate;
    private EditText editTextSOATDueDate, editTextRevisionNumber, editTextRevisionIssueDate;
    private EditText editTextRevisionDueDate, editTextPurchaseDate, editTextPurchaseValue;
    private EditText editTextColor, editTextWeight, editTextLength, editTextWidth, editTextHeight;
    private CardView btnEditTruck, btnSaveTruck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_truck, container, false);

        // Obtener los colores del tema
        colorOnPrimary = getColorFromTheme(com.google.android.material.R.attr.colorOnPrimary);
        colorOnSecondary = getColorFromTheme(com.google.android.material.R.attr.colorOnSecondary);

        // Inicializar las vistas
        scrollView = view.findViewById(R.id.scrollView);
        
        editTextBrandAndModel = view.findViewById(R.id.displayBrandAndModel);
        editTextPlateNumber = view.findViewById(R.id.displayPlateNumber);
        editTextYearProduction = view.findViewById(R.id.displayYearProduction);
        editTextEngineNumber = view.findViewById(R.id.displayEngineNumber);
        editTextChassisSerial = view.findViewById(R.id.displayChassisSerial);
        editTextFuelType = view.findViewById(R.id.displayFuelType);

        editTextInsuranceCompany = view.findViewById(R.id.displayInsuranceCompany);
        editTextInsuranceHolder = view.findViewById(R.id.displayInsuranceHolder);
        editTextHolderDNI = view.findViewById(R.id.displayHolderDNI);
        editTextSOATNumber = view.findViewById(R.id.displaySOATNumber);
        editTextSOATIssueDate = view.findViewById(R.id.displaySOATIssueDate);
        editTextSOATDueDate = view.findViewById(R.id.displaySOATDueDate);
        editTextRevisionNumber = view.findViewById(R.id.displayRevisionNumber);
        editTextRevisionIssueDate = view.findViewById(R.id.displayRevisionIssueDate);
        editTextRevisionDueDate = view.findViewById(R.id.displayRevisionDueDate);

        editTextPurchaseDate = view.findViewById(R.id.displayPurchaseDate);
        editTextPurchaseValue = view.findViewById(R.id.displayPurchaseValue);
        editTextColor = view.findViewById(R.id.displayColor);
        editTextWeight = view.findViewById(R.id.displayWeight);
        editTextLength = view.findViewById(R.id.displayLength);
        editTextWidth = view.findViewById(R.id.displayWidth);
        editTextHeight = view.findViewById(R.id.displayHeight);

        btnEditTruck = view.findViewById(R.id.cardViewModTruck);
        btnSaveTruck = view.findViewById(R.id.cardViewSaveTruck);

        btnSaveTruck.setVisibility(View.GONE); // Ocultar el botón de guardar al principio

        btnEditTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Habilitar la edición de los EditText
                habilitarEdicion();
                // Cambiar la visibilidad de los botones
                manejarVisibilidadBotones(false);
            }
        });

        btnSaveTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si todos los campos están completos
                if (camposNoVacios()) {
                    // Deshabilitar la edición de los EditText
                    deshabilitarEdicion();
                    // Cambiar la visibilidad de los botones
                    manejarVisibilidadBotones(true);
                    // Llama al método para actualizar los datos del camión
                    actualizarDatosCamion();
                }
            }
        });

        // Recuperar el objeto Truck pasado desde el fragmento anterior
        Bundle bundle = getArguments();
        if (bundle != null) {
            Truck truck = (Truck) bundle.getSerializable("truck");
            String truckNodePath = bundle.getString("truckNodePath");

            // Verificar si el objeto Truck y la ruta del nodo del camión son válidos
            if (truck != null && truckNodePath != null) {
                // Obtener la referencia al nodo del camión en la base de datos
                DatabaseReference truckRef = FirebaseDatabase.getInstance().getReference().child(truckNodePath);

                // Mostrar los datos del camión en las vistas correspondientes
                editTextBrandAndModel.setText(truck.getInformacion().getMarca() + " / " + truck.getInformacion().getModelo());
                editTextPlateNumber.setText(truck.getInformacion().getNumeroPlaca());
                editTextYearProduction.setText(truck.getInformacion().getAnioProduccion());
                editTextEngineNumber.setText(truck.getInformacion().getNumeroMotor());
                editTextChassisSerial.setText(truck.getInformacion().getSerieChasis());
                editTextFuelType.setText(truck.getInformacion().getTipoCombustible());

                editTextInsuranceCompany.setText(truck.getDocumentacionLegal().getCompaniaAseguradora());
                editTextInsuranceHolder.setText(truck.getDocumentacionLegal().getTitularSeguro());
                editTextHolderDNI.setText(truck.getDocumentacionLegal().getDniTitular());
                editTextSOATNumber.setText(truck.getDocumentacionLegal().getNumeroSOAT());
                editTextSOATIssueDate.setText(truck.getDocumentacionLegal().getFechaEmisionSOAT());
                editTextSOATDueDate.setText(truck.getDocumentacionLegal().getFechaVencimientoSOAT());
                editTextRevisionNumber.setText(truck.getDocumentacionLegal().getNumeroRevisionTecnica());
                editTextRevisionIssueDate.setText(truck.getDocumentacionLegal().getFechaEmisionRevisionTecnica());
                editTextRevisionDueDate.setText(truck.getDocumentacionLegal().getFechaVencimientoRevisionTecnica());

                editTextPurchaseDate.setText(truck.getDetalles().getFechaCompraVehiculo());
                editTextPurchaseValue.setText(truck.getDetalles().getValorCompraVehiculo());
                editTextColor.setText(truck.getDetalles().getColorVehiculo());
                editTextWeight.setText(truck.getDetalles().getPesoVehiculo());
                editTextLength.setText(truck.getDetalles().getLongitudVehiculo());
                editTextWidth.setText(truck.getDetalles().getAnchuraVehiculo());
                editTextHeight.setText(truck.getDetalles().getAlturaVehiculo());
            }
        }

        return view;
    }

    // Método para obtener los colores definidos en el tema de la actividad
    private int getColorFromTheme(int colorAttribute) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(colorAttribute, typedValue, true);
        return typedValue.data;
    }

    // Método para manejar la visibilidad de los botones
    private void manejarVisibilidadBotones(boolean mostrarEditar) {
        if (mostrarEditar) {
            // Ocultar el botón de guardar y mostrar el botón de editar
            btnSaveTruck.setVisibility(View.GONE);
            btnEditTruck.setVisibility(View.VISIBLE);

            // Restaurar el atributo layout_above del ScrollView
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.cardViewModTruck);
            scrollView.setLayoutParams(params);
        } else {
            // Mostrar el botón de guardar y ocultar el botón de editar
            btnSaveTruck.setVisibility(View.VISIBLE);
            btnEditTruck.setVisibility(View.GONE);

            // Cambiar el atributo layout_above del ScrollView al botón de guardar
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
            params.addRule(RelativeLayout.ABOVE, R.id.cardViewSaveTruck);
            scrollView.setLayoutParams(params);

            Toast.makeText(getContext(), "Modo edicion", Toast.LENGTH_SHORT).show();
        }
    }

    private void habilitarEdicion() {
        editTextBrandAndModel.setEnabled(true);
        editTextBrandAndModel.setTextColor(colorOnPrimary);
        editTextBrandAndModel.setBackgroundColor(colorOnSecondary);
        editTextPlateNumber.setEnabled(true);
        editTextPlateNumber.setTextColor(colorOnPrimary);
        editTextPlateNumber.setBackgroundColor(colorOnSecondary);
        editTextYearProduction.setEnabled(true);
        editTextYearProduction.setTextColor(colorOnPrimary);
        editTextYearProduction.setBackgroundColor(colorOnSecondary);
        editTextEngineNumber.setEnabled(true);
        editTextEngineNumber.setTextColor(colorOnPrimary);
        editTextEngineNumber.setBackgroundColor(colorOnSecondary);
        editTextChassisSerial.setEnabled(true);
        editTextChassisSerial.setTextColor(colorOnPrimary);
        editTextChassisSerial.setBackgroundColor(colorOnSecondary);
        editTextFuelType.setEnabled(true);
        editTextFuelType.setTextColor(colorOnPrimary);
        editTextFuelType.setBackgroundColor(colorOnSecondary);

        editTextInsuranceCompany.setEnabled(true);
        editTextInsuranceCompany.setTextColor(colorOnPrimary);
        editTextInsuranceCompany.setBackgroundColor(colorOnSecondary);
        editTextInsuranceHolder.setEnabled(true);
        editTextInsuranceHolder.setTextColor(colorOnPrimary);
        editTextInsuranceHolder.setBackgroundColor(colorOnSecondary);
        editTextHolderDNI.setEnabled(true);
        editTextHolderDNI.setTextColor(colorOnPrimary);
        editTextHolderDNI.setBackgroundColor(colorOnSecondary);
        editTextSOATNumber.setEnabled(true);
        editTextSOATNumber.setTextColor(colorOnPrimary);
        editTextSOATNumber.setBackgroundColor(colorOnSecondary);
        editTextSOATIssueDate.setEnabled(true);
        editTextSOATIssueDate.setTextColor(colorOnPrimary);
        editTextSOATIssueDate.setBackgroundColor(colorOnSecondary);
        editTextSOATDueDate.setEnabled(true);
        editTextSOATDueDate.setTextColor(colorOnPrimary);
        editTextSOATDueDate.setBackgroundColor(colorOnSecondary);
        editTextRevisionNumber.setEnabled(true);
        editTextRevisionNumber.setTextColor(colorOnPrimary);
        editTextRevisionNumber.setBackgroundColor(colorOnSecondary);
        editTextRevisionIssueDate.setEnabled(true);
        editTextRevisionIssueDate.setTextColor(colorOnPrimary);
        editTextRevisionIssueDate.setBackgroundColor(colorOnSecondary);
        editTextRevisionDueDate.setEnabled(true);
        editTextRevisionDueDate.setTextColor(colorOnPrimary);
        editTextRevisionDueDate.setBackgroundColor(colorOnSecondary);

        editTextPurchaseDate.setEnabled(true);
        editTextPurchaseDate.setTextColor(colorOnPrimary);
        editTextPurchaseDate.setBackgroundColor(colorOnSecondary);
        editTextPurchaseValue.setEnabled(true);
        editTextPurchaseValue.setTextColor(colorOnPrimary);
        editTextPurchaseValue.setBackgroundColor(colorOnSecondary);
        editTextColor.setEnabled(true);
        editTextColor.setTextColor(colorOnPrimary);
        editTextColor.setBackgroundColor(colorOnSecondary);
        editTextWeight.setEnabled(true);
        editTextWeight.setTextColor(colorOnPrimary);
        editTextWeight.setBackgroundColor(colorOnSecondary);
        editTextLength.setEnabled(true);
        editTextLength.setTextColor(colorOnPrimary);
        editTextLength.setBackgroundColor(colorOnSecondary);
        editTextWidth.setEnabled(true);
        editTextWidth.setTextColor(colorOnPrimary);
        editTextWidth.setBackgroundColor(colorOnSecondary);
        editTextHeight.setEnabled(true);
        editTextHeight.setTextColor(colorOnPrimary);
        editTextHeight.setBackgroundColor(colorOnSecondary);
    }

    private void deshabilitarEdicion() {
        editTextBrandAndModel.setEnabled(false);
        editTextBrandAndModel.setTextColor(colorOnSecondary);
        editTextBrandAndModel.setBackgroundResource(android.R.color.transparent);
        editTextPlateNumber.setEnabled(false);
        editTextPlateNumber.setTextColor(colorOnSecondary);
        editTextPlateNumber.setBackgroundResource(android.R.color.transparent);
        editTextYearProduction.setEnabled(false);
        editTextYearProduction.setTextColor(colorOnSecondary);
        editTextYearProduction.setBackgroundResource(android.R.color.transparent);
        editTextEngineNumber.setEnabled(false);
        editTextEngineNumber.setTextColor(colorOnSecondary);
        editTextEngineNumber.setBackgroundResource(android.R.color.transparent);
        editTextChassisSerial.setEnabled(false);
        editTextChassisSerial.setTextColor(colorOnSecondary);
        editTextChassisSerial.setBackgroundResource(android.R.color.transparent);
        editTextFuelType.setEnabled(false);
        editTextFuelType.setTextColor(colorOnSecondary);
        editTextFuelType.setBackgroundResource(android.R.color.transparent);

        editTextInsuranceCompany.setEnabled(false);
        editTextInsuranceCompany.setTextColor(colorOnSecondary);
        editTextInsuranceCompany.setBackgroundResource(android.R.color.transparent);
        editTextInsuranceHolder.setEnabled(false);
        editTextInsuranceHolder.setTextColor(colorOnSecondary);
        editTextInsuranceHolder.setBackgroundResource(android.R.color.transparent);
        editTextHolderDNI.setEnabled(false);
        editTextHolderDNI.setTextColor(colorOnSecondary);
        editTextHolderDNI.setBackgroundResource(android.R.color.transparent);
        editTextSOATNumber.setEnabled(false);
        editTextSOATNumber.setTextColor(colorOnSecondary);
        editTextSOATNumber.setBackgroundResource(android.R.color.transparent);
        editTextSOATIssueDate.setEnabled(false);
        editTextSOATIssueDate.setTextColor(colorOnSecondary);
        editTextSOATIssueDate.setBackgroundResource(android.R.color.transparent);
        editTextSOATDueDate.setEnabled(false);
        editTextSOATDueDate.setTextColor(colorOnSecondary);
        editTextSOATDueDate.setBackgroundResource(android.R.color.transparent);
        editTextRevisionNumber.setEnabled(false);
        editTextRevisionNumber.setTextColor(colorOnSecondary);
        editTextRevisionNumber.setBackgroundResource(android.R.color.transparent);
        editTextRevisionIssueDate.setEnabled(false);
        editTextRevisionIssueDate.setTextColor(colorOnSecondary);
        editTextRevisionIssueDate.setBackgroundResource(android.R.color.transparent);
        editTextRevisionDueDate.setEnabled(false);
        editTextRevisionDueDate.setTextColor(colorOnSecondary);
        editTextRevisionDueDate.setBackgroundResource(android.R.color.transparent);

        editTextPurchaseDate.setEnabled(false);
        editTextPurchaseDate.setTextColor(colorOnSecondary);
        editTextPurchaseDate.setBackgroundResource(android.R.color.transparent);
        editTextPurchaseValue.setEnabled(false);
        editTextPurchaseValue.setTextColor(colorOnSecondary);
        editTextPurchaseValue.setBackgroundResource(android.R.color.transparent);
        editTextColor.setEnabled(false);
        editTextColor.setTextColor(colorOnSecondary);
        editTextColor.setBackgroundResource(android.R.color.transparent);
        editTextWeight.setEnabled(false);
        editTextWeight.setTextColor(colorOnSecondary);
        editTextWeight.setBackgroundResource(android.R.color.transparent);
        editTextLength.setEnabled(false);
        editTextLength.setTextColor(colorOnSecondary);
        editTextLength.setBackgroundResource(android.R.color.transparent);
        editTextWidth.setEnabled(false);
        editTextWidth.setTextColor(colorOnSecondary);
        editTextWidth.setBackgroundResource(android.R.color.transparent);
        editTextHeight.setEnabled(false);
        editTextHeight.setTextColor(colorOnSecondary);
        editTextHeight.setBackgroundResource(android.R.color.transparent);
    }

    // Método para validar que los campos no están vacíos
    private boolean camposNoVacios() {
        // Verificar cada campo individualmente
        if (TextUtils.isEmpty(editTextBrandAndModel.getText().toString())
                || TextUtils.isEmpty(editTextPlateNumber.getText().toString())
                || TextUtils.isEmpty(editTextYearProduction.getText().toString())
                || TextUtils.isEmpty(editTextEngineNumber.getText().toString())
                || TextUtils.isEmpty(editTextChassisSerial.getText().toString())
                || TextUtils.isEmpty(editTextFuelType.getText().toString())

                || TextUtils.isEmpty(editTextInsuranceCompany.getText().toString())
                || TextUtils.isEmpty(editTextInsuranceHolder.getText().toString())
                || TextUtils.isEmpty(editTextHolderDNI.getText().toString())
                || TextUtils.isEmpty(editTextSOATNumber.getText().toString())
                || TextUtils.isEmpty(editTextSOATIssueDate.getText().toString())
                || TextUtils.isEmpty(editTextSOATDueDate.getText().toString())
                || TextUtils.isEmpty(editTextRevisionNumber.getText().toString())
                || TextUtils.isEmpty(editTextRevisionIssueDate.getText().toString())
                || TextUtils.isEmpty(editTextRevisionDueDate.getText().toString())

                || TextUtils.isEmpty(editTextPurchaseDate.getText().toString())
                || TextUtils.isEmpty(editTextPurchaseValue.getText().toString())
                || TextUtils.isEmpty(editTextColor.getText().toString())
                || TextUtils.isEmpty(editTextWeight.getText().toString())
                || TextUtils.isEmpty(editTextLength.getText().toString())
                || TextUtils.isEmpty(editTextWidth.getText().toString())
                || TextUtils.isEmpty(editTextHeight.getText().toString())) {
            // Si algún campo está vacío, mostrar un mensaje de error y devolver false
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para verificar si se realizaron cambios en los datos del camión
    private boolean comprobarCambios(Truck truck) {
        // Comparar los valores actuales con los originales
        if (!editTextBrandAndModel.getText().toString().equals(truck.getInformacion().getMarca() + " / " + truck.getInformacion().getModelo())
                || !editTextPlateNumber.getText().toString().equals(truck.getInformacion().getNumeroPlaca())
                || !editTextYearProduction.getText().toString().equals(truck.getInformacion().getAnioProduccion())
                || !editTextEngineNumber.getText().toString().equals(truck.getInformacion().getNumeroMotor())
                || !editTextChassisSerial.getText().toString().equals(truck.getInformacion().getSerieChasis())
                || !editTextFuelType.getText().toString().equals(truck.getInformacion().getTipoCombustible())

                || !editTextInsuranceCompany.getText().toString().equals(truck.getDocumentacionLegal().getCompaniaAseguradora())
                || !editTextInsuranceHolder.getText().toString().equals(truck.getDocumentacionLegal().getTitularSeguro())
                || !editTextHolderDNI.getText().toString().equals(truck.getDocumentacionLegal().getDniTitular())
                || !editTextSOATNumber.getText().toString().equals(truck.getDocumentacionLegal().getNumeroSOAT())
                || !editTextSOATIssueDate.getText().toString().equals(truck.getDocumentacionLegal().getFechaEmisionSOAT())
                || !editTextSOATDueDate.getText().toString().equals(truck.getDocumentacionLegal().getFechaVencimientoSOAT())
                || !editTextRevisionNumber.getText().toString().equals(truck.getDocumentacionLegal().getNumeroRevisionTecnica())
                || !editTextRevisionIssueDate.getText().toString().equals(truck.getDocumentacionLegal().getFechaEmisionRevisionTecnica())
                || !editTextRevisionDueDate.getText().toString().equals(truck.getDocumentacionLegal().getFechaVencimientoRevisionTecnica())

                || !editTextPurchaseDate.getText().toString().equals(truck.getDetalles().getFechaCompraVehiculo())
                || !editTextPurchaseValue.getText().toString().equals(truck.getDetalles().getValorCompraVehiculo())
                || !editTextColor.getText().toString().equals(truck.getDetalles().getColorVehiculo())
                || !editTextWeight.getText().toString().equals(truck.getDetalles().getPesoVehiculo())
                || !editTextLength.getText().toString().equals(truck.getDetalles().getLongitudVehiculo())
                || !editTextWidth.getText().toString().equals(truck.getDetalles().getAnchuraVehiculo())
                || !editTextHeight.getText().toString().equals(truck.getDetalles().getAlturaVehiculo())
        ) {
            return true;
        } else {
            return false;
        }
    }

    // Actualizar los datos del camión en la base de datos
    private void actualizarDatosCamion() {
        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Camiones");

        // Obtener el objeto Truck del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            Truck truck = (Truck) bundle.getSerializable("truck");
            if (truck != null) {
                // Obtener el identificador único del camión
                String uniqueIdentifier = truck.getUniqueIdentifier();
                Log.d(TAG, "Unique Identifier del camión: " + uniqueIdentifier);

                // Verificar si se realizaron cambios en los datos del camión
                if (comprobarCambios(truck)) {
                    // Crear un mapa para almacenar los datos actualizados
                    Map<String, Object> updates = new HashMap<>();

                    // Dividir el texto del EditText en marca y modelo
                    String brandAndModel = editTextBrandAndModel.getText().toString();
                    String[] parts = brandAndModel.split(" / ");

                    // Agregar los campos que deseas actualizar y sus nuevos valores al mapa
                    updates.put("Información/Marca", parts[0]);
                    updates.put("Información/Modelo", parts[1]);
                    updates.put("Información/Número de placa", editTextPlateNumber.getText().toString());
                    updates.put("Información/Año de producción", editTextYearProduction.getText().toString());
                    updates.put("Información/Número de motor", editTextEngineNumber.getText().toString());
                    updates.put("Información/Serie del chasis", editTextChassisSerial.getText().toString());
                    updates.put("Información/Tipo de combustible", editTextFuelType.getText().toString());

                    updates.put("Documentación legal/Compañía aseguradora", editTextInsuranceCompany.getText().toString());
                    updates.put("Documentación legal/Titular del seguro", editTextInsuranceHolder.getText().toString());
                    updates.put("Documentación legal/DNI del titular", editTextHolderDNI.getText().toString());
                    updates.put("Documentación legal/Número de SOAT", editTextSOATNumber.getText().toString());
                    updates.put("Documentación legal/Fecha de emisión del SOAT", editTextSOATIssueDate.getText().toString());
                    updates.put("Documentación legal/Fecha de vencimiento del SOAT", editTextSOATDueDate.getText().toString());
                    updates.put("Documentación legal/Número de Revisión Técnica", editTextRevisionNumber.getText().toString());
                    updates.put("Documentación legal/Fecha de emisión de la revisión técnica", editTextRevisionIssueDate.getText().toString());
                    updates.put("Documentación legal/Fecha de vencimiento de la revisión técnica", editTextRevisionDueDate.getText().toString());

                    updates.put("Detalles/Fecha de compra del vehículo", editTextPurchaseDate.getText().toString());
                    updates.put("Detalles/Valor de compra del vehículo", editTextPurchaseValue.getText().toString());
                    updates.put("Detalles/Color del vehículo", editTextColor.getText().toString());
                    updates.put("Detalles/Peso del vehículo", editTextWeight.getText().toString());
                    updates.put("Detalles/Longitud del vehículo", editTextLength.getText().toString());
                    updates.put("Detalles/Anchura del vehículo", editTextWidth.getText().toString());
                    updates.put("Detalles/Altura del vehículo", editTextHeight.getText().toString());

                    try {
                        // Actualizar los valores en la base de datos utilizando la referencia del camión y el mapa de actualización
                        databaseReference.child(uniqueIdentifier).updateChildren(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // La actualización se realizó exitosamente
                                        Toast.makeText(getContext(), "Los datos del camión se han actualizado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // La actualización falló
                                        Toast.makeText(getContext(), "Error al actualizar los datos del camión", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error al actualizar los datos del camión", e);
                                    }
                                });
                    } catch (Exception e) {
                        // Manejar cualquier excepción que pueda ocurrir durante la actualización de los datos del camión
                        Toast.makeText(getContext(), "Error al actualizar los datos del camión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al actualizar los datos del camión", e);
                    }
                }
            }
        }
    }
}