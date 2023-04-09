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
 * <p>AbstractBeanSingletonRegistry</p>
 *
 * <p>Created on 2023/4/1 2:06</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanSingletonRegistry implements BeanSingletonRegistry {

    private final Map<String, Object> singletonsByName = new HashMap<>(58);
    private final Map<String, ObjectFactory<?>> singletonFactoriesByName = new HashMap<>(16);

    private final Map<TypeReference<?>, List<String>> namesByType = new HashMap<>(64);

    @Override
    public <T> void registerSingleton(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull T beanObject) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        Asserts.notNull(beanObject, "beanObject");
        synchronized (singletonsByName) {
            if (containsByName(beanName)) {
                throw new NameAlreadyBoundException(beanName);
            }
            singletonsByName.put(beanName, beanObject);
            final List<String> namesByType =
                    this.namesByType.computeIfAbsent(beanTypeReference, k -> new ArrayList<>(16));
            namesByType.add(beanName);
        }
    }

    @Override
    public <T> void registerSingletonFactory(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull ObjectFactory<T> objectFactory) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        Asserts.notNull(objectFactory, "objectFactory");
        synchronized (singletonsByName) {
            if (containsByName(beanName)) {
                throw new NameAlreadyBoundException(beanName);
            }
            singletonFactoriesByName.put(beanName, objectFactory);
            final List<String> namesByType =
                    this.namesByType.computeIfAbsent(beanTypeReference, k -> new ArrayList<>(16));
            namesByType.add(beanName);
        }
    }

    private Object getSingletonByNameFromFactory(String beanName) {
        final ObjectFactory<?> objectFactory = singletonFactoriesByName.get(beanName);
        if (objectFactory != null) {
            final Object singleton = objectFactory.getObject();
            Asserts.notNull(singleton, "singleton");
            singletonFactoriesByName.remove(beanName);
            singletonsByName.put(beanName, singleton);
            return singleton;
        }
        return null;
    }

    private Object getSingletonByName(String beanName) {
        Object singleton = singletonsByName.get(beanName);
        if (singleton == null) {
            singleton = getSingletonByNameFromFactory(beanName);
        }
        return singleton;
    }

    @Nullable
    @Override
    public Object getSingleton(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (singletonsByName) {
            if (containsByName(beanName)) {
                return getSingletonByName(beanName);
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Map<String, Object> getSingletons(@NotNull Type beanType) {
        Asserts.notNull(beanType, "beanType");
        synchronized (singletonsByName) {
            if (!namesByType.isEmpty()) {
                final Map<String, Object> result = new HashMap<>(16);
                final Set<Map.Entry<TypeReference<?>, List<String>>> entries = this.namesByType.entrySet();
                List<String> namesByType;
                for (final Map.Entry<TypeReference<?>, List<String>> entry : entries) {
                    if (entry.getKey().isAssignableTo(beanType)) {
                        namesByType = entry.getValue();
                        for (final String beanName : namesByType) {
                            result.put(beanName, getSingletonByName(beanName));
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
    public Map<String, Object> getSingletons() {
        synchronized (singletonsByName) {
            if (!namesByType.isEmpty()) {
                final Map<String, Object> result = new HashMap<>(32);
                if (!singletonsByName.isEmpty()) {
                    result.putAll(singletonsByName);
                }
                if (!singletonFactoriesByName.isEmpty()) {
                    final Set<Map.Entry<String, ObjectFactory<?>>> entries = singletonFactoriesByName.entrySet();
                    String beanName;
                    for (final Map.Entry<String, ObjectFactory<?>> entry : entries) {
                        beanName = entry.getKey();
                        result.put(beanName, getSingletonByNameFromFactory(beanName));
                    }
                }
                return result;
            }
        }
        return Collections.emptyMap();
    }

    private boolean containsByName(String beanName) {
        return singletonsByName.containsKey(beanName) || singletonFactoriesByName.containsKey(beanName);
    }

    @Override
    public boolean containsSingleton(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        synchronized (singletonsByName) {
            return containsByName(beanName);
        }
    }

    @Override
    public void clear() {
        synchronized (singletonsByName) {
            singletonsByName.clear();
            singletonFactoriesByName.clear();
            namesByType.clear();
        }
    }
}
