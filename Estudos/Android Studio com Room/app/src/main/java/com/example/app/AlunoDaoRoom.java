package com.example.app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlunoDaoRoom {

    @Insert
    long inserir(Aluno aluno);

    @Update
    void atualizar(Aluno aluno);

    @Query("SELECT * FROM aluno")
    List<Aluno> obterTodos();

    @Delete
    void excluir(Aluno aluno);

    @Query("SELECT COUNT(*) FROM aluno WHERE cpf = :cpf")
    int cpfExistente(String cpf);

    default boolean isCpfValido(String cpf) {
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

    default boolean validaTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) return false;
        return telefone.replaceAll("[^0-9]", "").matches("^\\d{2}9\\d{8}$");
    }
}