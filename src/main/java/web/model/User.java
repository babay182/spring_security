package web.model;

import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int count;

    @Column(name ="name_login", unique = true )
    @NotEmpty(message = "Введите имя")
    @Size(min = 3, max = 30, message = "имя должно быть от 3 до 30 символов")
    private String name;

    @Column
    @NotEmpty(message = "Введите фамилию")
    @Size(min = 3, max = 30, message = "фамилия должна быть от 3 до 30 символов")
    private String surname;

    @Column
    @Min(value = 0, message = "введите корректный возраст")
    @Max(value = 120, message = "введите корректный возраст")
    private int age;

    @Column
    @Email(message = "введите корректный email")
    private String email;


    @Column
    @NotEmpty(message = "Введите пароль")
    @Size(min=3, max=255, message = "пароль должен быть от 3 символов")
    private String password;

    @Column
    @NotEmpty(message = "заполните роль")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "count"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Role> roles;

    public User() {}
    public User(int count, String name, String surname, int age, String email) {
        this.count = count;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }

    public User(int count, String name, String surname, int age, String email, String password, Set<Role> roles) {
        this.count = count;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }

    @Override
    public String getUsername() { return name; }

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

}
