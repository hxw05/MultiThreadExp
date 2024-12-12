package MultiThreadExp.Server;

public record Response(boolean ok, String message, String data) {
    @Override
    public String toString() {
        return ok + "|" + message + "|" + data;
    }

    public static Response from(String str) throws IllegalStateException {
        var res = str.split("\\|");
        if (res.length != 3) throw new IllegalStateException();
        return new Response(Boolean.parseBoolean(res[0]), res[1], res[2]);
    }
}
