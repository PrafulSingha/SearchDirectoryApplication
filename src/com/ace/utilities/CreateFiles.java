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
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

import com.ace.entity.FileResult;
import com.ace.exception.SDApplicationException;

public class CreateFiles {
	private static final Logger log = Logger.getLogger(CreateFiles.class.getName());

	private FileResult fileResult;
	private String report;
	
	
	public void createmtdFile(File f) throws IOException {
		fileResult=new FileResult();
		List<String> vowels=new ArrayList<String>();
		vowels.add("a");
		vowels.add("e");
		vowels.add("i");
		vowels.add("o");
		vowels.add("u");
		List<String> spclChars=new ArrayList<String>();
		spclChars.add("@");
		spclChars.add("#");
		spclChars.add("$");
		spclChars.add("*");
		long wordCount=0,charCount=0,vowelCount=0,splcharCount=0;
		List<String> lines = Files.readAllLines(Paths.get(f.toPath().toString()),Charset.defaultCharset());
		for(String line:lines){
			wordCount=wordCount+line.split(" ").length;
			for(String word:line.split(" ")){
				charCount=charCount+word.length();
				for(Character ch:word.toCharArray()){
					if(vowels.contains(ch.toString().toLowerCase())){
						vowelCount=vowelCount+1;
					}
					if(spclChars.contains(ch.toString())){
						splcharCount=splcharCount+1;
					}
					
				}
			}
		}
		
		fileResult.setWordCount(wordCount);
		fileResult.setLetterCount(charCount);
		fileResult.setVowelsCount(vowelCount);
		fileResult.setSpecialCharacterCount(splcharCount);
		fileResult.setFileName(f.getName());
		
		report = " WordCount : "
				+ fileResult.getWordCount() + "\n LetterCount : "
				+ fileResult.getLetterCount() + "\n VowelsCount : "
				+ fileResult.getVowelsCount() + "\n SpecialCharacterCount : "
				+ fileResult.getSpecialCharacterCount();
		System.out.println(report);
		
		Files.write(Paths.get("c:/New folder/"
				+ f.getName().substring(0, f.getName().indexOf("."))
				+ ".mtd"), report.getBytes());
	}
	
	public List<FileResult> createdmtdFile(Path path) throws IOException {
		List<FileResult> listOfData=new ArrayList<FileResult>();
		long mtdWordCount=0,mtdCharCount=0,mtdVowelCount=0,mtdSplCount=0;
		for (File f : path.toFile().listFiles()) {
			if (FilenameUtils.isExtension(f.getName(), "mtd")) {
				List<String> lines = Files.readAllLines(Paths.get(f.toPath().toString()),Charset.defaultCharset());
				fileResult=new FileResult();
				fileResult.setFileName(f.getName());
				for(String line:lines){
					if(line.split(" : ")[0].contains("WordCount")){
						fileResult.setWordCount(Long.valueOf(line.split(" : ")[1]));
						mtdWordCount=mtdWordCount+Long.valueOf(line.split(" : ")[1]);
					}
					if(line.split(" : ")[0].contains("LetterCount")){
						fileResult.setLetterCount(Long.valueOf(line.split(" : ")[1]));
						mtdCharCount=mtdCharCount+Long.valueOf(line.split(" : ")[1]);
					}
					if(line.split(" : ")[0].contains("VowelsCount")){
						fileResult.setVowelsCount(Long.valueOf(line.split(" : ")[1]));
						mtdVowelCount=mtdVowelCount+Long.valueOf(line.split(" : ")[1]);
					}
					if(line.split(" : ")[0].contains("SpecialCharacterCount")){
						fileResult.setSpecialCharacterCount(Long.valueOf(line.split(" : ")[1]));
						mtdSplCount=mtdSplCount+Long.valueOf(line.split(" : ")[1]);
					}
				}
				listOfData.add(fileResult);
				
			}
			report =" Total Word count "+mtdWordCount+" \n Total Letter count "+mtdCharCount+" \n Total Vowels count "+mtdVowelCount+" \n Total SpecialCharacter count "+mtdSplCount;

		}
		Files.write(Paths.get("c://New folder//New folder.dmtd"), report.getBytes());
		return listOfData;
	}

	public void createsmtdFile(List<FileResult> countList) throws SDApplicationException {
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
			if(prop.getProperty("SORT_ON").contains("LETTER")){
				Collections.sort(countList, new FileResultSortingChar());
			}
			if(prop.getProperty("SORT_ON").contains("VOWELS")){
				Collections.sort(countList, new FileResultSortingVowels());
			}
			if(prop.getProperty("SORT_ON").contains("SPLCHAR")){
				Collections.sort(countList, new FileResultSortingSplChar());
			}
			if (file.createNewFile()) {
				log.info("File is created!");
			} else {
				log.info("File already exists.");
			}
			
			FileWriter writer = new FileWriter(file);
			
			for(FileResult fileResult:countList){
				if(FilenameUtils.isExtension(fileResult.getFileName(), "mtd")){
					report=report+" File Name "+fileResult.getFileName() +" Number Of "+prop.getProperty("SORT_ON")+" " +fileResult.getWordCount() +"\n";
				}
				
			}
			writer.write(report);
			writer.close();
		} catch (IOException e) {
			log.log(Level.WARNING, "Error Occurred while creating files "+e.getMessage());
			throw new SDApplicationException("Error Occurred while creating files "+e.getMessage());
		}
		
		
	}

}
