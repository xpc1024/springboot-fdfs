package com.xpc.fdfs.controller;

import com.xpc.fdfs.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
public class FileUploadController {
    @RequestMapping("")
    public String index() {
        return "index.html";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()){
            return "空的你上传个屁啊";
        }
        return FileUtil.upload(file);
    }
}
