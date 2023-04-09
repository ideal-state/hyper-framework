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

import java.util.Map;

/**
 * <p>MapUtils</p>
 *
 * <p>Created on 2023/3/16 2:18</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class MapUtils {

    /**
     * 判断 map 是否为 null
     *
     * @param map map
     * @return map 是否 null
     */
    public static boolean isNull(Map<?, ?> map) {
        return map == null;
    }

    /**
     * 判断 map 是否为 null 或空
     *
     * @param map map
     * @return map 是否为 null 或空
     */
    public static boolean isNullOrEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
