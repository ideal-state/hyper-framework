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

package team.idealstate.hyper.context.ioc.annotation;

import java.lang.annotation.*;

/**
 * <p>Bean</p>
 *
 * <p>Created on 2023/3/23 10:50</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * @return 名称
     */
    String value() default "";

    /**
     * @return 是否为单例
     */
    boolean singleton() default true;

    /**
     * @return 是否懒加载
     */
    boolean lazy() default false;

    /**
     * @return 初始化方法名称
     */
    String initMethod() default "";

    /**
     * @return 销毁方法名称
     */
    String destroyMethod() default "";
}
