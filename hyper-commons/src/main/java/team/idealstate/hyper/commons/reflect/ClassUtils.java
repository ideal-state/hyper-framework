/*
 *    Copyright 2023 ideal-state
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package team.idealstate.hyper.commons.reflect;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>ClassUtils</p>
 *
 * <p>Created on 2023/3/18 22:16</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class ClassUtils {

    /**
     * 支持泛型类型的可转换关系判断
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 目标类型是否能转换成源类型
     */
    public static boolean isAssignableFrom(Type source, Type target) {
        if (source == null || target == null) {
            return false;
        }
        if (source.equals(target)) {
            return true;
        }
        if (source instanceof Class<?> sourceClass) {
            return isAssignableFrom(sourceClass, target);
        } else if (source instanceof ParameterizedType sourceParameterizedType) {
            return isAssignableFrom(sourceParameterizedType, target);
        } else if (source instanceof WildcardType sourceWildcardType) {
            return isAssignableFrom(sourceWildcardType, target);
        }
        return false;
    }

    /**
     * 支持泛型类型的可转换关系判断
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 目标类型是否能转换成源类型
     */
    public static boolean isAssignableFrom(Class<?> source, Type target) {
        if (source == null || target == null) {
            return false;
        }
        if (source.equals(target)) {
            return true;
        }
        if (target instanceof Class<?> targetClass) {
            return source.isAssignableFrom(targetClass);
        }
        if (target instanceof ParameterizedType targetParameterizedType) {
            return source.isAssignableFrom((Class<?>) targetParameterizedType.getRawType());
        }
        if (target instanceof WildcardType targetWildcardType) {
            if (targetWildcardType.getLowerBounds().length != 0) {
                return source == Object.class;
            }

            final Type bound = targetWildcardType.getUpperBounds()[0];
            if (bound instanceof Class<?> boundClass) {
                return source.isAssignableFrom(boundClass);
            }
            if (bound instanceof ParameterizedType boundParameterizedType) {
                return source.isAssignableFrom((Class<?>) boundParameterizedType.getRawType());
            }
        }
        return false;
    }

    // @formatter:off
    /**
     * 支持泛型类型的可转换关系判断
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 目标类型是否能转换成源类型
     */
    public static boolean isAssignableFrom(ParameterizedType source, Type target) {
        if (source == null || target == null) {
            return false;
        }
        if (source.equals(target)) {
            return true;
        }
        if (target instanceof Class<?> targetClass) {
            final Class<?> sourceRawType = (Class<?>) source.getRawType();
            if (sourceRawType.equals(targetClass)) {
                for (final Type actualTypeArgument : source.getActualTypeArguments()) {
                    if (!isAssignableFrom(actualTypeArgument, Object.class)) {
                        return false;
                    }
                }
                return true;
            }
            final Type targetGenericParent = findGenericParent(targetClass, sourceRawType);
            return isAssignableFrom(source, targetGenericParent);
        }
        if (target instanceof ParameterizedType targetParameterizedType) {
            if (!((Class<?>) source.getRawType()).isAssignableFrom((Class<?>) targetParameterizedType.getRawType())) {
                return false;
            }

            final Type[] sourceTypeArgs = source.getActualTypeArguments();
            final Type[] targetTypeArgs = targetParameterizedType.getActualTypeArguments();
            if (sourceTypeArgs.length != targetTypeArgs.length) {
                return false;
            }

            final int length = sourceTypeArgs.length;
            Type sourceTypeArg;
            for (int i = 0; i < length; i++) {
                sourceTypeArg = sourceTypeArgs[i];
                if (sourceTypeArg instanceof Class<?> sourceTypeArgClass) {
                    if (!isAssignableFrom(sourceTypeArgClass, targetTypeArgs[i])) {
                        return false;
                    }
                }

                else if (sourceTypeArg instanceof ParameterizedType sourceTypeArgParameterizedType) {
                    if (!isAssignableFrom(sourceTypeArgParameterizedType, targetTypeArgs[i])) {
                        return false;
                    }
                }

                else if (sourceTypeArg instanceof WildcardType sourceTypeArgWildcardType) {
                    if (!isAssignableFrom(sourceTypeArgWildcardType, targetTypeArgs[i])) {
                        return false;
                    }
                }

                else {
                    return false;
                }
            }
            return true;
        }
        if (target instanceof WildcardType targetWildcardType) {
            if (targetWildcardType.getLowerBounds().length != 0) {
                return false;
            }
            if (targetWildcardType.getUpperBounds()[0] instanceof ParameterizedType boundParameterizedType) {
                return isAssignableFrom(source, boundParameterizedType);
            }
        }
        return false;
    }
    // @formatter:on

    // @formatter:off
    /**
     * 支持泛型类型的可转换关系判断
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 目标类型是否能转换成源类型
     */
    public static boolean isAssignableFrom(WildcardType source, Type target) {
        if (source == null || target == null) {
            return false;
        }
        if (source.equals(target)) {
            return true;
        }
        if (target instanceof WildcardType targetWildcardType) {
            final boolean result = isAssignableFrom(source.getUpperBounds()[0], targetWildcardType.getUpperBounds()[0]);
            final Type[] sourceLowerBounds = source.getLowerBounds();
            final Type[] targetWildcardTypeLowerBounds = targetWildcardType.getLowerBounds();
            if (sourceLowerBounds.length != 0) {
                if (targetWildcardTypeLowerBounds.length != 0) {
                    return result && isAssignableFrom(sourceLowerBounds[0], targetWildcardTypeLowerBounds[0]);
                }
                return sourceLowerBounds[0] == Object.class;
            }
            return result;
        }
        if (isAssignableFrom(source.getUpperBounds()[0], target)) {
            final Type[] sourceLowerBounds = source.getLowerBounds();
            if (sourceLowerBounds.length != 0) {
                return isAssignableFrom(sourceLowerBounds[0], target);
            }
            return true;
        }
        return false;
    }
    // @formatter:on

    /**
     * 在源类型上寻找指定类型的泛型表示
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 找到则返回，反之则为 null
     */
    @Nullable
    public static Type findGenericParent(@NotNull Class<?> source, @NotNull Class<?> target) {
        Asserts.notNull(source, "source");
        Asserts.notNull(target, "target");

        List<Class<?>> parents = new ArrayList<>(ClassParents.forClass(source, ClassParents.INCLUDE_SELF));
        Iterator<Class<?>> iterator = parents.iterator();
        boolean flag = false;
        Class<?> next;
        while (iterator.hasNext()) {
            next = iterator.next();
            if (flag) {
                iterator.remove();
            }
            if (target.equals(next)) {
                iterator.remove();
                flag = true;
            }
        }

        if (flag) {
            Collections.reverse(parents);
            final boolean targetIsInterface = target.isInterface();
            iterator = parents.iterator();
            while (iterator.hasNext()) {
                next = iterator.next();
                if (targetIsInterface) {
                    final Type[] genericInterfaces = next.getGenericInterfaces();
                    for (final Type genericInterface : genericInterfaces) {
                        if (genericInterface instanceof Class<?> genericInterfaceClass) {
                            if (target.equals(genericInterfaceClass)) {
                                return genericInterface;
                            }
                        } else if (genericInterface instanceof ParameterizedType genericInterfaceParameterizedType) {
                            if (target.equals(genericInterfaceParameterizedType.getRawType())) {
                                return genericInterface;
                            }
                        }
                    }
                } else {
                    if (target.equals(next.getSuperclass())) {
                        return next.getGenericSuperclass();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断目标对象是否为原始数据类型
     *
     * @param object 目标对象
     * @return 是则 true，反之 false
     */
    public static boolean isPrimitive(Object object) {
        if (object == null) {
            return false;
        }
        return isPrimitive(object.getClass());
    }

    /**
     * 判断目标类是否为原始数据类型
     *
     * @param clazz 目标类
     * @return 是则 true，反之 false
     */
    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isPrimitive();
    }

    /**
     * 判断目标对象是否可以转换为原始数据类型
     *
     * @param object 目标对象
     * @return 是则 true，反之 false
     */
    public static boolean canAsPrimitive(Object object) {
        if (object == null) {
            return false;
        }
        return canAsPrimitive(object.getClass());
    }

    /**
     * 判断目标类是否可以转换为原始数据类型
     *
     * @param clazz 目标类
     * @return 是则 true，反之 false
     */
    public static boolean canAsPrimitive(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (Boolean.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Byte.class.isAssignableFrom(clazz) ||
                Short.class.isAssignableFrom(clazz) ||
                Integer.class.isAssignableFrom(clazz) ||
                Long.class.isAssignableFrom(clazz) ||
                Float.class.isAssignableFrom(clazz) ||
                Double.class.isAssignableFrom(clazz) ||
                Void.class.isAssignableFrom(clazz)
        ) {
            return true;
        }
        return clazz.isPrimitive();
    }
}
