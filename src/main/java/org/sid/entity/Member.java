package org.sid.entity;


import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "member", uniqueConstraints = { @UniqueConstraint(columnNames = "telephone"),
        @UniqueConstraint(columnNames = "email") })
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String civilite;
    private int telephone;
    private Instant dateUpdat;
    private Instant dateEnreg;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new HashSet<>();

    public Member(String firstName, String lastName, String email, String password, String civilite, int telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.civilite = civilite;
        this.telephone = telephone;
        this.dateEnreg = Instant.now();
    }

    public Member(String firstName, String lastName, String email, String password, String civilite, int telephone,
                  Instant dateUpdat, Instant dateEnreg, boolean enabled, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.civilite = civilite;
        this.telephone = telephone;
        this.dateUpdat = dateUpdat;
        this.dateEnreg = dateEnreg;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Member(String lastName) {
        this.lastName = lastName;
    }

    public Member(Long id, String firstName, String lastName, String email, String password, String civilite,
                  int telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.civilite = civilite;
        this.telephone = telephone;
        this.dateEnreg = Instant.now();
    }

    public Member() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public Instant getDateUpdat() {
        return dateUpdat;
    }

    public void setDateUpdat(Instant dateUpdat) {
        this.dateUpdat = dateUpdat;
    }

    public Instant getDateEnreg() {
        return dateEnreg;
    }

    public void setDateEnreg(Instant dateEnreg) {
        this.dateEnreg = dateEnreg;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Member [civilite=" + civilite + ", dateEnreg=" + dateEnreg + ", dateUpdat=" + dateUpdat + ", email="
                + email + ", enabled=" + enabled + ", firstName=" + firstName + ", id=" + id + ", lastName=" + lastName
                + ", password=" + password + ", roles=" + roles + ", telephone=" + telephone + "]";
    }

}
