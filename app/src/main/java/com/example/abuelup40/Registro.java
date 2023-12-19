package com.example.abuelup40;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    Button registrarse;
    EditText correo, edad, nombre, contrasena, confContrasena;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.setTitle("Registro de Usuario");

        // Verifica si la barra de acción es nula antes de configurarla
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();

        // Inicializa los elementos de la interfaz de usuario
        registrarse = findViewById(R.id.buttonRegister);
        correo = findViewById(R.id.editTextFullName);
        edad = findViewById(R.id.editTextAge);
        nombre = findViewById(R.id.editTextNewUsername);
        contrasena = findViewById(R.id.editTextComfirmNewPassword);
        confContrasena = findViewById(R.id.editTextConfirmPassword);

        registrarse.setOnClickListener(v -> realizarRegistro());
    }

    private void realizarRegistro() {
        String correoStr = correo.getText().toString().trim();
        String edadStr = edad.getText().toString().trim();
        String nombreStr = nombre.getText().toString().trim();
        String contrasenaStr = contrasena.getText().toString().trim();
        String confContrasenaStr = confContrasena.getText().toString().trim();

        // Mostrar mensaje de error si algún campo está vacío
        if (correoStr.isEmpty() || edadStr.isEmpty() || nombreStr.isEmpty() || contrasenaStr.isEmpty() || confContrasenaStr.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar mensaje de error si el correo no cumple con el formato
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correoStr).matches()) {
            Toast.makeText(getApplicationContext(), "El formato del correo no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasenaStr.equals(confContrasenaStr)) {
            // Contraseñas coinciden, proceder con el registro
            registrarUsuarioFirebase(correoStr, contrasenaStr);
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuarioFirebase(String correo, String contrasena) {
        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(getApplicationContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                    // Puedes agregar más lógica aquí según tus necesidades
                    // Por ejemplo, guardar otros detalles del usuario en Firestore
                    agregarUsuarioFirestore(correo, edad.getText().toString(), nombre.getText().toString());
                    // Navegar al otro diseño al hacer clic en el botón=ActivityMain
                    startActivity(new Intent(Registro.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void agregarUsuarioFirestore(String correo, String edad, String nombre) {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("Correo", correo);
        usuarioMap.put("Edad", edad);
        usuarioMap.put("Nombre", nombre);

        mFirestore.collection("Usuario").document(mAuth.getCurrentUser().getUid())
                .set(usuarioMap)
                .addOnSuccessListener(aVoid -> Log.d("Registro", "Datos del usuario agregados a Firestore"))
                .addOnFailureListener(e -> Log.e("Registro", "Error al agregar datos del usuario a Firestore: " + e.getMessage()));
    }
}