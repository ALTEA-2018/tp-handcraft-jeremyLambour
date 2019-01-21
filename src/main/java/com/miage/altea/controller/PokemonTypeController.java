package com.miage.altea.controller;

import com.miage.altea.annotations.Controller;
import com.miage.altea.annotations.RequestMapping;
import com.miage.altea.bo.PokemonType;
import com.miage.altea.repository.PokemonTypeRepository;

import java.util.Map;

@Controller
public class PokemonTypeController {

    private PokemonTypeRepository repository = new PokemonTypeRepository();

    @RequestMapping(uri = "/pokemons")
    public PokemonType getPokemon(Map<String,String[]> parameters){
        PokemonType pokemon = new PokemonType();
        if(parameters != null && !parameters.isEmpty()){

            if(parameters.containsKey("id")){
                pokemon = repository.findPokemonById(Integer.parseInt(parameters.get("id")[0]));
            }else if(parameters.containsKey("name")) {
                pokemon = repository.findPokemonByName(parameters.get("name")[0]);
            }else{
                throw  new IllegalArgumentException("unknown parameter");
            }
        }
        else{
            throw  new IllegalArgumentException("parameters should not be empty");
        }
        return pokemon;
    }
}