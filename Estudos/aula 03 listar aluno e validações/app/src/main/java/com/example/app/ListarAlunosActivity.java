package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {

    private ListView listView;
    private AlunoDao dao;
    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);
        //vincular variaveis com os campos do layout
        listView = findViewById(R.id.lista_alunos);
        dao = new AlunoDao(this);
        alunos = dao.obterTodos(); //todos alunos
        //ArrayAdapter já vem pronto no android para colocar essa lista de alunos na listview
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        //colocar na listView o adaptador
        listView.setAdapter(adaptador);
    }

    public void voltarParaCadastro(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}