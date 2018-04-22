package wsz.springboot.springbootshiro.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * JdbcRealm
 * 1.表信息与domain下的实体类之间的关系一致
 * Created by wsz
 * date 2018/4/22
 */
public class JdbcRealmTest {

    private final String URL ="jdbc:mysql://localhost:3306/shiro";

    private DataSource dataSource;

    @Test
    public void testJdbcReam(){
        dataSource = new DriverManagerDataSource(URL, "root","root");

        //JdbcRealm提供默认的查询语句
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(this.dataSource);

        //覆盖默认的查询语句-根据拥护名获取密码
        String sql = "select password from s_user where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        //覆盖默认的查询语句-获取角色的权限信息
        String roleSql = "select r.role from s_user u join user_role ur on u.uid=ur.user_id " +
                "join s_role r on ur.role_id=r.uid " +
                "where username=?";
        jdbcRealm.setUserRolesQuery(roleSql);


        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(jdbcRealm);

        SecurityUtils.setSecurityManager(manager);
        Subject subject =SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("mark","123456");
        subject.login(token);

        System.out.println(
                " isAuthenticated: "+subject.isAuthenticated() +
                " checkRoles: "+subject.hasRole("admin"));
    }
}
