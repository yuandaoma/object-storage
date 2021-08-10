package com.naver.fastdfs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fdfs")
public class FdfsController {

	@Autowired
	private FastDFSUtil fastDFSUtil;

	@PostMapping("/upload")
	public ResponseEntity upload(@RequestPart(name = "file") MultipartFile file) {
		try {
			Path path = Path.of(System.getProperty("user.dir") + File.separator + file.getOriginalFilename());
			Files.deleteIfExists(path);
			File uploadFile = Files.createFile(path).toFile();
			file.transferTo(uploadFile);
			String result = fastDFSUtil.uploadFile(uploadFile);
			return new ResponseEntity(result, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity("失败", HttpStatus.EXPECTATION_FAILED);
		}

	}
}
