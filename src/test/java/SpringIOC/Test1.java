/**
 * Copyright (C), 2018, 杨智杰
 * FileName: Test1
 * Author:   猪猪
 * Date:     2018/12/28 17:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package SpringIOC;

import cn.yzj.Spring.io.ClassPathXmlApplicationContext;
import cn.yzj.Spring.service.OrderService;
import cn.yzj.Spring.service.UserService;
import org.junit.Test;

/**
 * 〈功能简述〉<br> 
 * 〈〉
 *
 * @author 猪猪
 * @create 2018/12/28
 * @since 1.0.0
 */
public class Test1 {
    /**
     * xml方式测试springIOC框架
     * @throws Exception
     */
    @Test
    public void xmlTest() throws Exception {
        ClassPathXmlApplicationContext app = new
                ClassPathXmlApplicationContext("applicationContext-spring.xml");
        OrderService orderService = (OrderService) app.getBean("orderServiceImpl");
        orderService.addOrder();
    }

    /**
     * 注解方式测试springIOC框架
     * @throws Exception
     */
    @Test
    public void annotationTest() throws Exception {
        ClassPathXmlApplicationContext app = new
                ClassPathXmlApplicationContext("applicationContext-spring.xml");
        UserService userService = (UserService) app.getBean("userServiceImpl");
        userService.getUserList();
    }
}
