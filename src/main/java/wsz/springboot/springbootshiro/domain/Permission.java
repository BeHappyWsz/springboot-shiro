package wsz.springboot.springbootshiro.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 权限信息
 * Created by wsz
 * date 2018/4/16
 */
@Entity
@Table(name = "s_permission")
public class Permission implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long uid;

    private String name;

    @Column(columnDefinition = "enum('menu','button')")
    private String resourceType;

    private String url;

    private String permission;

    private Long parentId;

    private String parentIds;

    private Boolean avaiable = Boolean.FALSE;

    @ManyToMany
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name="permissionId")})
    private List<Role> roles;
}
