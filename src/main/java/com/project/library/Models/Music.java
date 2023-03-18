package com.project.library.Models;

import com.project.library.Enums.eMusicGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="musics")
public class Music {
    @Id @GeneratedValue @NotNull
    private int musicId;
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank
    private String author;
    @NotNull
    private List<Integer> likeList = new ArrayList<>();
    @NotNull
    private int likes = (int) likeList.stream().count();
    @Enumerated(EnumType.STRING) @NotNull
    private eMusicGender gender;

}
