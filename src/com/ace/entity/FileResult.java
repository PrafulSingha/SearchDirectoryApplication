package com.ace.entity;

public class FileResult {
	
	private String fileName;
	private long wordCount = 0;
	private long letterCount = 0;
	private long vowelsCount = 0;
	private long specialCharacterCount = 0;
	
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getWordCount() {
		return wordCount;
	}
	public void setWordCount(long wordCount) {
		this.wordCount = wordCount;
	}
	public long getLetterCount() {
		return letterCount;
	}
	public void setLetterCount(long letterCount) {
		this.letterCount = letterCount;
	}
	public long getVowelsCount() {
		return vowelsCount;
	}
	public void setVowelsCount(long vowelsCount) {
		this.vowelsCount = vowelsCount;
	}
	public long getSpecialCharacterCount() {
		return specialCharacterCount;
	}
	public void setSpecialCharacterCount(long specialCharacterCount) {
		this.specialCharacterCount = specialCharacterCount;
	}
	public FileResult() {
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (int) (letterCount ^ (letterCount >>> 32));
		result = prime
				* result
				+ (int) (specialCharacterCount ^ (specialCharacterCount >>> 32));
		result = prime * result + (int) (vowelsCount ^ (vowelsCount >>> 32));
		result = prime * result + (int) (wordCount ^ (wordCount >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileResult other = (FileResult) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (letterCount != other.letterCount)
			return false;
		if (specialCharacterCount != other.specialCharacterCount)
			return false;
		if (vowelsCount != other.vowelsCount)
			return false;
		if (wordCount != other.wordCount)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FileResult [fileName=" + fileName + ", wordCount=" + wordCount
				+ ", letterCount=" + letterCount + ", vowelsCount="
				+ vowelsCount + ", specialCharacterCount="
				+ specialCharacterCount + "]";
	}
	
	
	
	

}
