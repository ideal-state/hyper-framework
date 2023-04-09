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
import team.idealstate.hyper.commons.StringUtils;

import java.io.IOException;

/**
 * <p>默认类元数据记录器</p>
 *
 * <p>Created on 2023/2/26 16:01</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class ClassRecorder extends ClassVisitor implements MetadataRecorder<ClassMetadata> {

    private static final int NORMAL = 0;
    private static final int RECORD_SUPER = 1;
    private static final int RECORD_INTERFACES = 2;

    private final Object collector;
    private final ClassMetadata metadata = new ClassMetadata();
    private final int parsingOptions;
    private final int option;

    public ClassRecorder(int api, int parsingOptions) {
        this(api, null, parsingOptions);
    }

    public ClassRecorder(int api, ClassVisitor classVisitor, int parsingOptions) {
        super(api, classVisitor);

        this.collector = null;
        this.option = NORMAL;
        this.parsingOptions = parsingOptions;
    }

    private ClassRecorder(int api, Object collector, int option, int parsingOptions) {
        this(api, null, collector, option, parsingOptions);
    }

    private ClassRecorder(int api, ClassVisitor classVisitor, Object collector, int option, int parsingOptions) {
        super(api, classVisitor);

        this.collector = collector;
        if (option < NORMAL || option > RECORD_INTERFACES) {
            throw new IllegalArgumentException("Illegal option");
        }
        this.option = option;
        this.parsingOptions = parsingOptions;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        metadata.setVersion(version);
        metadata.setAccess(access);
        metadata.setName(name);
        metadata.setSignature(signature);
        try {
            if (!StringUtils.isNullOrBlank(superName)) {
                new ClassReader(superName).accept(new ClassRecorder(api, metadata, RECORD_SUPER, parsingOptions), parsingOptions);
            }
            if (!ArrayUtils.isNullOrEmpty(interfaces)) {
                for (String i : interfaces) {
                    new ClassReader(i).accept(new ClassRecorder(api, metadata, RECORD_INTERFACES, parsingOptions), parsingOptions);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    @SuppressWarnings({"removal"})
    public void visitOuterClass(String owner, String name, String descriptor) {
        metadata.setOuter(descriptor);
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationRecorder(api, super.visitAnnotation(descriptor, visible), metadata, descriptor, visible);
    }

    @Override
    @SuppressWarnings({"removal"})
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        metadata.addInner(name);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldRecorder(api, super.visitField(access, name, descriptor, signature, value),
                metadata, access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodRecorder(api,
                super.visitMethod(access, name, descriptor, signature, exceptions),
                metadata, access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if (collector != null) {
            if (collector instanceof ClassMetadata that) {
                switch (option) {
                    case RECORD_SUPER -> that.setSuperClass(metadata);
                    case RECORD_INTERFACES -> that.addInterface(metadata);
                }
            }
        }
        super.visitEnd();
    }

    @Override
    public ClassMetadata getMetadata() {
        return metadata;
    }
}
