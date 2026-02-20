package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

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
    private boolean existeCpf(String cpf) {
        String sql = "SELECT cpf FROM aluno WHERE cpf = ?";
        Cursor cursor = banco.rawQuery(sql, new String[]{cpf});

        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }

    /**
     * Validador matemático de CPF.
     */
    private boolean isCpfValido(String cpf) {
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
}
