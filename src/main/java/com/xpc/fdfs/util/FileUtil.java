package com.xpc.fdfs.util;

import com.xpc.fdfs.client.FastDFSClient;
import com.xpc.fdfs.entity.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String upload(MultipartFile multipartFile) throws IOException {
        String fileAbsolutePath[] = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] buffer = null;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            if (inputStream != null) {
                int len = inputStream.available();
                buffer = new byte[len];
                inputStream.read(buffer);
            }
            FastDFSFile fastDFSFile = new FastDFSFile(fileName, buffer, ext);
            fileAbsolutePath = FastDFSClient.upload(fastDFSFile);
            if (fileAbsolutePath == null) {
                logger.error("upload file fail! please try again");
            }
        } catch (Exception e) {
            logger.error("upload file Exception:", e);
        } finally {
            inputStream.close();
        }
        String path = FastDFSClient.getTrackerUrl() + "/" + fileAbsolutePath[0] + "/" +fileAbsolutePath[1];//获取上传后的文件的访问全路径
        return path;
    }
}
