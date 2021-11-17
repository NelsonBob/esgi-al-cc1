package org.sid.repository;

import java.time.Instant;

import org.sid.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // toutes ses fonctions sont ecrite grace au racourci que jpa nous donne

    // faire la recherche d'un membre par son email
    Member findByEmail(String email);

    // faire la recherche d'un membre par son telephone
    Member findByTelephone(int telephone);

    // retourne true si le telephone existe
    Boolean existsByTelephone(int telephone);

    // retourne true si l'email existe
    Boolean existsByEmail(String email);

    // retourne true si l'email avec l'id du membre existe
    Boolean existsByEmailAndId(String email, Long id);

    // retourne true si le telephone avec l'id du membre existe
    Boolean existsByTelephoneAndId(String username, Long id);

    @Modifying
    @Query("update Member u set u.password = :password where u.id = :id")
    public void updatePassword(@Param("password") String password, @Param("id") Long id);

    // cette fonction permet de faire la recherche du filtre sur les noms
    public Page<Member> findByFirstNameLikeOrLastNameLike(String firstName, String lastName, Pageable pageable);

    @Query("select e from Member e where e.id=:id")
    public Member findByID(@Param("id") Long id);

    // selectionne la liste des utilisateur du plus recent au plus ancien
    public Member findTopByOrderByIdDesc();

    @Modifying
    @Query("update Member  set firstName = :firstName , lastName = :lastName , email = :email , civilite = :civilite ,  telephone = :telephone , enabled = :enabled , dateUpdat = :dateUpdat where id = :id")
    void update(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                @Param("email") String email, @Param("civilite") String civilite, @Param("telephone") int telephone,
                @Param("enabled") boolean enabled, @Param("dateUpdat") Instant dateUpdat);

}
