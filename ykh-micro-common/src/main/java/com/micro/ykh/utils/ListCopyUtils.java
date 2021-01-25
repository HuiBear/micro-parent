package com.micro.ykh.utils;




import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName ListCopyUtils
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/25 11:45
 * @Version 1.0
 **/
public class ListCopyUtils<T> {

    public void copyList(Object obj, List<T> targetList, Class<T> classObj) {
        if ((!Objects.isNull(obj)) && (!Objects.isNull(targetList))) {
            List source = (List) obj;
            source.forEach(item -> {
                try {
                    T data = classObj.newInstance();
                    BeanUtils.copyProperties(item, data);
                    targetList.add(data);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }


            });
        }
    }
}
