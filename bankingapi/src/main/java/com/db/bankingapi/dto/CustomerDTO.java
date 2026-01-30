package com.db.bankingapi.dto;

import com.db.bankingapi.models.FullName;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CustomerDTO {

    protected long accountNo;

    protected FullNameDTO fullName;
    @Email(message = "Enter valid mail address")

    protected String email;
    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,10}$",message = "Password must have mininum one digit,one lowercase and one upper case")

    protected String password;

    protected long contactNo;
}
