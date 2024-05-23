package com.rodmat95.trucksgps.Providers;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkProvider {

    private Context context;

    public NetworkProvider(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showNoInternetConnectionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No hay conexión a Internet");
        builder.setMessage("Por favor, verifica tu conexión a Internet e inténtalo de nuevo.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}