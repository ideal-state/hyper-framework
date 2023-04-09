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
import org.objectweb.asm.FieldVisitor;

/**
 * <p>默认字段元数据记录器</p>
 *
 * <p>Created on 2023/2/26 16:41</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
class FieldRecorder extends FieldVisitor implements MetadataRecorder<FieldMetadata> {

    private final Object collector;
    private final FieldMetadata metadata = new FieldMetadata();

    public FieldRecorder(int api, FieldVisitor fieldVisitor, Object collector, int access, String name, String descriptor, String signature, Object value) {
        super(api, fieldVisitor);
        this.collector = collector;

        metadata.setAccess(access);
        metadata.setName(name);
        metadata.setDescriptor(descriptor);
        metadata.setSignature(signature);
        metadata.setValue(value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationRecorder(api, super.visitAnnotation(descriptor, visible), metadata, descriptor, visible);
    }

    @Override
    public void visitEnd() {
        if (collector != null) {
            if (collector instanceof ClassMetadata that) {
                that.addField(metadata);
            }
        }
        super.visitEnd();
    }

    @Override
    public FieldMetadata getMetadata() {
        return metadata;
    }
}
