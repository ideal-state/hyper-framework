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

package team.idealstate.hyper.context.ioc.exception;

import team.idealstate.hyper.commons.lang.NotNull;

import java.io.Serial;

/**
 * <p>NameAlreadyBoundException</p>
 *
 * <p>Created on 2023/3/22 14:01</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public class NameAlreadyBoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5711090210903812358L;

    public NameAlreadyBoundException(@NotNull String name) {
        super("[name: " + name + "] name is already bound");
    }
}
