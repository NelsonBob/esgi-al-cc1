package org.sid.service;

import org.sid.entity.Member;
import org.sid.entity.Role;
import org.sid.repository.MemberRepository;
import org.sid.repository.RoleRepository;
import org.sid.web.dto.MemberRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Member save(MemberRegistrationDto registration) {

        Member member = new Member();
        member.setFirstName(registration.getFirstName());
        member.setLastName(registration.getLastName());
        member.setEmail(registration.getEmail());
        member.setPassword(passwordEncoder.encode(registration.getPassword()));
        member.setCivilite(registration.getCivilite());
        member.setTelephone(registration.getTelephone());
        member.setRoles(Arrays.asList(roleRepository.findByName("ROLE_PARTICULIER")));
        member.setDateEnreg(Instant.now());
        member.setEnabled(true);
        member.setDateUpdat(Instant.now());

        return memberRepository.save(member);
    }

    @Override
    public Member saveProfessionnel(MemberRegistrationDto registration) {

        Member member = new Member();
        member.setFirstName(registration.getFirstName());
        member.setLastName(registration.getLastName());
        member.setEmail(registration.getEmail());
        member.setPassword(passwordEncoder.encode(registration.getPassword()));
        member.setCivilite(registration.getCivilite());
        member.setTelephone(registration.getTelephone());
        member.setRoles(Arrays.asList(roleRepository.findByName("ROLE_PROFESSIONNELLE")));
        member.setDateEnreg(Instant.now());
        member.setEnabled(true);
        member.setDateUpdat(Instant.now());

        return memberRepository.save(member);
    }

    @Override
    public Member saveAdmin(MemberRegistrationDto registration) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        Member member = new Member();
        member.setFirstName(registration.getFirstName());
        member.setLastName(registration.getLastName());
        member.setEmail(registration.getEmail());
        member.setPassword(passwordEncoder.encode(registration.getPassword()));
        member.setCivilite(registration.getCivilite());
        member.setTelephone(registration.getTelephone());
        member.setRoles(Arrays.asList(adminRole));
        member.setDateEnreg(Instant.now());
        member.setDateUpdat(Instant.now());
        member.setEnabled(true);
        return memberRepository.save(member);
    }

    @Override
    public void updatePassword(String password, Long userId) {
        memberRepository.updatePassword(passwordEncoder.encode(password), userId);
    }

    @Override
    public void deleteUg(Long id) {
        if (memberRepository.findById(id) != null) {
            memberRepository.deleteById(id);
        }
        throw new RuntimeException("Member not found");
    }

    @Override
    public void update(Member member, Long code) {

        /*
         * if (member.isEnabled() == true) { memberRepository.update(code,
         * member.getFirstName(), member.getLastName(), member.getEmail(),
         * member.getSexe(), member.getNationalite(), member.getProfession(),
         * member.getTelephone(), member.getDateNaissance(), true, Instant.now()); }
         * else { memberRepository.update(code, member.getFirstName(),
         * member.getLastName(), member.getEmail(), member.getSexe(),
         * member.getNationalite(), member.getProfession(), member.getTelephone(),
         * member.getDateNaissance(), false, Instant.now()); }
         */
    }

    @Override
    public void autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s successfully!", username));
        }
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("Adresse email ou mot de passe incorrect!");

        }
        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(),
                authoritis(member.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authoritis(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public Member saveAdminFist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        Member member = new Member();
        member.setFirstName("FOMEKONG");
        member.setLastName("BOB NELSON");
        member.setEmail("nels.fomekong@gmail.com");
        member.setPassword(passwordEncoder.encode("nelsonBob"));
        member.setCivilite("Monsieur");
        member.setTelephone(697630812);
        member.setRoles(Arrays.asList(adminRole));
        member.setDateEnreg(Instant.now());
        member.setDateUpdat(Instant.now());
        member.setEnabled(true);
        return memberRepository.save(member);
    }

    @Override
    public Page<Member> findByFirstNameLikeOrLastNameLike(String firstName, String lastName, Pageable pageable) {
        return memberRepository.findByFirstNameLikeOrLastNameLike(firstName, lastName, pageable);
    }

    @Override
    public Member findByID(Long id) {

        return memberRepository.findByID(id);
    }

    @Override
    public Member findTopByOrderByIdDesc() {

        return memberRepository.findTopByOrderByIdDesc();
    }

    @Override
    public List<Member> findAll() {

        return memberRepository.findAll();
    }

    @Override
    public void delete(Member member) {
        memberRepository.delete(member);

    }

}
