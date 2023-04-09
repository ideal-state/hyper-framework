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
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;

import java.io.Serial;

/**
 * <p>AbstractBeanDefinition</p>
 *
 * <p>Created on 2023/3/22 13:36</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractBeanDefinition implements ConfigurableBeanDefinition {
    private String name;
    private final Class<?> type;
    private final TypeReference<?> typeReference;
    private boolean singleton;
    private boolean lazy;
    private final String initMethod;
    private final String destroyMethod;

    public AbstractBeanDefinition(
            @NotNull TypeReference<?> typeReference,
            @Nullable String initMethod,
            @Nullable String destroyMethod
    ) {
        Asserts.notNull(typeReference, "typeReference");
        this.type = typeReference.getRawReferenceType();
        this.typeReference = typeReference;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        Asserts.hasText(name, "name");
        this.name = name;
    }

    @NotNull
    @Override
    public Class<?> getType() {
        return type;
    }

    @NotNull
    @Override
    public TypeReference<?> getTypeReference() {
        return typeReference;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public boolean isLazy() {
        return lazy;
    }

    @Override
    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    @Nullable
    @Override
    public String getInitMethod() {
        return initMethod;
    }

    @Nullable
    @Override
    public String getDestroyMethod() {
        return destroyMethod;
    }
}
