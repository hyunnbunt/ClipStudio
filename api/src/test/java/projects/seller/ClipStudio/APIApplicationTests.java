package projects.seller.ClipStudio;

import clipstudio.APIApplication;
import clipstudio.MVCController.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserController.class})
class APIApplicationTests {

	@Autowired
	private UserController userController;
	@Test
	void contextLoads() throws Exception {
		assertThat(userController).isNotNull();
	}
}
