package com.xujialin.Controller;

import com.xujialin.CommonResult.ReturnResult;
import com.xujialin.Utils.UUidUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author XuJiaLin
 * @date 2021/8/7 10:31
 * 图片上传接口
 */
@RestController
public class ImageUploadController {

    @PostMapping("/uploadImage")
    public String uploadImage(MultipartFile file){
        String filename=null;
        boolean result=false;
        //获取链接对象
        FTPClient ftpClient = new FTPClient();
        int reply;
        try {
            //连接ftp服务器
            ftpClient.connect("ip地址",21);
            //登录ftp
            ftpClient.login("username","pasword");
            //返回登录的状态码
            reply = ftpClient.getReplyCode();
            //判断状态码是否正常,连接失败则断开连接
            if(!FTPReply.isPositiveCompletion(reply)){
                ftpClient.disconnect();
                return "error";
            }
            //获取字节流
            InputStream inputStream = file.getInputStream();
            //生成文件名
            filename=UUidUtils.generateuuid()+ file.getOriginalFilename();
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
            //设置为二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //上传文件
            result= ftpClient.storeFile(filename, inputStream);
            //关闭字节流
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "http://ip地址:8090/"+filename;
    }
}
