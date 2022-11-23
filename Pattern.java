package analyzer;

public class Pattern {
    private final int priority;
    private final String pattern;
    private final String response;

    public Pattern(int priority, String pattern, String response) {
        this.priority = priority;
        this.pattern = pattern;
        this.response = response;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getResponse() {
        return response;
    }

}
