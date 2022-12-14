package org.jenkinsci.plugins.liquibase.install;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Functions;
import hudson.Util;
import hudson.model.EnvironmentSpecific;
import hudson.model.Node;
import hudson.model.TaskListener;
import hudson.slaves.NodeSpecific;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolInstaller;
import hudson.tools.ToolProperty;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class LiquibaseInstallation extends ToolInstallation implements NodeSpecific<LiquibaseInstallation>, EnvironmentSpecific<LiquibaseInstallation> {

    private static final long serialVersionUID = 1;

    private final String liquibaseHome;
    private String databaseDriverUrl;

    @DataBoundConstructor
    public LiquibaseInstallation(String name, String home, String databaseDriverUrl, List<? extends ToolProperty<?>> properties) {
        super(Util.fixEmptyAndTrim(name), "liquibase", properties);
        this.liquibaseHome = home;
        this.databaseDriverUrl = databaseDriverUrl;
    }

    @Override
    public LiquibaseInstallation forEnvironment(EnvVars environment) {
        return new LiquibaseInstallation(getName(), environment.expand(liquibaseHome), environment.expand(databaseDriverUrl), getProperties().toList());
    }

    @Override
    public LiquibaseInstallation forNode(Node node, TaskListener log) throws IOException, InterruptedException {
        return new LiquibaseInstallation(getName(), translateFor(node, log), databaseDriverUrl, getProperties().toList());
    }

    @Override
    public String getHome() {
        if (liquibaseHome != null) {
            return liquibaseHome;
        }
        return super.getHome();
    }

    public File getLiquibaseExe() {
        // Starting in v4.14.0, liquibase.jar was splitted in liquibase-core.jar
        // and liquibase-commercial.jar (https://www.liquibase.com/blog/two-jars-beat-as-one)
        // Changed to get the executable file directly
        if (Functions.isWindows()) {
            return new File(liquibaseHome, "liquibase.bat");
        }
        return new File(liquibaseHome, "liquibase");
    }

    public boolean isValidLiquibaseHome() {
        final File liquibaseExe = getLiquibaseExe();
        return liquibaseExe.exists();
    }

    public String getDatabaseDriverUrl() {
        return databaseDriverUrl;
    }

    @DataBoundSetter
    public void setDatabaseDriverUrl(String databaseDriverUrl) {
        this.databaseDriverUrl = Util.fixEmptyAndTrim(databaseDriverUrl);
    }

    @Extension
    public static class DescriptorImpl extends ToolDescriptor<LiquibaseInstallation> {

        private LiquibaseInstallation[] installations = new LiquibaseInstallation[0];

        public DescriptorImpl() {
        }

        @Override
        public String getDisplayName() {
            return "Liquibase";
        }

        @Override
        public List<? extends ToolInstaller> getDefaultInstallers() {
            return Collections.singletonList(new LiquibaseInstaller(null));
        }

        @Override
        public LiquibaseInstallation[] getInstallations() {
            return Arrays.copyOf(installations, installations.length);
        }

        @Override
        public void setInstallations(LiquibaseInstallation... installations) {
            this.installations = installations;
            save();
        }
    }

}
