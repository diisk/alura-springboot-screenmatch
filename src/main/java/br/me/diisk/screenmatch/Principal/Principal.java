package br.me.diisk.screenmatch.Principal;

import br.me.diisk.screenmatch.model.*;
import br.me.diisk.screenmatch.repository.SerieRepository;
import br.me.diisk.screenmatch.service.ConsumoAPI;
import br.me.diisk.screenmatch.service.ConverteDados;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/";
    private final String API_KEY = "6585022c";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    private static List<Serie> series = new ArrayList<>();

    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio){
        this.repositorio = repositorio;
    }

    public void exibeMenu(){
        var menuText = """
                Selecione uma opção:
                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries adicionadas
                4 - Buscar série no banco de dados
                5 - Buscar série personalizado
                6 - Buscar top 5 séries
                7 - Buscar por gênero
                8 - Filtrar series
                0 - Sair
                """;
        int selectedOption = -1;
        do{
            System.out.println(menuText);
            selectedOption = scanner.nextInt();
            scanner.nextLine();
            switch(selectedOption){
                case 1:
                    buscarSerieWeb();

                    break;
                case 2:
                    buscarEpisodiosPorSerie();
                    break;
                case 3:
                    listarSeriesAdicionadas();
                    break;
                case 4:
                    buscarSerieBanco();
                    break;
                case 5:
                    buscarSeriePersonalizado();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    buscarSeriesPorTemporadasAvaliacao();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
            System.out.println("----- Fim -----");
        }while(selectedOption!=0);
    }

    private void buscarSeriesPorTemporadasAvaliacao() {
        System.out.println("Digite o máximo de temporadas desejado:");
        Integer totalTemporadas = scanner.nextInt();
        System.out.println("Digite o minimo de avaliação necessário:");
        double avaliacao = scanner.nextDouble();
        List<Serie> series = repositorio.seriesPorTemporadaAvaliacao(totalTemporadas,avaliacao);
        if(series.isEmpty()){
            System.out.println("Nenhuma série foi encontrada.");
            return;
        }
        series.forEach(System.out::println);
    }

    private void buscarSeriePorGenero() {
        System.out.println("Digite o gênero desejado:");
        String generoName = scanner.nextLine();
        Optional<Genero> genero = Genero.fromPortugues(generoName);
        if(genero.isEmpty()){
            System.out.println("Gênero não encontrado!");
            return;
        }
        List<Serie> series = repositorio.findByGenerosContains(genero.get());
        series.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        series.forEach(System.out::println);
    }

    private void buscarSeriePersonalizado() {
        System.out.println("Digite o nome do ator:");
        String nomeAtor = scanner.nextLine();
        System.out.println("Digite um número de avaliação:");
        Double avaliacao = scanner.nextDouble();
        List<Serie> series = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacao);
        series.forEach(serie-> System.out.println("Title: "+serie.getTitulo()+" rate: "+serie.getAvaliacao()));
    }

    private void buscarSerieWeb(){
        System.out.println("Digite o nome da serie desejada:");
        String nome = scanner.nextLine();
        var json = consumoAPI.obterDados(getURL(nome));
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        if(dados.titulo()==null){
            System.out.println("\""+nome+"\" não encontrado.");
            return;
        }
        repositorio.save(new Serie(dados));
        System.out.println("\""+dados.titulo()+"\" adicionada com sucesso!");
    }

    private void buscarSerieBanco(){
        System.out.println("Digite o nome da serie desejada:");
        String nome = scanner.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nome);
        if(serieBuscada.isEmpty()){
            System.out.println("Série não encontrada!");
            return;
        }

        System.out.println("Dados da série: "+serieBuscada.get());
    }

    private void buscarEpisodiosPorSerie(){
        listarSeriesAdicionadas();
        System.out.println("Escolha uma série pelo nome:");
        var nomeSerie = scanner.nextLine();
        Optional<Serie> serieEncontrada = series.stream()
                .filter(
                        serie->serie.getTitulo().toLowerCase()
                                .contains(nomeSerie.toLowerCase())
                        )
                .findFirst();

        if(serieEncontrada.isEmpty()){
            System.out.println("Série não encontrada!");
            return;
        }

        Serie serie = serieEncontrada.get();
        List<Episodio> episodios = new ArrayList<>();
        for(int i=1;i<=serie.getTotalTemporadas();i++){
            var json = consumoAPI.obterDados(getURL(serie.getTitulo(),i));
            DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
            //System.out.println("----- Temporada "+dadosTemporada.numero()+" - "+dadosTemporada.episodios().size()+" ep(s) -----");
            episodios.addAll(
                dadosTemporada.episodios()
                    .stream().map(ep->new Episodio(ep.numero(),ep))
                        .toList()
            );
        }
        serie.addEpisodios(episodios);
        repositorio.save(serie);
    }

    private void listarSeriesAdicionadas(){
        System.out.println("----- Séries adicionadas -----");
        series = repositorio.findAll();
        if(series.isEmpty()){
            System.out.println("Não há séries na lista.");
            return;
        }
        series.forEach(System.out::println);
    }

    private String getURL(String nome){
        return getURL(nome,-1);
    }

    private String getURL(String nome, int season){
        StringBuilder builder = new StringBuilder(ENDERECO);

        builder.append("?t=");
        try {
            builder.append(URLEncoder.encode(nome,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if(season>0)
            builder.append("&season=").append(season);

        builder.append("&apikey=").append(API_KEY);

        return builder.toString();
    }
}
