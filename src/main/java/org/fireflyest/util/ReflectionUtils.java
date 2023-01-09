package org.fireflyest.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.Nullable;

/**
 * 反射工具类
 * @author Fireflyest
 * @since 1.1.7
 */
public class ReflectionUtils {

    private ReflectionUtils() {
    }
    
    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts.
     * @param ctor the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    @SuppressWarnings("all")
    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers())
                || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called
     * when actually necessary, to avoid unnecessary conflicts.
     * @param method the method to make accessible
     * @see java.lang.reflect.Method#setAccessible
     */
    @SuppressWarnings("all")
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) 
                || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }


    /**
     * Set the field represented by the supplied {@linkplain Field field object} on
     * the specified {@linkplain Object target object} to the specified {@code value}.
     * <p>In accordance with {@link Field#set(Object, Object)} semantics, the new value
     * is automatically unwrapped if the underlying field has a primitive type.
     * <p>This method does not support setting {@code static final} fields.
     * @param field the field to set
     * @param target the target object on which to set the field
     *      (or {@code null} for a static field)
     * @param value the value to set (may be {@code null})
     */
    @SuppressWarnings("all")
    public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

}
