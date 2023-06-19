package fr.fms.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data @NoArgsConstructor(force = true)
@AllArgsConstructor @ToString
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String roleName;

    @ManyToMany(mappedBy = "role")
    private Collection<User> users;
}
