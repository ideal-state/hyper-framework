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

import team.idealstate.hyper.commons.StringUtils;
import team.idealstate.hyper.commons.lang.NotNull;
import team.idealstate.hyper.commons.asserts.Asserts;
import team.idealstate.hyper.commons.os.OsUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * <p>资源相关工具</p>
 *
 * <p>Created on 2023/2/24 11:19</p>
 *
 * @author ketikai
 * @since 0.0.1
 */
public abstract class ResourceUtils {

    /**
     * 将地址实例化成 URI 对象
     *
     * @param location 地址
     * @return URI 对象
     * @throws URISyntaxException 详情请参阅：{@link URI#URI(String)}
     */
    @NotNull
    public static URI toURI(@NotNull String location) throws URISyntaxException {
        Asserts.notNull(location, "location");

        location = location
                .replace('\\', '/')
                .replace(" ", "%20");

        while (location.charAt(location.length() - 1) == '/') {
            location = location.substring(0, location.length() - 1);
        }

        return new URI(location);
    }

    /**
     * 将地址实例化成 URL 对象
     *
     * @param location 地址
     * @return URL 对象
     * @throws URISyntaxException    详情请参阅：{@link URI#toURL()}
     * @throws MalformedURLException 详情请参阅：{@link URI#toURL()}
     */
    @NotNull
    public static URL toURL(@NotNull String location) throws URISyntaxException, MalformedURLException {
        return toURI(location).toURL();
    }

    /**
     * 判断是否为文件资源 uri
     *
     * @param uri uri
     * @return true or false
     */
    public static boolean isFileURI(@NotNull URI uri) {
        Asserts.notNull(uri, "uri");

        final String location = uri.toString();
        final String scheme = uri.getScheme();
        final String path = uri.getRawPath();

        final boolean isAbsolutePath = !StringUtils.isNullOrEmpty(path) && path.charAt(0) == '/';
        if (scheme == null) {
            return OsUtils.isLinux() && isAbsolutePath && location.equals(path);
        } else {
            if (scheme.length() == 1 && OsUtils.isWindows() && isAbsolutePath && location.equals(scheme + ":" + path)) {
                final char c = scheme.charAt(0);
                return Character.isLetter(c);
            }
        }

        return Protocol.FILE.equals(scheme);
    }

    /**
     * 判断是否为 jar 包内资源 uri
     *
     * @param uri uri
     * @return true or false
     */
    public static boolean isJarPathURI(@NotNull URI uri) {
        Asserts.notNull(uri, "uri");

        final String scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }

        final String path = uri.getRawPath();
        return Protocol.JAR.equals(scheme) && !path.startsWith(".jar!/") && path.split("\\.jar!/").length == 2;
    }

    /**
     * 判断是否为 jar 包资源 uri
     *
     * @param uri uri
     * @return true or false
     */
    public static boolean isJarFileURI(@NotNull URI uri) {
        Asserts.notNull(uri, "uri");

        final String scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }

        return Protocol.JAR.equals(scheme);
    }

    /**
     * 判断是否为 http 资源 uri
     *
     * @param uri uri
     * @return true or false
     */
    public static boolean isHttpURI(@NotNull URI uri) {
        Asserts.notNull(uri, "uri");

        final String scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }

        return Protocol.HTTP.equals(scheme) || Protocol.HTTPS.equals(scheme);
    }

    /**
     * 判断是否为 ftp 资源 uri
     *
     * @param uri uri
     * @return true or false
     */
    public static boolean isFtpURI(@NotNull URI uri) {
        Asserts.notNull(uri, "uri");

        final String scheme = uri.getScheme();
        if (scheme == null) {
            return false;
        }

        return Protocol.FTP.equals(scheme);
    }
}
