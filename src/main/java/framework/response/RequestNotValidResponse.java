package framework.response;

public class RequestNotValidResponse extends Response{

    public RequestNotValidResponse() {

    }

    public String render() {

        StringBuilder responseContext = new StringBuilder();

        responseContext.append("HTTP/1.1 400 Bad request \n");
        for (String key :
                this.header.getKeys()) {
            responseContext.append(key).append(":").append(this.header.get(key)).append("\n");
        }
        responseContext.append("\n");

        responseContext.append("Bad request");

        return responseContext.toString();

    }


}
