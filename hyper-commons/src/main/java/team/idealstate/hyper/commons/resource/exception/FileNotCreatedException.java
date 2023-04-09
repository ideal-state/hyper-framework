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

package team.idealstate.hyper.commons.resource.exception;

import team.idealstate.hyper.commons.lang.NotNull;

import java.io.IOException;
import java.io.Serial;

/**
 * <p>未能完成文件创建时抛出</p>
 *
 * <p>Created on 2023/3/2 1:40</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class FileNotCreatedException extends IOException {

    @Serial
    private static final long serialVersionUID = -8690433120214785684L;

    public FileNotCreatedException(@NotNull String path) {
        super("failed to create: " + path);
    }

    public FileNotCreatedException(@NotNull String path, @NotNull Throwable cause) {
        super("failed to create: " + path, cause);
    }
}
