package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

public class MyHttpEndpointRouter implements HttpEndpointRouter{
    @Override
    public String route(List<String> endpoints) {
        int size = endpoints.size();
        return endpoints.get(new Random().nextInt(size));
    }
}
