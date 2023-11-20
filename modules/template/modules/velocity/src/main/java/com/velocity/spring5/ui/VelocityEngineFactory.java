package com.velocity.spring5.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.CommonsLogLogChute;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class VelocityEngineFactory {

    protected final Log logger = LogFactory.getLog(getClass());

    private Resource configLocation;

    private final Map<String, Object> velocityProperties = new HashMap<String, Object>();

    private String resourceLoaderPath;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private boolean preferFileSystemAccess = true;

    private boolean overrideLogging = true;

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setVelocityProperties(Properties velocityProperties) {
        CollectionUtils.mergePropertiesIntoMap(velocityProperties, this.velocityProperties);
    }

    public void setVelocityPropertiesMap(Map<String, Object> velocityPropertiesMap) {
        if (velocityPropertiesMap != null) {
            this.velocityProperties.putAll(velocityPropertiesMap);
        }
    }

    public void setResourceLoaderPath(String resourceLoaderPath) {
        this.resourceLoaderPath = resourceLoaderPath;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
        this.preferFileSystemAccess = preferFileSystemAccess;
    }

    protected boolean isPreferFileSystemAccess() {
        return this.preferFileSystemAccess;
    }

    public void setOverrideLogging(boolean overrideLogging) {
        this.overrideLogging = overrideLogging;
    }

    public VelocityEngine createVelocityEngine() throws IOException, VelocityException {
        VelocityEngine velocityEngine = newVelocityEngine();
        Map<String, Object> props = new HashMap<String, Object>();

        // Load config file if set.
        if (this.configLocation != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Loading Velocity config from [" + this.configLocation + "]");
            }
            CollectionUtils.mergePropertiesIntoMap(PropertiesLoaderUtils.loadProperties(this.configLocation), props);
        }

        // Merge local properties if set.
        if (!this.velocityProperties.isEmpty()) {
            props.putAll(this.velocityProperties);
        }

        // Set a resource loader path, if required.
        if (this.resourceLoaderPath != null) {
            initVelocityResourceLoader(velocityEngine, this.resourceLoaderPath);
        }

        // Log via Commons Logging?
        if (this.overrideLogging) {
            velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new CommonsLogLogChute());
        }

        // Apply properties to VelocityEngine.
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            velocityEngine.setProperty(entry.getKey(), entry.getValue());
        }

        postProcessVelocityEngine(velocityEngine);

        // Perform actual initialization.
        velocityEngine.init();

        return velocityEngine;
    }

    protected VelocityEngine newVelocityEngine() throws IOException, VelocityException {
        return new VelocityEngine();
    }

    protected void initVelocityResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath) {
        if (isPreferFileSystemAccess()) {
            // Try to load via the file system, fall back to SpringResourceLoader
            // (for hot detection of template changes, if possible).
            try {
                StringBuilder resolvedPath = new StringBuilder();
                String[] paths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
                for (int i = 0; i < paths.length; i++) {
                    String path = paths[i];
                    Resource resource = getResourceLoader().getResource(path);
                    File file = resource.getFile();  // will fail if not resolvable in the file system
                    if (logger.isDebugEnabled()) {
                        logger.debug("Resource loader path [" + path + "] resolved to file [" + file.getAbsolutePath() + "]");
                    }
                    resolvedPath.append(file.getAbsolutePath());
                    if (i < paths.length - 1) {
                        resolvedPath.append(',');
                    }
                }
                velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
                velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
                velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, resolvedPath.toString());
            }
            catch (IOException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot resolve resource loader path [" + resourceLoaderPath +
                            "] to [java.io.File]: using SpringResourceLoader", ex);
                }
                initSpringResourceLoader(velocityEngine, resourceLoaderPath);
            }
        }
        else {
            // Always load via SpringResourceLoader
            // (without hot detection of template changes).
            if (logger.isDebugEnabled()) {
                logger.debug("File system access not preferred: using SpringResourceLoader");
            }
            initSpringResourceLoader(velocityEngine, resourceLoaderPath);
        }
    }

    protected void initSpringResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath) {
        velocityEngine.setProperty(
                RuntimeConstants.RESOURCE_LOADER, SpringResourceLoader.NAME);
        velocityEngine.setProperty(
                SpringResourceLoader.SPRING_RESOURCE_LOADER_CLASS, SpringResourceLoader.class.getName());
        velocityEngine.setProperty(
                SpringResourceLoader.SPRING_RESOURCE_LOADER_CACHE, "true");
        velocityEngine.setApplicationAttribute(
                SpringResourceLoader.SPRING_RESOURCE_LOADER, getResourceLoader());
        velocityEngine.setApplicationAttribute(
                SpringResourceLoader.SPRING_RESOURCE_LOADER_PATH, resourceLoaderPath);
    }

    protected void postProcessVelocityEngine(VelocityEngine velocityEngine)
            throws IOException, VelocityException {
    }

}

