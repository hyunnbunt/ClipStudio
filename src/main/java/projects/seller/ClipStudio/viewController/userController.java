package projects.seller.ClipStudio.viewController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projects.seller.ClipStudio.oauth2.User.oauth2.CustomOAuth2User;
import org.springframework.ui.Model;

@Controller
@Slf4j
public class userController {
    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, Model model) {
        log.info("inside hello()");
        log.info(customOAuth2User.getAttributes().toString());
        model.addAttribute("attributes", customOAuth2User.getAttributes());
        return "index";
    }
}
