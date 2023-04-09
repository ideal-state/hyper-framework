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

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

/**
 * <p>PluginStarter</p>
 *
 * <p>Created on 2023/4/8 17:43</p>
 * @author ketikai
 * @since 1.0.0
 */
class PluginStarter: ProjectManagerListener {

    override fun projectOpened(project: Project) {
        DumbService.getInstance(project).runWithAlternativeResolveEnabled<Throwable> {
            HyperIntellijPlugin.main(project)
        }
    }
}