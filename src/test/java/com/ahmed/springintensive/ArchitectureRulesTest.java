package com.ahmed.springintensive;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Small architecture fitness functions for the current layered package layout.
 *
 * These tests intentionally preserve the existing uppercase package names.
 * A later package migration should update the rules in the same change.
 */
class ArchitectureRulesTest {

    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.ahmed.springintensive");

    @Test
    void controllersMustNotDependDirectlyOnRepositories() {
        noClasses().that().resideInAPackage("..Controller..")
                .should().dependOnClassesThat().resideInAPackage("..Repository..")
                .because("controllers should use application services rather than persistence directly")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void servicesMustNotDependOnControllers() {
        noClasses().that().resideInAPackage("..Service..")
                .should().dependOnClassesThat().resideInAPackage("..Controller..")
                .because("application logic must not depend on the HTTP adapter layer")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void repositoriesMustNotDependOnControllersOrServices() {
        noClasses().that().resideInAPackage("..Repository..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..Controller..", "..Service..")
                .because("persistence adapters must not depend on higher layers")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void entitiesMustNotDependOnControllersServicesOrRepositories() {
        noClasses().that().resideInAPackage("..Entity..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..Controller..", "..Service..", "..Repository..")
                .because("entities must not depend on delivery or application infrastructure")
                .check(PRODUCTION_CLASSES);
    }
}
