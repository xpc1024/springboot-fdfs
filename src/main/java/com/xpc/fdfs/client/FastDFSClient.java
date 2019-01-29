package com.xpc.fdfs.client;

import com.xpc.fdfs.entity.FastDFSFile;
import lombok.Data;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Data
public class FastDFSClient {
    private static final Logger logger = LoggerFactory.getLogger(FastDFSClient.class);
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageClient storageClient;
    private static StorageServer storageServer;

    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
            trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!", e);
        }
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    public static String[] upload(FastDFSFile file) {
        logger.info("FileName:" + file.getName() + "FileLength:" + file.getContent().length);
        Long startTime = System.currentTimeMillis();
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("auther", file.getAuthor());
        String[] uploadResults = null;
        try {
            storageClient = new StorageClient(trackerServer, storageServer);
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (IOException e) {
            logger.error("IOException when upload  the file:" + file.getName(), e);
        } catch (Exception e) {
            logger.error("No IOException when upload the file:" + file.getName(), e);
        }
        logger.info("Upload spend time:" + (System.currentTimeMillis() - startTime) + " ms");
        if (uploadResults == null) {
            logger.error("Upload fail! errorCode:" + storageClient.getErrorCode());
        }
        logger.info("Upload successfully! Group name:" + uploadResults[0] + " remoteFileName:" + uploadResults[1]);
        return uploadResults;
    }

    public static String getTrackerUrl() {
        return trackerServer.getInetSocketAddress().getHostName();//获取服务器Ip:192.168.174.128 但这种种方式获取方式有点慢 耗时约1.2s,在单机下当然可以从conf里直接读取,但集群的话好像只能这样取...
    }
}
