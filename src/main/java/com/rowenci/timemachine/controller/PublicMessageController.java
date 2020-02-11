package com.rowenci.timemachine.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rowenci.timemachine.entity.Message;
import com.rowenci.timemachine.entity.PublicArea;
import com.rowenci.timemachine.entity.PublicMessage;
import com.rowenci.timemachine.service.IMessageService;
import com.rowenci.timemachine.service.IPublicMessageService;
import com.rowenci.timemachine.util.CodeInfo.ServiceCodeInfo;
import com.rowenci.timemachine.util.CreateUUID;
import com.rowenci.timemachine.util.QrCodeUtil;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.MinioException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author rowenci
 * @since 2020-02-07
 */
@RestController
@RequestMapping("/timemachine/public-message")
public class PublicMessageController {

    @Resource
    private ServiceCodeInfo serviceCodeInfo;

    @Resource
    private IPublicMessageService iPublicMessageService;

    @Resource
    private IMessageService iMessageService;

    /**
     * 添加公共信件
     *
     * @param publicMessage
     * @return
     */
    @PostMapping("/")
    public String addPublicMessage(PublicMessage publicMessage) {

        ModelMap modelMap = new ModelMap();

        //获取uuid
        CreateUUID createUUID = new CreateUUID();
        String publicMessageId = createUUID.create();
        publicMessage.setPublicMessageId(publicMessageId);

        boolean res = iPublicMessageService.save(publicMessage);

        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "添加成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.ADD_PUBLIC_MESSAGE_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "添加失败");
        }
        return JSON.toJSONString(modelMap);

    }

    /**
     * 根据用户id获取用户公共信件的列表
     *
     * @param userId
     * @param limit
     * @param page
     * @return
     */
    @GetMapping("/")
    public String getPublicMessage(String userId, int limit, int page) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("user_id", userId);
        qw.notIn("banned", 1);
        IPage<PublicMessage> publicMessageIPage = new Page<>(page, limit);
        int count = iPublicMessageService.count(qw);
        List<PublicMessage> publicMessageList = iPublicMessageService.page(publicMessageIPage, qw).getRecords();

        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < publicMessageList.size(); i++) {
            //公共信件被ban
            if (publicMessageList.get(i).getBanned() == 1) {
                //跳出循环
                continue;
            }
            String messageId = publicMessageList.get(i).getMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);
            Message message = iMessageService.getOne(qwm);
            messageList.add(message);
        }

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "根据userid查找publicmessage成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);

    }

    /**
     * 获取公共区域的信件
     *
     * @param limit
     * @param page
     * @return
     */
    @GetMapping("/public_area")
    public String publicArea(int limit, int page) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.orderByDesc("good_number", "favorite_number");
        qw.notIn("banned", 1);
        IPage<PublicMessage> publicMessageIPage = new Page<>(page, limit);
        int count = iPublicMessageService.count(qw);
        List<PublicMessage> publicMessageList = iPublicMessageService.page(publicMessageIPage, qw).getRecords();

        //获取信件信息
        List<PublicArea> messageList = new ArrayList<>();
        for (int i = 0; i < publicMessageList.size(); i++) {

            //公共信件被ban
            if (publicMessageList.get(i).getBanned() == 1) {
                //跳出循环
                continue;
            }

            String messageId = publicMessageList.get(i).getMessageId();
            QueryWrapper qwm = new QueryWrapper();
            qwm.eq("message_id", messageId);

            Message message = iMessageService.getOne(qwm);

            PublicArea publicArea = new PublicArea();
            publicArea.setId(message.getId());
            publicArea.setUserId(message.getUserId());
            publicArea.setMessageId(messageId);
            publicArea.setTitle(message.getTitle());
            publicArea.setContext(message.getContext());
            publicArea.setWriteTime(message.getWriteTime());
            publicArea.setSendTime(message.getSendTime());
            publicArea.setGood_number(publicMessageList.get(i).getGoodNumber());
            publicArea.setFavorite_number(publicMessageList.get(i).getFavoriteNumber());
            publicArea.setIsSend(message.getIsSend());
            messageList.add(publicArea);
        }

        modelMap.addAttribute("code", serviceCodeInfo.LAYUI_SUCCESS);
        modelMap.addAttribute("msg", "生成public_area成功");
        modelMap.addAttribute("count", count);
        modelMap.addAttribute("data", messageList);
        return JSON.toJSONString(modelMap);
    }

    /**
     * 获取点赞数
     *
     * @param messageId
     * @return
     */
    @GetMapping("/getGoodNumber")
    public String getGoodNumber(String messageId) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        PublicMessage publicMessage = iPublicMessageService.getOne(qw);
        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", publicMessage.getGoodNumber());
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "获取点赞数成功");
        return JSON.toJSONString(modelMap);
    }

    /**
     * 获取收藏数
     *
     * @param messageId
     * @return
     */
    @GetMapping("/getFavoriteNumber")
    public String getFavoriteNumber(String messageId) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        PublicMessage publicMessage = iPublicMessageService.getOne(qw);
        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", publicMessage.getFavoriteNumber());
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "获取收藏数成功");
        return JSON.toJSONString(modelMap);
    }

    /**
     * 更改点赞数
     *
     * @param messageId
     * @param goodNumber
     * @return
     */
    @PutMapping("/goodNumber")
    public String updateGoodNumber(String messageId, int goodNumber) {
        ModelMap modelMap = new ModelMap();
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("message_id", messageId);
        uw.set("good_number", goodNumber);

        boolean res = iPublicMessageService.update(uw);
        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "修改成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.UPDATE_GOODNUMBER_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "修改失败");
        }
        return JSON.toJSONString(modelMap);
    }

    /**
     * 更改收藏数
     *
     * @param messageId
     * @param favoriteNumber
     * @return
     */
    @PutMapping("/favoriteNumber")
    public String updateFavoriteNumber(String messageId, int favoriteNumber) {
        ModelMap modelMap = new ModelMap();
        UpdateWrapper uw = new UpdateWrapper();
        uw.eq("message_id", messageId);
        uw.set("favorite_number", favoriteNumber);

        boolean res = iPublicMessageService.update(uw);
        if (res) {
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "修改成功");
        } else {
            modelMap.addAttribute("code", serviceCodeInfo.UPDATE_FAVORITE_NUMBER_ERROR);
            modelMap.addAttribute("data", "");
            modelMap.addAttribute("result", "error");
            modelMap.addAttribute("description", "修改失败");
        }
        return JSON.toJSONString(modelMap);
    }

    @GetMapping("/checkBlack")
    public String checkBlack(String messageId) {
        ModelMap modelMap = new ModelMap();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("message_id", messageId);
        PublicMessage publicMessage = iPublicMessageService.getOne(qw);
        modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
        modelMap.addAttribute("data", publicMessage.getBanned());
        modelMap.addAttribute("result", "success");
        modelMap.addAttribute("description", "获取信件黑名单状态成功");
        return JSON.toJSONString(modelMap);
    }

    /**
     * 分享信件
     * 先去minio服务器看是否有已经生成的二维码
     * 如果没有就生成并上传再返回二维码的url
     * 如果有就直接返回二维码的url
     * minio服务器里的文件名称是信件id
     * @param messageId
     * @return
     */
    @GetMapping("/share")
    public String share(String messageId) {
        ModelMap modelMap = new ModelMap();
        String filename = messageId + ".jpg";

        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.1.21:9001", "minio", "minio123");
            ObjectStat obj = minioClient.statObject("qrcode", filename);
            //二维码已经存在
            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("src", "http://192.168.1.21:9001/qrcode/" + filename);
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "获取二维码成功");
            return JSON.toJSONString(modelMap);
        } catch (Exception e) {
            //二维码不存在
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
            } catch (UnknownHostException ee) {
                ee.printStackTrace();
            }
            String host = "http://" + address.getHostAddress() + ":8080";

            String url = host + "/share/shareMessage?messageId=" + messageId;

            String dirPath = System.getProperty("user.dir");
            String path = File.separator + "uploadImg" + File.separator;

            try {
                QrCodeUtil.encode(url, dirPath + path, messageId, true);
                FileInputStream inputStream = new FileInputStream(new File(dirPath + path + filename));
                File file = new File(dirPath + path + filename);
                //上传文件至Minio
                try {
                    MinioClient minioClient = new MinioClient("http://192.168.1.21:9001", "minio", "minio123");
                    // 检查存储桶是否已经存在
                    boolean isExist = minioClient.bucketExists("qrcode");
                    if (isExist) {
                    } else {
                        // 创建一个名为timemachine的存储桶
                        minioClient.makeBucket("qrcode");
                    }

                    // 使用putObject上传一个文件到存储桶中。
                    minioClient.putObject("qrcode", filename, inputStream, file.length(), "img");
                    inputStream.close();
                    minioClient.statObject("qrcode", filename);

                    try {

                        ObjectStat obj1 = minioClient.statObject("qrcode", filename);

                    } catch (Exception ee) {

                        modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
                        modelMap.addAttribute("data", "");
                        modelMap.addAttribute("result", "error");
                        modelMap.addAttribute("description", "获取二维码失败");
                        return JSON.toJSONString(modelMap);

                    }

                } catch (MinioException ee) {

                    //上传文件到Minio失败
                    System.out.println("Error occurred: " + e);
                    inputStream.close();

                    modelMap.addAttribute("code", serviceCodeInfo.UPLOAD_ERROR);
                    modelMap.addAttribute("data", "");
                    modelMap.addAttribute("result", "error");
                    modelMap.addAttribute("description", "获取二维码失败");

                    return JSON.toJSONString(modelMap);
                }
                //上传文件到Minio成功
                //删除临时文件
                File tempFile = new File(dirPath + path + filename);
                tempFile.delete();
            } catch (Exception ee) {
                e.printStackTrace();
            }

            modelMap.addAttribute("code", serviceCodeInfo.SUCCESS);
            modelMap.addAttribute("src", "http://192.168.1.21:9001/qrcode/" + filename);
            modelMap.addAttribute("result", "success");
            modelMap.addAttribute("description", "获取二维码成功");
            return JSON.toJSONString(modelMap);
        }

    }
}
