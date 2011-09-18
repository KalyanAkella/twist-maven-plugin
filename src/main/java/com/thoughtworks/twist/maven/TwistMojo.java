package com.thoughtworks.twist.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 * Runs twist scenarios
 * @requiresDependencyResolution test
 * @goal test
 * @phase integration-test
 * @threadSafe
 */
public class TwistMojo extends AbstractMojo
{
    /**
     * @parameter expression="${scenarioDir}"
     * @required
     */
    private File scenarioDir;

    /**
     * @parameter expression="${reportsDir}"
     * @required
     */
    private File reportsDir;

    /**
     * @parameter expression="${confDir}"
     * @required
     */
    private File confDir;

    /**
     * @parameter expression="${tags}"
     * @required
     */
    private String tags;

    /**
     * @parameter default-value="" expression="${savedFilters}"
     */
    private String savedFilters;

    /**
     * @parameter default-value="1" expression="${threads}"
     */
    private int numberOfThreads;

    public File getScenarioDir() {
        return scenarioDir;
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public File getReportsDir() {
        return reportsDir;
    }

    public void setReportsDir(File reportsDir) {
        this.reportsDir = reportsDir;
    }

    public File getConfDir() {
        return confDir;
    }

    public void setConfDir(File confDir) {
        this.confDir = confDir;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSavedFilters() {
        return savedFilters;
    }

    public void setSavedFilters(String savedFilters) {
        this.savedFilters = savedFilters;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void execute() throws MojoExecutionException
    {
        getLog().info("Hello, world.");
    }
}
