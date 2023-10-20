package com.retooling.chickentestbackend;

import org.springframework.boot.SpringApplication;
import com.retooling.chickentestbackend.model.Chicken;
import com.retooling.chickentestbackend.model.Egg;
import com.retooling.chickentestbackend.model.Farm;
import com.retooling.chickentestbackend.repository.ChickenRepository;
import com.retooling.chickentestbackend.repository.EggRepository;
import com.retooling.chickentestbackend.repository.FarmRepository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ChickentestBackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ChickentestBackendApplication.class, args);

		Farm granja1 = new Farm("Granja de Pepe", 12000);
		Farm granja2 = new Farm("Granja de Carlos", 20000);
		Farm granja3 = new Farm("Granja de Julian", 15000);
//		Egg huevo1 = new Egg(4812, Long.getLong("2"));
		Egg huevo1 = new Egg(4812, granja1);
		huevo1.hatch();
		Egg huevo2 = new Egg(33321, granja1);
		huevo2.hatch();
		Egg huevo3 = new Egg(1234, granja2);
		Egg huevo4 = new Egg(1150.50, granja3);
//		Egg huevo2 = new Egg(12345, Long.getLong("2"));
//		Egg huevo3 = new Egg(23456, Long.getLong("1"));		
//		Egg huevo4 = new Egg(76554, null);

		Chicken pollo1 = new Chicken(123545, 2, granja1);
		Chicken pollo2 = new Chicken(50.29, 1, granja1);
		Chicken pollo3 = new Chicken(103.70, 4, granja2);
		Chicken pollo4 = new Chicken(123545, 6, granja3);
//		Chicken pollo1 = new Chicken(123.55, 6, Long.getLong("1") );
//		Chicken pollo2 = new Chicken(148.29, 5, Long.getLong("1") );
//		Chicken pollo3 = new Chicken(322, 2, Long.getLong("2") );
//	
		FarmRepository farmRepo = context.getBean(FarmRepository.class);
		farmRepo.save(granja1);
		farmRepo.save(granja2);
		farmRepo.save(granja3);

		EggRepository eggRepo = context.getBean(EggRepository.class);
		eggRepo.save(huevo1);
		eggRepo.save(huevo2);
		eggRepo.save(huevo3);
		eggRepo.save(huevo4);

		ChickenRepository chickenRepo = context.getBean(ChickenRepository.class);
		chickenRepo.save(pollo1);
		chickenRepo.save(pollo2);
		chickenRepo.save(pollo3);
		chickenRepo.save(pollo4);

	}

}
