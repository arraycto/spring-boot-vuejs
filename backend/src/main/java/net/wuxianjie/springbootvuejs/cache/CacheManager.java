package net.wuxianjie.springbootvuejs.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

/**
 * 本地集中式缓存管理器
 *
 * @author 吴仙杰
 */
public class CacheManager {

  /**
   * 30 天有效期的 TTL 缓存
   */
  private LoadingCache<String, Object> cache30DaysToLive;

  /**
   * 将构造方法私有化，使外部无法直接实例化
   */
  private CacheManager() {
    cache30DaysToLive = CacheBuilder.newBuilder()
      // 30 天有效期的 TTL 缓存
      .expireAfterWrite(30, TimeUnit.DAYS)
      .build(new CacheLoader<String, Object>() {
        @Override
        public Object load(String key) {
          return null;
        }
      });
  }

  /**
   * 单例模式：静态内部类法，延时加载，并且能保证线程安全
   *
   * <p>把 Singleton 实例放到一个静态内部类中，避免了静态实例在 Singleton 类加载的时候就创建对象</p>
   *
   * <p>由于静态内部类只会被加载一次，所以这种写法也是线程安全的</p>
   */
  private static class SingletonHolder {
    private static final CacheManager INSTANCE;
    static {
      INSTANCE = new CacheManager();
    }
  }

  /**
   * 获取单例对象
   */
  public static CacheManager getInstance() {
    return SingletonHolder.INSTANCE;
  }

  /**
   * 获取 30 天有效期的 TTL 缓存
   *
   * @return 30 天 TTL 本地缓存
   */
  public LoadingCache<String, Object> getCache30DaysToLive() {
    return cache30DaysToLive;
  }
}
