package com.project.library.Controllers;

import com.project.library.Models.CurrentSession;
import com.project.library.Models.Playlist;
import com.project.library.Repositorys.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    PlaylistRepository playlistRepository;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Playlist>> listAll(){
        return new ResponseEntity<>(playlistRepository.findAllByPublicAcess(true), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Playlist>> listMyPlaylists(){

        List<Playlist> myPlaylists = playlistRepository.findAllByPublicAcess(false)
                                    .stream()
                                    .filter(x -> x.getId() == CurrentSession.getSession().getAccount().getId())
                                    .toList();

        return new ResponseEntity<>(myPlaylists, HttpStatus.OK);
    }



}
