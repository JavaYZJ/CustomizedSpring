/**
 * Copyright (C), 2018, 杨智杰
 * FileName: OrderServiceImpl
 * Author:   猪猪
 * Date:     2018/12/28 22:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package cn.yzj.Spring.service.impl;

import cn.yzj.Spring.annotation.MyService;
import cn.yzj.Spring.service.OrderService;

/**
 * 〈功能简述〉<br> 
 * 〈〉
 *
 * @author 猪猪
 * @create 2018/12/28
 * @since 1.0.0
 */
@MyService
public class OrderServiceImpl implements OrderService {
    @Override
    public void addOrder() {
        System.out.println("将订单添加到数据库成功！！！！");
    }
}
