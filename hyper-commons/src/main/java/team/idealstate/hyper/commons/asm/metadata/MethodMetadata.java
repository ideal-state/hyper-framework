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
 * <p>默认方法元数据</p>
 *
 * <p>Created on 2023/2/26 17:48</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class MethodMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 9187502725560531618L;
    private int access;
    private String name;
    private String descriptor;
    private String signature;
    private List<String> exceptions;
    private List<AnnotationMetadata> annotations;
    private List<ParameterMetadata> parameters;

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

    public String[] getExceptions() {
        return CollectionUtils.isNull(exceptions) ? null : exceptions.toArray(String[]::new);
    }

    void addException(String exception) {
        if (CollectionUtils.isNull(exceptions)) {
            exceptions = new ArrayList<>(4);
        }
        exceptions.add(exception);
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

    public ParameterMetadata[] getParameters() {
        return CollectionUtils.isNull(parameters) ? null : parameters.toArray(ParameterMetadata[]::new);
    }

    void addParameter(ParameterMetadata parameter) {
        if (CollectionUtils.isNull(parameters)) {
            parameters = new ArrayList<>(8);
        }
        parameters.add(parameter);
    }

    /**
     * <p>默认方法形参元数据</p>
     *
     * <p>Created on 2023/2/26 17:48</p>
     *
     * @author ketikai
     * @since 0.0.1
     */
    public static class ParameterMetadata implements Serializable {

        @Serial
        private static final long serialVersionUID = -7691362471653088586L;
        private String name;
        private String descriptor;
        private String signature;
        private List<AnnotationMetadata> annotations;

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
}
