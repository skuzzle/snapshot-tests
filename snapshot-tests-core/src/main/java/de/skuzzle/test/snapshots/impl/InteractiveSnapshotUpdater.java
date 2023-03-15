package de.skuzzle.test.snapshots.impl;

class InteractiveSnapshotUpdater {

    boolean updateInteractively() {
        System.out.println("You want to update your snapshot? [y/N]");

        final String yn = System.console().readLine();
        return "y".equalsIgnoreCase(yn);
    }
}
