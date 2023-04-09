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

package team.idealstate.hyper.context.ioc.bean.definition;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;

/**
 * <p>AbstractBeanDefinitionDefiner</p>
 *
 * <p>Created on 2023/3/23 8:42</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanDefinitionDefiner implements BeanDefinitionDefiner {

    @NotNull
    @Override
    public BeanDefinition defineBean(@NotNull TypeReference<?> beanTypeReference) {
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        return doDefineBean(null, beanTypeReference, null, null);
    }

    @NotNull
    @Override
    public BeanDefinition defineBean(@NotNull String beanName, @NotNull TypeReference<?> beanTypeReference) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        return doDefineBean(beanName, beanTypeReference, null, null);
    }

    @NotNull
    @Override
    public BeanDefinition defineBean(@NotNull String beanName, @NotNull TypeReference<?> beanTypeReference, boolean singleton, boolean lazy) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        return doDefineBean(beanName, beanTypeReference, singleton, lazy);
    }

    @NotNull
    protected abstract BeanDefinition doDefineBean(String beanName, TypeReference<?> beanTypeReference, Boolean singleton, Boolean lazy);
}
