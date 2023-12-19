package com.example.abuelup40;

// Importaciones necesarias
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

// Declaración de la clase BusquedaFragment que extiende de Fragment
public class BusquedaFragment extends Fragment implements OnMapReadyCallback {

    // Constante para solicitar permisos de ubicación
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    // Variables miembro
    private GoogleMap mMap;
    private List<Marker> markerList = new ArrayList<>();

    // Constructor vacío requerido por Fragment
    public BusquedaFragment() {
    }

    @Override
    // Método llamado para inflar el diseño del fragmento
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_busqueda, container, false);
    }

    @Override
    // Método llamado después de que la vista del fragmento ha sido creada
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el fragmento de mapa y registrar el callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar el Spinner
        Spinner residenceSpinner = view.findViewById(R.id.residenceSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new String[]{"Residencia 1", "Residencia 2"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        residenceSpinner.setAdapter(spinnerAdapter);

        // Set a selection listener for the spinner
        residenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selection change
                onResidenceSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    @Override
    // Método llamado cuando el mapa está listo para ser utilizado
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Habilitar la capa de ubicación del usuario
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Mostrar la ubicación más cercana al iniciar el mapa
            showNearestResidenceFromCurrentLocation();
        } else {
            // Solicitar permisos si no están concedidos
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        // Agregar marcadores de residencias
        LatLng residencia1 = new LatLng(-36.82852948021048, -73.05770757167747);
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(residencia1).title("Residencia 1"));
        marker1.setTag("Características\n\nNombre: Servicio Nacional Del Adulto Mayor\n\nTelefono: +56412909800\n\nDireccion:Serrano 537, 4030640 Concepción, Bío Bío\n\nHorarios:\nlunes, 9 a.m.–1:45 p.m.\nmartes, 9 a.m.–1:45 p.m.\nmiércoles, 9 a.m.–1:45 p.m.\njueves, 9 a.m.–1:45 p.m.\nviernes, 9 a.m.–1:45 p.m.\nsábado, Cerrado\ndomingo, Cerrado\n\nSitio Web: http://www.senama.gob.cl/");
        markerList.add(marker1);

        LatLng residencia2 = new LatLng(-36.84402475455069, -73.05012113936105);
        Marker marker2 = mMap.addMarker(new MarkerOptions().position(residencia2).title("Residencia 2"));
        marker2.setTag("Características\n\nNombre: Hogar Hermanitas de los Pobres\n\nTelefono: +56957242246\n\nDireccion: Av. Francesa 148, 4040385 Concepción, Bío Bío\n\nHorarios:\nlunes, 9 a.m.–6 p.m.\nmartes, 9 a.m.–6 p.m.\nmiércoles, 9 a.m.–6 p.m.\njueves, 9 a.m.–6 p.m.\nviernes, 9 a.m.–6 p.m.\nsábado, 9 a.m.–1 p.m.\ndomingo, Cerrado\n\nSitio Web: http://www.residenciadelvalle.cl/");
        markerList.add(marker2);

        // Mover la cámara al primer marcador con un zoom predeterminado
        float zoomLevel = 14.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(residencia1, zoomLevel));

        // Configurar el listener para los clics en los marcadores
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Mostrar las características de la residencia al hacer clic en el marcador
                showResidenceDetails(marker);
                return true;  // Devolver true para indicar que el evento de clic está manejado
            }
        });
    }

    // Método llamado cuando se selecciona una residencia en el Spinner
    private void onResidenceSelected(int position) {
        LatLng selectedLatLng;

        if (position == 0) {
            selectedLatLng = new LatLng(-36.82852948021048, -73.05770757167747); // Residencia 1
        } else {
            selectedLatLng = new LatLng(-36.84402475455069, -73.05012113936105); // Residencia 2
        }

        // Mover la cámara a la residencia seleccionada
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 14.0f));
    }

    // Método llamado cuando se hace clic en un marcador para mostrar detalles de la residencia
    private void showResidenceDetails(Marker marker) {
        // Obtener las características de la residencia desde el tag del marcador
        String characteristics = (String) marker.getTag();

        // Crear un nuevo fragmento de detalles y pasar las características como argumento
        ResidenceDetailsFragment detailsFragment = new ResidenceDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("characteristics", characteristics);
        detailsFragment.setArguments(bundle);

        // Reemplazar el fragmento actual con el fragmento de detalles
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)  // Agregar transacción a la pila para la navegación hacia atrás
                    .commit();
        }
    }

    // Método para obtener la residencia más cercana a la ubicación del usuario
    private void showNearestResidenceFromCurrentLocation() {
        // Verificar si los permisos de ubicación están concedidos
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Obtener la última ubicación conocida del usuario
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    // La ubicación del usuario está disponible, ahora puedes calcular la residencia más cercana
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    LatLng nearestResidenceLatLng = getNearestResidence(userLatLng);

                    // Mover la cámara al marcador de la residencia más cercana
                    if (nearestResidenceLatLng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestResidenceLatLng, 14.0f));

                        // Mostrar las direcciones desde la ubicación actual hasta la residencia más cercana
                        showDirections(userLatLng, nearestResidenceLatLng);
                    }
                }
            });
        } else {
            // Si los permisos de ubicación no están concedidos, solicitarlos nuevamente
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    // Método para obtener la residencia más cercana a una ubicación dada
    private LatLng getNearestResidence(LatLng userLocation) {
        LatLng nearestResidence = null;
        double minDistance = Double.MAX_VALUE;

        for (Marker marker : markerList) {
            LatLng residenceLocation = marker.getPosition();
            double distance = calculateDistance(userLocation, residenceLocation);

            if (distance < minDistance) {
                minDistance = distance;
                nearestResidence = residenceLocation;
            }
        }

        return nearestResidence;
    }

    // Método para calcular la distancia entre dos puntos en la Tierra utilizando la fórmula de Haversine
    private double calculateDistance(LatLng start, LatLng end) {
        double earthRadius = 6371; // Radio de la Tierra en kilómetros

        double lat1 = Math.toRadians(start.latitude);
        double lon1 = Math.toRadians(start.longitude);

        double lat2 = Math.toRadians(end.latitude);
        double lon2 = Math.toRadians(end.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    // Método para mostrar las direcciones desde la ubicación actual hasta la residencia más cercana
    private void showDirections(LatLng origin, LatLng destination) {
        // Crear una solicitud de direcciones
        DirectionsApiRequest directions = new DirectionsApiRequest(/* inicializa tu contexto aquí */)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .mode(TravelMode.DRIVING); // Puedes cambiar a WALKING, BICYCLING, etc.

        // Realizar la solicitud de direcciones de forma asíncrona
        directions.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if (result.routes != null && result.routes.length > 0) {
                    // Decodificar los puntos polilíneales y agregar la ruta al mapa
                    List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Manejar el error
            }
        });
    }

    @Override
    // Método llamado cuando se solicitan permisos de ubicación
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // Verificar si el permiso fue concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, actualizar la ubicación del usuario
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                    // Mostrar la ubicación más cercana al iniciar el mapa
                    showNearestResidenceFromCurrentLocation();
                }
            } else {
                // Permiso denegado, manejar este caso según tus necesidades
                // Puedes mostrar un mensaje al usuario o tomar alguna otra acción
            }
        }
    }
}



