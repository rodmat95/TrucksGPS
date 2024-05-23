package com.rodmat95.trucksgps.Main.Home.Truck;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rodmat95.trucksgps.R;

import java.util.Calendar;

public class AddTruckFragment extends Fragment {

    private DatabaseReference mDatabase;
    MaterialButton buttonSave;
    RelativeLayout itemClickedInfo, itemClickedDocs, itemClickedDets;
    ImageView arrowImgInfo, arrowImgDocs, arrowImgDets;
    LinearLayout discLayoutInfo, discLayoutDocs, discLayoutDets;
    TextInputEditText editTextPlateNumber, editTextBrand, editTextModel, editTextYearProduction, editTextEngineNumber, editTextChassisSerial, editTextFuelType,
            editTextInsuranceCompany, editTextSOATNumber, editTextSOATIssueDate, editTextSOATDueDate, editTextInsuranceHolder, editTextDNI, editTextRevisionNumber, editTextRevisionIssueDate, editTextRevisionDueDate,
            editTextPurchaseDate, editTextPurchaseValue, editTextColor, editTextWeight, editTextLength, editTextWidth, editTextHeight;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_truck, container, false);

        // Obtener las referencias
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buttonSave = view.findViewById(R.id.buttonSave);

        itemClickedInfo = view.findViewById(R.id.itemClickedInfo);
        itemClickedDocs = view.findViewById(R.id.itemClickedDocs);
        itemClickedDets = view.findViewById(R.id.itemClickedDets);
        arrowImgInfo = view.findViewById(R.id.dropImgInfo);
        arrowImgDocs = view.findViewById(R.id.dropImgDocs);
        arrowImgDets = view.findViewById(R.id.dropImgDets);
        discLayoutInfo = view.findViewById(R.id.discLayoutInfo);
        discLayoutDocs = view.findViewById(R.id.discLayoutDocs);
        discLayoutDets = view.findViewById(R.id.discLayoutDets);

        editTextPlateNumber = view.findViewById(R.id.editTextPlateNumber);
        editTextBrand = view.findViewById(R.id.editTextBrand);
        editTextModel = view.findViewById(R.id.editTextModel);
        editTextYearProduction = view.findViewById(R.id.editTextYearProduction);
        editTextEngineNumber = view.findViewById(R.id.editTextEngineNumber);
        editTextChassisSerial = view.findViewById(R.id.editTextChassisSerial);
        editTextFuelType = view.findViewById(R.id.editTextFuelType);

        editTextInsuranceCompany = view.findViewById(R.id.editTextInsuranceCompany);
        editTextSOATNumber = view.findViewById(R.id.editTextSOATNumber);
        editTextSOATIssueDate = view.findViewById(R.id.editTextSOATIssueDate);
        editTextSOATDueDate = view.findViewById(R.id.editTextSOATDueDate);
        editTextInsuranceHolder = view.findViewById(R.id.editTextInsuranceHolder);
        editTextDNI = view.findViewById(R.id.editTextDNI);
        editTextRevisionNumber = view.findViewById(R.id.editTextRevisionNumber);
        editTextRevisionIssueDate = view.findViewById(R.id.editTextRevisionIssueDate);
        editTextRevisionDueDate = view.findViewById(R.id.editTextRevisionDueDate);

        editTextPurchaseDate = view.findViewById(R.id.editTextPurchaseDate);
        editTextPurchaseValue = view.findViewById(R.id.editTextPurchaseValue);
        editTextColor = view.findViewById(R.id.editTextColor);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextLength = view.findViewById(R.id. editTextLength);
        editTextWidth = view.findViewById(R.id.editTextWidth);
        editTextHeight = view.findViewById(R.id.editTextHeight);

        Calendar calendario = Calendar.getInstance();
        final int year = calendario.get(Calendar.YEAR);
        final int month = calendario.get(Calendar.MONTH);
        final int day = calendario.get(Calendar.DAY_OF_MONTH);

        // Configurar eventos de clic
        itemClicked(itemClickedInfo, discLayoutInfo, arrowImgInfo);
        itemClicked(itemClickedDocs, discLayoutDocs, arrowImgDocs);
        itemClicked(itemClickedDets, discLayoutDets, arrowImgDets);

        setUpDatePickerDialog(editTextSOATIssueDate);
        setUpDatePickerDialog(editTextSOATDueDate);
        setUpDatePickerDialog(editTextRevisionIssueDate);
        setUpDatePickerDialog(editTextRevisionDueDate);
        setUpDatePickerDialog(editTextPurchaseDate);

        saveTruck();
        //backView();

        return view;
    }

    private void itemClicked(View item, View discLayout, ImageView arrowImg) {
        item.setOnClickListener(v -> {
            if (discLayout.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition((ViewGroup) getView(), new AutoTransition());
                discLayout.setVisibility(View.GONE);
                arrowImg.setImageResource(R.drawable.ic_drop_down_gray_24dp);
            } else {
                TransitionManager.beginDelayedTransition((ViewGroup) getView(), new AutoTransition());
                discLayout.setVisibility(View.VISIBLE);
                arrowImg.setImageResource(R.drawable.ic_drop_up_gray_24dp);
            }
        });
    }

    private void setUpDatePickerDialog(final TextInputEditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el tema actual del sistema
                int themeResId;
                if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    // Si el tema del sistema es dark
                    themeResId = android.R.style.Theme_Holo_Dialog_MinWidth;
                } else {
                    // Si el tema del sistema es light
                    themeResId = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
                }

                // Obtener la fecha actual
                final Calendar calendario = Calendar.getInstance();
                int year = calendario.get(Calendar.YEAR);
                int month = calendario.get(Calendar.MONTH);
                int day = calendario.get(Calendar.DAY_OF_MONTH);

                // Crear y mostrar el DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        //themeResId,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String date = dayOfMonth + "/" + month + "/" + year;
                                editText.setText(date);
                            }
                        },
                        year,month,day);
                //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
    }

    private void saveTruck(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si algún campo está vacío
                if (validateFields()) {
                    // Utilizando el método push() para generar un ID único para cada registro
                    DatabaseReference truckRef = mDatabase.child("Camiones").push();
                    saveInformation(truckRef);
                    saveLegalDocumentation(truckRef);
                    saveDetails(truckRef);
                    addListener(truckRef);
                } else {
                    // Mostrar un mensaje de error o realizar alguna acción adecuada
                    Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        // Obtener los textos ingresados en los TextInputEditText
        // Validar que todos los campos estén completos
        return !editTextPlateNumber.getText().toString().isEmpty() &&
                !editTextBrand.getText().toString().isEmpty() &&
                !editTextModel.getText().toString().isEmpty() &&
                !editTextYearProduction.getText().toString().isEmpty() &&
                !editTextEngineNumber.getText().toString().isEmpty() &&
                !editTextChassisSerial.getText().toString().isEmpty() &&
                !editTextFuelType.getText().toString().isEmpty() &&

                !editTextInsuranceCompany.getText().toString().isEmpty() &&
                !editTextSOATNumber.getText().toString().isEmpty() &&
                !editTextSOATIssueDate.getText().toString().isEmpty() &&
                !editTextSOATDueDate.getText().toString().isEmpty() &&
                !editTextInsuranceHolder.getText().toString().isEmpty() &&
                !editTextDNI.getText().toString().isEmpty() &&
                !editTextRevisionNumber.getText().toString().isEmpty() &&
                !editTextRevisionIssueDate.getText().toString().isEmpty() &&
                !editTextRevisionDueDate.getText().toString().isEmpty() &&

                !editTextPurchaseDate.getText().toString().isEmpty() &&
                !editTextPurchaseValue.getText().toString().isEmpty() &&
                !editTextColor.getText().toString().isEmpty() &&
                !editTextWeight.getText().toString().isEmpty() &&
                !editTextLength.getText().toString().isEmpty() &&
                !editTextWidth.getText().toString().isEmpty() &&
                !editTextHeight.getText().toString().isEmpty();
    }

    private void saveInformation(DatabaseReference driverRef) {
        // Guardar los atributos del conductor en la base de datos bajo la referencia "Información"
        DatabaseReference informationRef = driverRef.child("Información");
        informationRef.child("Número de placa").setValue(editTextPlateNumber.getText().toString());
        informationRef.child("Marca").setValue(editTextBrand.getText().toString());
        informationRef.child("Modelo").setValue(editTextModel.getText().toString());
        informationRef.child("Año de producción").setValue(editTextYearProduction.getText().toString());
        informationRef.child("Número de motor").setValue(editTextEngineNumber.getText().toString());
        informationRef.child("Serie del chasis").setValue(editTextChassisSerial.getText().toString());
        informationRef.child("Tipo de combustible").setValue(editTextFuelType.getText().toString());
    }

    private void saveLegalDocumentation(DatabaseReference driverRef) {
        // Guardar los atributos del conductor en la base de datos bajo la referencia "Documentación legal"
        DatabaseReference legalDocumentationRef = driverRef.child("Documentación legal");
        legalDocumentationRef.child("Compañía aseguradora").setValue(editTextInsuranceCompany.getText().toString());
        legalDocumentationRef.child("Número de SOAT").setValue(editTextSOATNumber.getText().toString());
        legalDocumentationRef.child("Fecha de emisión del SOAT").setValue(editTextSOATIssueDate.getText().toString());
        legalDocumentationRef.child("Fecha de vencimiento del SOAT").setValue(editTextSOATDueDate.getText().toString());
        legalDocumentationRef.child("Titular del seguro").setValue(editTextInsuranceHolder.getText().toString());
        legalDocumentationRef.child("DNI del titular").setValue(editTextDNI.getText().toString());
        legalDocumentationRef.child("Número de Revisión Técnica").setValue(editTextRevisionNumber.getText().toString());
        legalDocumentationRef.child("Fecha de emisión de la revisión técnica").setValue(editTextRevisionIssueDate.getText().toString());
        legalDocumentationRef.child("Fecha de vencimiento de la revisión técnica").setValue(editTextRevisionDueDate.getText().toString());

    }

    private void saveDetails(DatabaseReference driverRef) {
        // Guardar los atributos del conductor en la base de datos bajo la referencia "Detalles"
        DatabaseReference detailsRef = driverRef.child("Detalles");
        detailsRef.child("Fecha de compra del vehículo").setValue(editTextPurchaseDate.getText().toString());
        detailsRef.child("Valor de compra del vehículo").setValue(editTextPurchaseValue.getText().toString());
        detailsRef.child("Color del vehículo").setValue(editTextColor.getText().toString());
        detailsRef.child("Peso del vehículo").setValue(editTextWeight.getText().toString());
        detailsRef.child("Longitud del vehículo").setValue(editTextLength.getText().toString());
        detailsRef.child("Anchura del vehículo").setValue(editTextWidth.getText().toString());
        detailsRef.child("Altura del vehículo").setValue(editTextHeight.getText().toString());

    }

    private void addListener(DatabaseReference driverRef) {
        // Añadir un listener para detectar cuando se completa la tarea de guardar los datos
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Comprobar si la operación fue exitosa
                if (dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "Datos del camión guardados correctamente", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al guardar los datos del camión", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que ocurra
                Toast.makeText(getContext(), "Error al guardar los datos del camión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}