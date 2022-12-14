package org.jenkinsci.plugins.liquibase.builder;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Util;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.util.ArgumentListBuilder;
import java.util.Properties;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Build step that invokes liquibase's tag command against a target database.
 */
@SuppressWarnings("ProhibitedExceptionThrown")
public class TagBuilder extends AbstractLiquibaseBuilder {

    @Extension
    public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

    private String tag;

    @DataBoundConstructor
    public TagBuilder() {
        super();
    }

    @Override
    protected void addCommandAndArguments(ArgumentListBuilder cliCommand, Properties configProperties,
            Run<?, ?> build, EnvVars environment, TaskListener listener) {
        String tagString = this.getTag();
        if (tagString == null || tagString.trim().equals("")) {
            tagString = build.getParent().getName() + "-" + build.getNumber();
        }

        cliCommand.add("tag", Util.replaceMacro(tagString, environment));
    }

    @Override
    public Descriptor<Builder> getDescriptor() {
        return DESCRIPTOR;
    }

    public String getTag() {
        return tag;
    }

    @DataBoundSetter
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Symbol("liquibaseTag")
    @Extension
    public static class DescriptorImpl extends AbstractLiquibaseDescriptor {

        public DescriptorImpl() {
        }

        public DescriptorImpl(Class<? extends TagBuilder> clazz) {
            super(clazz);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Liquibase: Tag Database";
        }
    }

}
