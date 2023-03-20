package com.project.library.Repositorys;

import com.project.library.Models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist,Integer> {
    public List<Playlist> findAllByPublicAccess(Boolean publicAccess);
}
