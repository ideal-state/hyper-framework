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

package team.idealstate.hyper.commons.generic;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.reflect.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>TypeReference</p>
 *
 * <p>Created on 2023/3/24 12:13</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public abstract class TypeReference<T> {

    private final Type referenceType;

    public TypeReference() {
        this.referenceType = findGenericParent(getClass());
    }

    private TypeReference(Type type) {
        Asserts.notNull(type, "type");
        if (type instanceof Class<?> || type instanceof ParameterizedType || type instanceof WildcardType) {
            this.referenceType = type;
        } else {
            throw new IllegalArgumentException("unsupported type: " + type.getClass().getTypeName());
        }
    }

    /**
     * @return 当前（泛型）类型
     */
    public Type getReferenceType() {
        return referenceType;
    }

    /**
     * @return 当前（泛型）类型的原始类型
     */
    public Class<?> getRawReferenceType() {
        if (referenceType instanceof Class<?>) {
            return (Class<?>) referenceType;
        }
        else if (referenceType instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) referenceType).getRawType();
        }
        else if (referenceType instanceof WildcardType wildcardType) {
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            if (lowerBounds.length > 0) {
                final Type lowerBound = lowerBounds[0];
                if (lowerBound instanceof Class<?>) {
                    return (Class<?>) lowerBound;
                }
                else if (lowerBound instanceof ParameterizedType) {
                    return (Class<?>) ((ParameterizedType) lowerBound).getRawType();
                }
            }
            final Type upperBound = wildcardType.getUpperBounds()[0];
            if (upperBound instanceof Class<?>) {
                return (Class<?>) upperBound;
            }
            else if (upperBound instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) upperBound).getRawType();
            }
        }
        throw new UnsupportedOperationException("unsupported type");
    }

    /**
     * 检查目标类型是否可以转换至当前（泛型）类型 {@link TypeReference#getReferenceType()}
     *
     * @param type 目标类型
     * @return 可以则为 true，反之则为 false
     * @see ClassUtils#isAssignableFrom(Type, Type)
     */
    public boolean isAssignableFrom(Type type) {
        return ClassUtils.isAssignableFrom(referenceType, type);
    }

    /**
     * 检查当前（泛型）类型 {@link TypeReference#getReferenceType()} 是否可以转换至目标类型
     *
     * @param type 目标类型
     * @return 可以则为 true，反之则为 false
     * @see ClassUtils#isAssignableFrom(Type, Type)
     */
    public boolean isAssignableTo(Type type) {
        return ClassUtils.isAssignableFrom(type, referenceType);
    }

    /**
     * 将目标对象的类型安全地转换至当前（泛型）类型 {@link TypeReference#getReferenceType()}
     *
     * @param object 目标对象
     * @return 能够转换则返回已转换类型的对象，反之则抛出类型转换异常
     * @throws ClassCastException 当无法进行转换时抛出
     */
    @NotNull
    public T safeCast(@NotNull Object object) {
        Asserts.notNull(object, "object");
        if (!isAssignableFrom(object.getClass())) {
            throw new ClassCastException(object + " cannot be cast to type " + referenceType);
        }
        return (T) object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final TypeReference that)) {
            return false;
        }

        return getReferenceType() != null ? getReferenceType().equals(that.getReferenceType()) : that.getReferenceType() == null;
    }

    @Override
    public int hashCode() {
        return getReferenceType() != null ? getReferenceType().hashCode() : 0;
    }

    private static final Map<Type, TypeReference> FOR_CLASS_CACHE = new HashMap<>(64);

    /**
     * 获取类型的（泛型）类型引用<br>
     * 该方法的优势在于缓存机制
     *
     * @param type 类型
     * @return 原始类型（不存在泛型信息）的（泛型）类型引用
     */
    public static TypeReference forType(@NotNull Type type) {
        Asserts.notNull(type, "type");
        if (FOR_CLASS_CACHE.containsKey(type)) {
            return FOR_CLASS_CACHE.get(type);
        }
        synchronized (FOR_CLASS_CACHE) {
            if (FOR_CLASS_CACHE.containsKey(type)) {
                return FOR_CLASS_CACHE.get(type);
            }
            final TypeReference<Object> typeReference = new TypeReference<>(type) {};
            FOR_CLASS_CACHE.put(type, typeReference);
            return typeReference;
        }
    }

    private static Type findGenericParent(Class<? extends TypeReference> targetClass) {
        final Class<?> superClass = targetClass.getSuperclass();
        if (superClass == TypeReference.class) {
            final Type genericSuperclass = targetClass.getGenericSuperclass();
            if (genericSuperclass instanceof Class<?>) {
                return Object.class;
            } else if (genericSuperclass instanceof ParameterizedType parameterizedType) {
                return parameterizedType.getActualTypeArguments()[0];
            }
            throw new UnsupportedOperationException("unsupported type");
        }
        return findGenericParent((Class<? extends TypeReference>) superClass);
    }
}
