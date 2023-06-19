package fr.fms.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data  @AllArgsConstructor @NoArgsConstructor(force = true)
@ToString
public class Contact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String contactName;
    @NotNull
    private String contactFirstName;
    @NotNull
    private String contactEmail;
    @NotNull
    private String contactPhone;

    private String contactPhoto;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;



}
