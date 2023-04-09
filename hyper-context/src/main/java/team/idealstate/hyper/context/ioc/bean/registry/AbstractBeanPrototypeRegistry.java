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
import team.idealstate.hyper.context.ioc.ObjectFactory;
import team.idealstate.hyper.context.ioc.exception.NameAlreadyBoundException;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>AbstractBeanPrototypeRegistry</p>
 *
 * <p>Created on 2023/4/1 2:06</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanPrototypeRegistry implements BeanPrototypeRegistry {

    private final Map<String, ObjectFactory<?>> prototypeFactoriesByName = new HashMap<>(32);

    private final Map<TypeReference<?>, List<String>> namesByType = new HashMap<>(32);

    @Override
    public <T> void registerPrototypeFactory(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull ObjectFactory<T> objectFactory) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        Asserts.notNull(objectFactory, "objectFactory");
        synchronized (prototypeFactoriesByName) {
            if (containsByName(beanName)) {
                throw new NameAlreadyBoundException(beanName);
            }
            prototypeFactoriesByName.put(beanName, objectFactory);
            final List<String> namesByType =
                    this.namesByType.computeIfAbsent(beanTypeReference, k -> new ArrayList<>(8));
            namesByType.add(beanName);
        }
    }

    private Object getPrototypeByNameFromFactory(String beanName) {
        final ObjectFactory<?> objectFactory = prototypeFactoriesByName.get(beanName);
        if (objectFactory != null) {
            final Object prototype = objectFactory.getObject();
            Asserts.notNull(prototype, "prototype");
            return prototype;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getPrototype(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (prototypeFactoriesByName) {
            if (containsByName(beanName)) {
                return getPrototypeByNameFromFactory(beanName);
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Map<String, Object> getPrototypes(@NotNull Type beanType) {
        Asserts.notNull(beanType, "beanType");
        synchronized (prototypeFactoriesByName) {
            if (!prototypeFactoriesByName.isEmpty()) {
                final Map<String, Object> result = new HashMap<>(8);
                final Set<Map.Entry<TypeReference<?>, List<String>>> entries = this.namesByType.entrySet();
                List<String> namesByType;
                for (final Map.Entry<TypeReference<?>, List<String>> entry : entries) {
                    if (entry.getKey().isAssignableTo(beanType)) {
                        namesByType = entry.getValue();
                        for (final String beanName : namesByType) {
                            result.put(beanName, getPrototypeByNameFromFactory(beanName));
                        }
                    }
                }
                return result;
            }
        }
        return Collections.emptyMap();
    }

    @NotNull
    @Override
    public Map<String, Object> getPrototypes() {
        synchronized (prototypeFactoriesByName) {
            if (!prototypeFactoriesByName.isEmpty()) {
                final Map<String, Object> result = new HashMap<>(16);
                final Set<Map.Entry<String, ObjectFactory<?>>> entries = prototypeFactoriesByName.entrySet();
                String beanName;
                for (final Map.Entry<String, ObjectFactory<?>> entry : entries) {
                    beanName = entry.getKey();
                    result.put(beanName, getPrototypeByNameFromFactory(beanName));
                }
                return result;
            }
        }
        return Collections.emptyMap();
    }

    private boolean containsByName(String beanName) {
        return prototypeFactoriesByName.containsKey(beanName);
    }

    @Override
    public boolean containsPrototype(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (prototypeFactoriesByName) {
            return containsByName(beanName);
        }
    }

    @Override
    public void clear() {
        synchronized (prototypeFactoriesByName) {
            prototypeFactoriesByName.clear();
            namesByType.clear();
        }
    }
}
