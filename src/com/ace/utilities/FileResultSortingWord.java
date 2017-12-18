package com.ace.utilities;

import java.util.Comparator;

import com.ace.entity.FileResult;

public class FileResultSortingWord implements Comparator<FileResult>{

	@Override
	public int compare(FileResult o1, FileResult o2) {
		return (int) (o1.getWordCount()-o2.getWordCount());
	}

}
