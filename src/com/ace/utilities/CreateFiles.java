package com.ace.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

import com.ace.entity.FileResult;

public class CreateFiles {
	private FileResult fileResult;
	private String report;
	
	
	public void createmtdFile(File f) throws IOException {
		fileResult=new FileResult();
		Stream<String> fileLines = Files.lines(f.toPath(),
				Charset.defaultCharset());
		fileResult.setWordCount(fileLines.flatMap(
				line -> Arrays.stream(line.split(" "))).count());
		fileLines.close();
		report = " WordCount : "
				+ fileResult.getWordCount() + "\n LetterCount : "
				+ fileResult.getLetterCount() + "\n VowelsCount : "
				+ fileResult.getVowelsCount() + "\n SpecialCharacterCount :"
				+ fileResult.getSpecialCharacterCount();
		System.out.println(report);
		
		Files.write(Paths.get("c:/New folder/"
				+ f.getName().substring(0, f.getName().indexOf("."))
				+ ".mtd"), report.getBytes());
	}
	
	public List<FileResult> createdmtdFile(Path path) throws IOException {
		List<FileResult> listOfData=new ArrayList<FileResult>();
		int mtdWordCount=0;
		for (File f : path.toFile().listFiles()) {
			if (FilenameUtils.isExtension(f.getName(), "mtd")) {
				List<String> lines = Files.readAllLines(Paths.get(f.toPath().toString()),Charset.defaultCharset());
				fileResult=new FileResult();
				fileResult.setFileName(f.getName());
				for(String line:lines){
					if(line.split(" : ")[0].contains("WordCount")){
						fileResult.setWordCount(Long.valueOf(line.split(" : ")[1]));
						mtdWordCount=mtdWordCount+Integer.valueOf(line.split(" : ")[1]);
					}
				}
				listOfData.add(fileResult);
				
			}
			report ="Total Word count "+mtdWordCount;

		}
		Files.write(Paths.get("c://New folder//New folder.dmtd"), report.getBytes());
		return listOfData;
	}

	public void createsmtdFile(List<FileResult> countList) {
		Properties prop = new Properties();
		InputStream input = null;
		report="";
		try {
			File file = new File("c://New folder//New folder.smtd");
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
			prop.load(input);
			if(prop.getProperty("SORT_ON").contains("WORD")){
				Collections.sort(countList, new FileResultSortingWord());
			}
			
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}
			
			FileWriter writer = new FileWriter(file);
			
			for(FileResult fileResult:countList){
				if(FilenameUtils.isExtension(fileResult.getFileName(), "mtd")){
					report=report+" File Name "+fileResult.getFileName() +" Number Of Word "+fileResult.getWordCount() +"\n";
				}
				
			}
			writer.write(report);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
