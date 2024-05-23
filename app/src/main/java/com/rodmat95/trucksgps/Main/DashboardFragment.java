package com.rodmat95.trucksgps.Main;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseError;
import com.rodmat95.trucksgps.Main.Dashboard.CreateNewRouteFragment;
import com.rodmat95.trucksgps.Main.Dashboard.DriversAvailableFragment;
import com.rodmat95.trucksgps.Main.Dashboard.NotificationsActivity;
import com.rodmat95.trucksgps.Main.Dashboard.RouteHistoryFragment;
import com.rodmat95.trucksgps.Main.Dashboard.RoutesInCourseFragment;
import com.rodmat95.trucksgps.Main.Dashboard.ScheduleMaintenanceFragment;
import com.rodmat95.trucksgps.Main.Dashboard.ScheduledRoutesFragment;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.UsersFragment;
import com.rodmat95.trucksgps.Main.Dashboard.TrucksAvailableFragment;
import com.rodmat95.trucksgps.Providers.AuthProvider;
import com.rodmat95.trucksgps.Providers.GeofireProvider;
import com.rodmat95.trucksgps.R;
import com.rodmat95.trucksgps.Main.Dashboard.SlideBar.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private ImageButton btnDrawerToggle;
    private ImageButton btnNotification;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private View headerView;
    private ImageView logoImage;
    private TextView textAppName;

    private RelativeLayout layoutMap;
    private ImageView btnMaximizeMap;
    private boolean maximized = false;

    private CardView btnScheduledRoutes;
    private CardView btnRoutesInCourse;
    private CardView btnRouteHistory;
    private CardView btnCreateNewRoute;
    private CardView btnTrucksAvailable;
    private CardView btnDriversAvailable;
    private CardView btnScheduleMaintenance;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private GeofireProvider mGeofireProvider;

    private FusedLocationProviderClient mFusedLocation;
    private LocationRequest mLocationRequest;

    private final static int LOCATION_REQUEST_CODE = 1;
    private static final int SETTINGS_REQUEST_CODE = 2;

    private LatLng mCurrentLatLng;

    private List<Marker> mUsersMarkers = new ArrayList<>();

    private boolean mIsFirstTime = true;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getContext() != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));

                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        getActiveUsers();
                    }

                    updateLocation();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        btnDrawerToggle = view.findViewById(R.id.btnDrawerToggle);
        btnNotification = view.findViewById(R.id.btnNotification);

        drawerLayout = view.findViewById(R.id.drawerLayout);
        navigationView = view.findViewById(R.id.navigationView);

        headerView = navigationView.getHeaderView(0);
        logoImage = headerView.findViewById(R.id.logoImage);
        textAppName = headerView.findViewById(R.id.textAppName);

        layoutMap = view.findViewById(R.id.layoutMap);
        btnMaximizeMap = view.findViewById(R.id.btnMaximizeMap);

        btnScheduledRoutes = view.findViewById(R.id.btnScheduledRoutes);
        btnRoutesInCourse = view.findViewById(R.id.btnRoutesInCourse);
        btnRouteHistory = view.findViewById(R.id.btnRouteHistory);
        btnCreateNewRoute = view.findViewById(R.id.btnCreateNewRoute);
        btnTrucksAvailable = view.findViewById(R.id.btnTrucksAvailable);
        btnDriversAvailable = view.findViewById(R.id.btnDriversAvailable);
        btnScheduleMaintenance = view.findViewById(R.id.btnScheduleMaintenance);

        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mAuthProvider = new AuthProvider();
        mGeofireProvider = new GeofireProvider();

        mFusedLocation = LocationServices.getFusedLocationProviderClient(requireContext());

        openMenu();
        openNotification();
        clickLogo(logoImage, textAppName);
        menuSelection();
        setupMaximizeBtnMap(layoutMap, btnMaximizeMap);
        createMapFragment();

        btnScheduledRoutes.setOnClickListener(v -> openFragment(new ScheduledRoutesFragment()));
        btnRoutesInCourse.setOnClickListener(v -> openFragment(new RoutesInCourseFragment()));
        btnRouteHistory.setOnClickListener(v -> openFragment(new RouteHistoryFragment()));
        btnCreateNewRoute.setOnClickListener(v -> openFragment(new CreateNewRouteFragment()));
        btnTrucksAvailable.setOnClickListener(v -> openFragment(new TrucksAvailableFragment()));
        btnDriversAvailable.setOnClickListener(v -> openFragment(new DriversAvailableFragment()));
        btnScheduleMaintenance.setOnClickListener(v -> openFragment(new ScheduleMaintenanceFragment()));

        return view;
    }

    private void openMenu() {
        btnDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void openNotification() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickLogo(ImageView logo_image, TextView text_app_name) {
        logo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), text_app_name.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void menuSelection() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navOptn1) {
                    Toast.makeText(getContext(), "Opción Establecer Sede", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (itemId == R.id.navOptn2) {
                    Toast.makeText(getContext(), "Opción Cambiar Iconos", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (itemId == R.id.navOptn3) {
                    Toast.makeText(getContext(), "Opción Reporte de Incidencias", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (itemId == R.id.navOptn4) {
                    Toast.makeText(getContext(), "Opción Estadísticas y Reportes", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (itemId == R.id.navOptn5) {
                    UsersFragment usersFragment = new UsersFragment();

                    FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, usersFragment).addToBackStack(null).commit();

                    return true;
                }

                if (itemId == R.id.navOptn6) {
                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    startActivity(intent);

                    return true;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void setupMaximizeBtnMap(final RelativeLayout layoutMap, final ImageView btnMaximizeMap) {
        btnMaximizeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximized = !maximized;

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutMap.getLayoutParams();
                if (maximized) {
                    params.removeRule(RelativeLayout.BELOW);
                    params.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                    int leftMargin = dpToPx(0, requireContext());
                    int topMargin = dpToPx(0, requireContext());
                    int rightMargin = dpToPx(0, requireContext());
                    int bottomMargin = dpToPx(15, requireContext());
                    params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    layoutMap.setLayoutParams(params);
                } else {
                    params.addRule(RelativeLayout.BELOW, R.id.textViewTitulo);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    int leftMargin = dpToPx(25, requireContext());
                    int topMargin = dpToPx(15, requireContext());
                    int rightMargin = dpToPx(25, requireContext());
                    int bottomMargin = dpToPx(15, requireContext());
                    params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    layoutMap.setLayoutParams(params);
                }

                if (maximized) {
                    btnMaximizeMap.setImageResource(R.drawable.ic_fullscreen_exit_gray_24dp);
                } else {
                    btnMaximizeMap.setImageResource(R.drawable.ic_fullscreen_gray_24dp);
                }
            }
        });
    }

    private void createMapFragment() {
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        } else {
            Log.d("DEBUG", "Error al cargar el mapa, fragmento de mapa es nulo");
            Toast.makeText(getContext(), "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
        }
    }

    private BitmapDescriptor getMarkerIcon() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_office_light_map);
        } else {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_office_dark_map);
        }
    }

    private void setMapMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_dark);
            mMap.setMapStyle(style);
        } else {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_light);
            mMap.setMapStyle(style);
        }
    }

    private void createMarker() {
        LatLng coordinates = new LatLng(-12.0546633, -76.9706688);
        MarkerOptions marker = new MarkerOptions()
                .position(coordinates)
                .title("Pacific Deep Frozen")
                .icon(getMarkerIcon());
        mMap.addMarker(marker);
    }

    private void setMarkerClickListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if ("Camion".equals(marker.getTitle())) {
                    LatLng position = marker.getPosition();
                    String coordinates = position.latitude + ", " + position.longitude;

                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Coordenadas", coordinates);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getContext(), "El camión está en: " + coordinates, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void mapLocation() {
        if (!mMap.isMyLocationEnabled()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private void updateLocation() {
        if (mAuthProvider.existSession() && mCurrentLatLng != null) {
            mGeofireProvider.saveLocation(mAuthProvider.getUserUid(), mCurrentLatLng);
        }
    }

    private void getActiveUsers() {
        mGeofireProvider.getActiveUsers(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (mGeofireProvider.esClaveDiferenteDePropioUsuario(key)) {
                    boolean marcadorExistente = false;
                    for (Marker marker : mUsersMarkers) {
                        if (marker.getTag() != null && marker.getTag().equals(key)) {
                            marcadorExistente = true;
                            break;
                        }
                    }

                    if (!marcadorExistente) {
                        LatLng userLatLng = new LatLng(location.latitude, location.longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(userLatLng)
                                .title("Camion")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_truck_3_map)));
                        marker.setTag(key);
                        mUsersMarkers.add(marker);
                    }
                }
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker marker: mUsersMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            marker.remove();
                            mUsersMarkers.remove(marker);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker marker: mUsersMarkers) {
                    if (marker.getTag() != null) {
                        if (marker.getTag().equals(key)) {
                            marker.setPosition(new LatLng(location.latitude, location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapMode();
        createMarker();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        setMarkerClickListener();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        starLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mapLocation();
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mapLocation();
        } else {
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void starLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mapLocation();
                } else {
                    showAlertDialogNOGPS();
                }
            } else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mapLocation();
            } else {
                showAlertDialogNOGPS();
            }
        }
    }

    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicación requiere de los permisos de ubicación para poder utilizarse.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}