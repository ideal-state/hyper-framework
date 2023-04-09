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

package team.idealstate.hyper.context.event;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.lang.Nullable;
import team.idealstate.hyper.commons.asserts.Asserts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>SimpleEventManager</p>
 *
 * <p>Created on 2023/3/15 12:50</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public class SimpleEventManager implements EventHandlerManager {

    private final Map<Class<?>, EventHandlerChain> registeredChains = new HashMap<>(64);
    private final List<EventHandlerChain> needReflushChains = new CopyOnWriteArrayList<>();

    @Override
    public void registerEventHandler(@NotNull EventHandler eventHandler) {
        Asserts.notNull(eventHandler, "eventHandler");

        synchronized (registeredChains) {

        }
    }

    @Override
    public void registerEventHandler(@NotNull EventHandler eventHandler, @NotNull Class<?>... eventTypes) {
        Asserts.notNull(eventHandler, "eventHandler");

        synchronized (registeredChains) {

        }
    }

    @Override
    public void unregisterEventHandler(@NotNull EventHandler eventHandler) {
        Asserts.notNull(eventHandler, "eventHandler");

        synchronized (registeredChains) {

        }
    }

    @Override
    public void unregisterEventHandler(@NotNull EventHandler eventHandler, @NotNull Class<?>... eventTypes) {
        Asserts.notNull(eventHandler, "eventHandler");

        synchronized (registeredChains) {

        }
    }

    @Override
    public void refreshEventHandlerManager() {
        synchronized (registeredChains) {
            needReflushChains.forEach(EventHandlerChain::refreshChain);
        }
    }

    @Override
    @Nullable
    public EventHandlerChain getEventHandlerChain(@NotNull Class<?> eventType) {
        return registeredChains.get(eventType);
    }
}
