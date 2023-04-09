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

package team.idealstate.hyper.commons;

import java.io.*;

/**
 * <p>ObjectUtils</p>
 *
 * <p>Created on 2023/4/1 0:55</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class ObjectUtils {

    /**
     * 拷贝对象（深拷贝）
     *
     * @param sourceObject 源对象
     * @param <T> 对象类型泛型
     * @return 拷贝后对象
     */
    @SuppressWarnings({"unchecked"})
    public static <T extends Serializable> T copy(T sourceObject) {
        if (sourceObject == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(64);
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(sourceObject);
            }
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
