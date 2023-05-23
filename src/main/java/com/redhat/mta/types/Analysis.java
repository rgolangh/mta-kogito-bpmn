package com.redhat.mta.types;

record Mode(boolean binary, boolean withDeps, boolean diva, String artifact) {}
record Rules(String path, String tags) {}
record Packages(String[] included, String[] excluded) {}
record Scope(boolean withKnown, Packages packages) {}
record Data(Mode mode, String output, Rules rules, Scope scope, String[] sources, String[] targets) {}
