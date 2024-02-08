package br.com.webflux.pokedex.controller;

import br.com.webflux.pokedex.model.Pokemon;
import br.com.webflux.pokedex.model.PokemonEvent;
import br.com.webflux.pokedex.repository.PokemonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

    private PokemonRepository pokemonRepository;

    public PokemonController(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pokemon> savePokemon(@RequestBody Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }

    @GetMapping
    public Flux<Pokemon> getAllPokemons() {
        return pokemonRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> getPokemonById(@PathVariable String id) {
        return pokemonRepository.findById(id).
                map(pokemon -> ResponseEntity.ok(pokemon)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pokemon>> updatePokemon(@PathVariable String id, @RequestBody Pokemon pokemon) {
        return pokemonRepository.findById(id).flatMap(existingPokemon -> {
            existingPokemon.setName(pokemon.getName());
            existingPokemon.setHability(pokemon.getHability());
            existingPokemon.setCategory(pokemon.getCategory());
            existingPokemon.setWeight(pokemon.getWeight());
            return pokemonRepository.save(existingPokemon);
        })
        .map(updatedPokemon -> ResponseEntity.ok(updatedPokemon))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePokemon(@PathVariable String id) {
        return pokemonRepository.findById(id)
                .flatMap(existingPokemon -> pokemonRepository.delete(existingPokemon).then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllPokemons() {
        return pokemonRepository.deleteAll();
    }

    @GetMapping(value = "/events" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PokemonEvent> getPokemonEvents() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(value -> new PokemonEvent(value , "Pokemonzitooos"));
    }
}
