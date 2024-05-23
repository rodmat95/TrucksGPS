package com.rodmat95.trucksgps.Main.Home.Truck.Adapters;

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

import com.google.firebase.database.DatabaseReference;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Details;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Information;
import com.rodmat95.trucksgps.Main.Home.Truck.ViewTruckFragment;
import com.rodmat95.trucksgps.R;
import com.rodmat95.trucksgps.Main.Home.Truck.Models.Truck;

import java.util.ArrayList;

public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Truck> trucksList;

    public TruckAdapter(ArrayList<Truck> trucksList, int resource, DatabaseReference databaseReference) {
        this.trucksList = trucksList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        Truck truck = trucksList.get(index);
        Information information = truck.getInformacion();
        Details details = truck.getDetalles();

        viewHolder.textViewBrand.setText(information.getMarca());
        viewHolder.textViewModel.setText(information.getModelo());
        viewHolder.textViewPlate.setText(information.getNumeroPlaca());
        viewHolder.textViewColor.setText(details.getColorVehiculo());
    }

    @Override
    public int getItemCount() {
        return trucksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewBrand, textViewModel, textViewPlate, textViewColor;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewBrand = (TextView) view.findViewById(R.id.textViewBrand);
            this.textViewModel = (TextView) view.findViewById(R.id.textViewModel);
            this.textViewPlate = (TextView) view.findViewById(R.id.textViewPlate);
            this.textViewColor = (TextView) view.findViewById(R.id.textViewColor);

            // Agregar un OnClickListener al view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la posición del elemento clickeado
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtener el objeto Truck correspondiente a la posición
                        Truck clickedTruck = trucksList.get(position);

                        // Obtener la ruta del nodo del camión en la base de datos
                        String truckNodePath = "Camiones/" + clickedTruck.getUniqueIdentifier();

                        // Abrir el fragmento ViewTruckFragment y pasarle la información del conductor
                        Fragment viewTruckFragment = new ViewTruckFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("truck", clickedTruck);
                        bundle.putString("truckNodePath", truckNodePath);
                        viewTruckFragment.setArguments(bundle);

                        // Reemplazar el fragmento actual con ViewTruckFragment
                        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, viewTruckFragment).addToBackStack(null).commit();
                    }
                }
            });
        }
    }
}