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
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.ioc.bean.definition.ConfigurableBeanDefinition;
import team.idealstate.hyper.context.ioc.exception.NameAlreadyBoundException;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>AbstractBeanDefinitionRegistry</p>
 *
 * <p>Created on 2023/4/1 0:02</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> byName = new HashMap<>(128);
    private final Map<TypeReference<?>, List<BeanDefinition>> byType = new HashMap<>(128);

    @NotNull
    protected abstract String generateBeanName(@NotNull BeanDefinition beanDefinition);

    @Override
    public void registerBeanDefinition(@NotNull BeanDefinition beanDefinition) {
        Asserts.notNull(beanDefinition, "beanDefinition");
        synchronized (byName) {
            String beanName = beanDefinition.getName();
            if (beanName == null) {
                beanName = generateBeanName(beanDefinition);
                Asserts.hasText(beanName, "beanName");
            }
            if (byName.containsKey(beanName)) {
                throw new NameAlreadyBoundException(beanName);
            }
            ((ConfigurableBeanDefinition) beanDefinition).setName(beanName);
            byName.put(beanName, beanDefinition);
            final List<BeanDefinition> beanDefinitionsByType =
                    byType.computeIfAbsent(beanDefinition.getTypeReference(), k -> new ArrayList<>(8));
            beanDefinitionsByType.add(beanDefinition);
        }
    }

    @Nullable
    @Override
    public BeanDefinition getBeanDefinition(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (byName) {
            return byName.get(beanName);
        }
    }

    @NotNull
    @Override
    public List<BeanDefinition> getBeanDefinitions(@NotNull Type beanType) {
        Asserts.notNull(beanType, "beanType");
        synchronized (byName) {
            if (!byType.isEmpty()) {
                final List<BeanDefinition> result = new ArrayList<>(16);
                final Set<Map.Entry<TypeReference<?>, List<BeanDefinition>>> entries = byType.entrySet();
                List<BeanDefinition> beanDefinitionsByType;
                for (final Map.Entry<TypeReference<?>, List<BeanDefinition>> entry : entries) {
                    if (entry.getKey().isAssignableTo(beanType)) {
                        beanDefinitionsByType = entry.getValue();
                        result.addAll(beanDefinitionsByType);
                    }
                }
                return result;
            }
        }
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        synchronized (byName) {
            if (!byName.isEmpty()) {
                final List<BeanDefinition> result = new ArrayList<>(64);
                final Collection<BeanDefinition> beanDefinitions = byName.values();
                result.addAll(beanDefinitions);
                return result;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean containsBeanDefinition(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (byName) {
            return byName.containsKey(beanName);
        }
    }

    @Override
    public void clear() {
        synchronized (byName) {
            if (!byName.isEmpty()) {
                byName.clear();
                byType.clear();
            }
        }
    }
}
