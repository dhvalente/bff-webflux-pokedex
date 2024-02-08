package br.com.webflux.pokedex;

import br.com.webflux.pokedex.model.Pokemon;
import br.com.webflux.pokedex.repository.PokemonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class PokedexApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokedexApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations operations, PokemonRepository pokemonRepository) {
		return args -> {
			Flux<Pokemon> pokemonFlux = Flux.just(
					new Pokemon("001", "Bulbasaur", "Grass", "Overgrow", "45"),
					new Pokemon("004", "Charmander", "Fire", "Blaze", "39"),
					new Pokemon("007", "Squirtle", "Water", "Torrent", "44")
			).flatMap(pokemonRepository::save);

			pokemonFlux
					.thenMany(pokemonRepository.findAll())
					.subscribe(
							pokemon -> System.out.println("Pokemon salvo/consultado: " + pokemon),
							error -> System.err.println("Erro ao salvar/consultar Pokémon: " + error),
							() -> System.out.println("Operação de salvamento/consulta concluída")
					);
		};
	}

}
