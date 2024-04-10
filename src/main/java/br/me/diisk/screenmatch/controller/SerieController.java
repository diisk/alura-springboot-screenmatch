package br.me.diisk.screenmatch.controller;

import br.me.diisk.screenmatch.dto.SerieDTO;
import br.me.diisk.screenmatch.model.Serie;
import br.me.diisk.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repositorio;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
        return repositorio.findAll()
                .stream().map(
                        serie->new SerieDTO(
                                serie.getId(),
                                serie.getTitulo(),
                                serie.getTotalTemporadas(),
                                serie.getAvaliacao(),
                                serie.getGeneros(),
                                serie.getAtores(),
                                serie.getPoster(),
                                serie.getSinopse()
                        )
                ).toList();
    }
}
