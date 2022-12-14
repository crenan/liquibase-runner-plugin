package org.jenkinsci.plugins.liquibase.common;

import java.util.Locale;

public enum LiquibaseProperty {
    USERNAME,
    PASSWORD,
    LABELS,
    DEFAULTS_FILE("defaultsFile"),
    CHANGELOG_FILE("changeLogFile"),
    CONTEXTS(),
    URL(),
    LOG_LEVEL("logLevel");

    private String cliOption;

    LiquibaseProperty(String cliOption) {
        this.cliOption = cliOption;
    }

    LiquibaseProperty() {
    }

    public String propertyName() {
        if (cliOption != null) {
            return cliOption;
        }
        return name().toLowerCase(Locale.ENGLISH);
    }

}
