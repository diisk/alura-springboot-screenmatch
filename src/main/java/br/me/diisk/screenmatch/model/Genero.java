package br.me.diisk.screenmatch.model;

import java.util.Arrays;

public enum Genero {
    ACAO("Action"),
    AVENTURA("Adventure"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    ROMANCE("Romance"),
    TERROR("Horror"),
    ;

    private String generoOmdb;

    Genero(String generoOmdb){
        this.generoOmdb = generoOmdb;
    }

    public static Genero fromPortugues(String text) {
        for (Genero categoria : Genero.values()) {
            if (categoria.name().equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Genero getByOmdbName(String text){
        var retornoGenero = Arrays.stream(values())
                .filter(genero->genero.generoOmdb.equalsIgnoreCase(text.trim()))
                .findFirst();
        if(retornoGenero.isPresent())
            return retornoGenero.get();

        throw new IllegalArgumentException("Categoria não encontrada: " + text);
    }

}
