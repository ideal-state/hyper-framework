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
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinitionDefiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>AbstractBeanFactory</p>
 *
 * <p>Created on 2023/3/23 11:08</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanFactory implements BeanFactory, BeanFactoryMultiComponentSupport {

    protected final BeanDefinitionDefiner beanDefinitionDefiner;
    protected final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    protected AbstractBeanFactory(
            @NotNull BeanDefinitionDefiner beanDefinitionDefiner
    ) {
        Asserts.notNull(beanDefinitionDefiner, "beanDefinitionDefiner");
        this.beanDefinitionDefiner = beanDefinitionDefiner;
    }

    @NotNull
    @Override
    public BeanDefinitionDefiner getBeanDefinitionDefiner() {
        return beanDefinitionDefiner;
    }

    @NotNull
    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        if (!beanPostProcessors.isEmpty()) {
            return new ArrayList<>(beanPostProcessors);
        }
        return Collections.emptyList();
    }

    @Override
    public void addBeanPostProcessor(@NotNull BeanPostProcessor beanPostProcessor) {
        Asserts.notNull(beanPostProcessor, "beanPostProcessor");
        if (!this.beanPostProcessors.contains(beanPostProcessor)) {
            this.beanPostProcessors.add(beanPostProcessor);
        }
    }

    @Override
    public void removeBeanPostProcessor(@NotNull BeanPostProcessor beanPostProcessor) {
        Asserts.notNull(beanPostProcessor, "beanPostProcessor");
        this.beanPostProcessors.remove(beanPostProcessor);
    }
}
