package wsz.springboot.springbootshiro.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * 自定义Realm测试
 * Created by wsz
 * date 2018/4/22
 */
public class CustomRelmTest {

    private CustomRealm customRealm = new CustomRealm();

    @Test
    public void testCustomReam(){

        //1.
        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(customRealm);

        //密码加密:方法+次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);
        //2.
        SecurityUtils.setSecurityManager(manager);
        Subject subject =SecurityUtils.getSubject();

        //3.
        UsernamePasswordToken token = new UsernamePasswordToken("mark","1234567");
        subject.login(token);

        System.out.println(
                " isAuthenticated: "+subject.isAuthenticated() +
                        " checkRoles: "+subject.hasRole("admin"));
        subject.checkPermissions("user:add");
    }

    @Test
    public void md5(){
        Md5Hash md5Hash = new Md5Hash("1234567");
        System.out.println(md5Hash.toString());
    }
}
