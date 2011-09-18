package com.thoughtworks.twist.maven;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /**
     * Map of project artifacts.
     *
     * @parameter expression="${project.artifactMap}"
     * @required
     * @readonly
     */
    private Map projectArtifactMap;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * Arbitrary JVM options to set on the command line.
     *
     * @parameter expression="${argLine}"
     */
    private String argLine;

    /**
     * Additional environment variables to set on the command line.
     *
     * @parameter
     */
    private Map environmentVariables = new HashMap();

    /**
     * Command line working directory.
     *
     * @parameter expression="${basedir}"
     */
    private File workingDirectory;

    public Map getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getArgLine() {
        return argLine;
    }

    public void setArgLine(String argLine) {
        this.argLine = argLine;
    }

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

    public Map getProjectArtifactMap() {
        return projectArtifactMap;
    }

    public void setProjectArtifactMap(Map projectArtifactMap) {
        this.projectArtifactMap = projectArtifactMap;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Execute Scenarios task");
        getLog().info("Checked configuration. OK");
        getLog().info(String.valueOf(project));
        Commandline commandline = new Commandline();
        if (StringUtils.isNotEmpty(argLine)) commandline.createArg().setLine(StringUtils.chopNewline(argLine));
        commandline.setExecutable(getJVM());
        commandline.setWorkingDirectory(workingDirectory.getAbsolutePath());
        addEnvironmentVariables(commandline);
        addClasspathAndMainClass(commandline);
        addArg(commandline, scenarioDir.getAbsolutePath());
        addArg(commandline, reportsDir.getAbsolutePath());
        addArg(commandline, confDir.getAbsolutePath());
        addArg(commandline, tags);
        addArg(commandline, String.valueOf(numberOfThreads));
        addArg(commandline, savedFilters);
        getLog().info("Initialized Java: OK. Executing " + commandline + "...");
//        executeCommand(commandline);
    }

    private void addClasspathAndMainClass(Commandline commandline) throws MojoExecutionException {
        commandline.createArg().setValue("-cp");
        commandline.createArg().setValue(StringUtils.join(getClasspath().iterator(), File.pathSeparator));
        commandline.createArg().setValue("com.thoughtworks.twist.core.execution.ant.ScenarioExecutorAntMain");
    }

    private void addArg(Commandline commandline, String argVal) {
        commandline.createArg().setValue(argVal);
    }

    private void executeCommand(Commandline commandline) throws MojoFailureException {
        int retVal;
        try {
            DefaultConsumer defaultConsumer = new DefaultConsumer();
            retVal = CommandLineUtils.executeCommandLine(commandline, defaultConsumer, defaultConsumer);
        } catch (CommandLineException e) {
            throw new MojoFailureException("Twist scenario execution failed", e);
        }

        if (retVal != 0)
            throw new MojoFailureException("Twist scenario execution failed");
    }

    private void addEnvironmentVariables(Commandline cli) {
        if (environmentVariables != null) {
            Iterator iterator = environmentVariables.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) environmentVariables.get(key);
                cli.addEnvironment(key, value);
            }
        }
    }

    private String getJVM() {
        return System.getProperty( "java.home" ) + File.separator + "bin" + File.separator + "java";
    }

    private List getClasspath() throws MojoExecutionException {
        try {
            return project.getTestClasspathElements();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("An error occurred while resolving project dependencies", e);
        }
    }

}
