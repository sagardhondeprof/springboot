package com.springboot.util;

import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {
	public static boolean checkExcelFormat(MultipartFile file)
	{
		String contentType = file.getContentType();
		return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			
	}
}
