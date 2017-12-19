package com.ace.utilities;

import java.util.Comparator;

import com.ace.entity.FileResult;

public class FileResultSortingSplChar implements Comparator<FileResult>{

	@Override
	public int compare(FileResult o1, FileResult o2) {
		return (int) (o1.getSpecialCharacterCount()-o2.getSpecialCharacterCount());
	}

}
