package MultiThreadExp.Server;

import MultiThreadExp.Request;

public abstract class CommonHandler {
    public Request request;

    public CommonHandler(Request request) {
        this.request = request;
    }

    public abstract Response handle();
}
