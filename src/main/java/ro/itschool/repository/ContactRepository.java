package ro.itschool.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.itschool.entity.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact> findAll(Sort sort);

    @Transactional
    void deleteById(Integer id);

    @Query(
            value = "SELECT * FROM contact c WHERE c.username LIKE %:keyword% OR c.id LIKE %:keyword% OR c.email LIKE %:keyword% OR c.subject LIKE %:keyword% " +
                    "OR c.message LIKE %:keyword%",
            nativeQuery = true)
    List<Contact> searchContact(@Param("keyword") String keyword);

}