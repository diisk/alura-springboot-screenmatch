package br.me.diisk.screenmatch.Principal;

import br.me.diisk.screenmatch.model.DadosSerie;
import br.me.diisk.screenmatch.model.DadosTemporada;
import br.me.diisk.screenmatch.service.ConsumoAPI;
import br.me.diisk.screenmatch.service.ConverteDados;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/";
    private final String API_KEY = "6585022c";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();
    public void exibeMenu(){
        System.out.println("Digite o nome da serie:");
        String nome = scanner.nextLine();
        var json = consumoAPI.obterDados(getURL(nome));
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<DadosTemporada>();
        for(int i=1;i<=dados.totalTemporadas();i++){
            json = consumoAPI.obterDados(getURL(nome,i));
            DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
            temporadas.add(dadosTemporada);
            System.out.println("Temporada "+dadosTemporada.numero()+" - "+dadosTemporada.episodios().size()+" ep(s)");
            dadosTemporada.episodios().forEach(ep-> System.out.println(ep.titulo()));
        }
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
