package com.devekoc.altaris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(
//        basePackages = "com.devekoc.altaris",
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.REGEX,
//                        pattern = ".*(Province|Diocese|Zone|Parish|Servant|Office).*"
//                )
//        }
//)
public class AltarisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AltarisApplication.class, args);
	}

}
