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

import team.idealstate.hyper.commons.StringUtils;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.commons.order.Order;
import team.idealstate.hyper.commons.order.OrderComparator;
import team.idealstate.hyper.context.ioc.ObjectFactory;
import team.idealstate.hyper.context.ioc.annotation.Autowired;
import team.idealstate.hyper.context.ioc.annotation.DestroyMethod;
import team.idealstate.hyper.context.ioc.annotation.InitMethod;
import team.idealstate.hyper.context.ioc.annotation.Qualifier;
import team.idealstate.hyper.context.ioc.aware.BeanFactoryAware;
import team.idealstate.hyper.context.ioc.aware.BeanNameAware;
import team.idealstate.hyper.context.ioc.aware.IAware;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinitionDefiner;
import team.idealstate.hyper.context.ioc.bean.registry.BeanDefinitionRegistry;
import team.idealstate.hyper.context.ioc.bean.registry.BeanObjectRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * <p>AbstractAutowireableBeanFactory</p>
 *
 * <p>Created on 2023/4/3 1:42</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractAutowireableBeanFactory extends AbstractCreatableBeanFactory {

    protected AbstractAutowireableBeanFactory(
            @NotNull BeanDefinitionDefiner beanDefinitionDefiner,
            @NotNull BeanDefinitionRegistry beanDefinitionRegistry,
            @NotNull BeanObjectRegistry beanObjectRegistry) {
        super(beanDefinitionDefiner, beanDefinitionRegistry, beanObjectRegistry);
    }

    @NotNull
    @Override
    protected Object createSingleton(@NotNull BeanDefinition beanDefinition) {
        return createSingletonFactory(beanDefinition).getObject();
    }

    @NotNull
    @Override
    protected ObjectFactory<?> createSingletonFactory(@NotNull BeanDefinition beanDefinition) {
        return new AutowireableBeanObjectFactory(this, beanDefinition);
    }

    @NotNull
    @Override
    protected ObjectFactory<?> createPrototypeFactory(@NotNull BeanDefinition beanDefinition) {
        return new AutowireableBeanObjectFactory(this, beanDefinition);
    }

    @Override
    protected void invokeAwareMethod(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        if (beanObject instanceof IAware) {
            if (beanObject instanceof BeanNameAware beanNameAware) {
                beanNameAware.setBeanName(beanDefinition.getName());
            }
            if (beanObject instanceof BeanFactoryAware beanFactoryAware) {
                beanFactoryAware.setBeanFactory(this);
            }
        }
    }

    @Override
    protected void initBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        final Class<?> beanType = beanDefinition.getType();
        final String initMethodName = beanDefinition.getInitMethod();
        try {
            invokeLifeMethod(beanType, initMethodName, beanObject);
            final Method[] declaredMethods = beanType.getDeclaredMethods();
            invokeAnnotatedLifeMethods(declaredMethods, initMethodName, InitMethod.class, beanObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void destroyBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        final Class<?> beanType = beanDefinition.getType();
        final String destroyMethodName = beanDefinition.getDestroyMethod();
        try {
            invokeLifeMethod(beanType, destroyMethodName, beanObject);
            final Method[] declaredMethods = beanType.getDeclaredMethods();
            invokeAnnotatedLifeMethods(declaredMethods, destroyMethodName, DestroyMethod.class, beanObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeLifeMethod(Class<?> beanType, String methodName, Object object) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.isNullOrBlank(methodName)) {
            return;
        }
        try {
            final Method method = beanType.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(object);
        } catch (NoSuchMethodException ignored) {}
    }

    private void invokeAnnotatedLifeMethods(
            Method[] declaredMethods,
            String excludeName,
            Class<? extends Annotation> annotationType,
            Object object
    ) throws InvocationTargetException, IllegalAccessException {
        final List<Method> methods = new ArrayList<>(declaredMethods.length / 2);
        for (final Method declaredMethod : declaredMethods) {
            if (!declaredMethod.getName().equals(excludeName) &&
                    declaredMethod.getDeclaredAnnotation(annotationType) != null
            ) {
                methods.add(declaredMethod);
            }
        }
        methods.sort(OrderComparator.reflectElement());
        for (final Method method : methods) {
            method.setAccessible(true);
            method.invoke(object);
        }
    }

    @Override
    protected void populateBean(@NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {

    }

    @SuppressWarnings({"rawtypes"})
    private static class AutowireableBeanObjectFactory implements ObjectFactory {

        private static final Class<? extends Annotation> AUTOWIRED = Autowired.class;
        private static final TypeReference<Map<String, Object>> BEANS_MAP_TYPE = new TypeReference<>(){};
        private static final TypeReference<Collection<Object>> BEANS_COLLECTION_TYPE = new TypeReference<>(){};

        private final BeanFactory beanFactory;
        private final BeanDefinition beanDefinition;

        private AutowireableBeanObjectFactory(
                @NotNull BeanFactory beanFactory,
                @NotNull BeanDefinition beanDefinition
        ) {
            Asserts.notNull(beanFactory, "beanFactory");
            Asserts.notNull(beanDefinition, "beanDefinition");
            this.beanFactory = beanFactory;
            this.beanDefinition = beanDefinition;

            final Type referenceType = beanDefinition.getTypeReference().getReferenceType();
            if (referenceType instanceof Class<?> that) {
                if (that.isInterface() ||
                        that.isArray() ||
                        that.isAnnotation() ||
                        that.isEnum() ||
                        that.isPrimitive() ||
                        Modifier.isAbstract(that.getModifiers())
                ) {
                    throw new IllegalArgumentException("unsupported type: " + referenceType.getTypeName());
                }
            } else {
                throw new IllegalArgumentException("unsupported type: " + referenceType.getTypeName());
            }
        }

        @Override
        @SuppressWarnings({"unchecked"})
        public Object getObject() {
            final List<Constructor<?>> constructors = resolveConstructors(beanDefinition.getType());
            Parameter[] parameters;
            final List<Object> parameterObjects = new ArrayList<>(6);
            for (final Constructor<?> constructor : constructors) {
                parameters = constructor.getParameters();
                if (parameters.length > 0) {
                    Qualifier qualifier;
                    Type parameterizedType;
                    String parameterName;
                    TypeReference beanTypeReference;
                    Object parameterObject;
                    Map<String, Object> beanObjects;
                    for (final Parameter parameter : parameters) {
                        qualifier = parameter.getDeclaredAnnotation(Qualifier.class);
                        parameterizedType = parameter.getParameterizedType();
                        parameterName = parameter.getName();
                        if (qualifier != null && !StringUtils.isNullOrBlank(qualifier.value())) {
                            parameterName = qualifier.value();
                        }
                        beanTypeReference = TypeReference.forType(parameterizedType);
                        parameterObject = beanFactory.getBean(parameterName, beanTypeReference);
                        if (parameterObject == null && qualifier == null) {
                            beanObjects = beanFactory.getBeans(beanTypeReference);
                            for (final Object beanObject : beanObjects.values()) {
                                parameterObject = beanObject;
                                break;
                            }
                        }
                        // @formatter:off
                        if (parameterObject == null && parameterizedType instanceof ParameterizedType that) {
                            final Type thatRawType = that.getRawType();
                            if (BEANS_MAP_TYPE.isAssignableFrom(that) &&
                                    Map.class.equals(thatRawType)
                            ) {
                                final Type beanType = that.getActualTypeArguments()[1];
                                parameterObject = beanFactory.getBeans(TypeReference.forType(beanType));
                            }
                            else if (BEANS_COLLECTION_TYPE.isAssignableFrom(that)) {
                                final Type beanType = that.getActualTypeArguments()[0];
                                if (Collection.class.equals(thatRawType) || List.class.equals(thatRawType)) {
                                    parameterObject = new ArrayList<>(
                                            beanFactory.getBeans(TypeReference.forType(beanType)).values());
                                }
                                else if (Set.class.equals(thatRawType)) {
                                    parameterObject = new HashSet<>(
                                            beanFactory.getBeans(TypeReference.forType(beanType)).values());
                                }
                            }
                        }
                        // @formatter:on
                        if (parameter.getDeclaredAnnotation(NotNull.class) != null) {
                            Asserts.notNull(parameterObject, parameterName);
                        }
                        parameterObjects.add(parameterObject);
                    }
                }
                constructor.setAccessible(true);
                try {
                    return constructor.newInstance(parameterObjects.toArray());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
                if (!parameterObjects.isEmpty()) {
                    parameterObjects.clear();
                }
            }
            throw new RuntimeException("no constructor is available");
        }

        private List<Constructor<?>> resolveConstructors(Class<?> type) {
            final List<Constructor<?>> constructors = new ArrayList<>(List.of(type.getDeclaredConstructors()));
            final Iterator<Constructor<?>> iterator = constructors.iterator();
            Constructor<?> constructor;
            Constructor<?> noParamConstructor = null;
            while (iterator.hasNext()) {
                constructor = iterator.next();
                if (constructor.getParameterCount() == 0) {
                    if (constructor.getDeclaredAnnotation(Order.class) == null) {
                        iterator.remove();
                        noParamConstructor = constructor;
                        continue;
                    }
                }
                if (constructor.getDeclaredAnnotation(AUTOWIRED) == null) {
                    iterator.remove();
                }
            }
            constructors.sort(OrderComparator.reflectElement());
            if (noParamConstructor != null) {
                constructors.add(noParamConstructor);
            }
            if (constructors.isEmpty()) {
                throw new RuntimeException("no constructor is available");
            }
            return constructors;
        }
    }
}
