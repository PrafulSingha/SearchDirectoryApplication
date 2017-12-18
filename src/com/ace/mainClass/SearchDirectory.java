/**
 * 
 */
package com.ace.mainClass;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

/**
 * @author pku160
 *
 */
public class SearchDirectory {
	private static final Logger log = Logger.getLogger(SearchDirectory.class.getName());
	
	private long wordCount = 0;
	private long letterCount = 0;
	private long vowelsCount = 0;
	private long specialCharacterCount = 0;
	private Map<String,Long> fileDetails=new HashMap<String,Long>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SearchDirectory sc=new SearchDirectory();
			WatchService watchService = FileSystems.getDefault()
					.newWatchService();
			
			Path path = Paths.get("C:\\New folder");

			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			SearchDirectory sDirectory=new SearchDirectory();
			sDirectory.createMapData(path);
			
			WatchKey key= watchService.poll(2, TimeUnit.SECONDS);
			sc.createReport(path);
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					System.out.println("Event kind:" + event.kind()
							+ ". File affected: " + event.context() + ".");	
					sc.createReport(path);
				}
				key.reset();
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	private  void createMapData(Path path) throws IOException {
		for (File f : path.toFile().listFiles()) {
			if(FilenameUtils.isExtension(f.getName(),"txt") || FilenameUtils.isExtension(f.getName(),"csv")){
		 		  fileDetails.put(f.toPath().toString(), new Long (f.lastModified()));
		 		  createmtdFile(f);
			}

		}
		
	}



	public void createReport(Path path) throws IOException{
		for (File f : path.toFile().listFiles()) {
			if(FilenameUtils.isExtension(f.getName(),"txt") || FilenameUtils.isExtension(f.getName(),"csv")){
				if((fileDetails.containsKey(f.toPath().toString()) && !fileDetails.get(f.toPath().toString()).equals(f.lastModified())) || (!fileDetails.containsKey(f.toPath().toString()))){
					fileDetails.put(f.toPath().toString(), new Long (f.lastModified()));
					createmtdFile(f);
				}
		  }
		/*for (File f : path.toFile().listFiles()) {
		 if(FilenameUtils.isExtension(f.getName(),".mtd")){
      	   Stream<String> fileLines = Files.lines(f.toPath(), Charset.defaultCharset());
      	
      	   fileLines.forEach(s -> {
      		   System.out.println(s.substring(s.lastIndexOf(" wordCount "), s.indexOf(" letterCount ")));
      	   });
      	   //Files.write(Paths.get("c:/New folder.mtd"), report.getBytes());
  	   
  	   }
		}*/
	}
	
	}
	
	public void createmtdFile(File f) throws IOException{
	   Stream<String> fileLines = Files.lines(f.toPath(), Charset.defaultCharset());
	   wordCount = fileLines.flatMap(line -> Arrays.stream(line.split(" "))).count();
 	   String report ="File Name "+f.getName()+ " wordCount "+wordCount +" letterCount "+letterCount +" vowelsCount "+vowelsCount + " specialCharacterCount "+specialCharacterCount;
 	   System.out.println(report);
 	   
 	   Files.write(Paths.get("c:/New folder/"+f.getName().substring(0, f.getName().indexOf(".")-1)+".mtd"), report.getBytes());
	}

}
