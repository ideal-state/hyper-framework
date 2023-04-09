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

package team.idealstate.hyper.commons;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>数组相关工具</p>
 *
 * <p>Created on 2023/2/26 18:40</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class ArrayUtils {

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(Object[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(byte[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(short[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(int[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(long[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(char[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(boolean[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(float[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否为 null
     *
     * @param array 数组
     * @return 数组是否 null
     */
    public static boolean isNull(double[] array) {
        return array == null;
    }

    /**
     * 判断数组是否为 null 或空
     *
     * @param array 数组
     * @return 数组是否为 null 或空
     */
    public static boolean isNullOrEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 根据原数组类型和大小创建新数组
     *
     * @param original 原数组
     * @param <E>      数组成员类型泛型
     * @return 如果原数组为空，则返回原数组，而不是新数组
     */
    @Nullable
    @SuppressWarnings({"unchecked"})
    public static <E> E[] newInstance(@Nullable E[] original) {
        if (isNullOrEmpty(original)) {
            return original;
        }

        return (E[]) Array.newInstance(original.getClass().getComponentType(), original.length);
    }

    /**
     * 拷贝数组
     *
     * @param original 原数组
     * @param <E>      数组成员类型泛型
     * @return 如果原数组为空，则返回原数组，而不是新数组
     */
    @Nullable
    public static <E> E[] copy(@Nullable E[] original) {
        if (isNullOrEmpty(original)) {
            return original;
        }

        return Arrays.copyOf(original, original.length);
    }

    /**
     * 拷贝数组并添加新元素
     *
     * @param original 原数组
     * @param elements 新元素
     * @param <E>      数组成员类型泛型
     * @return 如果原数组为空，则返回原数组，而不是新数组
     */
    @Nullable
    @SafeVarargs
    public static <E> E[] copy(@Nullable E[] original, E... elements) {
        final int elementsLen = elements.length;
        if (elementsLen == 0) {
            return copy(original);
        }

        if (isNullOrEmpty(original)) {
            return copy(elements);
        }

        final int originalLen = original.length;
        final E[] newArray = Arrays.copyOf(original, originalLen + elementsLen);
        System.arraycopy(elements, 0, newArray, originalLen, elementsLen);
        return newArray;
    }

    /**
     * 泛型集合转泛型数组
     *
     * @param collection  集合
     * @param elementType 集合元素类型
     * @param <T>         泛型
     * @param <E>         集合元素泛型，必须与泛型 <T> 是实现关系
     * @return 泛型数组
     */
    @NotNull
    @SuppressWarnings({"unchecked"})
    public static <T, E extends T> T[] toArray(@NotNull Collection<E> collection, @NotNull Class<E> elementType) {
        Asserts.notNull(collection, "collection");
        Asserts.notNull(elementType, "elementType");

        if (collection.isEmpty()) {
            return (T[]) Array.newInstance(elementType, 0);
        }

        T[] array = (T[]) Array.newInstance(elementType, collection.size());
        int index = 0;
        for (T element : collection) {
            array[index++] = element;
        }

        return array;
    }
}
