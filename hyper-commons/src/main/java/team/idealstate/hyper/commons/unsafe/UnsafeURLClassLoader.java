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

package team.idealstate.hyper.commons.unsafe;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import sun.misc.Unsafe;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * <p>为 {@link URLClassLoader} 提供包装</p>
 * 不推荐使用，因为基于 {@link Unsafe}
 *
 * <p>Created on 2023/1/5 12:00</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public final class UnsafeURLClassLoader {

    private final Object ucp;
    private final List<URL> path;
    private final Deque<URL> unopenedUrls;

    private UnsafeURLClassLoader(@NotNull Object ucp, @NotNull List<URL> path, @NotNull Deque<URL> unopenedUrls) {
        this.ucp = ucp;
        this.path = path;
        this.unopenedUrls = unopenedUrls;
    }

    /**
     * 包装一个 {@link URLClassLoader}
     *
     * @param ucl 详见 {@link URLClassLoader}
     * @return 详见 {@link UnsafeURLClassLoader}
     */
    @SuppressWarnings({"unchecked"})
    public static UnsafeURLClassLoader wrap(@NotNull URLClassLoader ucl) {
        Asserts.notNull(ucl, "ucl");

        final Object ucp;
        final List<URL> path;
        final Deque<URL> unopenedUrls;
        try {
            ucp = UnsafeWrapper.getValue(URLClassLoader.class, ucl, "ucp");
            path = (List<URL>) UnsafeWrapper.getValue(Objects.requireNonNull(ucp), "path");
            unopenedUrls = (Deque<URL>) UnsafeWrapper.getValue(ucp, "unopenedUrls");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return new UnsafeURLClassLoader(ucp, Objects.requireNonNull(path), Objects.requireNonNull(unopenedUrls));
    }

    /**
     * 添加 url 到类加载器
     *
     * @param urls 待添加的 url
     */
    public synchronized void addURL(@NotNull URL... urls) {
        if (urls == null || urls.length == 0 || isClosed()) {
            return;
        }
        synchronized (unopenedUrls) {
            for (final URL url : urls) {
                if (url != null && !path.contains(url)) {
                    unopenedUrls.addLast(url);
                    path.add(url);
                }
            }
        }
    }

    private boolean isClosed() {
        try {
            final Object closed = UnsafeWrapper.getValue(ucp, "closed");
            return closed == null || Boolean.TRUE.equals(closed);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
