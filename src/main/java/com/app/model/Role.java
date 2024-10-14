package com.app.model;

import com.app.Enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.method.P;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name= "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id") )
    private Set<Permission> permissionList =new HashSet<>();


}
