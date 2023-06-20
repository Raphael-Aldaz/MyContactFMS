package fr.fms.repositories;

import fr.fms.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUserId(Long userId, PageRequest pageable);
    Page<Contact> findByContactNameContains(String kw, PageRequest pageable);
    Page<Contact> findByContactNameContainsAndUserId(String kw, Long userId, PageRequest pageable);
    Page<Contact> findByCategoryId(Long idCat, PageRequest pageRequest);
    Page<Contact> findByCategoryIdAndUserId(Long idCat,Long userId, PageRequest pageRequest);
}
