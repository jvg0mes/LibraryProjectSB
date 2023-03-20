package com.project.library.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="playlists")
public class Playlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull @NotEmpty
    private String name;
    @NotNull
    private List<Integer> musics = new ArrayList<>();
    @NotNull
    private int ownerId;
    private boolean publicAccess = false;

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", musics=" + musics +
                ", ownerId=" + ownerId +
                ", publicAccess=" + publicAccess +
                '}';
    }
}
