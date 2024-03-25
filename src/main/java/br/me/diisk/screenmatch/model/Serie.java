package br.me.diisk.screenmatch.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @ElementCollection(targetClass = Genero.class)
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.JOIN)
    private List<Genero> generos;
    @ElementCollection
    @CollectionTable(name = "atores", joinColumns = @JoinColumn(name = "serie_id"))
    @Column(name = "actor")
    @Fetch(FetchMode.JOIN)
    private List<String> atores;
    private String poster;
    private String sinopse;

    @Transient
    private List<Episodio> episodios = new ArrayList<>();

    public Serie(){}

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.generos = Arrays.stream(dadosSerie.generos().split(",")).map(Genero::getByOmdbName).toList();
        this.atores = Arrays.stream(dadosSerie.atores().split(",")).map(String::trim).toList();
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    public List<String> getAtores() {
        return atores;
    }

    public void setAtores(List<String> atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return "titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", generos=" + generos +
                ", atores=" + atores +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';
    }
}
