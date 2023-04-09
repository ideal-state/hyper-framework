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

import java.lang.annotation.*;

/**
 * <p>标记了该注解的类或方法成员会跳过调用者检查</p>
 * 该注解仅在 team.idealstate.hyper 包下有效
 *
 * <p>Created on 2023/2/17 21:13</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipCallerWalk {
}
