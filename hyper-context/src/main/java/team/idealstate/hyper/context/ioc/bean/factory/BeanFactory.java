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
import team.idealstate.hyper.commons.generic.TypeReference;

import java.util.Map;

/**
 * <p>BeanFactory</p>
 *
 * <p>Created on 2023/3/22 13:29</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface BeanFactory {

    @Nullable
    <T> T getBean(@NotNull String beanName);

    @Nullable
    <T> T getBean(@NotNull Class<T> beanType);

    @Nullable
    <T> T getBean(@NotNull TypeReference<T> beanTypeReference);

    @Nullable
    <T> T getBean(@NotNull String beanName, @NotNull Class<T> beanType);

    @Nullable
    <T> T getBean(@NotNull String beanName, @NotNull TypeReference<T> beanTypeReference);

    @NotNull
    <T> Map<String, T> getBeans(@NotNull Class<T> beanType);

    @NotNull
    <T> Map<String, T> getBeans(@NotNull TypeReference<T> beanTypeReference);
}
