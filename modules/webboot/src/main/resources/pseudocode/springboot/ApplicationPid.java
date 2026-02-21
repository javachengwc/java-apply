package com.boot.pseudocode.springboot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class ApplicationPid
{
    private final String pid;

    public ApplicationPid()
    {
        this.pid = getPid();
    }

    protected ApplicationPid(String pid) {
        this.pid = pid;
    }

    private String getPid() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return jvmName.split("@")[0];
        } catch (Throwable ex) {
        }
        return null;
    }

    public String toString()
    {
        return this.pid == null ? "???" : this.pid;
    }

    public void write(File file) throws IOException
    {
        createParentFolder(file);
        FileWriter writer = new FileWriter(file);
        try {
            writer.append(this.pid);
            writer.close();
        }
        finally {
            writer.close();
        }
    }

    private void createParentFolder(File file)
    {
        File parent = file.getParentFile();
        if (parent != null)
            parent.mkdirs();
    }
}
