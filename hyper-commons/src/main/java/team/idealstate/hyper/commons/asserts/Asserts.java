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

package team.idealstate.hyper.commons.asserts;

import team.idealstate.hyper.commons.ArrayUtils;
import team.idealstate.hyper.commons.CollectionUtils;
import team.idealstate.hyper.commons.StringUtils;

import java.util.Collection;

/**
 * <p>断言相关工具</p>
 *
 * <p>Created on 2023/2/16 19:50</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class Asserts {

    /**
     * 断言表达式的结果为 true
     *
     * @param expr 表达式
     * @param msg  异常信息
     */
    public static void isTrue(Boolean expr, String msg) {
        if (!Boolean.TRUE.equals(expr)) {
            if (StringUtils.isNullOrBlank(msg)) {
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言对象为空
     *
     * @param obj     对象
     * @param argName 参数名
     */
    public static void isNull(Object obj, String argName) {
        isNull(obj, argName, "must be null");
    }

    /**
     * 断言对象为空
     *
     * @param obj     对象
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNull(Object obj, String argName, String msg) {
        isTrue(obj == null, generateMsg(argName, msg));
    }

    /**
     * 断言对象非空
     *
     * @param obj     对象
     * @param argName 参数名
     */
    public static void notNull(Object obj, String argName) {
        notNull(obj, argName, "must not be null");
    }

    /**
     * 断言对象非空
     *
     * @param obj     对象
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNull(Object obj, String argName, String msg) {
        isTrue(obj != null, generateMsg(argName, msg));
    }

    /**
     * 断言集合为空
     *
     * @param coll    集合
     * @param argName 参数名
     */
    public static void isNullOrEmpty(Collection<?> coll, String argName) {
        isNullOrEmpty(coll, argName, "must be null or empty");
    }

    /**
     * 断言集合为空
     *
     * @param coll    集合
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(Collection<?> coll, String argName, String msg) {
        isTrue(CollectionUtils.isNullOrEmpty(coll), generateMsg(argName, msg));
    }

    /**
     * 断言集合非空
     *
     * @param coll    集合
     * @param argName 参数名
     */
    public static void notNullOrEmpty(Collection<?> coll, String argName) {
        notNullOrEmpty(coll, argName, "must not be null or empty");
    }

    /**
     * 断言集合非空
     *
     * @param coll    集合
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(Collection<?> coll, String argName, String msg) {
        isTrue(!CollectionUtils.isNullOrEmpty(coll), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(Object[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(Object[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(Object[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(Object[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(byte[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(byte[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(byte[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(byte[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(short[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(short[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(short[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(short[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(int[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(int[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(int[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(int[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(long[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(long[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(long[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(long[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(char[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(char[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(char[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(char[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(boolean[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(boolean[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(boolean[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(boolean[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(float[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(float[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(float[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(float[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void isNullOrEmpty(double[] array, String argName) {
        isNullOrEmpty(array, argName, "must be null or empty");
    }

    /**
     * 断言数组为空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void isNullOrEmpty(double[] array, String argName, String msg) {
        isTrue(ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     */
    public static void notNullOrEmpty(double[] array, String argName) {
        notNullOrEmpty(array, argName, "must not be null or empty");
    }

    /**
     * 断言数组非空
     *
     * @param array   数组
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void notNullOrEmpty(double[] array, String argName, String msg) {
        isTrue(!ArrayUtils.isNullOrEmpty(array), generateMsg(argName, msg));
    }

    /**
     * 断言字符串有文本内容（不包括空白字符）
     *
     * @param str     字符
     * @param argName 参数名
     */
    public static void hasText(String str, String argName) {
        hasText(str, argName, "must be a valid text");
    }

    /**
     * 断言字符串有文本内容（不包括空白字符）
     *
     * @param str     字符
     * @param argName 参数名
     * @param msg     异常信息
     */
    public static void hasText(String str, String argName, String msg) {
        isTrue(!StringUtils.isNullOrBlank(str), generateMsg(argName, msg));
    }

    private static String generateMsg(String argName, String msg) {
        argName = StringUtils.isNullOrBlank(argName) ? "" : "[argName: " + argName + "] ";
        msg = StringUtils.isNullOrBlank(msg) ? "" : msg;
        return argName + msg;
    }
}
