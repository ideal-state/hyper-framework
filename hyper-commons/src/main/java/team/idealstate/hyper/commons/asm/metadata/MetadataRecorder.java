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

package team.idealstate.hyper.commons.asm.metadata;

/**
 * <p>提供元数据记录器相关接口</p>
 *
 * <p>Created on 2023/2/26 16:51</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public interface MetadataRecorder<M> {

    /**
     * 获取元数据
     *
     * @return 元数据
     */
    M getMetadata();
}
