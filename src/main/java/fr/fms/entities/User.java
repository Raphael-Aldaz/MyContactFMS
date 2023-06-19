package fr.fms.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor(force = true)
@ToString
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private Boolean userStatus;

    @OneToMany(mappedBy = "user")
    private Collection<Contact> contacts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Collection<Role> role;
}
