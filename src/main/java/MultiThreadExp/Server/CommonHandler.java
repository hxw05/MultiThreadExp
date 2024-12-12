package MultiThreadExp.Server;

import MultiThreadExp.Request;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class CommonHandler {
    public Request request;

    public CommonHandler(Request request) {
        this.request = request;
    }

    public abstract Response handle();
}
