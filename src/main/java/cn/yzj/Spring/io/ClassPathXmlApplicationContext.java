/**
 * Copyright (C), 2018, 杨智杰
 * FileName: ClassPathXmlApplicationContext
 * Author:   猪猪
 * Date:     2018/12/28 17:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

package cn.yzj.Spring.io;

import cn.yzj.Spring.annotation.MyResource;
import cn.yzj.Spring.annotation.MyService;
import cn.yzj.Spring.utils.ClassUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈功能简述〉<br> 
 * 〈手写springIOC框架，支持xml和注解两种配置方式〉
 *
 * @author 猪猪
 * @create 2018/12/28
 * @since 1.0.0
 */
public class ClassPathXmlApplicationContext {

    private String xmlPath;
    private String packageName;
    //SpringIOC容器
    private Map<String,Object> map = new ConcurrentHashMap<>();
    public ClassPathXmlApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * 通过指定beanID从springioc容器获取对象bean对象
     * @param beanId
     * @return
     * @throws Exception
     */
    public Object getBean(String beanId) throws Exception {
        //读取spring配置文件
        Map<String, Object> map = loadSpringXml();
        //获取扫描包下的所有带有注解的类的class对象集合
        if (packageName != null) {
            List<Class> classes = hasExitMyServiceAnnotation();
            //利用反射技术为所有带有注解的类的class对象实例化对象
            initBean(classes);
        }
        //获取指定id的bean对象
        Object bean = map.get(beanId);
        if (bean != null) {
            //利用反射技术为带有注解的字段赋值
            attributeAssign(bean);
        }

        //返回bean对象
        return bean;
    }

    /**
     * 读取spring配置文件，反射实例化对象并加入IOC容器中
     * @return
     * @throws Exception
     */
    public Map<String, Object> loadSpringXml() throws Exception {
        //利用类加载器将配置文件转换为二进制流
        InputStream is = Resources.getResourceAsStream(xmlPath);
        //利用dom4j和xpath解析spring配置文件
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        //获取配置文件中的bean标签
        List<Element> nodes = document.selectNodes("//bean");
        //获取配置文件中的包扫描标签
        List<Element> list = document.selectNodes("//context:component-scan");
        //获取包的全限类名
        if ( !list.isEmpty()) {
            packageName = list.get(0).attributeValue("base-package");
        }

        //遍历bean，获取相应的id、class等属性值
        for (Element node : nodes) {
            String id = node.attributeValue("id");
            String className = node.attributeValue("class");
            //反射实例化对象
            Object instance = Class.forName(className).newInstance();
            //存入IOC容器中
            map.put(id,instance);
        }
        return map;
    }

    /**
     * 为带有注解并已经实例化的对象中带有注解的字段赋值
     * @param object
     * @throws IllegalAccessException
     */
    public void attributeAssign(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        //获取类中的所有字段属性
        Field[] fields = clazz.getDeclaredFields();
        //遍历
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //判断该字段属性是否含有指定的注解
            MyResource annotation = field.getAnnotation(MyResource.class);
            if (annotation != null){
                //获取字段名
                String fieldName = field.getName();
                //根据字段名去IOC容器查出对应的实例对象
                Object bean = map.get(fieldName);
                if (bean != null) {
                    //暴力访问
                    field.setAccessible(true);
                    //为该字段属性赋值
                    field.set(object,bean);
                }
            }
        }
    }

    /**
     * 获取指定包下的所有带有注解的类的class对象
     * @return
     */
    public List<Class> hasExitMyServiceAnnotation(){
        //获取指定包下的所有类的class对象
        List<Class<?>> classList = ClassUtil.getClasses(packageName);
        List<Class> exitMyServiceAnno  = new ArrayList<>();
        //遍历
        for (Class<?> classInfo : classList) {
            //判断该class对象是否含有指定注解
            MyService annotation = classInfo.getDeclaredAnnotation(MyService.class);
            if (annotation != null) {
                //如果含有，则加入list集合中
                exitMyServiceAnno.add(classInfo);
            }
        }
        return exitMyServiceAnno;
    }

    /**
     * 实例化指定包下所有带有注解的类
     * @param hasAnnotationClassList
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void initBean(List<Class> hasAnnotationClassList) throws IllegalAccessException, InstantiationException {
        for (Class classInfo : hasAnnotationClassList) {
            //反射实例化
            Object instance = classInfo.newInstance();
            //将该类的首个字母小写（spring默认bean的id为该类的首字母小写）
            String beanId = toLowerCaseFirstOne(classInfo.getSimpleName());
            //存入IOC容器
            map.put(beanId,instance);
        }
    }
    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
