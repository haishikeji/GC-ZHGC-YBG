package cn.px.framework.datasource;


import lombok.extern.slf4j.Slf4j;

/**
 * 数据源切换处理
 *
 * @author 品讯科技
 */
@Slf4j
public class DynamicDataSourceContextHolder
{

    private static final ThreadLocal<String> db = new ThreadLocal<>();

    public static void setDataSourceKey(String key) {
        db.set(key);
    }

    public static String getDataSourceKey() {
        return db.get();
    }

    public static void clearDataSourceKey() {
        db.remove();
    }
}
