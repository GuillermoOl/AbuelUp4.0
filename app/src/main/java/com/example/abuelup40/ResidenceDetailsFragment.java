package com.example.abuelup40;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResidenceDetailsFragment extends Fragment {

    public ResidenceDetailsFragment() {
        // Constructor vacío requerido por Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento de detalles
        View view = inflater.inflate(R.layout.fragment_residence_details, container, false);

        // Obtener las características de la residencia desde los argumentos
        String characteristics = getArguments().getString("characteristics", "");

        // Mostrar las características en un TextView
        TextView characteristicsTextView = view.findViewById(R.id.characteristicsTextView);
        characteristicsTextView.setText(characteristics);

        // Configurar el botón de cerrar
        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el fragmento
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }
}