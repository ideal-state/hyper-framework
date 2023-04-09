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

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;

/**
 * <p>类型描述相关工具</p>
 *
 * <p>Created on 2023/3/8 12:56</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class DescriptorUtils {

    /**
     * 将类型描述转换成类型名称
     *
     * @param typeDescriptor 类型描述
     * @return 类型名称
     */
    @NotNull
    public static String toTypeName(@NotNull String typeDescriptor) {
        Asserts.hasText(typeDescriptor, "typeDescriptor");

        final byte[] origin = typeDescriptor.getBytes();
        int charsLen = origin.length;
        int count = 0;
        byte curChar, lastChar = 0x0;
        for (int i = 0; i < charsLen; i++) {
            curChar = origin[i];
            switch (curChar) {
                case 'L' -> {
                    if (i == 0 || lastChar == '<' || lastChar == ';' || lastChar == '[') {
                        origin[i] = 0x0;
                        count++;
                    }
                }
                case '>' -> {
                    if (lastChar == ';') {
                        origin[i - 1] = 0x0;
                        count++;
                    }
                }
                case '/' -> origin[i] = '.';
            }
            lastChar = curChar;
        }

        charsLen -= count;
        final byte[] result = new byte[charsLen];
        count = 0;
        for (final byte c : origin) {
            if (c == 0x0) {
                continue;
            }
            result[count] = c;
            count++;
        }

        int curSquareAt = -1;
        int nextSquareAt = -1;
        count = 0;
        for (int i = 0; i < charsLen; i++) {
            curChar = result[i];
            if (curSquareAt == -1) {
                if (curChar == '[') {
                    curSquareAt = i;
                }
            } else if (curChar == '[') {
                if (nextSquareAt == -1) {
                    nextSquareAt = i;
                }
            } else if (curChar == '<') {
                count++;
            } else if (count == 0) {
                if (curChar == ';' || curChar == '>') {
                    for (int j = curSquareAt; j < i; j++) {
                        result[j] = result[j + 1];
                    }
                    result[i - 1] = ']';
                    if (nextSquareAt != -1) {
                        i = curSquareAt = nextSquareAt - 1;
                        nextSquareAt = -1;
                    } else {
                        curSquareAt = -1;
                    }
                }
            } else if (curChar == '>') {
                count--;
            }
        }

        return new String(result, 0, charsLen - 1)
                .replace(";", ", ")
                .replace("]", "[]");
    }
}
