package com.rodmat95.trucksgps.Main.Home.Driver.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rodmat95.trucksgps.Main.Home.Driver.Models.LegalDocumentation;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.PersonalData;
import com.rodmat95.trucksgps.Main.Home.Driver.ViewDriverFragment;
import com.rodmat95.trucksgps.Main.Home.Driver.Models.Driver;
import com.rodmat95.trucksgps.R;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Driver> driversList;

    public DriverAdapter(ArrayList<Driver> driversList, int resource) {
        this.driversList = driversList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public DriverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new DriverAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAdapter.ViewHolder viewHolder, int index) {
        Driver driver = driversList.get(index);
        PersonalData personalData = driver.getDatosPersonales();
        LegalDocumentation legalDocumentation = driver.getDocumentacionLegal();

        viewHolder.textViewFullName.setText(personalData.getNombreCompleto());
        viewHolder.textViewIDType.setText(personalData.getTipoDocumento());
        viewHolder.textViewIDNumber.setText(personalData.getNumeroDocumento());
        viewHolder.textViewLicenseClass.setText(legalDocumentation.getClaseLicencia());
        viewHolder.textViewLicenseCategory.setText(legalDocumentation.getCategoriaLicencia());
    }

    @Override
    public int getItemCount() {
        return driversList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFullName, textViewIDType, textViewIDNumber, textViewLicenseClass, textViewLicenseCategory;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewFullName = (TextView) view.findViewById(R.id.textViewFullName);
            this.textViewIDType = (TextView) view.findViewById(R.id.textViewIDType);
            this.textViewIDNumber = (TextView) view.findViewById(R.id.textViewIDNumber);
            this.textViewLicenseClass = (TextView) view.findViewById(R.id.textViewLicenseClass);
            this.textViewLicenseCategory = (TextView) view.findViewById(R.id.textViewLicenseCategory);

            // Agregar un OnClickListener al view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la posición del elemento clickeado
                    int position = getAdapterPosition();
                    // Verificar si la posición es válida
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtener el objeto Driver correspondiente a la posición
                        Driver clickedDriver = driversList.get(position);

                        // Obtener la ruta del nodo del conductor en la base de datos
                        String driverNodePath = "Conductores/" + clickedDriver.getUniqueIdentifier();

                        // Abrir el fragmento ViewDriverFragment y pasarle la información del conductor
                        Fragment viewDriverFragment = new ViewDriverFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("driver", clickedDriver);
                        bundle.putString("driverNodePath", driverNodePath);
                        viewDriverFragment.setArguments(bundle);

                        // Reemplazar el fragmento actual con ViewDriverFragment
                        FragmentManager fragmentManager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, viewDriverFragment).addToBackStack(null).commit();
                    }
                }
            });
        }
    }
}