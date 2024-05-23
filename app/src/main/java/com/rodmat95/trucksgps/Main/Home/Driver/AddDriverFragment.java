package com.rodmat95.trucksgps.Main.Home.Driver;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class AddDriverFragment extends Fragment {

    private DatabaseReference mDatabase;
    MaterialButton buttonSave;
    RelativeLayout itemClickedData, itemClickedDocs;
    ImageView arrowImgData, arrowImgDocs;
    LinearLayout discLayoutData, discLayoutDocs;
    TextInputEditText editTextFullName, editTextIDType, editTextIDNumber, editTextPhoneNumber, editTextDateBirth, editTextResidenceAddress, editTextContactName, editTextContactNumber,
            editTextLicenseNumber, editTextLicenseClass, editTextLicenseCategory, editTextExpeditionDate, editTextRevalidationDate, editTextSpecialAuthorizations, editTextRestrictions;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_driver, container, false);

        // Obtener las referencias
        mDatabase = FirebaseDatabase.getInstance().getReference();
        buttonSave = view.findViewById(R.id.buttonSave);

        itemClickedData = view.findViewById(R.id.itemClickedData);
        itemClickedDocs = view.findViewById(R.id.itemClickedDocs);
        arrowImgData = view.findViewById(R.id.dropImgData);
        arrowImgDocs = view.findViewById(R.id.dropImgDocs);
        discLayoutData = view.findViewById(R.id.discLayoutData);
        discLayoutDocs = view.findViewById(R.id.discLayoutDocs);

        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextIDType = view.findViewById(R.id.editTextIDType);
        editTextIDNumber = view.findViewById(R.id.editTextIDNumber);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        editTextDateBirth = view.findViewById(R.id.editTextDateBirth);
        editTextResidenceAddress = view.findViewById(R.id.editTextResidenceAddress);
        editTextContactName = view.findViewById(R.id.editTextContactName);
        editTextContactNumber = view.findViewById(R.id.editTextContactNumber);

        editTextLicenseNumber = view.findViewById(R.id.editTextLicenseNumber);
        editTextLicenseClass = view.findViewById(R.id.editTextLicenseClass);
        editTextLicenseCategory = view.findViewById(R.id.editTextLicenseCategory);
        editTextExpeditionDate = view.findViewById(R.id.editTextExpeditionDate);
        editTextRevalidationDate = view.findViewById(R.id.editTextRevalidationDate);
        editTextSpecialAuthorizations = view.findViewById(R.id.editTextSpecialAuthorizations);
        editTextRestrictions = view.findViewById(R.id.editTextRestrictions);

        Calendar calendario = Calendar.getInstance();
        final int year = calendario.get(Calendar.YEAR);
        final int month = calendario.get(Calendar.MONTH);
        final int day = calendario.get(Calendar.DAY_OF_MONTH);

        // Configurar eventos de clic
        itemClicked(itemClickedData, discLayoutData, arrowImgData);
        itemClicked(itemClickedDocs, discLayoutDocs, arrowImgDocs);

        setUpDatePickerDialog(editTextDateBirth);
        setUpDatePickerDialog(editTextExpeditionDate);
        setUpDatePickerDialog(editTextRevalidationDate);

        saveDriver();

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
                        themeResId,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String date = dayOfMonth + "/" + month + "/" + year;
                                editText.setText(date);
                            }
                        },
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
    }

    private void saveDriver(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si algún campo está vacío
                if (validateFields()) {
                    // Utilizando el método push() para generar un ID único para cada registro
                    DatabaseReference driverRef = mDatabase.child("Conductores").push();
                    savePersonalData(driverRef);
                    saveLegalDocumentation(driverRef);
                    addListener(driverRef);
                } else {
                    // Mostrar un mensaje de error
                    Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        // Obtener los textos ingresados en los TextInputEditText
        // Validar que todos los campos estén completos
        return !editTextFullName.getText().toString().isEmpty() &&
                !editTextIDType.getText().toString().isEmpty() &&
                !editTextIDNumber.getText().toString().isEmpty() &&
                !editTextPhoneNumber.getText().toString().isEmpty() &&
                !editTextDateBirth.getText().toString().isEmpty() &&
                !editTextResidenceAddress.getText().toString().isEmpty() &&
                !editTextContactName.getText().toString().isEmpty() &&
                !editTextContactNumber.getText().toString().isEmpty() &&

                !editTextLicenseNumber.getText().toString().isEmpty() &&
                !editTextLicenseClass.getText().toString().isEmpty() &&
                !editTextLicenseCategory.getText().toString().isEmpty() &&
                !editTextExpeditionDate.getText().toString().isEmpty() &&
                !editTextRevalidationDate.getText().toString().isEmpty() &&
                !editTextSpecialAuthorizations.getText().toString().isEmpty() &&
                !editTextRestrictions.getText().toString().isEmpty();
    }

    private void savePersonalData(DatabaseReference driverRef) {
        // Guardar los atributos del conductor en la base de datos bajo la referencia "Datos personales"
        DatabaseReference personalDataRef = driverRef.child("Datos personales");
        personalDataRef.child("Nombre completo").setValue(editTextFullName.getText().toString());
        personalDataRef.child("Tipo de documento").setValue(editTextIDType.getText().toString());
        personalDataRef.child("Número de documento").setValue(editTextIDNumber.getText().toString());
        personalDataRef.child("Número de teléfono").setValue(editTextPhoneNumber.getText().toString());
        personalDataRef.child("Fecha de nacimiento").setValue(editTextDateBirth.getText().toString());
        personalDataRef.child("Dirección de residencia").setValue(editTextResidenceAddress.getText().toString());
        personalDataRef.child("Nombre del contacto").setValue(editTextContactName.getText().toString());
        personalDataRef.child("Número del contacto").setValue(editTextContactNumber.getText().toString());
    }

    private void saveLegalDocumentation(DatabaseReference driverRef) {
        // Guardar los atributos del conductor en la base de datos bajo la referencia "Documentación legal"
        DatabaseReference legalDocumentationRef = driverRef.child("Documentación legal");
        legalDocumentationRef.child("Numero de licencia").setValue(editTextLicenseNumber.getText().toString());
        legalDocumentationRef.child("Clase de licencia").setValue(editTextLicenseClass.getText().toString());
        legalDocumentationRef.child("Categoría de licencia").setValue(editTextLicenseCategory.getText().toString());
        legalDocumentationRef.child("Fecha de expedición de licencia").setValue(editTextExpeditionDate.getText().toString());
        legalDocumentationRef.child("Fecha de revalidación de licencia").setValue(editTextRevalidationDate.getText().toString());
        legalDocumentationRef.child("Autorizaciones especiales de licencia").setValue(editTextSpecialAuthorizations.getText().toString());
        legalDocumentationRef.child("Restricciones de licencia").setValue(editTextRestrictions.getText().toString());
    }

    private void addListener(DatabaseReference driverRef) {
        // Añadir un listener para detectar cuando se completa la tarea de guardar los datos
        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Comprobar si la operación fue exitosa
                if (dataSnapshot.exists()) {
                    Toast.makeText(getContext(), "Datos del conductor guardados correctamente", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al guardar los datos del conductor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que ocurra
                Toast.makeText(getContext(), "Error al guardar los datos del conductor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}