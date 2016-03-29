package com.other.findconflict;

/**
 * 类信息DO
 */
public class ClassInfosDO {

    private String classFullName;
    private String jarName;

    /**
     * @param classFullName
     * @param jarName
     */
    public ClassInfosDO(String classFullName, String jarName) {
        this.classFullName = classFullName;
        this.jarName = jarName;
    }

    /**
     * @return the classFullName
     */
    public String getClassFullName() {
        return classFullName;
    }

    /**
     * @param classFullName the classFullName to set
     */
    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    /**
     * @return the jarName
     */
    public String getJarName() {
        return jarName;
    }

    /**
     * @param jarName the jarName to set
     */
    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

}
