package com.project.library.Repositorys;

import com.project.library.Models.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music,Integer> {

    public Music findByName(String name);
    public boolean existsByName(String name);
    public boolean existsByAuthor(String author);

}
