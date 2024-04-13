package br.me.diisk.screenmatch.service;

import br.me.diisk.screenmatch.dto.EpisodioDTO;
import br.me.diisk.screenmatch.dto.SerieDTO;
import br.me.diisk.screenmatch.model.Serie;
import br.me.diisk.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;


    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGeneros(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .toList();
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id, numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .toList();
    }

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGeneros(), s.getAtores(), s.getPoster(), s.getSinopse());
        }
        return null;
    }
}
