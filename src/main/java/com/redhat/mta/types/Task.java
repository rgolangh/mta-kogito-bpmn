package com.redhat.mta.types;

public record Task(Application application, String state, String name, String addon, Data data, String bucket) {}