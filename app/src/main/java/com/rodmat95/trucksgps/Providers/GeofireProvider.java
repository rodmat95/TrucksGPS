package com.rodmat95.trucksgps.Providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {
    private DatabaseReference mDatabase;
    private GeoFire mGeofire;
    private String mOwnUserId;
    private AuthProvider mAuthProvider;

    public GeofireProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios activos");
        mGeofire = new GeoFire(mDatabase);
        mAuthProvider = new AuthProvider();
        mOwnUserId = obtenerIdPropioDispositivo();
    }

    public void saveLocation(String inUser, LatLng latLng) {
        mGeofire.setLocation(inUser, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public GeoQuery getActiveUsers(LatLng latLng) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 650);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

    private String obtenerIdPropioDispositivo() {
        return mAuthProvider.getUserUid();
    }

    public boolean esClaveDiferenteDePropioUsuario(String userId) {
        return !userId.equals(mOwnUserId);
    }
}