package wsz.springboot.springbootshiro.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Shiro认证过程
 * 1.构建SecurityManager环境
 * 2.主体请求认证
 * 3.SecurityManager认证
 * 4.Authentication认证
 * 5.Realm验证
 *
 * Shiro授权过程
 * 1.构建SecurityManager环境
 * 2.主体授权
 * 3.SecurityManager授权
 * 4.Authorizer授权
 * 5.Realm获取角色权限数据
 * Created by wsz
 * date 2018/4/22
 */
public class AuthenticationTest {

    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("mark","123456","admin");
    }

    @Test
    public void testAuthentication(){
        //1.SecurityManager环境
        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(simpleAccountRealm);

        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(manager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("mark","123456");
        subject.login(token);

        boolean authenticated = subject.isAuthenticated();//是否认证
        subject.checkRole("admin");
        System.out.println(authenticated);

        //退出
        subject.logout();
        authenticated = subject.isAuthenticated();//是否认证
        System.out.println(authenticated);
    }


}
