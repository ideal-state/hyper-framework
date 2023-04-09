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
import team.idealstate.hyper.commons.generic.TypeReference;

import java.io.Serializable;

/**
 * <p>BeanDefinition</p>
 *
 * <p>Created on 2023/3/22 13:31</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface BeanDefinition {

    String getName();

    @NotNull
    Class<?> getType();

    @NotNull
    TypeReference<?> getTypeReference();

    boolean isSingleton();

    boolean isLazy();

    @Nullable
    String getInitMethod();

    @Nullable
    String getDestroyMethod();
}