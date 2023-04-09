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

import com.intellij.codeInsight.NullableNotNullManager
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope

object NullableNotNullAnnotationSetter {

    private const val NULLABLE = "team.idealstate.hyper.commons.lang.Nullable"
    private const val NOTNULL = "team.idealstate.hyper.commons.lang.NotNull"

    fun set(project: Project) {
        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val nullableNotNullManager = NullableNotNullManager.getInstance(project)
        val allScope = GlobalSearchScope.allScope(project)
        javaPsiFacade.findClass(NULLABLE, allScope)?.apply {
            nullableNotNullManager.setNullables(NULLABLE)
            nullableNotNullManager.defaultNullable = NULLABLE
        }
        javaPsiFacade.findClass(NOTNULL, allScope)?.apply {
            nullableNotNullManager.setNotNulls(NOTNULL)
            nullableNotNullManager.defaultNotNull = NOTNULL
        }
    }
}