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

package team.idealstate.hyper.commons.caller;

import team.idealstate.hyper.commons.lang.NotNull;

import java.lang.reflect.Method;

/**
 * <p>调用者相关工具</p>
 *
 * <p>Created on 2023/2/17 20:15</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
@SkipCallerWalk
public abstract class CallerUtils {

    private static final String PRES_KETIKAI_HYPER = "team.idealstate.hyper";
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * 获取调用者所属类
     *
     * @return 调用者所属类
     * @see CallerUtils#getCaller()
     */
    @NotNull
    public static Class<?> getCallerClass() {
        return getCaller().getCallerClass();
    }

    /**
     * 获取调用者基本信息
     *
     * @return 调用者基本信息
     * @throws IllegalCallerException 未找到调用者时抛出，此异常通常是在主程序类入口方法内调用该方法时发生
     */
    @NotNull
    public static Caller getCaller() {
        return walk(1);
    }

    /**
     * 获取此方法的调用者基本信息<br>
     * 注意！是调用此方法的调用者，与 {@link CallerUtils#getCaller()} 所指不同
     *
     * @return 调用者基本信息
     * @throws IllegalCallerException 未找到调用者时抛出，此异常通常是在主程序类入口方法内调用该方法时发生
     */
    @NotNull
    public static Caller getSelf() {
        return walk(0);
    }

    @NotNull
    private static Caller walk(int skip) {
        final int temp = Math.max(skip, 0);
        return STACK_WALKER.walk(stream -> stream.map(stackFrame -> new Caller(
                stackFrame.getClassName(),
                stackFrame.getMethodName(),
                stackFrame.getDeclaringClass(),
                stackFrame.getMethodType(),
                stackFrame.isNativeMethod()
        )).filter(caller -> {
            if (!caller.getClassName().startsWith(PRES_KETIKAI_HYPER)) {
                return true;
            }
            final boolean checkOnClass = caller.getCallerClass().getAnnotation(SkipCallerWalk.class) == null;
            final Method callerMethod = caller.getMethod();
            final boolean checkOnMethod = callerMethod == null || callerMethod.getAnnotation(SkipCallerWalk.class) == null;
            return checkOnClass && checkOnMethod;
        }).skip(temp).findFirst().orElseThrow(IllegalCallerException::new));
    }
}
