package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {

    private ListView listView;
    private EditText editPesquisa; // Adicionado
    private AlunoDaoRoom alunoDaoRoom;
    private List<Aluno> alunosFiltrados = new ArrayList<>();
    private ArrayAdapter<Aluno> adaptador; // Promovido a escopo global da classe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);

        listView = findViewById(R.id.lista_alunos);

        // ATENÇÃO: Verifique se o ID no seu arquivo XML é "editPesquisa"
        editPesquisa = findViewById(R.id.editPesquisa);

        alunoDaoRoom = AppDatabase.getInstance(this).alunoDaoRoom();

        // Inicializa o adaptador com a lista vazia (como pede a atividade)
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        listView.setAdapter(adaptador);

        registerForContextMenu(listView);
    }

    // NOVO MÉTODO: Acionado ao clicar no botão Buscar
    public void pesquisarAluno(View view) {
        String termoBusca = editPesquisa.getText().toString().trim();
        alunosFiltrados.clear();

        if (termoBusca.isEmpty()) {
            // Se vazio, exibe todos
            alunosFiltrados.addAll(alunoDaoRoom.obterTodos());
        } else {
            // Se digitado, busca pelas iniciais
            alunosFiltrados.addAll(alunoDaoRoom.buscarPorNome(termoBusca));
        }

        // Atualiza a ListView
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }

    public void excluir(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o aluno?")
                .setNegativeButton("NÃO", null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alunosFiltrados.remove(alunoExcluir);
                        alunoDaoRoom.excluir(alunoExcluir);
                        adaptador.notifyDataSetChanged(); // Atualiza a lista na hora
                    }
                }).create();
        dialog.show();
    }

    public void atualizar(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Aluno alunoAtualizar = alunosFiltrados.get(menuInfo.position);

        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("aluno", alunoAtualizar);
        startActivity(it);
    }

    @Override
    protected void onResume() {
        super.onResume();
        alunosFiltrados.clear();
        if (editPesquisa != null) editPesquisa.setText("");
        if (adaptador != null) adaptador.notifyDataSetChanged();
    }

    public void voltarParaCadastro(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}