package com.rodmat95.trucksgps.Main.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.rodmat95.trucksgps.Providers.AuthProvider;
import com.rodmat95.trucksgps.R;

public class ChangePasswordFragment extends Fragment {

		private AuthProvider mAuthProvider;

		private TextInputEditText editTextPassword, editTextNewPassword, editTextRepeatNewPassword;
		private MaterialButton buttonChangePassword;

		@Nullable
		@Override
		public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
				View view=inflater.inflate (R.layout.fragment_change_password, container, false);

				mAuthProvider = new AuthProvider();

				editTextPassword=view.findViewById (R.id.editTextPassword);
				editTextNewPassword=view.findViewById (R.id.editTextNewPassword);
				editTextRepeatNewPassword=view.findViewById (R.id.editTextRepeatNewPassword);
				buttonChangePassword=view.findViewById (R.id.buttonChangePassword);

				changePassword ();
				return view;
		}

		private void changePassword() {
				FirebaseUser user = mAuthProvider.getCurrentUser();

				String password = editTextPassword.getText().toString();
				final String newPassword = editTextNewPassword.getText().toString();
				String repeatNewPassword = editTextRepeatNewPassword.getText().toString();

				// Verificar si los campos de nueva contraseña y repetición de nueva contraseña coinciden
				if (!newPassword.equals(repeatNewPassword)) {
						Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
						return;
				}

				// Reautenticar al usuario para cambiar la contraseña
				AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
				user.reauthenticate(credential)
												.addOnCompleteListener(new OnCompleteListener<Void> () {
														@Override
														public void onComplete(@NonNull Task<Void> task) {
																if (task.isSuccessful()) {
																		// Cambiar la contraseña
																		user.updatePassword(newPassword)
																										.addOnCompleteListener(new OnCompleteListener<Void>() {
																												@Override
																												public void onComplete(@NonNull Task<Void> task) {
																														if (task.isSuccessful()) {
																																// Mostrar mensaje de éxito
																																Toast.makeText(requireContext(), "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
																														} else {
																																// Manejar errores al cambiar la contraseña
																																Toast.makeText(requireContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
																														}
																												}
																										});
																} else {
																		// Manejar errores al reautenticar al usuario
																		Toast.makeText(requireContext(), "Error al reautenticar al usuario", Toast.LENGTH_SHORT).show();
																}
														}
												});
		}
}