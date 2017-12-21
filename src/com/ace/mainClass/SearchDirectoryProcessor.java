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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import com.ace.entity.FileResult;
import com.ace.exception.SDApplicationException;
import com.ace.utilities.CreateFiles;

/**
 * @author pku160
 *
 */
public class SearchDirectoryProcessor {
	private static final Logger log = Logger.getLogger(SearchDirectoryProcessor.class.getName());
	private CreateFiles cFiles=new CreateFiles();
	private static Map<String,Long>  fileDetailsMap=new ConcurrentHashMap<String,Long>();

	/**
	 * Starting Point of Application
	 */
	public static void main(String[] args) throws SDApplicationException{
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
			while (true) {
				for (WatchEvent<?> event : key.pollEvents()) {
					log.info("Event kind:" + event.kind()
							+ ". File affected: " + event.context() + ".");
					
					sc.createReport(path);
				}
				boolean valid = key.reset();
			    if (!valid) {
			        break;
			    }
			}
		} catch (InterruptedException | IOException e) {
			log.log(Level.WARNING, "Error Occurred "+e.getMessage());
			throw new SDApplicationException("Error Occurred "+e.getMessage());
		}

	}
	
	
	/**
	 * createMapData It will create map entry for all existing csv and txt files
	 */
	private void createMapData(Path path) throws IOException {

		for (File f : path.toFile().listFiles()) {
			if (FilenameUtils.isExtension(f.getName(), "txt")
					|| FilenameUtils.isExtension(f.getName(), "csv")) {
				createCommonFile(f,path);
			}

		}

	}


	/**
	 * createMapData It will create report for new  csv and txt files
	 */
	public void createReport(Path path) throws IOException, SDApplicationException{
		for (File f : path.toFile().listFiles()) {
			if(FilenameUtils.isExtension(f.getName(),"txt") || FilenameUtils.isExtension(f.getName(),"csv")){
				if((fileDetailsMap.containsKey(f.toPath().toString()) && !fileDetailsMap.get(f.toPath().toString()).equals(f.lastModified())) || (!fileDetailsMap.containsKey(f.toPath().toString()))){
					createCommonFile(f,path);				}
		  }
		}
		
	}
	
	public void createCommonFile(File f,Path path){
		ExecutorService service = Executors.newFixedThreadPool(Runtime
				.getRuntime().availableProcessors());
		service.submit(new Runnable() {
			public void run() {
				fileDetailsMap.put(f.toPath().toString(), new Long (f.lastModified()));
		 		 
					try {
						cFiles.createmtdFile(f);
						List<FileResult> listOfFileResult=cFiles.createdmtdFile(path);
						cFiles.createsmtdFile(listOfFileResult);
					} catch (IOException | SDApplicationException e) {
						log.log(Level.WARNING, " IO Exception "+e.getMessage());
					}
				
			}
		});
		service.shutdown();
	}
	
	
}
