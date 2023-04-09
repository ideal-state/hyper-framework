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
import team.idealstate.hyper.context.ioc.able.Clearable;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>BeanPrototypeRegistry</p>
 *
 * <p>Created on 2023/4/1 1:55</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface BeanPrototypeRegistry extends Clearable {

    <T> void registerPrototypeFactory(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference, @NotNull ObjectFactory<T> objectFactory);

    @Nullable
    Object getPrototype(@NotNull String beanName);

    @NotNull
    Map<String, Object> getPrototypes(@NotNull Type beanType);

    @NotNull
    Map<String, Object> getPrototypes();

    boolean containsPrototype(@NotNull String beanName);
}
