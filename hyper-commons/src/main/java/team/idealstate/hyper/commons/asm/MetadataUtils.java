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

package team.idealstate.hyper.commons.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import team.idealstate.hyper.commons.ArrayUtils;
import team.idealstate.hyper.commons.CollectionUtils;
import team.idealstate.hyper.commons.StringUtils;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asm.metadata.*;
import team.idealstate.hyper.commons.asserts.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>类元数据相关工具</p>
 *
 * <p>Created on 2023/3/2 20:00</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class MetadataUtils {

    /**
     * 获取一个类的元数据信息
     *
     * @param className      全类名
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     */
    @NotNull
    public static ClassMetadata getClassMetadata(@NotNull String className, int api, int parsingOptions) {
        Asserts.hasText(className, "className");

        final ClassReader reader;
        try {
            reader = new ClassReader(className);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final ClassRecorder recorder = new ClassRecorder(api, parsingOptions);
        reader.accept(recorder, parsingOptions);
        return recorder.getMetadata();
    }

    /**
     * 获取一个类文件的元数据信息<br>
     * 该操作不会自动关闭流，请自行手动关闭
     *
     * @param inputStream    类文件输入流
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     */
    @NotNull
    public static ClassMetadata getClassMetadata(@NotNull InputStream inputStream, int api, int parsingOptions) {
        Asserts.notNull(inputStream, "inputStream");

        final byte[] classFile;
        try {
            classFile = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getClassMetadata(classFile, api, parsingOptions);
    }

    /**
     * 获取一个类文件的元数据信息
     *
     * @param classFile      类文件字节数组
     * @param api            ASM API ，详见 {@link Opcodes}
     * @param parsingOptions 类读取选项，详见 {@link ClassReader}
     * @return 类元数据
     */
    @NotNull
    public static ClassMetadata getClassMetadata(byte[] classFile, int api, int parsingOptions) {
        Asserts.notNullOrEmpty(classFile, "classFile");

        final ClassReader reader = new ClassReader(classFile);
        final ClassRecorder recorder = new ClassRecorder(api, parsingOptions);
        reader.accept(recorder, parsingOptions);
        return recorder.getMetadata();
    }

    /**
     * 获取类元数据表示的类是否继承自指定的父类
     *
     * @param classMetadata 类元数据
     * @param superName     直接父类描述名
     * @return 类元数据表示的类是否继承自指定的父类
     * @see Type#getInternalName(Class)
     */
    public static boolean isExtends(@NotNull ClassMetadata classMetadata, @NotNull String superName) {
        Asserts.notNull(classMetadata, "classMetadata");
        Asserts.hasText(superName, "superName");

        ClassMetadata temp = classMetadata;
        while ((temp = temp.getSuperClass()) != null) {
            if (superName.equals(temp.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取类元数据表示的类是否实现了指定的接口
     *
     * @param classMetadata  类元数据
     * @param interfaceNames 直接接口描述名
     * @return 类元数据表示的类是否实现了指定的接口
     * @see Type#getInternalName(Class)
     */
    public static boolean isImplements(@NotNull ClassMetadata classMetadata, @NotNull String... interfaceNames) {
        Asserts.notNull(classMetadata, "classMetadata");
        Asserts.isTrue(!ArrayUtils.isNullOrEmpty(interfaceNames), "interfaceNames");
        Asserts.isTrue(Arrays.stream(interfaceNames).noneMatch(StringUtils::isNullOrBlank),
                "interfaceName must be a valid text");

        final Set<String> allInterfaceNames = getAllInterfaceNames(classMetadata);
        if (CollectionUtils.isNullOrEmpty(allInterfaceNames)) {
            return false;
        }

        return allInterfaceNames.containsAll(List.of(interfaceNames));
    }

    private static Set<String> getAllInterfaceNames(@Nullable ClassMetadata classMetadata) {
        if (classMetadata == null) {
            return null;
        }

        final ClassMetadata[] interfaces = classMetadata.getInterfaces();
        if (ArrayUtils.isNullOrEmpty(interfaces)) {
            return null;
        }

        Set<String> result = new HashSet<>(4);
        Set<String> temp;
        for (final ClassMetadata i : interfaces) {
            if (!CollectionUtils.isNullOrEmpty(temp = getAllInterfaceNames(i))) {
                result.addAll(temp);
            }
            result.add(i.getName());
        }
        return result;
    }

    /**
     * 获取类元数据中指定字段元数据
     *
     * @param classMetadata 类元数据
     * @param fieldName     字段名
     * @return 有则返回该字段元数据，反之则为 null
     */
    @Nullable
    public static FieldMetadata getField(@NotNull ClassMetadata classMetadata, @NotNull String fieldName) {
        Asserts.notNull(classMetadata, "classMetadata");
        Asserts.hasText(fieldName, "fieldName");

        final FieldMetadata[] fields = classMetadata.getFields();
        if (ArrayUtils.isNullOrEmpty(fields)) {
            return null;
        }
        for (final FieldMetadata metadata : fields) {
            if (fieldName.equals(metadata.getName())) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * 获取类元数据中指定方法元数据
     *
     * @param classMetadata 类元数据
     * @param methodName    字段名
     * @param returnType    方法返回值类型
     * @param argumentTypes 方法形参类型
     * @return 有则返回该方法元数据，反之则为 null
     * @see Type#getType(Class)
     * @see Type#getType(String)
     */
    @Nullable
    public static MethodMetadata getMethod(@NotNull ClassMetadata classMetadata, @NotNull String methodName, @NotNull Type returnType, @NotNull Type... argumentTypes) {
        Asserts.notNull(classMetadata, "classMetadata");
        Asserts.hasText(methodName, "methodName");
        Asserts.notNull(returnType, "returnType");
        Asserts.notNull(argumentTypes, "argumentTypes");

        final MethodMetadata[] methods = classMetadata.getMethods();
        if (ArrayUtils.isNullOrEmpty(methods)) {
            return null;
        }

        final String descriptor = Type.getMethodDescriptor(returnType, argumentTypes);
        for (final MethodMetadata metadata : methods) {
            if (methodName.equals(metadata.getName()) && descriptor.equals(metadata.getDescriptor())) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * 获取类元数据中指定注解元数据
     *
     * @param classMetadata  类元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    @Nullable
    public static AnnotationMetadata getAnnotation(@NotNull ClassMetadata classMetadata, @NotNull String annotationType) {
        Asserts.notNull(classMetadata, "classMetadata");
        Asserts.hasText(annotationType, "annotationType");

        final AnnotationMetadata[] annotations = classMetadata.getAnnotations();
        return getAnnotation(annotations, annotationType);
    }

    /**
     * 获取字段元数据中指定注解元数据
     *
     * @param fieldMetadata  字段元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    @Nullable
    public static AnnotationMetadata getAnnotation(@NotNull FieldMetadata fieldMetadata, @NotNull String annotationType) {
        Asserts.notNull(fieldMetadata, "fieldMetadata");
        Asserts.hasText(annotationType, "annotationType");

        final AnnotationMetadata[] annotations = fieldMetadata.getAnnotations();
        return getAnnotation(annotations, annotationType);
    }

    /**
     * 获取方法元数据中指定注解元数据
     *
     * @param methodMetadata 方法元数据
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    @Nullable
    public static AnnotationMetadata getAnnotation(@NotNull MethodMetadata methodMetadata, @NotNull String annotationType) {
        Asserts.notNull(methodMetadata, "methodMetadata");
        Asserts.hasText(annotationType, "annotationType");

        final AnnotationMetadata[] annotations = methodMetadata.getAnnotations();
        return getAnnotation(annotations, annotationType);
    }

    /**
     * 获取注解元数据组中指定注解元数据
     *
     * @param annotations    注解元数据组
     * @param annotationType 注解类型
     * @return 有则返回该注解元数据，反之则为 null
     * @see Type#getDescriptor(Class)
     */
    @Nullable
    public static AnnotationMetadata getAnnotation(@NotNull AnnotationMetadata[] annotations, @NotNull String annotationType) {
        Asserts.hasText(annotationType, "annotationType");

        if (ArrayUtils.isNullOrEmpty(annotations)) {
            return null;
        }

        AnnotationMetadata.ValueMetadata[] methods;
        Object[] array;
        for (final AnnotationMetadata metadata : annotations) {
            if (annotationType.equals(metadata.getDescriptor())) {
                return metadata;
            }
            methods = metadata.getMethods();
            if (ArrayUtils.isNullOrEmpty(methods)) {
                continue;
            }
            for (final AnnotationMetadata.ValueMetadata method : methods) {
                if (!"value".equals(method.getName())) {
                    continue;
                }

                if (method.getValue() instanceof AnnotationMetadata.ValueMetadata.ArrayValueMetadata that) {
                    array = that.getArray();
                    if (ArrayUtils.isNullOrEmpty(array)) {
                        continue;
                    }

                    if (array[0] instanceof AnnotationMetadata that0) {
                        if (annotationType.equals(that0.getDescriptor())) {
                            return metadata;
                        }
                    }
                }
            }
        }

        return null;
    }
}
