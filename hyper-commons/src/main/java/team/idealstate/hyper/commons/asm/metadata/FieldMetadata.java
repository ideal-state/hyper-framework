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

import team.idealstate.hyper.commons.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>默认字段元数据</p>
 *
 * <p>Created on 2023/2/26 16:20</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class FieldMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 9104489147483725194L;
    private int access;
    private String name;
    private String descriptor;
    private String signature;
    private Object value;
    private List<AnnotationMetadata> annotations;

    public int getAccess() {
        return access;
    }

    void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getSignature() {
        return signature;
    }

    void setSignature(String signature) {
        this.signature = signature;
    }

    public Object getValue() {
        return value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    public AnnotationMetadata[] getAnnotations() {
        return CollectionUtils.isNull(annotations) ? null : annotations.toArray(AnnotationMetadata[]::new);
    }

    void addAnnotation(AnnotationMetadata annotation) {
        if (CollectionUtils.isNull(annotations)) {
            annotations = new ArrayList<>(8);
        }
        annotations.add(annotation);
    }
}
