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

package team.idealstate.hyper.commons;

import java.util.Collection;

/**
 * <p>集合相关工具</p>
 *
 * <p>Created on 2023/3/7 16:03</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class CollectionUtils {


    /**
     * 判断集合是否为 null
     *
     * @param collection 集合
     * @return 集合是否 null
     */
    public static boolean isNull(Collection<?> collection) {
        return collection == null;
    }

    /**
     * 判断集合是否为 null 或空
     *
     * @param collection 集合
     * @return 集合是否为 null 或空
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
