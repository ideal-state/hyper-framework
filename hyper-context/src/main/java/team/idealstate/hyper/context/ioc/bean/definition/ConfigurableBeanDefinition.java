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

/**
 * <p>ConfigurableBeanDefinition</p>
 *
 * <p>Created on 2023/4/1 5:54</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public interface ConfigurableBeanDefinition extends BeanDefinition {

    void setName(@NotNull String name);

//    void setType(@NotNull Class<?> type);

//    void setTypeReference(@NotNull TypeReference<?> typeReference);

    void setSingleton(boolean singleton);

    void setLazy(boolean lazy);
}
