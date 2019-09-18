package com.pseudocode.netflix.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//zuul支持动态加载Filter类文件。
//它监控存放Filter文件的目录，定期扫描这些目录，如果发现有新Filter源码文件或者Filter源码文件有改动，则对文件进行编译加载。
//FilterFileManager用于管理Filter存放目录，并定期扫描目录的变化
public class FilterFileManager {

    private static final Logger LOG = LoggerFactory.getLogger(FilterFileManager.class);

    String[] aDirectories;
    int pollingIntervalSeconds;
    Thread poller;
    boolean bRunning = true;

    static FilenameFilter FILENAME_FILTER;

    static FilterFileManager INSTANCE;

    private FilterFileManager() {
    }

    public static void setFilenameFilter(FilenameFilter filter) {
        FILENAME_FILTER = filter;
    }

    public static void init(int pollingIntervalSeconds, String... directories) throws Exception, IllegalAccessException, InstantiationException {
        if (INSTANCE == null) INSTANCE = new FilterFileManager();

        INSTANCE.aDirectories = directories;
        INSTANCE.pollingIntervalSeconds = pollingIntervalSeconds;
        INSTANCE.manageFiles();
        INSTANCE.startPoller();

    }

    public static FilterFileManager getInstance() {
        return INSTANCE;
    }

    public static void shutdown() {
        INSTANCE.stopPoller();
    }


    void stopPoller() {
        bRunning = false;
    }

    void startPoller() {
        poller = new Thread("GroovyFilterFileManagerPoller") {
            public void run() {
                while (bRunning) {
                    try {
                        sleep(pollingIntervalSeconds * 1000);
                        manageFiles();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        poller.setDaemon(true);
        poller.start();
    }

    public File getDirectory(String sPath) {
        File  directory = new File(sPath);
        if (!directory.isDirectory()) {
            URL resource = FilterFileManager.class.getClassLoader().getResource(sPath);
            try {
                directory = new File(resource.toURI());
            } catch (Exception e) {
                LOG.error("Error accessing directory in classloader. path=" + sPath, e);
            }
            if (!directory.isDirectory()) {
                throw new RuntimeException(directory.getAbsolutePath() + " is not a valid directory");
            }
        }
        return directory;
    }

    List<File> getFiles() {
        List<File> list = new ArrayList<File>();
        for (String sDirectory : aDirectories) {
            if (sDirectory != null) {
                File directory = getDirectory(sDirectory);
                File[] aFiles = directory.listFiles(FILENAME_FILTER);
                if (aFiles != null) {
                    list.addAll(Arrays.asList(aFiles));
                }
            }
        }
        return list;
    }

    void processGroovyFiles(List<File> aFiles) throws Exception, InstantiationException, IllegalAccessException {

        for (File file : aFiles) {
            FilterLoader.getInstance().putFilter(file);
        }
    }

    void manageFiles() throws Exception, IllegalAccessException, InstantiationException {
        List<File> aFiles = getFiles();
        processGroovyFiles(aFiles);
    }

}