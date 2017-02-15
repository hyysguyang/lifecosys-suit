package com.lansent.gradle.scs;

import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;

import java.util.*;

public class DependencyHelper {

    DependencyHandler dependencyHandler;

    public DependencyHelper(DependencyHandler dependencyHandler) {
        this.dependencyHandler = dependencyHandler;
    }

    public void addCoreDependencies() {
        Dependency javaslang = dependencyHandler.create("io.javaslang:javaslang:2.0.2");
        Dependency commonsIo = dependencyHandler.create("commons-io:commons-io:2.4");
        Dependency jodd = dependencyHandler.create("org.jodd:jodd-lagarto:3.7.1");
        Dependency slf4j = dependencyHandler.create("org.slf4j:slf4j-api:1.7.21");
        Arrays.asList(javaslang, commonsIo,jodd,slf4j).forEach(d -> dependencyHandler.add("compile", d));
        dependencyHandler.add("testCompile","junit:junit:4.12");
    }

}
