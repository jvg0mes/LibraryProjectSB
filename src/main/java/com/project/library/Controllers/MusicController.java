package com.project.library.Controllers;

import com.project.library.Models.Music;
import com.project.library.Repositorys.MusicRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    MusicRepository musicRepository;

    @GetMapping
    public String homeMusic(){
        return "Welcome to musics endpoint!";
    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public Music getMusicByName(@PathVariable String name){
        return musicRepository.findByName(name);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Music>> getMusics(){
        try{
            return new ResponseEntity<>(musicRepository.findAll(),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(Arrays.asList(new Music()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMusic(@Valid @RequestBody Music music){
        try {
            Music oMusic = musicRepository.findByName(music.getName());

            if(oMusic != null){
                if(oMusic.getAuthor().equalsIgnoreCase(music.getAuthor())){
                    return new ResponseEntity<>("A musica ja existe", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            musicRepository.save(music);
            return new ResponseEntity<>("Musica " + music.getName() + " criada com sucesso", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("Falha ao criar a musica " + music.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
