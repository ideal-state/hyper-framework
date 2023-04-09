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

package team.idealstate.hyper.commons.reflect.help;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asm.MetadataUtils;
import team.idealstate.hyper.commons.asm.metadata.ClassMetadata;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.generic.TypeReference;
import team.idealstate.hyper.commons.reflect.ClassParents;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <p>ReflectHelper</p>
 * 该类中的所有内容都不是线程安全的
 *
 * <p>Created on 2023/4/4 9:26</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public final class ReflectHelper {

    private static final Map<Class<?>, ClassMetadata> CLASS_METADATA_CACHE = new HashMap<>(64);

    private static ClassMetadata getClassMetadata(Class<?> reflectClass) {
        ClassMetadata classMetadata;
        synchronized (CLASS_METADATA_CACHE) {
            classMetadata = CLASS_METADATA_CACHE.get(reflectClass);
            if (classMetadata == null) {
                classMetadata = MetadataUtils.getClassMetadata(
                        Objects.requireNonNull(reflectClass.getResourceAsStream("")),
                        Opcodes.ASM9,
                        ClassReader.SKIP_FRAMES
                );
                CLASS_METADATA_CACHE.put(reflectClass, classMetadata);
            }
        }
        return classMetadata;
    }

    private final Class<?> reflectClass;
    private final ClassMetadata reflectClassMetadata;
    private boolean scanParent;
    private List<Class<?>> parentClasses;


    public ReflectHelper(@NotNull Class<?> reflectClass, boolean scanParent) {
        Asserts.notNull(reflectClass, "reflectClass");
        this.reflectClass = reflectClass;
        this.reflectClassMetadata = getClassMetadata(this.reflectClass);

        scanParent(scanParent);
    }

    @NotNull
    public ReflectHelper scanParent(boolean scanParent) {
        this.scanParent = scanParent;
        if (scanParent && parentClasses == null) {
            parentClasses = ClassParents.forClass(reflectClass, ClassParents.SKIP_INTERFACE);
        }
        return this;
    }

    private final Map<String, FieldHelper<?>> declaredFields = new HashMap<>(8);
    private final Map<String, FieldHelper<?>> fields = new HashMap<>(8);
    private final List<String> notExistFields = new ArrayList<>(8);
    @NotNull
    public FieldHelper<?> findField(@NotNull String fieldName) throws NoSuchFieldException {
        Asserts.hasText(fieldName, "fieldName");
        if (notExistFields.contains(fieldName)) {
            throw new NoSuchFieldException(fieldName);
        }
        FieldHelper<?> fieldHelper = declaredFields.get(fieldName);
        if (fieldHelper != null) {
            return fieldHelper;
        }
        if (scanParent && (fieldHelper = fields.get(fieldName)) != null) {
            return fieldHelper;
        }

        Field field = null;
        NoSuchFieldException noSuchFieldException;
        try {
            field = reflectClass.getDeclaredField(fieldName);
            fieldHelper = new FieldHelper<>(this, field);
            declaredFields.put(fieldName, fieldHelper);
            return fieldHelper;
        } catch (NoSuchFieldException e) {
            noSuchFieldException = e;
        }

        if (scanParent) {
            try {
                field = reflectClass.getField(fieldName);
            } catch (NoSuchFieldException e) {
                noSuchFieldException = e;
                for (final Class<?> parentClass : parentClasses) {
                    try {
                        field = parentClass.getDeclaredField(fieldName);
                        break;
                    } catch (NoSuchFieldException ignored) {}
                }
            }
        }
        if (field == null) {
            notExistFields.add(fieldName);
            throw noSuchFieldException;
        }
        fieldHelper = new FieldHelper<>(this, field);
        fields.put(fieldName, fieldHelper);
        return fieldHelper;
    }

    @NotNull
    public <T> FieldHelper<T> findField(
            @NotNull String fieldName,
            @NotNull TypeReference<T> typeReference
    ) throws NoSuchFieldException {
        return null;
    }



    /**
     * <p>FieldHelper</p>
     *
     * <p>Created on 2023/4/4 12:42</p>
     *
     * @author ketikai
     * @since 1.0.0
     */
    public static final class FieldHelper<T> {

        private final ReflectHelper owner;
        private final Field field;
        private final boolean isStatic;
        private final boolean isFinal;

        FieldHelper(@NotNull ReflectHelper owner, @NotNull Field field) {
            Asserts.notNull(owner, "owner");
            Asserts.notNull(field, "field");
            this.owner = owner;
            this.field = field;
            final int modifiers = this.field.getModifiers();
            this.isStatic = Modifier.isStatic(modifiers);
            this.isFinal = Modifier.isFinal(modifiers);
        }

        @NotNull
        public FieldHelper ignoreAccess() {
            field.setAccessible(true);
            return this;
        }

        @Nullable
        @SuppressWarnings({"unchecked"})
        public T get(Object targetObject) throws IllegalAccessException {
            return (T) field.get(targetObject);
        }

        @NotNull
        public ReflectHelper getOwner() {
            return owner;
        }

        @NotNull
        public Field getField() {
            return field;
        }

        public boolean isStatic() {
            return isStatic;
        }

        public boolean isFinal() {
            return isFinal;
        }
    }
}
