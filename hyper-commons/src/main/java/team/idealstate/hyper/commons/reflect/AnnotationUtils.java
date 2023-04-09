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

package team.idealstate.hyper.commons.reflect;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>AnnotationUtils</p>
 *
 * <p>Created on 2023/3/18 16:08</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AnnotationUtils {

    /**
     * 获取被注解元素上所标记的指定注解<br>
     * 该方法不会在被注解元素的父项或其注解的元注解上寻找
     *
     * @param annotatedElement 指定的被注解元素
     * @param annotationType 指定的注解类型
     * @param <T> 注解类型泛型
     * @return 有则返回注解实例，无则为 null
     * @see AnnotationUtils#getAnnotation(AnnotatedElement, Class, boolean, boolean)
     */
    @Nullable
    public static <T extends Annotation> T getAnnotation(
            @NotNull AnnotatedElement annotatedElement,
            @NotNull Class<T> annotationType
    ) {
        return getAnnotation(annotatedElement, annotationType, false, false);
    }

    // @formatter:off
    /**
     * 获取被注解元素上所标记的指定注解
     *
     * @param annotatedElement 指定的被注解元素
     * @param annotationType 指定的注解类型
     * @param includeParent 包含父项
     * @param includeMetaAnnotation 包含元注解
     * @param <T> 注解类型泛型
     * @return 有则返回注解实例，无则为 null
     */
    @Nullable
    @SuppressWarnings({"unchecked"})
    public static <T extends Annotation> T getAnnotation(
            @NotNull AnnotatedElement annotatedElement,
            @NotNull Class<T> annotationType,
            boolean includeParent,
            boolean includeMetaAnnotation
    ) {
        return (T) getAnnotations(annotatedElement, Collections.singleton(annotationType),
                includeParent, includeMetaAnnotation).get(annotationType);
    }
    // @formatter:on

    /**
     * 获取被注解元素上所标记的指定注解<br>
     * 该方法不会在被注解元素的父项或其注解的元注解上寻找
     *
     * @param annotatedElement 指定的被注解元素
     * @param annotationTypes 指定的注解类型
     * @return 有则返回注解实例，无则为 null
     * @see AnnotationUtils#getAnnotations(AnnotatedElement, Set, boolean, boolean)
     */
    @NotNull
    public static Map<Class<? extends Annotation>, Annotation> getAnnotations(
            @NotNull AnnotatedElement annotatedElement,
            @NotNull Set<Class<? extends Annotation>> annotationTypes
    ) {
        return getAnnotations(annotatedElement, annotationTypes,
                false, false);
    }

    // @formatter:off
    /**
     * 获取被注解元素上所标记的指定注解
     *
     * @param annotatedElement 指定的被注解元素
     * @param annotationTypes 指定的注解类型
     * @param includeParent 包含父项
     * @param includeMetaAnnotation 包含元注解
     * @return 有则返回注解实例，无则为 null
     */
    @NotNull
    public static Map<Class<? extends Annotation>, Annotation> getAnnotations(
            @NotNull AnnotatedElement annotatedElement,
            @NotNull Set<Class<? extends Annotation>> annotationTypes,
            boolean includeParent,
            boolean includeMetaAnnotation
    ) {
        Asserts.notNull(annotatedElement, "annotatedElement");
        Asserts.notNull(annotationTypes, "annotationTypes");
        Asserts.notNullOrEmpty(annotationTypes, "annotationTypes");

        Map<Class<? extends Annotation>, Annotation> resultMap = null;

        if (annotatedElement instanceof Class<?> that) {
            resultMap = findAnnotations(
                    that,
                    declaringClass -> declaringClass,
                    annotationTypes, includeParent, includeMetaAnnotation
            );
        }

        else if (annotatedElement instanceof Method that) {
            resultMap = findAnnotations(
                    that.getDeclaringClass(),
                    declaringClass -> {
                        final Method method = declaringClass.getDeclaredMethod(that.getName(), that.getParameterTypes());
                        if (MethodUtils.isOverride(that, method)) {
                            return method;
                        }
                        return null;
                    },
                    annotationTypes, includeParent, includeMetaAnnotation
            );
        }

        else if (annotatedElement instanceof Parameter that) {
            if (that.getDeclaringExecutable() instanceof Method that0) {
                final String parameterName = that.getName();
                final Type parameterizedType = that.getParameterizedType();
                resultMap = findAnnotations(
                        that0.getDeclaringClass(),
                        declaringClass -> {
                            final Method method = declaringClass
                                    .getDeclaredMethod(that0.getName(), that0.getParameterTypes());
                            if (!MethodUtils.isOverride(that0, method)) {
                                return null;
                            }
                            for (final Parameter parameter : method.getParameters()) {
                                if (parameterName.equals(parameter.getName()) &&
                                        parameterizedType.equals(parameter.getParameterizedType())) {
                                    return parameter;
                                }
                            }
                            return null;
                        },
                        annotationTypes, includeParent, includeMetaAnnotation
                );
            }
        }

        if (resultMap == null) {
            resultMap = findAnnotations(
                    AnnotationUtils.class,
                    declaringClass -> annotatedElement,
                    annotationTypes, false, includeMetaAnnotation
            );
        }

        return resultMap;
    }
    // @formatter:on

    /**
     * 根据被注解元素所在类和被注解元素提供接口获取被注解元素，并在被注解元素上进行注解查找
     *
     * @param sourceClass 指定的被注解元素所在类
     * @param annotatedElementProvider 指定的被注解元素提供接口
     * @param annotationTypes 指定的注解类型
     * @param includeParent 包含父项
     * @param includeMetaAnnotation 包含元注解
     * @return 有则返回 map<注解类型, 注解实例>，无则为空集，该方法不会返回 null
     * @see AnnotatedElementProvider
     */
    @NotNull
    public static Map<Class<? extends Annotation>, Annotation> findAnnotations(
            @NotNull Class<?> sourceClass,
            @NotNull AnnotatedElementProvider annotatedElementProvider,
            @NotNull Set<Class<? extends Annotation>> annotationTypes,
            boolean includeParent,
            boolean includeMetaAnnotation
    ) {
        Asserts.notNull(sourceClass, "sourceClass");
        Asserts.notNull(annotatedElementProvider, "annotatedElementProvider");
        Asserts.notNullOrEmpty(annotationTypes, "annotationTypes");

        annotationTypes = new HashSet<>(annotationTypes);
        final Map<Class<? extends Annotation>, Annotation> resultMap = new HashMap<>(annotationTypes.size());
        final List<Class<?>> sourceClasses = includeParent ?
                ClassParents.forClass(sourceClass, ClassParents.INCLUDE_SELF) : Collections.singletonList(sourceClass);

        AnnotatedElement annotatedElement;
        Class<? extends Annotation> declaredAnnotationType;
        Annotation[] declaredAnnotations;
        Set<Annotation> metaAnnotations;
        for (final Class<?> target : sourceClasses) {
            try {
                annotatedElement = annotatedElementProvider.get(target);
            } catch (ReflectiveOperationException e) {
                continue;
            }
            if (annotatedElement == null) {
                continue;
            }
            declaredAnnotations = annotatedElement.getDeclaredAnnotations();
            for (final Annotation declaredAnnotation : declaredAnnotations) {
                declaredAnnotationType = declaredAnnotation.annotationType();
                if (annotationTypes.remove(declaredAnnotationType) &&
                        !resultMap.containsKey(declaredAnnotationType)) {
                    resultMap.put(declaredAnnotationType, declaredAnnotation);
                    if (annotationTypes.isEmpty()) {
                        break;
                    }
                }

                if (!includeMetaAnnotation) {
                    continue;
                }

                // todo: 目前开启了扫描所有父项元注解，未来版本可能考虑关闭
                metaAnnotations = getMetaAnnotations(declaredAnnotation, true);
                for (final Annotation metaAnnotation : metaAnnotations) {
                    declaredAnnotationType = metaAnnotation.annotationType();
                    if (annotationTypes.remove(declaredAnnotationType) &&
                            !resultMap.containsKey(declaredAnnotationType)) {
                        resultMap.put(declaredAnnotationType, metaAnnotation);
                        if (annotationTypes.isEmpty()) {
                            break;
                        }
                    }
                }
            }
        }

        return resultMap;
    }

    /**
     * 获取指定注解实例上的元注解
     *
     * @param sourceAnnotation 指定的注解实例
     * @param includeParent 包含父项
     * @return 有则返回，无则为空集，该方法不会返回 null
     */
    @NotNull
    public static Set<Annotation> getMetaAnnotations(@NotNull Annotation sourceAnnotation, boolean includeParent) {
        Asserts.notNull(sourceAnnotation, "sourceAnnotation");

        final Class<? extends Annotation> annotationType = sourceAnnotation.annotationType();
        if (annotationType.getTypeName().startsWith("java.lang.lang")) {
            return Collections.singleton(sourceAnnotation);
        }

        final Set<Annotation> result = new LinkedHashSet<>(32);
        final Annotation[] declaredAnnotations = annotationType.getDeclaredAnnotations();
        result.addAll(Arrays.asList(declaredAnnotations));
        if (includeParent) {
            for (final Annotation declaredAnnotation : declaredAnnotations) {
                result.addAll(getMetaAnnotations(declaredAnnotation, true));
            }
        }

        return result;
    }

    /**
     * <p>被注解元素提供器<p/>
     * 用于在目标类上获取并返回被注解元素
     */
    public interface AnnotatedElementProvider {

        @Nullable
        AnnotatedElement get(@NotNull Class<?> declaringClass) throws ReflectiveOperationException;
    }
}
