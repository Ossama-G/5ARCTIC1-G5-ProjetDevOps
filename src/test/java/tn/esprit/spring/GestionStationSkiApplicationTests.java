package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GestionStationSkiApplicationTests {

	@MockBean
	private ModelMapper modelMapper;
	@Test
	void contextLoads() {
		// Add your test implementation here
	}

}
