package com.project.library.Repositorys;

import com.project.library.Models.Account;
import com.project.library.Models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session,String> {
    public Session findByAccount(int account);
}
