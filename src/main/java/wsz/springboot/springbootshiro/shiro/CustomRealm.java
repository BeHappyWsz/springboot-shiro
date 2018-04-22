package wsz.springboot.springbootshiro.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

/**
 * 自定义Realm
 * Created by wsz
 * date 2018/4/22
 */
public class CustomRealm extends AuthorizingRealm{

    private DataSource dataSource;

    public CustomRealm() {
        ResourceBundle bundle = ResourceBundle.getBundle("db");
        String url = bundle.getString("datasource.url");
        String username = bundle.getString("datasource.username");
        String password = bundle.getString("datasource.password");
        dataSource = new DriverManagerDataSource(url,username,password);
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.从主体传过来的认证信息中，获取用户名
        String username = (String)principalCollection.getPrimaryPrincipal();
        //2.获取用户相关的权限以及权限描述信息
        Set<String> roles = getRolesByUserName(username);
        Set<String> permission = getPermissionByUserName(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permission);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证过程
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的认证信息中，获取用户名
        String username = (String)authenticationToken.getPrincipal();

        //2.通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(username);
        String realname = getRealNameByUserName(username);
        System.out.println(password+"  "+realname);
        if(password == null || realname == null)
            return null;
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,password,realname);

        return authenticationInfo;
    }

    /**
     * 根据用户名获取权限的描述信息
     * @param username
     * @return
     */
    private Set<String> getPermissionByUserName(String username){
        Set<String> per = new TreeSet<>();
        per.add("user:delete");
        per.add("user:add");
        return per;
    }

    /**
     * 获取用户的权限信息
     * @param username
     * @return
     */
    private Set<String> getRolesByUserName(String username){
        Set<String> roles = new TreeSet<String>();
        try {
            String sql = "select r.role " +
                    "from s_user u join user_role ur on u.uid=ur.user_id join s_role r on r.uid= ur.role_id " +
                    "where u.username=? ";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
               roles.add(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 获取用户密码
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username){
        try {
            String sql = "select password from s_user where username=?";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取用户的真实名字
     * @param username
     * @return
     */
    private String getRealNameByUserName(String username){
        try {
            String sql = "select real_name as realname from s_user where username=?";
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return resultSet.getString("realname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
