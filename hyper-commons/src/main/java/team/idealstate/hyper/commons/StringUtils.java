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

/**
 * <p>字符串相关工具</p>
 *
 * <p>Created on 2023/2/16 21:08</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class StringUtils {

    /**
     * 判断字符串是否为空或空白内容
     *
     * @param str 字符串
     * @return 为空或空白内容时为 true，否则为 false
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 判断字符串是否为空或无任何内容
     *
     * @param str 字符串
     * @return 为空或无任何内容时为 true，否则为 false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
