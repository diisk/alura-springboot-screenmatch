package br.me.diisk.screenmatch;

import br.me.diisk.screenmatch.Principal.Principal;
import br.me.diisk.screenmatch.model.DadosEpisodio;
import br.me.diisk.screenmatch.model.DadosSerie;
import br.me.diisk.screenmatch.model.DadosTemporada;
import br.me.diisk.screenmatch.repository.SerieRepository;
import br.me.diisk.screenmatch.service.ConsumoAPI;
import br.me.diisk.screenmatch.service.ConverteDados;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;

	@Value("${spring.datasource.url}") // Replace "your.property.name" with the actual property name
	private String propertyValue;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(propertyValue);


		Principal principal = new Principal(repositorio);
		principal.exibeMenu();


	}
}
