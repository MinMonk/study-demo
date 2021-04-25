package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/file")
@Api(value = "文件上传下载测试服务")
public class FileController {

    private final static String file_folder = "D:\\apps\\lightcsb\\resource\\restDemo\\tempUploadFile";

    public static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping
    @ApiOperation(value = "上传文件")
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        logger.info("------------" + multipartFile);

        File targetFileParentDir = new File(file_folder);
        if(!targetFileParentDir.exists()) {
            targetFileParentDir.mkdirs();
        }

        File file = new File(file_folder, multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "下载文件")
    public void downloadFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        try (InputStream inputStream = new FileInputStream(new File(file_folder, id + ".txt"));
             OutputStream outputStream = response.getOutputStream();) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
