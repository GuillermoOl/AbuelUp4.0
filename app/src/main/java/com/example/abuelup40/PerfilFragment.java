package com.example.abuelup40;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilFragment extends Fragment {

    private FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializar FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Obtener la referencia al usuario actual
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Obtener el ID del usuario actual
            String uid = currentUser.getUid();

            // Obtener la referencia al documento "Usuario"
            DocumentReference userRef = db.collection("Usuario").document(uid);

            // Obtener los TextView del diseño
            TextView textViewNombre = view.findViewById(R.id.txtNombre);
            TextView textViewEdad = view.findViewById(R.id.txtEdad);
            TextView textViewCorreo = view.findViewById(R.id.txtCorreo);

            // Obtener datos del usuario
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Obtener los datos del documento "Usuario"
                        String nombre = document.getString("Nombre");
                        String edad = document.getString("Edad");
                        String correo = document.getString("Correo");

                        // Actualizar los TextView con la información del usuario
                        textViewNombre.setText("Nombre: " + nombre);
                        textViewEdad.setText("Edad: " + edad);
                        textViewCorreo.setText("Correo: " + correo);
                    } else {
                        // El documento no existe
                        textViewNombre.setText("Usuario no encontrado");
                        textViewEdad.setText("");
                        textViewCorreo.setText("");
                    }
                } else {
                    // Error al obtener el documento
                    textViewNombre.setText("Error al cargar el usuario");
                    textViewEdad.setText("");
                    textViewCorreo.setText("");
                }
            });
        }

        return view;
    }
}