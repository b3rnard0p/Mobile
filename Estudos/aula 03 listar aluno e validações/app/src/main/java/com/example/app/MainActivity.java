package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private EditText endereco;
    private EditText curso;

    private AlunoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        dao = new AlunoDao(this);
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

        if (!dao.isCpfValido(cpfDigitado)) {
            Toast.makeText(this, "CPF inválido. Digite novamente.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dao.existeCpf(cpfDigitado)) {
            Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dao.validaTelefone(telefoneDigitado)) {
            Toast.makeText(this, "Telefone inválido! Use o formato: (XX) 9XXXX-XXXX", Toast.LENGTH_SHORT).show();
            return;
        }
        Aluno a = new Aluno();
        a.setNome(nomeDigitado);
        a.setCpf(cpfDigitado);
        a.setTelefone(telefoneDigitado);
        a.setEndereco(enderecoDigitado);
        a.setCurso(cursoDigitado);

        long id = dao.inserir(a);

        if (id == -2) {
            Toast.makeText(this, "Erro: O CPF informado é inválido.", Toast.LENGTH_LONG).show();
        }
        else if (id == -3) {
            Toast.makeText(this, "Erro: Este CPF já está cadastrado no sistema.", Toast.LENGTH_LONG).show();
        }
        else if (id != -1) {
            Toast.makeText(this, "Aluno inserido com sucesso! ID: " + id, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Erro desconhecido ao salvar no banco de dados.", Toast.LENGTH_LONG).show();
        }
    }

    public void irParaListar(View view) {
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }
}
