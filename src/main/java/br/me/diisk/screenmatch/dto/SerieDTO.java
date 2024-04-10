package br.me.diisk.screenmatch.dto;

import br.me.diisk.screenmatch.model.Genero;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

public record SerieDTO(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double avaliacao,
        List<Genero> generos,
        List<String> atores,
        String poster,
        String sinopse
) {

}
