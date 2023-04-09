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

import org.objectweb.asm.*;
import team.idealstate.hyper.commons.ArrayUtils;
import team.idealstate.hyper.commons.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>默认方法元数据记录器</p>
 *
 * <p>Created on 2023/2/26 17:47</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
class MethodRecorder extends MethodVisitor implements MetadataRecorder<MethodMetadata> {

    private final List<MethodMetadata.ParameterMetadata> parameters = new ArrayList<>(8);
    private final Object collector;
    private final MethodMetadata metadata = new MethodMetadata();
    private final boolean isStatic;
    private final int parameterCount;

    public MethodRecorder(int api, MethodVisitor methodVisitor, Object collector, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(api, methodVisitor);
        this.collector = collector;

        metadata.setAccess(access);
        metadata.setName(name);
        metadata.setDescriptor(descriptor);
        parameterCount = Type.getMethodType(descriptor).getArgumentTypes().length;
        metadata.setSignature(signature);
        if (!ArrayUtils.isNullOrEmpty(exceptions)) {
            for (final String exception : exceptions) {
                metadata.addException(exception);
            }
        }
        isStatic = (access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationRecorder(api, super.visitAnnotation(descriptor, visible), metadata, descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        if (isInvisibleParameter(parameter)) {
            return super.visitParameterAnnotation(parameter, descriptor, visible);
        }

        final MethodMetadata.ParameterMetadata parameterMetadata = new MethodMetadata.ParameterMetadata();
        parameters.add(parameterMetadata);
        return new AnnotationRecorder(api, super.visitParameterAnnotation(parameter, descriptor, visible), parameterMetadata, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index);

        final int paramIndex;
        if (isStatic) {
            paramIndex = index;
        } else if (index > 0) {
            paramIndex = index - 1;
        } else {
            return;
        }

        if (isInvisibleParameter(paramIndex)) {
            return;
        }

        final MethodMetadata.ParameterMetadata parameterMetadata;
        if (parameters.size() <= paramIndex) {
            parameterMetadata = new MethodMetadata.ParameterMetadata();
            parameters.add(parameterMetadata);
        } else {
            parameterMetadata = parameters.get(paramIndex);
        }

        if (parameterMetadata != null) {
            parameterMetadata.setName(name);
            parameterMetadata.setDescriptor(descriptor);
            parameterMetadata.setSignature(signature);
        }
    }

    @Override
    public void visitEnd() {
        if (!CollectionUtils.isNullOrEmpty(parameters)) {
            for (final MethodMetadata.ParameterMetadata parameter : parameters) {
                metadata.addParameter(parameter);
            }
        }
        if (collector != null) {
            if (collector instanceof ClassMetadata that) {
                that.addMethod(metadata);
            }
        }
        super.visitEnd();
    }

    private boolean isInvisibleParameter(int parameterIndex) {
        return parameterIndex >= parameterCount;
    }

    @Override
    public MethodMetadata getMetadata() {
        return metadata;
    }
}
