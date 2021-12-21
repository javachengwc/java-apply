package com.newrelic.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
/**
 * @goal example
 */
//@Mojo(name="example", defaultPhase= LifecyclePhase.NONE, threadSafe=true, requiresDependencyResolution= ResolutionScope.NONE)
public class ExampleMojo extends AbstractMojo {

    /**
     * @parameter expression="${example.message}" default-value="this is example"
     */
    @Parameter(defaultValue="this is example", property="example.message", required=true)
    private String message;

    //@注解没什么用，/**/才有用，应该跟编译插件maven版本有关
    public void execute() throws MojoExecutionException {
        getLog().info("goal example execute begin");
        getLog().info(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

