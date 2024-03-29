package com.thanhtu.myAPI.helper;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {
	public static List<String> listFilesUsingJavaIO(String dir) {
		return Stream.of(new File(dir).listFiles()).filter(file -> !file.isDirectory()).map(File::getName)
				.filter(x -> x.contains(".json")).collect(Collectors.toList());
	}

	public static void writeFileJavaClass(String fileName, List<String> contents, List<String> listGetSet,
			StringBuilder result) {
		try {
			FileWriter fw = new FileWriter(fileName);
			result.append("<strong>" + fileName.toUpperCase() + "</strong><br>");
			for (String str : contents) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			fw.write(System.lineSeparator());
			for (String str : listGetSet) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			result.append("======================================================================================<br>");
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		System.out.println("Success...");
	}

	public static void writeFileJavaClassAndUpperCase(String fileName, List<String> contents,
			List<String> upperCaseContents, List<String> listGetSet, List<String> listGetSetUppercase,
			StringBuilder result) {
		try {
			FileWriter fw = new FileWriter(fileName);
			result.append("<strong>" + fileName.toUpperCase() + "</strong><br>");
			for (String str : contents) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			fw.write(System.lineSeparator());
			for (String str : upperCaseContents) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			for (String str : listGetSet) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			for (String str : listGetSetUppercase) {
				fw.write(str + System.lineSeparator());
				result.append(str + "<br>");
			}
			result.append("======================================================================================<br>");
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		System.out.println("Success...");
	}

	public static String readFileAsString(String fileName) throws Exception {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		return data;
	}
}
