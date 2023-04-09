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

package team.idealstate.hyper.hyperideaplugin

import com.intellij.openapi.project.Project

/**
 * <p>HyperIntellijPlugin</p>
 *
 * <p>Created on 2023/4/9 15:23</p>
 * @author ketikai
 * @since 1.0.0
 */
object HyperIntellijPlugin {

    internal fun main(project: Project) {
        NullableNotNullAnnotationSetter.set(project)
    }
}