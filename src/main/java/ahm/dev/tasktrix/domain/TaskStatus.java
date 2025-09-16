package ahm.dev.tasktrix.domain;

public enum TaskStatus {
    NEW("New"),
    IN_PROGRESS("In Progress"), 
    COMPLETED("Completed");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}