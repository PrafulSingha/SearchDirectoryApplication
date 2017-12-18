/**
 * 
 */
package com.ace.mainClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

import com.ace.entity.FileResult;
import com.ace.utilities.createFiles;

/**
 * @author pku160
 *
 */
public class SearchDirectoryProcessor {
	private static final Logger log = Logger.getLogger(SearchDirectoryProcessor.class.getName());
	private createFiles cFiles=new createFiles();
	private Map<String,Long> fileDetails=new HashMap<String,Long>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SearchDirectoryProcessor sc=new SearchDirectoryProcessor();
			WatchService watchService = FileSystems.getDefault()
					.newWatchService();
			
			Path path = Paths.get("C:\\New folder");

			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			SearchDirectoryProcessor sDirectory=new SearchDirectoryProcessor();
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
		 		 cFiles.createmtdFile(f);
			}

		}
		
	}



	public void createReport(Path path) throws IOException{
		for (File f : path.toFile().listFiles()) {
			if(FilenameUtils.isExtension(f.getName(),"txt") || FilenameUtils.isExtension(f.getName(),"csv")){
				if((fileDetails.containsKey(f.toPath().toString()) && !fileDetails.get(f.toPath().toString()).equals(f.lastModified())) || (!fileDetails.containsKey(f.toPath().toString()))){
					fileDetails.put(f.toPath().toString(), new Long (f.lastModified()));
					 cFiles.createmtdFile(f);
				}
		  }
		}
		List<FileResult> listOfFileResult=cFiles.createdmtdFile(path);
		
		cFiles.createsmtdFile(listOfFileResult);
		
	}
	
	
	
	
}
