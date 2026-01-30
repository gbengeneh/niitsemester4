package com.db.bankingapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Role_Id")
    private long roleId;
    @Column(name="Role_Name",length=50)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @Schema(hidden = true)
    private List<User> users;

    public Role(String role) {
        this.roleName = role;
    }



}
