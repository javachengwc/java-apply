package com.otd.boot.component.velocity;

import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ToolboxManager;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.servlet.ServletToolboxManager;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class VelocityToolboxView extends VelocityView {

    private String toolboxConfigLocation;

    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }

    protected String getToolboxConfigLocation() {
        return this.toolboxConfigLocation;
    }

    @Override
    protected Context createVelocityContext(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Create a ChainedContext instance.
        ChainedContext velocityContext = new ChainedContext(
                new VelocityContext(model), getVelocityEngine(), request, response, getServletContext());

        // Load a Velocity Tools toolbox, if necessary.
        if (getToolboxConfigLocation() != null) {
            ToolboxManager toolboxManager = ServletToolboxManager.getInstance(
                    getServletContext(), getToolboxConfigLocation());
            Map toolboxContext = toolboxManager.getToolbox(velocityContext);
            velocityContext.setToolbox(toolboxContext);
        }

        return velocityContext;
    }

    @Override
    protected void initTool(Object tool, Context velocityContext) throws Exception {
        // Velocity Tools 1.3: a class-level "init(Object)" method.
        Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", Object.class);
        if (initMethod != null) {
            ReflectionUtils.invokeMethod(initMethod, tool, velocityContext);
        }
    }

}
