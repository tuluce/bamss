package net.bamss.bamss.controllers;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.bamss.bamss.models.Greeting;
import net.bamss.bamss.configurations.MongoConfiguration;

@RestController
public class DbtestController {
	
	private static final ApplicationContext context = new AnnotationConfigApplicationContext(MongoConfiguration.class);
	private static final MongoDatabase db = (MongoDatabase) context.getBean("db");
	
	@GetMapping("/dbtest")
	public Greeting dbtest(@RequestParam(value = "name", defaultValue = "world") String name) {
    List<Document> result = new ArrayList<Document>();
		MongoCollection<Document> collection = db.getCollection("songs");
		FindIterable<Document> records = collection.find();
		MongoCursor<Document> iterator = records.iterator();
		while (iterator.hasNext()) {
      Document doc = iterator.next();
      System.out.println(doc);
      result.add(doc);
		}
		return new Greeting(1, result.toString());
	}
}
