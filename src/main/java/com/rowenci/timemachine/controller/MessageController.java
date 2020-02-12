package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IPublicMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-07
 */
@RestController
@RequestMapping("/timemachine/message")
@Slf4j
public class MessageController {

    @Resource
    private IMessageService iMessageService;

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Value("${spring.minio.url}")
    private String minioUrl;

    @Value("${spring.minio.accessKey}")
    private String accessKey;

    @Value("${spring.minio.secretKey}")
    private String secretKey;

    /**
     * 添加信件
     * @param message
     * @return
     */
    @PostMapping("/")
    public String addMessage(Message message) {
        ModelMap model = new ModelMap();

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String messageId = createUUID.create();
        String publicMessageId = createUUID.create();
        message.setMessageId(messageId);
        boolean res = iMessageService.save(message);

        if (res){
            if (message.getIsPublic() == 1){
                //添加公共信件
                PublicMessage publicMessage = new PublicMessage();
                publicMessage.setUserId(message.getUserId());
                publicMessage.setMessageId(message.getMessageId());
                publicMessage.setPublicMessageId(publicMessageId);
                boolean res1 = iPublicMessageService.save(publicMessage);
                if (res1){
                    model.addAttribute("code", serviceCodeInfo.SUCCESS);
                    model.addAttribute("data", "");
                    model.addAttribute("result", "success");
                    model.addAttribute("description", "添加信件成功");
                }else {
                    model.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_ERROR);
                    model.addAttribute("data", "");
                    model.addAttribute("result", "error");
                    model.addAttribute("description", "添加信件失败");
                }
            }else {
                model.addAttribute("code", serviceCodeInfo.SUCCESS);
                model.addAttribute("data", "");
                model.addAttribute("result", "success");
                model.addAttribute("description", "添加信件成功");
            }
        }else {
            model.addAttribute("code", serviceCodeInfo.ADD_MESSAGE_ERROR);
            model.addAttribute("data", "");
            model.addAttribute("result", "error");
            model.addAttribute("description", "添加信件失败");
        }
        return JSON.toJSONString(model);
    }

    /**
     * 上传图片
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws XmlPullParserException
     */
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {

        ModelMap modelMap = new ModelMap();
        CreateUUID createUUID = new CreateUUID();

        //判断文件后缀 不在下面列表中的无法上传
        List<String> imageType = new ArrayList<>();
        imageType.add("jpg");
        imageType.add("jpeg");
        imageType.add("png");
        imageType.add("bmp");
        imageType.add("gif");
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        //新文件名称（UUID）
        String newFileName = createUUID.create() + "." + fileSuffix;
        //文件地址
        String dirPath = System.getProperty("user.dir");
        String path = File.separator + "uploadImg" + File.separator + newFileName;
        if (imageType.contains(fileSuffix)) {
            //文件类型符合要修
            File destFile = new File(dirPath + path);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(destFile);
            } catch (IOException e) {
                System.out.println(e);
            }
        }else {
            //文件类型不符合要求
            modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "上传文件失败");
            return JSON.toJSONString(modelMap);
        }

        //上传文件至服务器作为临时文件
        log.info("文件名----" + newFileName);
        log.info("文件路径----" + dirPath);
        log.info("文件路径----" + path);
        log.info("打开inputstream");
        FileInputStream inputStream = new FileInputStream(new File(dirPath + path));
        //上传文件至Minio
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(minioUrl, accessKey, secretKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("timemachine");
            if (isExist) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为timemachine的存储桶
                minioClient.makeBucket("timemachine");
            }

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("timemachine", newFileName, inputStream, file.getSize(), "img");
            inputStream.close();
            minioClient.statObject("timemachine", newFileName);
            log.info("关闭inputstream");

            try {

                ObjectStat obj = minioClient.statObject("timemachine", newFileName);
                log.info("statobject----" + obj.bucketName());
                log.info("statobject----" + obj.name());

            }catch (Exception e){

                modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
                modelMap.addAttribute("data", "");
                modelMap.addAttribute("result", "error");
                modelMap.addAttribute("description", "上传文件失败");
                return JSON.toJSONString(modelMap);

            }

        } catch (MinioException e) {

            //上传文件到Minio失败
            System.out.println("Error occurred: " + e);
            inputStream.close();

            modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "上传文件失败");

            return JSON.toJSONString(modelMap);
        }

        log.info("上传文件到Minio成功");
        //上传文件到Minio成功
        //删除临时文件
        File tempFile = new File(dirPath + path);
        tempFile.delete();

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("src", minioUrl + "/timemachine/" + newFileName);
        infoMap.put("title", newFileName);
        modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_MESSAGE_PIC_SUCCESS);
        modelMap.addAttribute("msg", "上传文件成功");
        modelMap.addAttribute("data", infoMap);
        return JSON.toJSONString(modelMap);

    }

    /**
     * 根据用户id查找用户拥有的所有信件
     * @param userId
     * @return
     */
    @GetMapping("/")
    public String getMessageListByUserId(String userId, int limit, int page){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        IPage<Message> messageIPage = new Page<>(page, limit);
        int count = iMessageService.count(qw);
        List<Message> messageList = iMessageService.page(messageIPage, qw).getRecords();

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "查找成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);
    }

    /**
     * 根据信件id获取信件信息
     * @param messageId
     * @return
     */
    @GetMapping("/findById")
    public String getMessageByMessageId(String messageId){
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        Message message = iMessageService.getOne(qw);

        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", message);
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "查找信件成功");
        return JSON.toJSONString(modelMap);
    }

}
