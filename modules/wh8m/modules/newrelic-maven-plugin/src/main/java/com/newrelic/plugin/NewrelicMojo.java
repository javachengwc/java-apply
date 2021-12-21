package com.newrelic.plugin;

import com.newrelic.Main;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;

//maven默认使用的自行开发的Plexus，Plexus使用javadoc来管理依赖注入，
//下面的@goal，@parameter都是通过这种方式注入，非常原始，不过maven3将迁移到guice。
/**
 * newrelic插件入口
 * @goal newrelic
 */
//@Mojo(name="newrelic", defaultPhase= LifecyclePhase.PACKAGE, requiresProject=true,threadSafe=true, requiresDependencyResolution= ResolutionScope.RUNTIME)
public class NewrelicMojo extends AbstractMojo {

    //变量表达式在赋值的时候会自动转换成对应的值
    /**
     * @parameter expression="spring.path" default-value="${project.basedir}/src/main/resources/spring"
     */
    @Parameter(defaultValue="${project.basedir}/src/main/resources/spring", property="spring.path",required=true)
    private String springPath;

    /**
     * @parameter expression="${newrelic.output}" default-value="${project.build.directory}"
     */
    @Parameter(defaultValue="${project.build.directory}", property="newrelic.output",required=true)
    private String outputPath;

    /**
     * @parameter expression="${newrelic.pointcut}" default-value="all"
     */
    @Parameter(defaultValue="all", property="newrelic.pointcut", required=true)
    private String pointcut;

    /**
     * @parameter expression="${build.dir}" default-value="${project.build.directory}"
     */
    @Parameter(defaultValue="${project.build.directory}", required=true,readonly=true)
    private String buildDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("goal newrelic execute begin");
        getLog().info("buildDirectory:"+buildDirectory+"  "+System.getProperty("project.build.directory"));

        String args[]= new String[4];
        args[0]=springPath;
        args[1]=pointcut;
        args[2]=getClassPath(buildDirectory)+","+getDependencyPath(buildDirectory);
        args[3]=outputPath;
        getLog().info("springPath:"+args[0]);
        getLog().info("pointcut:"+args[1]);
        getLog().info("class and jar path:"+args[2]);
        getLog().info("outputPath:"+args[3]);
        Main.main(args);
        getLog().info("goal newrelic execute end");
    }

    protected static String getClassPath(String basedir)
    {
        return basedir+ File.separator+"classes";
    }
    protected static String getDependencyPath(String basedir)
    {
        return basedir+ File.separator+"dependency";
    }
}

/**
 * 其他maven工程使用此插件的配置
 * <plugin>
 * <groupId>com.app</groupId>
 * <artifactId>maven-newrelic-plugin</artifactId>
 * <version>1.0-SNAPSHOT</version>
 * <configuration>
 *     <springPath>${project.basedir}/src/main/resources/spring</springPath>
 *     <pointcut>all</pointcut>
 * </configuration>
 * <executions>
 *    <execution>
 *         <id>make-assembly</id>
 *         <phase>package</phase>
 *         <goals>
 *             <goal>newrelic</goal>
 *         </goals>
 *     </execution>
 * </executions>
 * </plugin>
 **/