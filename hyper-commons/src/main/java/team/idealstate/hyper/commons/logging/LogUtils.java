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

package team.idealstate.hyper.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.caller.CallerUtils;

/**
 * <p>日志相关工具</p>
 *
 * <p>Created on 2023/2/27 17:09</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class LogUtils {

    /**
     * 获取日志记录器
     *
     * @return 日志记录器
     */
    @NotNull
    public static Logger getLogger() {
        return LoggerFactory.getLogger(CallerUtils.getCallerClass());
    }
}
