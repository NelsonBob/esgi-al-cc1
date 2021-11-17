package org.sid.service;

import org.sid.entity.Member;
import org.sid.web.dto.MemberRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface MemberService extends UserDetailsService {

    public Optional<Member> findById(Long id);

    public Member findByEmail(String email);

    public Member save(MemberRegistrationDto registration);

    public Member saveProfessionnel(MemberRegistrationDto registration);

    public Member saveAdmin(MemberRegistrationDto registration);

    public Member saveAdminFist();

    public void updatePassword(String password, Long userId);

    public void deleteUg(Long id);

    public void update(Member member, Long id);

    public void autologin(String username, String password);

    public String findLoggedInUsername();

    public Page<Member> findByFirstNameLikeOrLastNameLike(String firstName, String lastName, Pageable pageable);

    public Member findByID(Long id);

    public Member findTopByOrderByIdDesc();

    public List<Member> findAll();

    public void delete(Member member);

}
