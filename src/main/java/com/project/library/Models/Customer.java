package com.project.library.Models;

import com.project.library.Enums.eGender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="customers")
public class Customer {

    @Id @NotNull @NotEmpty @NotBlank
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")
    private String cpf;
    @NotNull @NotEmpty @NotBlank
    private String name;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private BigDecimal income;
    @NotNull
    @Enumerated(EnumType.STRING)
    private eGender gender;

}
