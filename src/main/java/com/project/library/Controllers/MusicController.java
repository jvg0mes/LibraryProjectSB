package com.project.library.Controllers;

import com.project.library.Models.CurrentSession;
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

    @PostMapping("name/{name}/like")
    public ResponseEntity<String> likeMusic(@PathVariable String name){

        Music music = musicRepository.findByName(name);

        if(CurrentSession.getSession() == null){
            return new ResponseEntity<>("Nenhum usuario logado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(music.getLikeList().contains(CurrentSession.getSession().getAccount().getId())){
            return new ResponseEntity<>("Musica ja curtida", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            music.getLikeList().add(CurrentSession.getSession().getAccount().getId());
            music.setLikes((int) music.getLikeList().stream().count());
            musicRepository.save(music);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao curtir a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Musica " + music.getName() + " curtida com sucesso", HttpStatus.OK);
    };

    @PostMapping("name/{name}/unlike")
    public ResponseEntity<String> unlikeMusic(@PathVariable String name){

        Music music = musicRepository.findByName(name);

        if(CurrentSession.getSession() == null){
            return new ResponseEntity<>("Nenhum usuario logado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!music.getLikeList().contains(CurrentSession.getSession().getAccount().getId())){
            return new ResponseEntity<>("Voce nao havia curtido a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            music.getLikeList().removeIf(x -> x.equals(CurrentSession.getSession().getAccount().getId()));
            music.setLikes((int) music.getLikeList().stream().count());
            musicRepository.save(music);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao descurtir a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Musica " + music.getName() + " descurtida com sucesso", HttpStatus.OK);
    };

}
