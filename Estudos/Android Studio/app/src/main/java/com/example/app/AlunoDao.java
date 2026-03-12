package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class AlunoDao {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public AlunoDao(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Aluno aluno){
        if (!isCpfValido(aluno.getCpf())) return -2;
        if (existeCpf(aluno.getCpf())) return -3;

        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("curso", aluno.getCurso());
        values.put("fotoBytes", aluno.getFotoBytes()); // Persistindo a foto [cite: 194]

        return banco.insert("aluno", null, values);
    }

    public void atualizar(Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("curso", aluno.getCurso());
        values.put("fotoBytes", aluno.getFotoBytes());

        banco.update("aluno", values, "id = ?", new String[]{aluno.getId().toString()});
    }

    public List<Aluno> obterTodos(){
        List<Aluno> alunos = new ArrayList<>();
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone", "endereco", "curso", "fotoBytes"},
                null, null,null,null,null);

        while(cursor.moveToNext()){
            Aluno a = new Aluno();
            a.setId(cursor.getInt(0));
            a.setNome(cursor.getString(1));
            a.setCpf(cursor.getString(2));
            a.setTelefone(cursor.getString(3));
            a.setEndereco(cursor.getString(4));
            a.setCurso(cursor.getString(5));
            a.setFotoBytes(cursor.getBlob(6));
            alunos.add(a);
        }
        cursor.close();
        return alunos;
    }

    public void excluir(Aluno a){
        banco.delete("aluno", "id = ?", new String[]{a.getId().toString()});
    }

    public boolean existeCpf(String cpf) {
        String sql = "SELECT cpf FROM aluno WHERE cpf = ?";
        Cursor cursor = banco.rawQuery(sql, new String[]{cpf});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public boolean isCpfValido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int soma = 0, peso = 10;
            for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - 48) * peso--;
            int resto = 11 - (soma % 11);
            char dig10 = (resto > 9) ? '0' : (char)(resto + 48);
            soma = 0; peso = 11;
            for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - 48) * peso--;
            resto = 11 - (soma % 11);
            char dig11 = (resto > 9) ? '0' : (char)(resto + 48);
            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        } catch (Exception e) { return false; }
    }

    public boolean validaTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) return false;
        return telefone.replaceAll("[^0-9]", "").matches("^\\d{2}9\\d{8}$");
    }
}