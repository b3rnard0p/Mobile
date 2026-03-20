package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private EditText endereco;
    private EditText curso;

    private AlunoDaoRoom alunoDaoRoom;
    private Aluno aluno = null;

    private ImageView imageView;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 200;

    private static final int REQUEST_CEP = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        imageView = findViewById(R.id.imageView);

        alunoDaoRoom = AppDatabase.getInstance(this).alunoDaoRoom();

        Intent it = getIntent();
        if(it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
            endereco.setText(aluno.getEndereco());
            curso.setText(aluno.getCurso());

            byte[] foto = aluno.getFotoBytes();
            if (foto != null && foto.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void abrirBuscaCep(View view) {
        Intent intent = new Intent(this, BuscarCepActivity.class);
        startActivityForResult(intent, REQUEST_CEP);
    }

    public void tirarFoto(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Log.e("CAMERA_DEBUG", "Erro: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissão necessária para a câmera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            if (aluno == null) aluno = new Aluno();
            aluno.setFotoBytes(byteArray);
        }

        if (requestCode == REQUEST_CEP && resultCode == RESULT_OK && data != null) {
            String enderecoCompleto = data.getStringExtra("enderecoCompleto");
            if (enderecoCompleto != null) {
                endereco.setText(enderecoCompleto);
            }
        }
    }

    public void salvar(View view) {
        String nomeDigitado = nome.getText().toString().trim();
        String cpfDigitado = cpf.getText().toString().trim();
        String telefoneDigitado = telefone.getText().toString().trim();
        String enderecoDigitado = endereco.getText().toString().trim();
        String cursoDigitado = curso.getText().toString().trim();

        if (nomeDigitado.isEmpty() || cpfDigitado.isEmpty() || telefoneDigitado.isEmpty() ||
                enderecoDigitado.isEmpty() || cursoDigitado.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!alunoDaoRoom.isCpfValido(cpfDigitado)) {
            Toast.makeText(this, "CPF inválido. Digite novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aluno == null || !cpfDigitado.equals(aluno.getCpf())) {
            // O Room retorna a contagem, então verificamos se é maior que 0
            if (alunoDaoRoom.cpfExistente(cpfDigitado) > 0) {
                Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!alunoDaoRoom.validaTelefone(telefoneDigitado)) {
            Toast.makeText(this, "Telefone inválido! Use o formato: (XX) 9XXXX-XXXX", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aluno == null || aluno.getId() == null) {
            if (aluno == null) {
                aluno = new Aluno();
            }

            aluno.setNome(nomeDigitado);
            aluno.setCpf(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            aluno.setEndereco(enderecoDigitado);
            aluno.setCurso(cursoDigitado);

            long id = alunoDaoRoom.inserir(aluno);

            if (id == -1) {
                Toast.makeText(this, "Erro ao inserir no banco de dados.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Aluno inserido com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            aluno.setNome(nomeDigitado);
            aluno.setCpf(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            aluno.setEndereco(enderecoDigitado);
            aluno.setCurso(cursoDigitado);

            alunoDaoRoom.atualizar(aluno);
            Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void irParaListar(View view) {
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }
}