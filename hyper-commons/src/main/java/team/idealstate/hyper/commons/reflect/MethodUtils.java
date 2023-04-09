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

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * <p>Method</p>
 *
 * <p>Created on 2023/3/18 22:17</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class MethodUtils {

    /**
     * 判断子方法是否重写了父方法
     *
     * @param superMethod 父方法
     * @param thisMethod 子方法
     * @return 子方法是否重写了父方法
     */
    public static boolean isOverride(@NotNull Method superMethod, @NotNull Method thisMethod) {
        Asserts.notNull(superMethod, "superMethod");
        Asserts.notNull(thisMethod, "thisMethod");

        if (superMethod.equals(thisMethod)) {
            return true;
        }

        if (!superMethod.getDeclaringClass().isAssignableFrom(thisMethod.getDeclaringClass())) {
            return false;
        }

        if (!superMethod.getName().equals(thisMethod.getName())) {
            return false;
        }

        final Type[] superParameterTypes = superMethod.getGenericParameterTypes();
        final Type[] thisParameterTypes = thisMethod.getGenericParameterTypes();
        final int length = superParameterTypes.length;
        if (length != thisParameterTypes.length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (superParameterTypes[i].equals(thisParameterTypes[i])) {
                continue;
            }
            return false;
        }

        return superMethod.getGenericReturnType().equals(thisMethod.getGenericReturnType());
    }
}
