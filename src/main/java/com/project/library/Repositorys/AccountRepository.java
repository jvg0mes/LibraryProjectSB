package com.project.library.Repositorys;

import com.project.library.Models.Account;
import com.project.library.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    public Boolean existsByCustomer(Customer customer);
    public Boolean existsByUsername(String username);
    public Account findByUsername(String username);
}
