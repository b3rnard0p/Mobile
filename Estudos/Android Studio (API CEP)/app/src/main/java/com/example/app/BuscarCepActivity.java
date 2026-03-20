package com.example.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscarCepActivity extends AppCompatActivity {

    private EditText editCEP, editLogradouro, editBairro, editCidade, editEstado, editNumero, editComplemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cep);

        editCEP = findViewById(R.id.editCEP);
        editLogradouro = findViewById(R.id.editLogradouro);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editEstado = findViewById(R.id.editEstado);

        editNumero = findViewById(R.id.editNumero);
        editComplemento = findViewById(R.id.editComplemento);
    }

    public void buscarCep(View view) {
        String cep = editCEP.getText().toString().trim();
        if (cep.isEmpty() || cep.length() != 8) {
            Toast.makeText(this, "Digite um CEP válido!", Toast.LENGTH_SHORT).show();
            return;
        }

        new BuscarCepTask().execute(cep);
    }

    private class BuscarCepTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = "https://viacep.com.br/ws/" + params[0] + "/json/";
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) return;

            try {
                JSONObject jsonObject = new JSONObject(result);

                if (jsonObject.has("erro")) {
                    Toast.makeText(BuscarCepActivity.this, "CEP não existe!", Toast.LENGTH_SHORT).show();
                } else {
                    editLogradouro.setText(jsonObject.getString("logradouro"));
                    editBairro.setText(jsonObject.getString("bairro"));
                    editCidade.setText(jsonObject.getString("localidade"));
                    editEstado.setText(jsonObject.getString("uf"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void confirmarEndereco(View view) {
        String complementoDigitado = editComplemento.getText().toString().trim();
        String strComplemento = "";
        if (!complementoDigitado.isEmpty()) {
            strComplemento = ", " + complementoDigitado;
        }

        String enderecoCompleto = editLogradouro.getText().toString() + ", " +
                editNumero.getText().toString() +
                strComplemento + " - " +
                editBairro.getText().toString() + ", " +
                editCidade.getText().toString() + " - " +
                editEstado.getText().toString();

        // Cria a Intent de retorno para a MainActivity
        Intent resultado = new Intent();
        resultado.putExtra("enderecoCompleto", enderecoCompleto);
        setResult(RESULT_OK, resultado);
        finish();
    }
}