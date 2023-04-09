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

package team.idealstate.hyper.context.ioc.bean.factory;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.context.ioc.able.Clearable;
import team.idealstate.hyper.context.ioc.able.Closeable;
import team.idealstate.hyper.context.ioc.able.Refreshable;
import team.idealstate.hyper.context.ioc.bean.definition.BeanDefinitionDefiner;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>AbstractStatusBeanFactory</p>
 *
 * <p>Created on 2023/3/23 11:49</p>
 *
 * @author ketikai
 * @since 1.0.0
 */
public abstract class AbstractStatusBeanFactory extends AbstractBeanFactory implements Refreshable, Clearable, Closeable {

    private final AtomicBoolean closed = new AtomicBoolean(false);

    protected final Lock globalLock = new ReentrantLock();

    protected AbstractStatusBeanFactory(
            @NotNull BeanDefinitionDefiner beanDefinitionDefiner
    ) {
        super(beanDefinitionDefiner);
    }

    @Override
    public void refresh() {
        try {
            globalLock.lock();
            throwExIfClosed();
            doRefresh();
        } finally {
            globalLock.unlock();
        }
    }

    protected abstract void doRefresh();

    @Override
    public void clear() {
        try {
            globalLock.lock();
            throwExIfClosed();
            doClear();
        } finally {
            globalLock.unlock();
        }
    }

    protected abstract void doClear();

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public void close() {
        try {
            globalLock.lock();
            if (!closed.getAndSet(true)) {
                doClose();
            }
        } finally {
            globalLock.unlock();
        }
    }

    protected abstract void doClose();

    private void throwExIfClosed()  {
        if (isClosed()) {
            throw new RuntimeException("the factory has been closed");
        }
    }
}
