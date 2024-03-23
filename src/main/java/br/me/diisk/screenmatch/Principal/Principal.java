package br.me.diisk.screenmatch.Principal;

import br.me.diisk.screenmatch.model.DadosEpisodio;
import br.me.diisk.screenmatch.model.DadosSerie;
import br.me.diisk.screenmatch.model.DadosTemporada;
import br.me.diisk.screenmatch.model.Episodio;
import br.me.diisk.screenmatch.service.ConsumoAPI;
import br.me.diisk.screenmatch.service.ConverteDados;

import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/";
    private final String API_KEY = "6585022c";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    public void exibeMenu(){
        var menuText = """
                Selecione uma opção:
                
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries adicionadas
                
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
                    break;
                case 3:
                    listarSeriesAdicionadas();
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
        System.out.println("Digite o nome da serie desejada:");
        String nome = scanner.nextLine();
        var json = consumoAPI.obterDados(getURL(nome));
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<DadosTemporada>();
        for(int i=1;i<=dados.totalTemporadas();i++){
            json = consumoAPI.obterDados(getURL(nome,i));
            DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
            temporadas.add(dadosTemporada);
            System.out.println("----- Temporada "+dadosTemporada.numero()+" - "+dadosTemporada.episodios().size()+" ep(s) -----");
            dadosTemporada.episodios().forEach(ep-> System.out.println(ep.titulo()));
        }

        List<Episodio> episodios = temporadas.stream()
                .flatMap(
                        temp->temp.episodios().stream()
                                .map(dadosEp->new Episodio(temp.numero(),dadosEp))
                )
                .toList();

        Map<Integer,Double> avaliacoesPorTemporada = episodios.stream()
                .filter(ep->ep.getAvaliacao()>0)
                        .collect(Collectors.groupingBy(
                                Episodio::getTemporada,
                                Collectors.averagingDouble(Episodio::getAvaliacao))
                        );

        System.out.println(">>>>> Avaliações por temporada <<<<<");
        System.out.println(avaliacoesPorTemporada);
        DoubleSummaryStatistics statistics = episodios.stream()
                .filter(ep->ep.getAvaliacao()>0)
                        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println(statistics);

        System.out.println(">>>>> Top 10 Eps <<<<<");
        episodios.stream()
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(10)
                .forEach(System.out::println);

        System.out.println("Digite o trecho do titulo de um episodio:");
        var trecho = scanner.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(ep -> ep.getTitulo().toUpperCase().contains(trecho.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("----- Episodio encontrado! -----");
            System.out.println(episodioBuscado.get());
        }else{
            System.out.println("----- Episodio não encontrado! -----");
        }
    }

    private void buscarSerieWeb(){
        System.out.println("Digite o nome da serie desejada:");
        String nome = scanner.nextLine();
        var json = consumoAPI.obterDados(getURL(nome));
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        dadosSeries.add(dados);
        System.out.println("\""+dados.titulo()+"\" adicionada com sucesso!");
    }

    private void listarSeriesAdicionadas(){
        System.out.println("----- Séries adicionadas -----");
        if(dadosSeries.isEmpty()){
            System.out.println("Não há séries na lista.");
            return;
        }
        dadosSeries.forEach(dados-> System.out.println(dados.titulo()));
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
