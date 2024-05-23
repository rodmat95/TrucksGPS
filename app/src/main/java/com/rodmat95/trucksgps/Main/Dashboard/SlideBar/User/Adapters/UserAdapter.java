package com.rodmat95.trucksgps.Main.Dashboard.SlideBar.User.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.rodmat95.trucksgps.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private int resource;
    private ArrayList<User> userList;

    public UserAdapter(ArrayList<User> userList, int resource) {
        this.userList = userList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int index) {
        User user = userList.get(index);
        if (user != null) {
            GeneralData generalData = user.getDatosGenerales();
            if (generalData != null) {
                viewHolder.textViewUserName.setText(generalData.getUsuario());
                viewHolder.textViewMail.setText(generalData.getCorreo());
                // Determinar el rol del usuario y establecer la imagen correspondiente
                String rol = generalData.getRol();
                if (rol != null) {
                    if (rol.equals("Administrador")) {
                        viewHolder.imageViewRol.setImageResource(R.drawable.ic_admin_gray_24dp);
                    } else if (rol.equals("Usuario")) {
                        viewHolder.imageViewRol.setImageResource(R.drawable.ic_coffee_gray_24dp);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUserName, textViewMail;
        private ImageView imageViewRol;
        public View view;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.textViewUserName = view.findViewById(R.id.textViewUserName);
            this.textViewMail = view.findViewById(R.id.textViewMail);
            this.imageViewRol = view.findViewById(R.id.imageViewRol);

            // Agregar un OnClickListener al view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la posición del elemento clickeado
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtener el objeto User correspondiente a la posición
                        User clickedUser = userList.get(position);

                        // Obtener el identificador único del usuario seleccionado
                        String userId = clickedUser.getUniqueIdentifier();

                        // Crear un diálogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        LayoutInflater inflater = LayoutInflater.from(view.getContext());
                        View dialogView = inflater.inflate(R.layout.dialog_view_user, null);
                        builder.setView(dialogView);

                        // Obtener referencias a las vistas del diálogo
                        TextView textViewUsernameDialog = dialogView.findViewById(R.id.textViewUsernameDialog);
                        Spinner spinnerUserRole = dialogView.findViewById(R.id.spinnerUserRole);
                        TextView textViewEmailVerificationStatus = dialogView.findViewById(R.id.textViewEmailVerificationStatus);

                        // Mostrar el nombre de usuario en el diálogo
                        textViewUsernameDialog.setText(clickedUser.getDatosGenerales().getUsuario());

                        /*
                        // Obtener el objeto FirebaseUser del usuario seleccionado
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = mAuth.getUser(userId);

                        // Verificar el estado de verificación del correo electrónico
                        boolean isEmailVerified = firebaseUser.isEmailVerified();

                        // Mostrar el estado de verificación del correo electrónico en el TextView
                        if (isEmailVerified) {
                            textViewEmailVerificationStatus.setText("Correo electrónico verificado");
                        } else {
                            textViewEmailVerificationStatus.setText("Correo electrónico no verificado");
                        }
                         */

                        // Configurar el adaptador para el spinner de roles
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                                R.array.roles_array, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUserRole.setAdapter(adapter);

                        // Obtener el rol actual del usuario
                        String currentRole = clickedUser.getDatosGenerales().getRol();

                        // Seleccionar el elemento correspondiente en el spinner
                        if (currentRole.equals("Usuario")) {
                            spinnerUserRole.setSelection(adapter.getPosition("Usuario"));
                        } else if (currentRole.equals("Administrador")) {
                            spinnerUserRole.setSelection(adapter.getPosition("Administrador"));
                        }

                        // Agregar botones al diálogo
                        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Obtener el nuevo rol seleccionado
                                String newRole = spinnerUserRole.getSelectedItem().toString();

                                // Comparar con el rol actual del usuario en Firebase
                                if (!newRole.equals(clickedUser.getDatosGenerales().getRol())) {
                                    // Actualizar el rol en Firebase
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId).child("Datos generales").child("Rol");
                                    userRef.setValue(newRole)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Mostrar un Toast indicando que se actualizó el rol
                                                    Toast.makeText(view.getContext(), "Rol actualizado", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Mostrar un Toast indicando que ocurrió un error al actualizar el rol
                                                    Toast.makeText(view.getContext(), "Error al actualizar el rol: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });

                        // Mostrar el diálogo
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });
        }
    }
}