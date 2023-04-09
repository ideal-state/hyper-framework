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

package team.idealstate.hyper.context.ioc.exception;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;

import java.io.Serial;

/**
 * <p>BeanException</p>
 *
 * <p>Created on 2023/3/23 17:52</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class BeanException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4962105339931402730L;

    protected BeanException(@NotNull BeanDefinition beanDefinition, @NotNull String message) {
        super(toMessage(beanDefinition, message));
    }

    protected BeanException(@NotNull BeanDefinition beanDefinition, @NotNull String message, Throwable cause) {
        super(toMessage(beanDefinition, message), cause);
    }

    private static String toMessage(@NotNull BeanDefinition beanDefinition, @NotNull String message) {
        Asserts.notNull(beanDefinition, "beanDefinition");
        return "[name: " + beanDefinition.getName() +
                ", type: " + beanDefinition.getType() +
                ", singleton: " + beanDefinition.isSingleton() +
                ", lazy: " + beanDefinition.isLazy() +
                "] " + message;
    }
}
