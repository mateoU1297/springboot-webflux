package com.udemy.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.udemy.springboot.webflux.app.models.dao.ProductoDao;
import com.udemy.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private final Logger log = LoggerFactory.getLogger(SpringbootWebfluxApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		
		Flux.just(new Producto("TV Panasonic LCD", 456.89),
				new Producto("Sony Camara HD", 177.89),
				new Producto("Apple iPod", 46.89)
				)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return productoDao.save(producto);
			})
		.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
	}

}
