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

package team.idealstate.hyper.commons.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.unsafe.UnsafeWrapper;

import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * <p>agent 加载器</p>
 *
 * <p>Created on 2023/2/13 13:04</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class AgentLoader {

    private static final VirtualMachine VM;

    static {
        try {
            final String className = "sun.tools.attach.HotSpotVirtualMachine";
            final Class<?> clazz = Class.forName(className);
            final String fieldName = "ALLOW_ATTACH_SELF";
            final boolean allowAttachSelf = Boolean.TRUE.equals(UnsafeWrapper.getStaticValue(clazz, fieldName));
            if (!allowAttachSelf) {
                UnsafeWrapper.putStaticValue(clazz, fieldName, true);
            }

            final String pid = String.valueOf(ManagementFactory.getRuntimeMXBean().getPid());
            VM = VirtualMachine.attach(pid);
        } catch (ClassNotFoundException | NoSuchFieldException | AttachNotSupportedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(@NotNull String agent) {
        Asserts.notNull(VM, "VM");
        Asserts.hasText(agent, "agent");

        try {
            VM.loadAgent(agent);
        } catch (AgentLoadException | IOException | AgentInitializationException e) {
            throw new RuntimeException(e);
        }
    }
}
