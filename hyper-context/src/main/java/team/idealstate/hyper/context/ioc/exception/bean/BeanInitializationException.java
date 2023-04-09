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

package team.idealstate.hyper.context.ioc.exception.bean;

import team.idealstate.hyper.commons.StringUtils;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.ioc.exception.BeanException;

import java.io.Serial;

/**
 * <p>BeanInitializationException</p>
 *
 * <p>Created on 2023/3/23 17:44</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public class BeanInitializationException extends BeanException {

    @Serial
    private static final long serialVersionUID = -7307719787800547199L;

    public BeanInitializationException(@NotNull BeanDefinition beanDefinition, @Nullable String message) {
        super(beanDefinition, getOrDefault(message));
    }

    public BeanInitializationException(@NotNull BeanDefinition beanDefinition, @Nullable String message, Throwable cause) {
        super(beanDefinition, getOrDefault(message), cause);
    }

    @NotNull
    private static String getOrDefault(@Nullable String message) {
        return StringUtils.isNullOrBlank(message) ? "bean initialization failure" : message;
    }
}
