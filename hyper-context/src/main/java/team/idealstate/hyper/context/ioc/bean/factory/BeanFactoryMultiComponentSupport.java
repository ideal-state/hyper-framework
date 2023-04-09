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

package team.idealstate.hyper.context.ioc.bean.factory;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinitionDefiner;

import java.util.List;

/**
 * <p>BeanFactoryMultiComponentSupport</p>
 *
 * <p>Created on 2023/3/23 9:25</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface BeanFactoryMultiComponentSupport {

    @NotNull
    BeanDefinitionDefiner getBeanDefinitionDefiner();

    @NotNull
    List<BeanPostProcessor> getBeanPostProcessors();

    void addBeanPostProcessor(@NotNull BeanPostProcessor beanPostProcessor);

    void removeBeanPostProcessor(@NotNull BeanPostProcessor beanPostProcessor);
}
