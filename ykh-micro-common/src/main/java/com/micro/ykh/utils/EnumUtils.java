package com.micro.ykh.utils;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @ClassName EnumUtils
 * @Description 根据条件获得枚举类
 * @Author xiongh
 * @Date 2021/1/9 13:34
 * @Version 1.0
 **/
public class EnumUtils {

    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    /**
     * 根据条件获取枚举对象
     *
     * @param tClass    枚举类
     * @param predicate 筛选条件
     * @param <T>
     * @return
     */
    public static <T> Optional<T> getEnumObject(Class<T> tClass, Predicate<T> predicate) {
        if (!tClass.isEnum()) {
            return Optional.empty();
        }
        Object obj = map.get(tClass);
        T[] ts;
        if(ObjectUtils.isEmpty(obj)){
            ts = tClass.getEnumConstants();
        }else{
            ts = (T[]) obj;
        }
        // 根据预期条件筛选到一个枚举
        return Arrays.stream(ts).filter(predicate).findAny();
    }
}
