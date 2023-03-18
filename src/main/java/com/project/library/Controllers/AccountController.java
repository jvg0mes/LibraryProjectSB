package com.project.library.Controllers;

import com.project.library.Dtos.CreateAccountDto;
import com.project.library.Dtos.LoginDto;
import com.project.library.Models.Account;
import com.project.library.Models.CurrentSession;
import com.project.library.Models.Customer;
import com.project.library.Models.Session;
import com.project.library.Repositorys.AccountRepository;
import com.project.library.Repositorys.CustomerRepository;
import com.project.library.Repositorys.SessionRepository;
import com.project.library.Security.AESCrypto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SessionRepository sessionRepository;

    AESCrypto crypto = new AESCrypto("account_secret_key");

    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody CreateAccountDto createAccountDto){

        Customer customer = customerRepository.findByCpf(createAccountDto.getCpf());
        if(accountRepository.existsByCustomer(customer)){
            return new ResponseEntity<>("Conta ja possui um usuario",HttpStatus.INTERNAL_SERVER_ERROR);
        };

        try {
            accountRepository.save(new Account(
                    customer,
                    createAccountDto.getUserName(),
                    crypto.encrypt(createAccountDto.getPassword())
                    )
            );
            return new ResponseEntity<>("Conta criada com sucesso", HttpStatus.OK);
        }
        catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Falha ao criar a conta", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {

        if(CurrentSession.getSession() != null){
            return new ResponseEntity<>("Ja existe sessao ativa, efetue o logout", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!accountRepository.existsByUsername(loginDto.getUsername())){
            return new ResponseEntity<>("Conta nao existe", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account account = accountRepository.findByUsername(loginDto.getUsername());

        System.out.println(account);

        if(!crypto.decrypt(account.getPassword()).equals(loginDto.getPassword())){
            return new ResponseEntity<>("Senha incorreta", HttpStatus.INTERNAL_SERVER_ERROR);
        } else{
            Session newSession = new Session(account);
            System.out.println(newSession);
            sessionRepository.save(newSession);
            CurrentSession.setSession(newSession);
            System.out.println(CurrentSession.getSession().getId());
        }

        return new ResponseEntity<>("Login efetuado com sucesso", HttpStatus.OK);
    }

    @PutMapping("/logout")
        public ResponseEntity<String> logout() {

        if(CurrentSession.getSession() == null){
            return new ResponseEntity<>("Nenhum usuario logado", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CurrentSession.logout();

        return new ResponseEntity<>("Sessao encerrada com sucesso", HttpStatus.OK);
    }



    }
