package MultiThreadExp;

public record Request(String action, String... data) {
    public static Request from(String s) {
        var split = s.split("\\|");
        if (split.length != 2 && split.length != 1) return null;
        return split.length == 1 ? new Request(split[0]) : new Request(split[0], split[1].split("&"));
    }

    @Override
    public String toString() {
        return action + "|" + String.join("&", data);
    }
}
