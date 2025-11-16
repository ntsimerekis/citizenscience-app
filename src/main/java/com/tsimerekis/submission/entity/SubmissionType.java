package com.tsimerekis.submission.entity;

import java.util.Arrays;
import java.util.Collection;

public class SubmissionType {
    public final static String SPECIES = "SPECIES";
    public final static String POLLUTION = "POLLUTION";

    public static Collection<String> getAllTypes() {
        return Arrays.asList(SPECIES, POLLUTION);
    }
}
