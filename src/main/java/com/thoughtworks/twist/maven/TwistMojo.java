package com.thoughtworks.twist.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Runs twist scenarios
 * @goal test
 */
public class TwistMojo extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
        getLog().info("Hello, world.");
    }
}
