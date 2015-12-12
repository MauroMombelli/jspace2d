package jspace2d.blueprint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class BlueprintFactory {

	static private final File folder = new File("blueprint");
	static private Map<String, Blueprint> map = new HashMap<>();
	static private final ObjectMapper jsonMapper = new ObjectMapper()
			.enableDefaultTyping()
			.enable(SerializationFeature.INDENT_OUTPUT)
			.setVisibility(PropertyAccessor.ALL, Visibility.NONE)
			.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
            ;
	
	static {
		
	}
	 

	static public Blueprint get(String name) throws FileNotFoundException, IOException{
		Blueprint blueprint = map.get(name);
		if (blueprint == null){
			blueprint = load(name);
		}
		return blueprint;
	}

	static private  Blueprint load(String name) throws FileNotFoundException, IOException {
		final File assetFile = new File(folder, name);
		
		try (FileReader f = new FileReader(assetFile)){
			final Blueprint result = jsonMapper.readValue(f, Blueprint.class);
			map.put( name, result );
			
			return result;
		}
	}
	
	static public void save(String name, Blueprint b) throws IOException{
		final File assetFile = new File(folder, name);
		
		final String result = jsonMapper.writeValueAsString(b);
		
		try (FileWriter f = new FileWriter(assetFile)){
			f.write(result);
		}
	}
}
