package com.zgsu.graduation.utils;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppPushUtil {
    // STEP1：获取应用基本信息
    private static String appId = "FHtGVyffFL6V0b4Tjb1SB9";
    private static String appKey = "kSOJGMHMwi68fnQomdmOr7";
    private static String masterSecret = "DTzOcTU3rwAUEmO0y2Myb7";
    private static String url = "http://sdk.open.api.igexin.com/apiex.htm";
    private static String CID = "473873f7be2b4d74a05a968c330a44e5";

    /**
     * 初始化IGtPush对象
     * @return
     */
    public static IGtPush getPush(){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        return push;
    }

    /**
     * 设置推送标题、推送内容、响铃、震动等推送效果
     *
     * @param content 通知栏内容
     * @return
     */
    public static Style0 getStyle(String content) {
        Style0 style = new Style0();
        style.setTitle("您有一个会议邀请");
        style.setText(content);
        style.setLogo("push.png");  // 设置推送图标
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动
        return style;
    }

    /**
     * 选择通知模板
     *
     * @return
     */
    public static NotificationTemplate getTemplate() {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        return template;
    }

    /**
     * 给指定用户(cid)推送消息
     * @param push
     * @param template
     * @param cidList
     * @return
     */
    public static IPushResult listPush(IGtPush push, NotificationTemplate template, List<String> cidList) {
        ListMessage message = new ListMessage();
        message.setData(template);
        //设置消息离线，并设置离线时间
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        List targets = new ArrayList();
        for (String cid : cidList) {
            Target target = new Target();
            target.setAppId(appId);
            target.setClientId(cid);
            targets.add(target);
        }
        return push.pushMessageToList(taskId, targets);
    }

    /**
     * 给指定用户(alias)推送消息
     * @param push
     * @param aliasList
     * @param template
     * @return
     */
    public static IPushResult listPush(IGtPush push, List<String> aliasList,NotificationTemplate template) {
        ListMessage message = new ListMessage();
        message.setData(template);
        //设置消息离线，并设置离线时间
        message.setOffline(true);
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        List targets = new ArrayList();
        for (String alias : aliasList) {
            Target target = new Target();
            target.setAppId(appId);
            target.setAlias(alias);
            targets.add(target);
        }
        return push.pushMessageToList(taskId, targets);
    }

    public static void main(String[] args) throws IOException {

        IGtPush push = new IGtPush(url, appKey, masterSecret);

        Style0 style = new Style0();
        // STEP2：设置推送标题、推送内容
        style.setTitle("请输入通知栏标题test2");
        style.setText("请输入通知栏内容");
        style.setLogo("push.png");  // 设置推送图标
        // STEP3：设置响铃、震动等推送效果
        style.setRing(true);  // 设置响铃
        style.setVibrate(true);  // 设置震动


        // STEP4：选择通知模板
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setStyle(style);


        // STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
//        AppMessage message = new AppMessage();
//        message.setData(template);
//        message.setAppIdList(appIds);
//        message.setOffline(true);
//        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);

        // STEP6：执行推送
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(CID);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
        // IPushResult ret = push.pushMessageToApp(message);
        //System.out.println(ret.getResponse().toString());
    }
}
