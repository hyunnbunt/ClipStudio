package projects.seller.ClipStudio.oauth2.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    @GetMapping("/")
    public String hello() {
        return "hello!!!";
    }
}
