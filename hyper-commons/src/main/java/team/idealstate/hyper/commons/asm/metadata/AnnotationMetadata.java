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
 * <p>默认注解元数据</p>
 *
 * <p>Created on 2023/2/26 16:20</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public class AnnotationMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = -2465349168156920230L;
    private String descriptor;
    private boolean visible;
    private List<ValueMetadata> methods;

    public String getDescriptor() {
        return descriptor;
    }

    void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public boolean isVisible() {
        return visible;
    }

    void setVisible(boolean visible) {
        this.visible = visible;
    }

    public ValueMetadata[] getMethods() {
        return CollectionUtils.isNull(methods) ? null : methods.toArray(ValueMetadata[]::new);
    }

    void addMethod(ValueMetadata method) {
        if (CollectionUtils.isNull(methods)) {
            methods = new ArrayList<>(16);
        }
        this.methods.add(method);
    }

    /**
     * <p>默认注解值元数据</p>
     *
     * <p>Created on 2023/2/26 16:20</p>
     *
     * @author ketikai
     * @since 0.0.1
     */
    public static class ValueMetadata implements Serializable {

        @Serial
        private static final long serialVersionUID = -2702037833223771540L;
        private String name;
        private Object value;

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        void setValue(Object value) {
            this.value = value;
        }


        /**
         * <p>默认注解枚举值元数据</p>
         *
         * <p>Created on 2023/2/26 16:20</p>
         *
         * @author ketikai
         * @since 0.0.1
         */
        public static class EnumValueMetadata implements Serializable {

            @Serial
            private static final long serialVersionUID = -4056333554344737670L;
            private String descriptor;
            private String value;

            public String getDescriptor() {
                return descriptor;
            }

            void setDescriptor(String descriptor) {
                this.descriptor = descriptor;
            }

            public String getValue() {
                return value;
            }

            void setValue(String value) {
                this.value = value;
            }
        }


        /**
         * <p>默认注解数组值元数据</p>
         *
         * <p>Created on 2023/2/26 16:20</p>
         *
         * @author ketikai
         * @since 0.0.1
         */
        public static class ArrayValueMetadata implements Serializable {

            @Serial
            private static final long serialVersionUID = 6023398817707731194L;
            private List<Object> array;

            public Object[] getArray() {
                return CollectionUtils.isNull(array) ? null : array.toArray();
            }

            void addElement(Object element) {
                if (CollectionUtils.isNull(array)) {
                    array = new ArrayList<>(16);
                }
                this.array.add(element);
            }
        }
    }
}
