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
@Table(name = "s_role")
public class Role implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Setter
    @Getter
    private Long uid;

    @Setter
    @Getter
    private String role;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private Boolean available = Boolean.FALSE;

    /**
     * 角色-权限:多对多
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "roleId")},inverseJoinColumns = {@JoinColumn(name = "permissionId")})
    private List<Permission> permissions;

    /**
     * 用户-角色:多对多
     */
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<User> users;

}
