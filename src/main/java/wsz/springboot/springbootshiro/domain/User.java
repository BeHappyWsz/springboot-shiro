package wsz.springboot.springbootshiro.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by wsz
 * date 2018/4/16
 */
@Entity
@Table(name = "s_user")
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Setter
    @Getter
    private Long uid;

    @Setter
    @Getter
    @Column(unique = true)
    private String username;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String realName;

    @Setter
    @Getter
    private String salt;

    @Setter
    @Getter
    private byte state;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "userId")}, inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<Role> roles;
}
