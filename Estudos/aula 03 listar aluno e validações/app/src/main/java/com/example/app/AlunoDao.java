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
        if (!isCpfValido(aluno.getCpf())) {
            return -2;
        }

        if (existeCpf(aluno.getCpf())) {
            return -3;
        }

        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("cpf", aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("curso", aluno.getCurso());

        return banco.insert("aluno", null, values);
    }

    /**
     * Verifica se o CPF já existe no banco de dados.
     */
    public boolean existeCpf(String cpf) {
        String sql = "SELECT cpf FROM aluno WHERE cpf = ?";
        Cursor cursor = banco.rawQuery(sql, new String[]{cpf});

        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }

    /**
     * Validador matemático de CPF.
     */
    public boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                int num = (int) (cpf.charAt(i) - 48);
                soma = soma + (num * peso);
                peso--;
            }

            int resto = 11 - (soma % 11);
            char digito10 = (resto == 10 || resto == 11) ? '0' : (char) (resto + 48);

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                int num = (int) (cpf.charAt(i) - 48);
                soma = soma + (num * peso);
                peso--;
            }

            resto = 11 - (soma % 11);
            char digito11 = (resto == 10 || resto == 11) ? '0' : (char) (resto + 48);

            return (digito10 == cpf.charAt(9)) && (digito11 == cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    public List<Aluno> obterTodos(){
        List<Aluno> alunos = new ArrayList<>();
        //cursor aponta para as linhas retornadas
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone"},
                null, null,null,null,null); //nome da tabela, nome das colunas, completa com null o método
        //que por padrão pede esse número de colunas obrigatórias
        while(cursor.moveToNext()){ //verifica se consegue mover para o próximo ponteiro ou linha
            Aluno a = new Aluno();
            a.setId(cursor.getInt(0)); // new String[]{"id", "nome", "cpf", "telefone"}, id é coluna '0'
            a.setNome(cursor.getString(1)); // new String[]{"id", "nome", "cpf", "telefone"}, nome é coluna '1'
            a.setCpf(cursor.getString(2)); // new String[]{"id", "nome", "cpf", "telefone"}, cpf é coluna '2'
            a.setTelefone(cursor.getString(3)); // new String[]{"id", "nome", "cpf", "telefone"}, telefone é coluna '3'
            alunos.add(a);
        }
        return alunos;
    }

    /**
     * Validador de Telefone (NOVO)
     * Verifica se o formato é exatamente (XX) 9XXXX-XXXX
     */
    public boolean validaTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return false;
        }
        // A Regex abaixo exige: (2 dígitos) espaço 9 + 4 dígitos - 4 dígitos
        return telefone.matches("^\\(\\d{2}\\) 9\\d{4}-\\d{4}$");
    }
}
