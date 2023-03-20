package com.project.library.Controllers;

import com.project.library.Dtos.CreatePlaylistDto;
import com.project.library.Models.CurrentSession;
import com.project.library.Models.Music;
import com.project.library.Models.Playlist;
import com.project.library.Repositorys.MusicRepository;
import com.project.library.Repositorys.PlaylistRepository;
import com.project.library.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    MusicRepository musicRepository;

    PlaylistService playlistService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Playlist>> listAll() {
        return new ResponseEntity<>(playlistRepository.findAllByPublicAccess(true), HttpStatus.OK);
    }

    @GetMapping("/my")
    @ResponseBody
    public ResponseEntity<List<Playlist>> listMyPlaylists() {

        List<Playlist> myPlaylists = playlistRepository.findAllByPublicAccess(false)
                .stream()
                .filter(x -> x.getId() == CurrentSession.getSession().getAccount().getId())
                .toList();

        return new ResponseEntity<>(myPlaylists, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPlaylist(@RequestBody CreatePlaylistDto playlistDto){

        if(CurrentSession.getSession() == null){
            return new ResponseEntity<>("Efetue o login para criar playlists", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Playlist playlist = new Playlist();
            playlist.setName(playlistDto.getName());
            playlist.setPublicAccess(playlistDto.isPublicAccess());
            playlist.setOwnerId(CurrentSession.getSession().getAccount().getId());
            playlistRepository.save(playlist);
            return new ResponseEntity<>("Playlist criada com sucesso",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao criar a playlist",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Playlist> getPlaylist(@PathVariable int id){

        Playlist selectedPlaylist;

        try {
            Optional playlist = playlistRepository.findById(id);
            if (!playlist.isPresent()) {
                return new ResponseEntity<>(new Playlist(), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                selectedPlaylist = (Playlist) playlist.get();
            }
        } catch (Exception e){
            return new ResponseEntity<>(new Playlist(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!playlistService.checkPrivateAccessRequest(selectedPlaylist)){
            return new ResponseEntity<>(new Playlist(), HttpStatus.INTERNAL_SERVER_ERROR);
        };

        return new ResponseEntity<>(selectedPlaylist,HttpStatus.OK);

    }

    @PostMapping("/{id}/add/{musicName}")
    public ResponseEntity<String> addMusic(@PathVariable int id,@PathVariable String musicName){

        Playlist selectedPlaylist;
        try {
            selectedPlaylist = getPlaylist(id).getBody();
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao encontrar a playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Music music;
        try {
            music = musicRepository.findByName(musicName);
            if (music == null) {
                return new ResponseEntity<>("Musica nao encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Falha ao buscar a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            if(selectedPlaylist.getMusics().contains(music.getMusicId())){
                return new ResponseEntity<>("Musica ja foi incluida na playlist anteriormente",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            selectedPlaylist.getMusics().add(music.getMusicId());
            System.out.println(selectedPlaylist);
            playlistRepository.save(selectedPlaylist);
            return new ResponseEntity<>("Musica adicionada com sucesso",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Falha ao adicionar a musica a playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/{id}/remove/{musicName}")
    public ResponseEntity<String> removeMusic(@PathVariable int id,@PathVariable String musicName){

        Playlist selectedPlaylist;
        try {
            selectedPlaylist = getPlaylist(id).getBody();
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao encontrar a playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Music music;
        try {
            music = musicRepository.findByName(musicName);
            if (music == null) {
                return new ResponseEntity<>("Musica nao encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Falha ao buscar a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!selectedPlaylist.getMusics().contains(music.getMusicId())){
            return new ResponseEntity<>("A busca nao esta na playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            selectedPlaylist.getMusics().removeIf(x -> x == music.getMusicId());
            playlistRepository.save(selectedPlaylist);
            return new ResponseEntity<>("Musica removida com sucesso",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Falha ao remover a musica a playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/{id}/play/{musicName}")
    public ResponseEntity<String> playMusic(@PathVariable int id,@PathVariable String musicName){

        if(CurrentSession.getSession() == null){
            return new ResponseEntity<>("Faca o login para tocar musicas", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Playlist selectedPlaylist;
        try {
            selectedPlaylist = getPlaylist(id).getBody();
        }
        catch(Exception e){
            return new ResponseEntity<>("Falha ao encontrar a playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Music music;
        try {
            music = musicRepository.findByName(musicName);
            if (music == null) {
                return new ResponseEntity<>("Musica nao encontrada", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if(!selectedPlaylist.getMusics().contains(music.getMusicId())){
                return new ResponseEntity<>("A playlist nao possui esta musica", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Falha ao buscar a musica", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(music.play(), HttpStatus.OK);

    }

}
