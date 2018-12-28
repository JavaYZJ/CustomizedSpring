/**
 * Copyright (C), 2018, 杨智杰
 * FileName: UserServiceImpl
 * Author:   猪猪
 * Date:     2018/12/28 17:10
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package cn.yzj.Spring.service.impl;

import cn.yzj.Spring.annotation.MyResource;
import cn.yzj.Spring.annotation.MyService;
import cn.yzj.Spring.po.User;
import cn.yzj.Spring.service.OrderService;
import cn.yzj.Spring.service.UserService;

import java.util.List;

/**
 * 〈功能简述〉<br> 
 * 〈〉
 *
 * @author 猪猪
 * @create 2018/12/28
 * @since 1.0.0
 */
@MyService
public class UserServiceImpl implements UserService {

    @MyResource
    private OrderService orderServiceImpl;

    @Override
    public List<User> getUserList() {
        System.out.println(orderServiceImpl);
        orderServiceImpl.addOrder();
        System.out.println("调用dao，从数据库查询到UserList。。。。");
        return null;
    }
}
