package com.spring.pseudocode.core.core.io.support;

import com.spring.pseudocode.core.core.io.DefaultResourceLoader;
import com.spring.pseudocode.core.core.io.Resource;
import com.spring.pseudocode.core.core.io.ResourceLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Spring加载resource时classpath*:与classpath:的区别
 * classpath：只会到你指定的class路径中查找文件;
 * classpath*：不仅包含class路径，还包括jar文件中(class路径)进行查找
 */
public class PathMatchingResourcePatternResolver implements ResourcePatternResolver {

    private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);

    private static Method equinoxResolveMethod;

    private final ResourceLoader resourceLoader;

    private PathMatcher pathMatcher = new AntPathMatcher();

    public PathMatchingResourcePatternResolver() {
        this.resourceLoader = new DefaultResourceLoader();
    }

    public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public PathMatchingResourcePatternResolver(ClassLoader classLoader) {
        this.resourceLoader = new DefaultResourceLoader(classLoader);
    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public ClassLoader getClassLoader() {
        return getResourceLoader().getClassLoader();
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    @Override
    public Resource getResource(String location) {
        return getResourceLoader().getResource(location);
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            if (getPathMatcher().isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                return findPathMatchingResources(locationPattern);
            }
            else {
                return findAllClassPathResources(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        }
        else {
            int prefixEnd = locationPattern.indexOf(":") + 1;
            if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd))) {
                return findPathMatchingResources(locationPattern);
            }
            else {
                return new Resource[] {getResourceLoader().getResource(locationPattern)};
            }
        }
    }

    protected Resource[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Set<Resource> result = doFindAllClassPathResources(path);
        if (logger.isDebugEnabled()) {
            logger.debug("Resolved classpath location [" + location + "] to resources " + result);
        }
        return result.toArray(new Resource[result.size()]);
    }

    protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
        Set<Resource> result = new LinkedHashSet<Resource>(16);
        ClassLoader cl = getClassLoader();
        Enumeration<URL> resourceUrls = (cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path));
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            result.add(convertClassLoaderURL(url));
        }
        if ("".equals(path)) {
            //addAllClassLoaderJarRoots(cl, result);
        }
        return result;
    }

    protected Resource convertClassLoaderURL(URL url) {
        //return new UrlResource(url);
        return null;
    }

    protected boolean isJarResource(Resource resource) throws IOException {
        return false;
    }

    protected JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            try {
                return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
            }
            catch (URISyntaxException ex) {
                // Fallback for URLs that are not valid URIs (should hardly ever happen).
                return new JarFile(jarFileUrl.substring(ResourceUtils.FILE_URL_PREFIX.length()));
            }
        }
        else {
            return new JarFile(jarFileUrl);
        }
    }

    protected Resource[] findPathMatchingResources(String locationPattern) throws IOException
    {
        //String rootDirPath = determineRootDir(locationPattern);
        String rootDirPath=null;
        String subPattern = locationPattern.substring(rootDirPath.length());
        Resource[] rootDirResources = getResources(rootDirPath);
        Set result = new LinkedHashSet(16);
        for (Resource rootDirResource : rootDirResources) {
            //rootDirResource = resolveRootDirResource(rootDirResource);
            URL rootDirURL = rootDirResource.getURL();
            if ((equinoxResolveMethod != null) &&
                    (rootDirURL.getProtocol().startsWith("bundle"))) {
                rootDirURL = (URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] { rootDirURL });
                //rootDirResource = new UrlResource(rootDirURL);
            }

            if (rootDirURL.getProtocol().startsWith("vfs")) {
               // result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirURL, subPattern, getPathMatcher()));
            }
            else if ((ResourceUtils.isJarURL(rootDirURL)) || (isJarResource(rootDirResource))) {
                //result.addAll(doFindPathMatchingJarResources(rootDirResource, rootDirURL, subPattern));
            }
            else {
                //result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));
            }
        }
        return (Resource[])result.toArray(new Resource[result.size()]);
    }
}
