package com.xpc.fdfs.entity;

import lombok.Data;

@Data
public class FastDFSFile {
    private String name;
    private byte[] content;
    private String ext;//扩展名
    private String md5;
    private String author;

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }
}
