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
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.context.ioc.ObjectFactory;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>DefaultBeanObjectRegistry</p>
 *
 * <p>Created on 2023/4/1 5:44</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public final class DefaultBeanObjectRegistry implements BeanObjectRegistry {

    private final BeanSingletonRegistry beanSingletonRegistry = new DefaultBeanSingletonRegistry();
    private final BeanPrototypeRegistry beanPrototypeRegistry = new DefaultBeanPrototypeRegistry();

    @Override
    public <T> void registerSingleton(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull T beanObject) {
        beanSingletonRegistry.registerSingleton(beanName, beanTypeReference, beanObject);
    }

    @Override
    public <T> void registerSingletonFactory(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull ObjectFactory<T> objectFactory) {
        beanSingletonRegistry.registerSingletonFactory(beanName, beanTypeReference, objectFactory);
    }

    @Nullable
    @Override
    public Object getSingleton(@NotNull String beanName) {
        return beanSingletonRegistry.getSingleton(beanName);
    }

    @NotNull
    @Override
    public Map<String, Object> getSingletons(@NotNull Type beanType) {
        return beanSingletonRegistry.getSingletons(beanType);
    }

    @NotNull
    @Override
    public Map<String, Object> getSingletons() {
        return beanSingletonRegistry.getSingletons();
    }

    @Override
    public boolean containsSingleton(@NotNull String beanName) {
        return beanSingletonRegistry.containsSingleton(beanName);
    }

    @Override
    public <T> void registerPrototypeFactory(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull ObjectFactory<T> objectFactory) {
        beanPrototypeRegistry.registerPrototypeFactory(beanName, beanTypeReference, objectFactory);
    }

    @Nullable
    @Override
    public Object getPrototype(@NotNull String beanName) {
        return beanPrototypeRegistry.getPrototype(beanName);
    }

    @NotNull
    @Override
    public Map<String, Object> getPrototypes(@NotNull Type beanType) {
        return beanPrototypeRegistry.getPrototypes(beanType);
    }

    @NotNull
    @Override
    public Map<String, Object> getPrototypes() {
        return beanPrototypeRegistry.getPrototypes();
    }

    @Override
    public boolean containsPrototype(@NotNull String beanName) {
        return beanPrototypeRegistry.containsPrototype(beanName);
    }

    @Override
    public void clear() {
        beanSingletonRegistry.clear();
        beanPrototypeRegistry.clear();
    }

    @Override
    public boolean containsBean(@NotNull String beanName) {
        return containsSingleton(beanName) || containsPrototype(beanName);
    }
}
