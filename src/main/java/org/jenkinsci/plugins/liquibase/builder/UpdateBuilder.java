package org.jenkinsci.plugins.liquibase.builder;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import java.util.Properties;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Jenkins builder which evaluates liquibase changesets.
 */
@SuppressWarnings("ProhibitedExceptionThrown")
public class UpdateBuilder extends AbstractLiquibaseBuilder {

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    @DataBoundConstructor
    public UpdateBuilder() {
        super();
    }

    @Override
    protected void addCommandAndArguments(ArgumentListBuilder cliCommand, Properties configProperties,
            Run<?, ?> build, EnvVars environment, TaskListener listener) {
        cliCommand.add("update");
    }

    @Override
    public Descriptor<Builder> getDescriptor() {
        return DESCRIPTOR;
    }

    @Symbol("liquibaseUpdate")
    @Extension
    public static class DescriptorImpl extends AbstractLiquibaseDescriptor {

        public DescriptorImpl() {
        }

        public DescriptorImpl(Class<? extends UpdateBuilder> clazz) {
            super(clazz);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Liquibase: Update Database";
        }
    }

}
