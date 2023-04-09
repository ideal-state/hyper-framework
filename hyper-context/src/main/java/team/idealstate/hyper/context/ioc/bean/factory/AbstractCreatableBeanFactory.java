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
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.commons.order.OrderComparator;
import team.idealstate.hyper.context.ioc.ObjectFactory;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinitionDefiner;
import team.idealstate.hyper.context.ioc.bean.registry.BeanDefinitionRegistry;
import team.idealstate.hyper.context.ioc.bean.registry.BeanObjectRegistry;
import team.idealstate.hyper.context.ioc.exception.bean.BeanCreationException;
import team.idealstate.hyper.context.ioc.exception.bean.BeanInitializationException;
import team.idealstate.hyper.context.ioc.exception.bean.CircularDependencyException;

import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>AbstractCreatableBeanFactory</p>
 *
 * <p>Created on 2023/3/23 13:34</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractCreatableBeanFactory extends AbstractRegistrableBeanFactory {

    private final Map<String, Object> earlyObjects = new HashMap<>(16);

    private final Map<String, BeanDefinition> currentlyInCreationBeans = new LinkedHashMap<>(16);

    protected AbstractCreatableBeanFactory(
            @NotNull BeanDefinitionDefiner beanDefinitionDefiner,
            @NotNull BeanDefinitionRegistry beanDefinitionRegistry,
            @NotNull BeanObjectRegistry beanObjectRegistry
    ) {
        super(beanDefinitionDefiner, beanDefinitionRegistry, beanObjectRegistry);
    }

    @Override
    protected void doRefresh() {
        final List<BeanDefinition> needRefreshBeanDefinitions = beanDefinitionRegistry.getBeanDefinitions()
                .stream()
                .filter(beanDefinition -> {
                    final String beanName = beanDefinition.getName();
                    return isEagerRefresh(beanName);
                })
                .toList();
        for (final BeanDefinition beanDefinition : needRefreshBeanDefinitions) {
            getOrCreateBean(beanDefinition, true);
        }
    }

    private boolean isReady(String beanName) {
        return beanObjectRegistry.containsBean(beanName);
    }

    private boolean isEagerRefresh(String beanName) {
        return !isReady(beanName);
    }

    private boolean isCurrentlyCreating(String beanName) {
        return currentlyInCreationBeans.containsKey(beanName);
    }

    protected abstract void destroyBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject);

    @Override
    protected void doClear() {
        final Map<String, Object> singletons = beanObjectRegistry.getSingletons();
        if (!singletons.isEmpty()) {
            singletons.forEach((beanName, singleton) -> {
                final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
                assert beanDefinition != null;
                destroyBean(beanDefinition, singleton);
            });
        }
        beanDefinitionRegistry.clear();
        beanObjectRegistry.clear();
    }

    @NotNull
    protected abstract Object createSingleton(@NotNull BeanDefinition beanDefinition);

    @NotNull
    protected abstract ObjectFactory<?> createSingletonFactory(@NotNull BeanDefinition beanDefinition);

    @NotNull
    protected abstract ObjectFactory<?> createPrototypeFactory(@NotNull BeanDefinition beanDefinition);

    private void doCreateBean(BeanDefinition beanDefinition) {
        final String beanName = beanDefinition.getName();
        if (isCurrentlyCreating(beanName)) {
            throw new CircularDependencyException(beanDefinition, null);
        }
        currentlyInCreationBeans.put(beanName, beanDefinition);

        if (beanDefinition.isLazy() || !beanDefinition.isSingleton()) {
            doCreateObjectFactory(beanDefinition);
            return;
        }

        Object singleton = beanObjectRegistry.getSingleton(beanName);
        if (singleton == null) {
            singleton = earlyObjects.get(beanName);
        }
        if (singleton == null) {
            singleton = createSingleton(beanDefinition);
            Asserts.notNull(singleton, "singleton");
            verifyType(beanDefinition.getTypeReference(), singleton.getClass());
        }
        earlyObjects.put(beanName, singleton);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private void doCreateObjectFactory(BeanDefinition beanDefinition) {
        final boolean isSingleton = beanDefinition.isSingleton();
        final ObjectFactory objectFactory;
        if (isSingleton) {
            objectFactory = createSingletonFactory(beanDefinition);
            Asserts.notNull(objectFactory, "singletonFactory");
        } else {
            objectFactory = createPrototypeFactory(beanDefinition);
            Asserts.notNull(objectFactory, "prototypeFactory");
        }
        // @formatter:off
//        final Type genericParent = ClassUtils.findGenericParent(objectFactory.getClass(), BeanFactory.class);
//        Asserts.notNull(genericParent, "genericParent");
//        try {
//            assert genericParent != null;
//            final TypeReference<?> beanTypeReference = beanDefinition.getTypeReference();
//            final Type actualTypeArgument = ((ParameterizedType) genericParent).getActualTypeArguments()[0];
//            verifyType(beanTypeReference, actualTypeArgument);
//            final String beanName = beanDefinition.getName();
//            if (isSingleton) {
//                beanObjectRegistry.registerSingletonFactory(beanName, beanTypeReference, objectFactory);
//            } else {
//                beanObjectRegistry.registerPrototypeFactory(beanName, beanTypeReference, objectFactory);
//            }
//        } catch (ClassCastException e) {
//            throw new IllegalStateException("cannot get actual type arguments in raw types");
//        }
        // @formatter:on
        final String beanName = beanDefinition.getName();
        final TypeReference<?> beanTypeReference = beanDefinition.getTypeReference();
        if (isSingleton) {
            beanObjectRegistry.registerSingletonFactory(beanName, beanTypeReference, objectFactory);
        } else {
            beanObjectRegistry.registerPrototypeFactory(beanName, beanTypeReference, objectFactory);
        }
    }

    private Object doGetBean(BeanDefinition beanDefinition) {
        String beanName = beanDefinition.getName();
        Object beanObject;
        final boolean isSingleton = beanDefinition.isSingleton();
        if (isSingleton) {
            beanObject = beanObjectRegistry.getSingleton(beanName);
        } else {
            beanObject = beanObjectRegistry.getPrototype(beanName);
        }
        if (beanObject != null && (!isSingleton || beanDefinition.isLazy())) {
            return initializeBean(beanDefinition, beanObject);
        }
        return beanObject;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private Object getOrCreateBean(BeanDefinition beanDefinition, boolean onlyCreate) {
        String beanName = beanDefinition.getName();
        if (isReady(beanName)) {
            if (onlyCreate) {
                return null;
            }
            return doGetBean(beanDefinition);
        }

        Object earlyObject = earlyObjects.get(beanName);
        if (earlyObject == null) {
            try {
                doCreateBean(beanDefinition);
                if (isReady(beanName)) {
                    if (onlyCreate) {
                        return null;
                    }
                    return doGetBean(beanDefinition);
                }
                earlyObject = earlyObjects.get(beanName);
                Asserts.notNull(earlyObject, "earlyObject");
            } catch (Exception e) {
                throw new BeanCreationException(beanDefinition, null, e);
            }
        }
        earlyObject = initializeBean(beanDefinition, earlyObject);
        beanObjectRegistry.registerSingleton(beanName,
                (TypeReference) beanDefinition.getTypeReference(), earlyObject);
        earlyObjects.remove(beanName);

        if (!currentlyInCreationBeans.isEmpty()) {
            currentlyInCreationBeans.clear();
        }
        return earlyObject;
    }

    protected abstract void invokeAwareMethod(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject);

    protected abstract void initBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject);

    protected abstract void populateBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject);

    @NotNull
    private Object initializeBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        Asserts.notNull(beanDefinition, "beanDefinition");
        Asserts.notNull(beanObject, "beanObject");
        try {
            final String beanName = beanDefinition.getName();
            final TypeReference<?> beanTypeReference = beanDefinition.getTypeReference();

            for (final BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanObject = beanPostProcessor.postProcessBeforeInitialization(beanName, beanObject);
                Asserts.notNull(beanObject, "beanObject");
                verifyType(beanTypeReference, beanObject.getClass());
            }

            invokeAwareMethod(beanDefinition, beanObject);

            initBean(beanDefinition, beanObject);

            populateBean(beanDefinition, beanObject);

            for (final BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanObject = beanPostProcessor.postProcessAfterInitialization(beanName, beanObject);
                Asserts.notNull(beanObject, "beanObject");
                verifyType(beanTypeReference, beanObject.getClass());
            }
            return beanObject;
        } catch (Exception e) {
            throw new BeanInitializationException(beanDefinition, null, e);
        }
    }

    private void verifyType(TypeReference<?> beanType, Type type) {
        if (!beanType.isAssignableFrom(type)) {
            throw new IllegalStateException("[beanType: " + beanType.getReferenceType().getTypeName() +
                    ", type: " + type.getTypeName() + "] type cannot be assigned to beanType");
        }
    }

    @Nullable
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getBean(@NotNull String beanName) {
        Asserts.hasText(beanName, "beanName");
        try {
            globalLock.lock();
            final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (beanDefinition == null) {
                return null;
            }
            return (T) getOrCreateBean(beanDefinition, false);
        } finally {
            globalLock.unlock();
        }
    }

    @Nullable
    @Override
    public <T> T getBean(@NotNull Class<T> beanType) {
        Asserts.notNull(beanType, "beanType");
        final List<T> result = new ArrayList<>(getBeans(beanType).values());
        result.sort(OrderComparator.normal());
        for (final T beanObject : result) {
            return beanObject;
        }
        return null;
    }

    @Nullable
    @Override
    public <T> T getBean(@NotNull TypeReference<T> beanTypeReference) {
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        final List<T> result = new ArrayList<>(getBeans(beanTypeReference).values());
        result.sort(OrderComparator.normal());
        for (final T beanObject : result) {
            return beanObject;
        }
        return null;
    }

    @Nullable
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getBean(@NotNull String beanName, @NotNull Class<T> beanType) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanType, "beanType");
        return (T) getBean(beanName, TypeReference.forType(beanType));
    }

    @Nullable
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getBean(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference) {
        Asserts.hasText(beanName, "beanName");
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        try {
            globalLock.lock();
            final BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);
            if (beanDefinition == null ||
                    !beanTypeReference.isAssignableFrom(beanDefinition.getTypeReference().getReferenceType())
            ) {
                return null;
            }
            try {
                return (T) getOrCreateBean(beanDefinition, false);
            } catch (ClassCastException ignored) {}
        } finally {
            globalLock.unlock();
        }
        return null;
    }

    @NotNull
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> Map<String, T> getBeans(@NotNull Class<T> beanType) {
        Asserts.notNull(beanType, "beanType");
        return getBeans(TypeReference.forType(beanType));
    }

    @NotNull
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> Map<String, T> getBeans(@NotNull TypeReference<T> beanTypeReference) {
        Asserts.notNull(beanTypeReference, "beanTypeReference");
        try {
            globalLock.lock();
            final Type beanReferenceType = beanTypeReference.getReferenceType();
            final List<BeanDefinition> beanDefinitions =
                    beanDefinitionRegistry.getBeanDefinitions(beanReferenceType);
            if (beanDefinitions.isEmpty()) {
                return Collections.emptyMap();
            }
            final Map<String, T> result = new HashMap<>(32);
            for (final BeanDefinition beanDefinition : beanDefinitions) {
                try {
                    result.put(beanDefinition.getName(), (T) getOrCreateBean(beanDefinition, false));
                } catch (ClassCastException ignored) {}
            }
            return result;
        } finally {
            globalLock.unlock();
        }
    }
}
