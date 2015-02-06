package com.lbg.file;

import java.io.File;
import java.io.FileNotFoundException;

public interface SearchPath {
	File search(String fileName) throws FileNotFoundException;
}
