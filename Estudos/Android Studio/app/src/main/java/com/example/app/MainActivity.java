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

    public void salvar(View view){
        Aluno a = new Aluno();

        a.setNome(nome.getText().toString());
        a.setCpf(cpf.getText().toString());
        a.setTelefone(telefone.getText().toString());
        a.setEndereco(endereco.getText().toString());
        a.setCurso(curso.getText().toString()); 

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
}
