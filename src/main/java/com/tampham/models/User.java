package com.tampham.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Entity này sẽ cần implements UserDetail của spring security, để tạo ra thông tin username sử dụng cho loadUsername tại
 * UserDetailService
 * */
@Entity
@Table(name = "users")
public class User extends BaseModel implements UserDetails {
    @Getter
    @Setter
    @NotEmpty(message = "Vui lòng nhập tên")
    private String fullName;

    @Getter
    @Setter
    private String email;

    @Column(unique=true)
    private String username;

    private String password;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private String phoneNumber;

    @DateTimeFormat (pattern="yyyy-MM-dd")
    @Getter
    @Setter
    private Date birthdate;

    @Getter
    @Setter
    @Lob
    private byte[] avatar;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Set<Role> authorities;

    public User(){
        super();
        authorities = new HashSet<>();
    }

    public User(String username, String password, String fullName, Set<Role> authorities) {
        super();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(String roleName) {
        for (Role role : this.authorities) {
            if (role.getAuthority().equals(roleName)) {
                return true;
            }
        }

        return false;
    }

    public String getAvatarBase64(){
        if (avatar != null) {
            return Base64.getEncoder().encodeToString(avatar);
        } else {
            return null;
        }
    }
}
