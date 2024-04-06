package br.me.diisk.screenmatch.model;

import java.util.Arrays;
import java.util.Optional;

public enum Genero {
    ACAO("Action"),
    AVENTURA("Adventure"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    DRAMA("Drama"),
    FANTASIA("Fantasy"),
    ROMANCE("Romance"),
    TERROR("Horror"),
    MISTERIO("Mystery"),
    ;

    private String generoOmdb;

    Genero(String generoOmdb){
        this.generoOmdb = generoOmdb;
    }

    public static Optional<Genero> fromPortugues(String text) {
        for (Genero categoria : Genero.values()) {
            if (categoria.name().equalsIgnoreCase(text)) {
                return Optional.of(categoria);
            }
        }
        return Optional.empty();
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
