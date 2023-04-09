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

import org.objectweb.asm.AnnotationVisitor;

/**
 * <p>默认注解元数据记录器</p>
 *
 * <p>Created on 2023/2/26 18:16</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
class AnnotationRecorder extends AnnotationVisitor implements MetadataRecorder<AnnotationMetadata> {

    private final Object collector;
    private final boolean visible;
    private final AnnotationMetadata metadata = new AnnotationMetadata();

    public AnnotationRecorder(int api, AnnotationVisitor annotationVisitor, Object collector, String descriptor, boolean visible) {
        super(api, annotationVisitor);
        this.collector = collector;
        this.visible = visible;

        metadata.setDescriptor(descriptor);
        metadata.setVisible(this.visible);
    }

    @Override
    public void visit(String name, Object value) {
        final AnnotationMetadata.ValueMetadata valueMetadata = new AnnotationMetadata.ValueMetadata();
        valueMetadata.setName(name);
        valueMetadata.setValue(value);
        metadata.addMethod(valueMetadata);
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        final AnnotationMetadata.ValueMetadata valueMetadata = new AnnotationMetadata.ValueMetadata();
        valueMetadata.setName(name);
        final AnnotationMetadata.ValueMetadata.EnumValueMetadata enumValueMetadata =
                new AnnotationMetadata.ValueMetadata.EnumValueMetadata();
        enumValueMetadata.setDescriptor(descriptor);
        enumValueMetadata.setValue(value);
        valueMetadata.setValue(enumValueMetadata);
        metadata.addMethod(valueMetadata);
        super.visitEnum(name, descriptor, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        final AnnotationMetadata.ValueMetadata valueMetadata = new AnnotationMetadata.ValueMetadata();
        valueMetadata.setName(name);
        final AnnotationRecorder annotationRecorder =
                new AnnotationRecorder(api, super.visitAnnotation(name, descriptor),
                        valueMetadata, descriptor, visible);
        metadata.addMethod(valueMetadata);
        return annotationRecorder;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return new ArrayValueRecorder(api, super.visitArray(name), metadata, name, visible);
    }

    @Override
    public void visitEnd() {
        if (collector != null) {
            if (collector instanceof ClassMetadata that) {
                that.addAnnotation(metadata);
            } else if (collector instanceof FieldMetadata that) {
                that.addAnnotation(metadata);
            } else if (collector instanceof MethodMetadata that) {
                that.addAnnotation(metadata);
            } else if (collector instanceof MethodMetadata.ParameterMetadata that) {
                that.addAnnotation(metadata);
            } else if (collector instanceof AnnotationMetadata.ValueMetadata that) {
                that.setValue(metadata);
            }
        }
        super.visitEnd();
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return metadata;
    }

    /**
     * <p>默认注解数组值元数据记录器</p>
     *
     * <p>Created on 2023/2/26 18:16</p>
     *
     * @author ketikai
     * @since 0.0.1
     */
    static class ArrayValueRecorder extends AnnotationVisitor implements MetadataRecorder<AnnotationMetadata.ValueMetadata> {

        private final Object collector;
        private final AnnotationMetadata.ValueMetadata metadata = new AnnotationMetadata.ValueMetadata();
        private final AnnotationMetadata.ValueMetadata.ArrayValueMetadata arrayValueMetadata = new AnnotationMetadata.ValueMetadata.ArrayValueMetadata();
        private final boolean visible;

        public ArrayValueRecorder(int api, AnnotationVisitor annotationVisitor, Object collector, String name, boolean visible) {
            super(api, annotationVisitor);
            this.collector = collector;
            this.visible = visible;

            metadata.setName(name);
            metadata.setValue(arrayValueMetadata);
        }

        @Override
        public void visit(String name, Object value) {
            arrayValueMetadata.addElement(value);
            super.visit(name, value);
        }

        @Override
        public void visitEnum(String name, String descriptor, String value) {
            final AnnotationMetadata.ValueMetadata.EnumValueMetadata enumValueMetadata =
                    new AnnotationMetadata.ValueMetadata.EnumValueMetadata();
            enumValueMetadata.setDescriptor(descriptor);
            enumValueMetadata.setValue(value);
            arrayValueMetadata.addElement(enumValueMetadata);
            super.visitEnum(name, descriptor, value);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            return new AnnotationRecorder(api, super.visitAnnotation(name, descriptor), arrayValueMetadata, descriptor, visible);
        }

        @Override
        public void visitEnd() {
            if (collector != null) {
                if (collector instanceof AnnotationMetadata that) {
                    that.addMethod(metadata);
                }
            }
            super.visitEnd();
        }

        @Override
        public AnnotationMetadata.ValueMetadata getMetadata() {
            return metadata;
        }
    }
}
