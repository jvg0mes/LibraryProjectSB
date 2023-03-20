package com.project.library.Dtos;

import lombok.Data;

@Data
public class CreatePlaylistDto {

    private String name;
    private boolean publicAccess = false;

    public CreatePlaylistDto(String name, boolean publicAccess){
        this.name = name;
        this.publicAccess = publicAccess;
    }

}
