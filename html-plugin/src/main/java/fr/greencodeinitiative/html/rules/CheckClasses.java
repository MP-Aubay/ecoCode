package fr.greencodeinitiative.html.rules;

import java.util.List;

import fr.greencodeinitiative.html.checks.AvoidAutoplayCheck;

public final class CheckClasses {

    private static final List<Class<?>> CLASSES = List.of(AvoidAutoplayCheck.class);

    private CheckClasses() {

    }

    /**
     * Gets the list of XML checks.
     */
    public static List<Class<?>> getCheckClasses() {
        return CLASSES;
    }
}
