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
 * <p>默认类元数据</p>
 *
 * <p>Created on 2023/2/26 16:05</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class ClassMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2227779633238061702L;
    private int version;
    private int access;
    private String name;
    private String signature;
    private ClassMetadata superClass;
    private List<ClassMetadata> interfaces;
    private List<AnnotationMetadata> annotations;
    private List<FieldMetadata> fields;
    private List<MethodMetadata> methods;
    private String outer;
    private List<String> inners;

    public int getVersion() {
        return version;
    }

    void setVersion(int version) {
        this.version = version;
    }

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

    public String getSignature() {
        return signature;
    }

    void setSignature(String signature) {
        this.signature = signature;
    }

    public ClassMetadata getSuperClass() {
        return superClass;
    }

    void setSuperClass(ClassMetadata superClass) {
        this.superClass = superClass;
    }

    public ClassMetadata[] getInterfaces() {
        return CollectionUtils.isNull(interfaces) ? null : interfaces.toArray(ClassMetadata[]::new);
    }

    void addInterface(ClassMetadata _interface) {
        if (CollectionUtils.isNull(interfaces)) {
            interfaces = new ArrayList<>(8);
        }
        interfaces.add(_interface);
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

    public FieldMetadata[] getFields() {
        return CollectionUtils.isNull(fields) ? null : fields.toArray(FieldMetadata[]::new);
    }

    void addField(FieldMetadata field) {
        if (CollectionUtils.isNull(fields)) {
            fields = new ArrayList<>(16);
        }
        fields.add(field);
    }

    public MethodMetadata[] getMethods() {
        return CollectionUtils.isNull(methods) ? null : methods.toArray(MethodMetadata[]::new);
    }

    void addMethod(MethodMetadata method) {
        if (CollectionUtils.isNull(methods)) {
            methods = new ArrayList<>(16);
        }
        methods.add(method);
    }

    @Deprecated(since = "1.0.0", forRemoval = true)
    public String getOuter() {
        return outer;
    }

    @Deprecated(since = "1.0.0", forRemoval = true)
    void setOuter(String outer) {
        this.outer = outer;
    }

    @Deprecated(since = "1.0.0", forRemoval = true)
    public String[] getInners() {
        return CollectionUtils.isNull(inners) ? null : inners.toArray(String[]::new);
    }

    @Deprecated(since = "1.0.0", forRemoval = true)
    void addInner(String inner) {
        if (CollectionUtils.isNull(inners)) {
            inners = new ArrayList<>(4);
        }
        inners.add(inner);
    }
}
