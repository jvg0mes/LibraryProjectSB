package com.project.library.Services;

import com.project.library.Models.CurrentSession;
import com.project.library.Models.Playlist;
import com.project.library.Repositorys.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;

    public static boolean checkPrivateAccessRequest(Playlist playlist){
        if(!playlist.isPublicAccess()){
            if(playlist.getOwnerId() == CurrentSession.getSession().getAccount().getId()) {
                return true;
            }
        } else{
            return true;
        }
        return false;
    }

}
