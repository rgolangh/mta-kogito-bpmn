package com.redhat.mta.types;

public record TaskGroup(int id, String name, String state, String addon, Data data, String bucket, Task[] tasks) {
	public static TaskGroup ofCloudReadiness(int appID) {
		return new TaskGroup(0, "taskgroups.windup", null, "windup",
				new Data(new Mode(false, false, false, ""), "/windup/report", new Rules("", null),
						new Scope(false, new Packages(new String[] {}, new String[] {})), new String[] {},
						new String[] { "cloud-readiness" }),
				null, new Task[] { new Task(new Application(appID, "parodos", null), null,
						String.format("parodos.%s.windup", appID), null, null, null) });
	}
}