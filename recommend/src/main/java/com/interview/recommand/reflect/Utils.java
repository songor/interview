package com.interview.recommand.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 在运行期获得泛型类的泛型类型
 */
public class Utils {

    public static <T> Class<T> getGenricClassType(Class clz) {
        Type type = clz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type[] types = pt.getActualTypeArguments();
            if (types.length > 0 && types[0] instanceof Class) {
                return (Class) types[0];
            }
        }
        return (Class) Object.class;
    }

}
