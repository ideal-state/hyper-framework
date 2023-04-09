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

import team.idealstate.hyper.commons.StringUtils;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.context.ioc.annotation.Bean;

/**
 * <p>DefaultBeanDefinitionDefiner</p>
 *
 * <p>Created on 2023/3/23 8:45</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public final class DefaultBeanDefinitionDefiner extends AbstractBeanDefinitionDefiner {

    @NotNull
    @Override
    protected BeanDefinition doDefineBean(String beanName, TypeReference<?> beanTypeReference, Boolean singleton, Boolean lazy) {
        final Bean bean = beanTypeReference.getRawReferenceType().getDeclaredAnnotation(Bean.class);
        String initMethod = null;
        String destroyMethod = null;
        if (bean != null) {
            if (StringUtils.isNullOrBlank(beanName)) {
                beanName = bean.value();
            }
            if (singleton == null) {
                singleton = bean.singleton();
            }
            if (lazy == null) {
                lazy = bean.lazy();
            }
            initMethod = bean.initMethod();
            initMethod = StringUtils.isNullOrBlank(initMethod) ? null : initMethod;
            destroyMethod = bean.destroyMethod();
            destroyMethod = StringUtils.isNullOrBlank(destroyMethod) ? null : destroyMethod;
        }

        final ConfigurableBeanDefinition beanDefinition = new DefaultBeanDefinition(
                beanTypeReference,
                initMethod,
                destroyMethod
        );
        if (!StringUtils.isNullOrBlank(beanName)) {
            beanDefinition.setName(beanName);
        }
        beanDefinition.setSingleton(singleton == null || singleton);
        beanDefinition.setLazy(lazy != null && lazy);

        return beanDefinition;
    }
}
