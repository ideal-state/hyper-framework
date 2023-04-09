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

package team.idealstate.hyper.commons.resource;

import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.resource.exception.FileNotCreatedException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Jar 包内资源相关工具</p>
 *
 * <p>Created on 2023/3/1 20:38</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class JarUtils {

    /**
     * 读取指定 Jar 包内指定地址下的文件内容的字节数组
     *
     * @param sourceJarClass 源 Jar 包内的已加载 class
     * @param location       源地址，相对于 sourceJar 内的根目录，此参数不应该指向目录
     * @throws IOException 详见 {@link JarUtils#readAllBytes(String, String)}
     */
    public static byte[] readAllBytes(@NotNull Class<?> sourceJarClass, @NotNull String location) throws IOException {
        return readAllBytes(getSourceJar(sourceJarClass), location);
    }

    /**
     * 读取指定 Jar 包内指定地址下的文件内容的字节数组
     *
     * @param sourceJar 源 Jar 包
     * @param location  源地址，相对于 sourceJar 内的根目录，此参数不应该指向目录
     * @throws IOException 详见方法体逻辑
     */
    public static byte[] readAllBytes(@NotNull String sourceJar, @NotNull String location) throws IOException {
        try (final InputStream is = getInputStream(sourceJar, location)) {
            final byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return bytes;
        }
    }

    /**
     * 获取指定 Jar 包内指定地址下的文件内容的输入流
     *
     * @param sourceJarClass 源 Jar 包内的已加载 class
     * @param location       源地址，相对于 sourceJar 内的根目录，此参数不应该指向目录
     * @throws IOException 详见 {@link JarUtils#getInputStream(String, String)}
     */
    @NotNull
    public static InputStream getInputStream(@NotNull Class<?> sourceJarClass, @NotNull String location) throws IOException {
        return getInputStream(getSourceJar(sourceJarClass), location);
    }

    /**
     * 获取指定 Jar 包内指定地址下的文件内容的输入流
     *
     * @param sourceJar 源 Jar 包
     * @param location  源地址，相对于 sourceJar 内的根目录，此参数不应该指向目录
     * @throws IOException 详见方法体逻辑
     */
    @NotNull
    public static InputStream getInputStream(@NotNull String sourceJar, @NotNull String location) throws IOException {
        Asserts.hasText(sourceJar, "sourceJar");
        Asserts.hasText(location, "location");

        checkSourceJar(sourceJar);
        location = rewriteLocation(location);
        try (final JarFile jarFile = new JarFile(sourceJar)) {
            final JarEntry jarEntry = jarFile.getJarEntry(location);
            if (jarEntry == null || jarEntry.isDirectory()) {
                throw new FileNotFoundException("location: " + location);
            }
            try (InputStream is = jarFile.getInputStream(jarEntry)) {
                return new ByteArrayInputStream(is.readAllBytes());
            }
        }
    }

    /**
     * 拷贝指定 Jar 包内指定地址下的内容到目标目录下<br>
     * 此方法不会替换目标目录下已有内容
     *
     * @param sourceJarClass 源 Jar 包内的已加载 class
     * @param location       源地址，相对于 sourceJar 内的根目录
     * @param targetDir      目标目录
     * @throws IOException 详见 {@link JarUtils#copy(String, String, File, boolean)}
     */
    public static void copy(@NotNull Class<?> sourceJarClass, @NotNull String location, @NotNull File targetDir) throws IOException {
        copy(getSourceJar(sourceJarClass), location, targetDir, false);
    }

    /**
     * 拷贝指定 Jar 包内指定地址下的内容到目标目录下<br>
     * 此方法不会替换目标目录下已有内容
     *
     * @param sourceJar 源 Jar 包
     * @param location  源地址，相对于 sourceJar 内的根目录
     * @param targetDir 目标目录
     * @throws IOException 详见 {@link JarUtils#copy(String, String, File, boolean)}
     */
    public static void copy(@NotNull String sourceJar, @NotNull String location, @NotNull File targetDir) throws IOException {
        copy(sourceJar, location, targetDir, false);
    }

    /**
     * 拷贝指定 Jar 包内指定地址下的内容到目标目录下
     *
     * @param sourceJarClass 源 Jar 包内的已加载 class
     * @param location       源地址，相对于 sourceJar 内的根目录
     * @param targetDir      目标目录
     * @param replace        是否替换已有内容
     * @throws IOException 详见 {@link JarUtils#copy(String, String, File, boolean)}
     */
    public static void copy(@NotNull Class<?> sourceJarClass, @NotNull String location, @NotNull File targetDir, boolean replace) throws IOException {
        copy(getSourceJar(sourceJarClass), location, targetDir, replace);
    }

    /**
     * 拷贝指定 Jar 包内指定地址下的内容到目标目录下
     *
     * @param sourceJar 源 Jar 包
     * @param location  源地址，相对于 sourceJar 内的根目录
     * @param targetDir 目标目录
     * @param replace   是否替换已有内容
     * @throws IOException 详见方法体逻辑
     */
    public static void copy(@NotNull String sourceJar, @NotNull String location, @NotNull File targetDir, boolean replace) throws IOException {
        Asserts.hasText(sourceJar, "sourceJar");
        Asserts.hasText(location, "location");
        Asserts.notNull(targetDir, "targetDir");

        checkSourceJar(sourceJar);
        location = rewriteLocation(location);

        final String targetDirAbsolutePath = targetDir.getAbsolutePath();
        if (targetDir.exists()) {
            if (targetDir.isFile()) {
                throw new FileNotFoundException("Invalid directory: " + targetDirAbsolutePath);
            }
        } else if (!targetDir.mkdirs()) {
            throw new FileNotCreatedException(targetDirAbsolutePath);
        }

        try (final JarFile jarFile = new JarFile(sourceJar)) {
            JarEntry jarEntry = jarFile.getJarEntry(location);
            File file;
            if (jarEntry == null) {
                location = location + "/";
                jarEntry = jarFile.getJarEntry(location);
                if (jarEntry == null) {
                    throw new FileNotFoundException("location: " + location);
                }
            } else if (!jarEntry.isDirectory()) {
                final String[] temp = jarEntry.getName().split("/");
                file = new File(targetDir, temp[temp.length - 1]);
                try (InputStream is = jarFile.getInputStream(jarEntry)) {
                    if (replace) {
                        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.copy(is, file.toPath());
                    }
                }

                return;
            }

            final Iterator<JarEntry> iterator = jarFile.stream().iterator();
            String entryName;
            File parentFile;
            while (iterator.hasNext()) {
                jarEntry = iterator.next();
                entryName = jarEntry.getName();
                if (!entryName.startsWith(location)) {
                    continue;
                }

                file = new File(targetDir, entryName.substring(location.length()));
                if (jarEntry.isDirectory()) {
                    if (!file.isDirectory() && !file.mkdirs()) {
                        throw new FileNotCreatedException(file.getAbsolutePath());
                    }
                    continue;
                }

                if (file.exists() && !replace) {
                    continue;
                }

                parentFile = file.getParentFile();
                if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                    throw new FileNotCreatedException(file.getAbsolutePath());
                }
                try (InputStream is = jarFile.getInputStream(jarEntry)) {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static String getSourceJar(@NotNull Class<?> sourceJarClass) {
        Asserts.notNull(sourceJarClass, "sourceJarClass");

        return sourceJarClass
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath()
                .replace("%20", " ");
    }

    private static void checkSourceJar(@NotNull String sourceJar) throws FileNotFoundException {
        final File sourceJarFile = new File(sourceJar);
        if (!sourceJarFile.exists() || sourceJarFile.isDirectory() || !sourceJarFile.toString().endsWith(".jar")) {
            throw new FileNotFoundException("Invalid jar file: " + sourceJarFile.getAbsolutePath());
        }
    }

    private static String rewriteLocation(@NotNull String location) {
        location = location.replace('\\', '/');
        while (location.startsWith("/")) {
            location = location.substring(1);
        }
        while (location.endsWith("/")) {
            location = location.substring(0, location.length() - 1);
        }
        return location;
    }
}
