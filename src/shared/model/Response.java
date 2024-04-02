package shared.model;

public class Response <T>{

    private T payload;
    private boolean success;

    public Response(T payload, boolean success) {
        this.payload = payload;
        this.success = success;
    }

    public T getPayload() {
        return payload;
    }

    public boolean isSuccess() {
        return success;
    }
}
