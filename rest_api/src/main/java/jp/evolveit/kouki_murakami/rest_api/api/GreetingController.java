package jp.evolveit.kouki_murakami.rest_api.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/api/greeting")
    public GreetingResponse greeting(@Validated @RequestBody GreetingRequest request) {
        return new GreetingResponse("Hello " + request.getName() + "!");
    }
}
