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

package team.idealstate.hyper.commons.order;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.util.Comparator;

/**
 * <p>OrderComparator</p>
 *
 * <p>Created on 2023/3/23 16:34</p>
 *
 * @author ketikai
 * @since 1.0.0
 * @see Order
 * @see IOrder
 */
public final class OrderComparator<T> implements Comparator<T>, Serializable {

    @Serial
    private static final long serialVersionUID = -4371714781372232233L;
    private static final int NORMAL_MODE = 0;
    private static final int REFLECT_ELEMENT_MODE = 1;
    @SuppressWarnings({"rawtypes"})
    private static final OrderComparator NORMAL = new OrderComparator<>(NORMAL_MODE);
    @SuppressWarnings({"rawtypes"})
    private static final OrderComparator REFLECT_ELEMENT = new OrderComparator<>(REFLECT_ELEMENT_MODE);

    @SuppressWarnings({"unchecked"})
    public static <T> OrderComparator<T> normal() {
        return (OrderComparator<T>) NORMAL;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> OrderComparator<T> reflectElement() {
        return (OrderComparator<T>) REFLECT_ELEMENT;
    }

    private final int mode;

    private OrderComparator(int mode) {
        this.mode = mode;
    }

    @Override
    public int compare(T a, T b) {
        return Integer.compare(parseOrder(a), parseOrder(b));
    }

    private int parseOrder(T t) {
        if (t != null) {
            if (t instanceof IOrder that) {
                return that.getOrder();
            }
            AnnotatedElement annotatedElement = t.getClass();
            if (mode == REFLECT_ELEMENT_MODE) {
                if (t.getClass().getTypeName().startsWith("java.lang.reflect")) {
                    if (t instanceof AnnotatedElement) {
                        annotatedElement = (AnnotatedElement) t;
                    }
                } else {
                    throw new IllegalArgumentException(t + " is not a reflect element");
                }
            }
            final Order order;
            if ((order = annotatedElement.getDeclaredAnnotation(Order.class)) != null) {
                return order.value();
            }
        }
        return Order.DEFAULT_ORDER;
    }
}
