package net.wuxianjie.springbootvuejs.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 用于从静态上下文获取 Spring bean 的工具类
 *
 * @author 吴仙杰
 */
@SuppressWarnings("unused")
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * 返回唯一匹配指定对象类型的 bean 实例（如果有的话）
   *
   * @param clazz bean 必须匹配的类型；可以是接口或超类
   * @return 与指定类型所匹配的单个 bean 实例
   * @throws NoSuchBeanDefinitionException 如果没有找到指定类型的 bean
   * @throws NoUniqueBeanDefinitionException 如果找到多个指定类型的 bean
   * @throws BeansException 如果无法创建该 bean
   */
  public static <T> T getBean(Class<T> clazz) {
    return ApplicationContextHolder.applicationContext.getBean(clazz);
  }

  /**
   * 返回指定 bean 的实例
   *
   * @param qualifier bean 的名字
   * @param clazz bean 必须匹配的类型；可以是接口或超类
   * @return 指定 bean 的实例
   */
  public static <T> T getBean(String qualifier, Class<T> clazz) {
    return ApplicationContextHolder.applicationContext.getBean(qualifier, clazz);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    synchronized (this) {
      if (ApplicationContextHolder.applicationContext == null) {
        ApplicationContextHolder.applicationContext = applicationContext;
      }
    }
  }
}
