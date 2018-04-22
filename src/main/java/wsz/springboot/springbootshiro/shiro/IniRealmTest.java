package wsz.springboot.springbootshiro.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;


/**
 * Created by wsz
 * date 2018/4/22
 */
public class IniRealmTest {

    @Test
    public void testAuthentication(){
        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        DefaultSecurityManager manager = new DefaultSecurityManager();
        manager.setRealm(iniRealm);

        SecurityUtils.setSecurityManager(manager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("mark","123456");
        subject.login(token);

        System.out.println(subject.isAuthenticated());
    }
}
