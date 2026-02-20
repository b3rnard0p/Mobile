package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private EditText endereco; // Novo componente
    private EditText curso;    // Novo componente

    private AlunoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        // Presumindo que você colocou os IDs editEndereco e editCurso no seu XML
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        dao = new AlunoDao(this);
    }

    public void salvar(View view){
        Aluno a = new Aluno();

        a.setNome(nome.getText().toString());
        a.setCpf(cpf.getText().toString());
        a.setTelefone(telefone.getText().toString());
        a.setEndereco(endereco.getText().toString()); // Pegando o texto novo
        a.setCurso(curso.getText().toString());       // Pegando o texto novo

        long id = dao.inserir(a);

        Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
    }
}