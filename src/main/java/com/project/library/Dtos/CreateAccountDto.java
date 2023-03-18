package com.project.library.Dtos;

import lombok.Data;

@Data
public class CreateAccountDto{
    private String cpf;
    private String userName;
    private String password;

    public CreateAccountDto(String cpf,String userName,String password){
        this.cpf = cpf;
        this.userName = userName;
        this.password = password;
    }

}
