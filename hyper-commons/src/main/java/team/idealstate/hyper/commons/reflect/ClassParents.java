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
import team.idealstate.hyper.commons.asserts.Asserts;

import java.util.*;

/**
 * <p>ClassParents</p>
 *
 * <p>Created on 2023/4/4 10:19</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class ClassParents {

    public static final int INCLUDE_SELF = 0x0001;
    public static final int INCLUDE_SAME = 0x0002;
    public static final int EXCLUDE_SUPER_CLASS = 0x0004;
    public static final int EXCLUDE_INTERFACE = 0x0008;
    public static final int SKIP_SUPER_CLASS = 0x0010;
    public static final int SKIP_INTERFACE = 0x0020;


    /**
     * 获取指定类型的父项（包括直接、间接）<br>
     * 返回的集合是有序的，且遵循原继承关系顺序<br>
     * 同一层次的父项，父类会排在父接口之前
     *
     * @param sourceClass 指定类型
     * @param options 执行参数<br>
     * {@link ClassParents#INCLUDE_SELF} 结果将包含 sourceClass 自身<br>
     * {@link ClassParents#INCLUDE_SAME} 结果将允许包含相同的父项<br>
     * {@link ClassParents#EXCLUDE_SUPER_CLASS} 结果将不包含父类<br>
     * {@link ClassParents#EXCLUDE_INTERFACE} 结果将不包含父接口<br>
     * {@link ClassParents#SKIP_SUPER_CLASS} 扫描时跳过父类<br>
     * {@link ClassParents#SKIP_INTERFACE} 扫描时跳过父接口
     * @return 有则返回，无则为空集，该方法不会返回 null
     */
    @NotNull
    public static List<Class<?>> forClass(@NotNull Class<?> sourceClass, int options) {
        Asserts.notNull(sourceClass, "sourceClass");

        final Collection<Class<?>> result = (options & INCLUDE_SAME) == 0 ?
                new LinkedHashSet<>(32) : new ArrayList<>(32);

        boolean excludeSelf = (options & INCLUDE_SELF) == 0;
        final boolean scanSuperClass = (options & SKIP_SUPER_CLASS) == 0;
        final boolean scanInterface = (options & SKIP_INTERFACE) == 0;
        final boolean excludeSuperClass = (options & EXCLUDE_SUPER_CLASS) != 0;
        final boolean excludeInterface = (options & EXCLUDE_INTERFACE) != 0;

        final List<Class<?>> current = new ArrayList<>(8);
        current.add(sourceClass);
        if (!(scanSuperClass || scanInterface) || (excludeSuperClass && excludeInterface)) {
            if (excludeSelf) {
                return Collections.emptyList();
            }
            return current;
        }

        final List<Class<?>> next = new ArrayList<>(8);
        Class<?> superClass;
        Class<?>[] interfaces;
        while (!current.isEmpty()) {
            for (final Class<?> c : current) {
                if (scanSuperClass && !c.isInterface()) {
                    superClass = c.getSuperclass();
                    if (superClass != null) {
                        next.remove(superClass);
                        next.add(superClass);
                    }
                }
                if (scanInterface) {
                    interfaces = c.getInterfaces();
                    for (final Class<?> i : interfaces) {
                        if (i != null) {
                            next.remove(i);
                            next.add(i);
                        }
                    }
                }
            }
            if (excludeSelf) {
                excludeSelf = false;
            } else {
                result.addAll(current);
            }
            current.clear();
            current.addAll(next);
            next.clear();
        }
        final Iterator<Class<?>> it = result.iterator();
        while (it.hasNext()) {
            superClass = it.next();
            if (superClass.isInterface()) {
                if (excludeInterface) {
                    it.remove();
                }
                continue;
            }
            if (excludeSuperClass) {
                it.remove();
            }
        }

        return new ArrayList<>(result);
    }
}
