package com.monk.app.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @ClassName FileController
 * @Description: TODO
 * @Author Monk
 * @Date 2020/4/5
 * @Version V1.0
 **/
@RestController
@RequestMapping("/file")
public class FileController {

    private final static String file_folder = "D:\\Repository\\github\\MinMonk\\PublicRepo\\StudyDemo\\spring-security-demo\\security-demo\\src\\main\\java\\com\\monk\\security\\controller";

    public static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @PostMapping
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        logger.info("------------" + multipartFile);
        File file = new File(file_folder, System.currentTimeMillis() + ".txt");
        multipartFile.transferTo(file);
        return file.getAbsolutePath();
    }

    @GetMapping("/{id:\\d+}")
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
