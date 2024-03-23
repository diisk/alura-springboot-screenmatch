package br.me.diisk.screenmatch.model;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

public class Serie {

    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private List<Genero> generos;
    private List<String> atores;
    private String poster;
    private String sinopse;

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.generos = Arrays.stream(dadosSerie.generos().split(",")).map(Genero::getByOmdbName).toList();
        this.atores = Arrays.stream(dadosSerie.atores().split(",")).toList();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
    }
}
