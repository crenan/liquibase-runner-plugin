package org.jenkinsci.plugins.liquibase.dsl;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javaposse.jobdsl.plugin.ExecuteDslScripts;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.jvnet.hudson.test.JenkinsRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiquibaseRunnerDslExtensionTest {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseRunnerDslExtensionTest.class);

    @ClassRule
    public static JenkinsRule jenkinsRule = new JenkinsRule();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    protected FreeStyleProject project;
    protected File workspace;
    protected String expectedProjectName;

    @Before
    public void setup() throws IOException {
        project = jenkinsRule.createFreeStyleProject(RandomStringUtils.randomAlphabetic(5));
        workspace = temporaryFolder.newFolder(RandomStringUtils.randomAlphabetic(8));

        expectedProjectName = RandomStringUtils.randomAlphabetic(10);
    }

//    @Test
//    public void should_spawn_liquibase_project() throws IOException, ExecutionException, InterruptedException {
//        FreeStyleBuild build = launchDslProject(expectedProjectName, "/dsl/liquibase-update-fullconfig.groovy");
//
//        LOG.debug("build log:{}", formatLogForLog(build.getLog(1000)));
//
//        assertThat(build, isSuccessful());
//
//        List<AbstractProject> projects = jenkinsRule.getInstance().getItems(AbstractProject.class);
//
//        assertThat(projects, hasItem(isProjectWithName(expectedProjectName)));
//
//        FreeStyleProject project =
//                jenkinsRule.getInstance().getItemByFullName(expectedProjectName, FreeStyleProject.class);
//
//        UpdateBuilder builder = project.getBuildersList().get(UpdateBuilder.class);
//
//        assertThat(builder.getChangeLogFile(), is("sunny-day-changeset.xml"));
////        assertThat(builder.isTestRollbacks(), is(true));
//        assertThat(builder.getUrl(), is("jdbc:postgresql://localhost:5432/sample-db"));
//        assertThat(builder.getContexts(), is("staging"));
//        assertThat(builder.getChangeLogParameters(), containsString("sample.table.name=blue"));
//        assertThat(builder.getChangeLogParameters(), containsString("favorite.food=spaghetti"));
//    }

//    @Test
//    public void should_build_generated_update_project_successfully()
//            throws InterruptedException, ExecutionException, IOException {
//
//        launchDslProject(expectedProjectName, "/dsl/liquibase-update.groovy");
//
//        Project project = jenkinsRule.getInstance().getItemByFullName(expectedProjectName, Project.class);
//
//        assertThat(project, notNullValue());
//
//        LiquibaseTestUtil.createFileFromResource(workspace, "/example-changesets/sunny-day-changeset.xml");
//
//        FreeStyleBuild build = ((FreeStyleProject) project).scheduleBuild2(0).get();
//
//        logBuildLog(build);
//
//        assertThat(build, isSuccessful());
//
//    }

//      @Test
//    public void should_generate_rollback_project() throws InterruptedException, ExecutionException, IOException {
//        FreeStyleBuild build = launchDslProject(expectedProjectName, "/dsl/rollback-dsl.groovy");
//        LOG.debug("build log:{}", formatLogForLog(build.getLog(100)));
//
//        List<AbstractProject> projects = jenkinsRule.getInstance().getItems(AbstractProject.class);
//
//        assertThat(projects, hasItem(isProjectWithName(expectedProjectName)));
//
//        Project project = jenkinsRule.getInstance().getItemByFullName(expectedProjectName, Project.class);
//        RollbackBuilder rollbackBuilder =
//                (RollbackBuilder) project.getBuildersList().getAll(RollbackBuilder.class).get(0);
//
//        assertThat(rollbackBuilder.getNumberOfChangesetsToRollback(), is("2"));
//        assertThat(rollbackBuilder.getRollbackToTag(), is("tag"));
//        assertThat(rollbackBuilder.getRollbackToDate(), is("13/10/1973 8:00"));
//        assertThat(rollbackBuilder.getRollbackLastHours(), is("1"));
//
//
//    }

    private static void logBuildLog(FreeStyleBuild build) throws IOException {
        LOG.debug("build log of generated project:{}", formatLogForLog(build.getLog(1000)));
    }

    private FreeStyleBuild launchDslProject(String jobName, String scriptResourcePath)
            throws IOException, InterruptedException, ExecutionException {
        String scriptTemplate = getScript(scriptResourcePath);
        String script = scriptTemplate.replaceAll("@WORKSPACE@", workspace.getAbsolutePath());
        String resolvedScript = script.replaceAll("@JOB_NAME@", jobName);

        ExecuteDslScripts executeDslScripts = new ExecuteDslScripts();
        executeDslScripts.setScriptText(resolvedScript);

        project.getBuildersList().add(executeDslScripts);

        return project.scheduleBuild2(0).get();
    }

    private String getScript(String resourcePath) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(resourcePath));
    }

    static String formatLogForLog(Iterable<String> buildLog) {
        StringBuilder sb = new StringBuilder();
        for (String line : buildLog) {
            sb.append(line).append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
