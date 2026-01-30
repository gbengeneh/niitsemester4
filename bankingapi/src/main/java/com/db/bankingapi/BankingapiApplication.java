package com.db.bankingapi;

import com.db.bankingapi.models.FullName;
//import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestClient;

import java.util.Arrays;

@SpringBootApplication
//@EnableAdminServer
public class BankingapiApplication implements CommandLineRunner {

	//@Autowired
	//private FullName fullName,fullName1;
	@Autowired
    private RestClient restClient;
	public static void main(String[] args) {
		SpringApplication.run(BankingapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Rocking with sb");
		/*
		fullName.setFirstName("Parameswari");
		fullName.setLastName("Bala");
        System.out.println(fullName.getFirstName() + " " + fullName.getLastName());
		fullName1.setFirstName("Vignesh");
		fullName1.setLastName("Manickam");
		System.out.println(fullName1.getFirstName() + " " + fullName1.getLastName());

		String response=restClient.get().uri("https://jsonplaceholder.typicode.com/users")
				.retrieve()
				.body(String.class);
		System.out.println(response);
*/


		/*
		//spring container
		AnnotationConfigApplicationContext annotationConfigApplicationContext =
				new AnnotationConfigApplicationContext();

		annotationConfigApplicationContext.register(FullName.class);
		annotationConfigApplicationContext.refresh();
		//creating bean instance--singleton object
		FullName fullName = annotationConfigApplicationContext.getBean(FullName.class);
		fullName.setFirstName("Parameswari");
		//check object existence
		if(fullName!=null)
			System.out.println(fullName.getFirstName());
		else
			System.out.println("Object not created");

		FullName fullName1 = annotationConfigApplicationContext.getBean(FullName.class);
        fullName1.setFirstName("Bala");

		FullName fullName2 = annotationConfigApplicationContext.getBean(FullName.class);
		fullName2.setFirstName("Samyuktha");
		System.out.println(fullName.getFirstName());
		System.out.println(fullName1.getFirstName());
		System.out.println(fullName2.getFirstName());

		 */
	}
}
