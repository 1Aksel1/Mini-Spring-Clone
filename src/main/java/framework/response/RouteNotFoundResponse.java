package framework.response;

import com.google.gson.Gson;

public class RouteNotFoundResponse extends Response{

    public RouteNotFoundResponse() {

    }

    public String render() {

        StringBuilder responseContext = new StringBuilder();

        responseContext.append("HTTP/1.1 404 Not Found \n");
        for (String key :
                this.header.getKeys()) {
            responseContext.append(key).append(":").append(this.header.get(key)).append("\n");
        }
        responseContext.append("\n");

        responseContext.append("We're sorry, we couldn't find the route you requested");

        return responseContext.toString();

    }

}




