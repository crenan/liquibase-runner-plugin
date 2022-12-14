package dsl;

freeStyleJob('@JOB_NAME@') {
    customWorkspace('@WORKSPACE@')
    steps {
        liquibaseUpdate {
            changeLogFile('sunny-day-changeset.xml')
            testRollbacks(true)
            url('jdbc:postgresql://localhost:5432/sample-db')
            contexts('staging')
            changeLogParameters(["sample.table.name": "blue",
                           "favorite.food"    : "spaghetti"])
        }
    }
}
