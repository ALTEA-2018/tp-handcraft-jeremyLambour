package com.miage.altea.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.bo.PokemonType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PokemonTypeRepository {

    private List<PokemonType> pokemons;

    public PokemonTypeRepository() {
        try {
            var pokemonsStream = this.getClass().getResourceAsStream("/pokemons.json");

            var objectMapper = new ObjectMapper();
            var pokemonsArray = objectMapper.readValue(pokemonsStream, PokemonType[].class);
            this.pokemons = Arrays.asList(pokemonsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonType findPokemonById(int id) {
        System.out.println("Loading Pokemon information for Pokemon id " + id);
        Optional<PokemonType> type =  pokemons.stream().filter(x -> x.getId() == id).findFirst();
        return (type.isPresent() ? type.get() : null);
    }

    public PokemonType findPokemonByName(String name) {
        System.out.println("Loading Pokemon information for Pokemon name " + name);
        Optional<PokemonType> type = pokemons.stream().filter(x -> x.getName().equalsIgnoreCase(name)).findFirst();
        return (type.isPresent() ? type.get() : null);
        // TODO
    }

    public List<PokemonType> findAllPokemon() {
        return pokemons;
    }
}