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

package team.idealstate.hyper.context.ioc.bean.registry;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.context.ioc.able.Clearable;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;

import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>BeanDefinitionRegistry</p>
 *
 * <p>Created on 2023/3/31 23:51</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface BeanDefinitionRegistry extends Clearable {


    void registerBeanDefinition(@NotNull BeanDefinition beanDefinition);

    @Nullable
    BeanDefinition getBeanDefinition(@NotNull String beanName);

    @NotNull
    List<BeanDefinition> getBeanDefinitions(@NotNull Type beanType);

    @NotNull
    List<BeanDefinition> getBeanDefinitions();

    boolean containsBeanDefinition(@NotNull String beanName);
}
