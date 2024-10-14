package com.app.model;


import com.app.Enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false, updatable = false)
    //@Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private PermissionEnum permissionEnum;
}
