package projects.seller.ClipStudio.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class userController {
    @GetMapping("/hello")
    public String hello() {
        log.info("inside hello()");
        return "hello";
    }
    @GetMapping("/rr")
    public String rr() {
        return "redirect:/hello";
    }
}
