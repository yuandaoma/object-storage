package com.naver.fastdfs;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

@Component
public class FastDFSUtil {

	@Autowired
    private FastFileStorageClient fastFileStorageClient;

	@Autowired
    private FdfsWebServer fdfsWebServer;

    public String uploadFile(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), null);
            return fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] downloadFile(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        return fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
    }

    public Boolean deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(filePath);
            fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 封装文件完整 URL 地址
     *
     * @param path
     * @return
     */
    public String getResAccessUrl(String path) {
        return fdfsWebServer.getWebServerUrl() + path;
    }
}
