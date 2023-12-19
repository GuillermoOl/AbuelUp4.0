package com.example.abuelup40;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Inicio extends AppCompatActivity {

    private InicioFragment inicio;
    private BusquedaFragment busqueda;
    private PerfilFragment perfil;

    private static final int ID_INICIO = R.id.navigation_inicio;
    private static final int ID_BUSQUEDA = R.id.navigation_busqueda;
    private static final int ID_PERFIL = R.id.navigation_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        inicio = new InicioFragment();
        busqueda = new BusquedaFragment();
        perfil = new PerfilFragment();

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            loadFragment(inicio);
        }
    }

    private final BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == ID_INICIO) {
                selectedFragment = inicio;
            } else if (itemId == ID_BUSQUEDA) {
                selectedFragment = busqueda;
            } else if (itemId == ID_PERFIL) {
                selectedFragment = perfil;
            } else {
                Log.e("Inicio", "bottom_navigation");
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commitNow();

            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}