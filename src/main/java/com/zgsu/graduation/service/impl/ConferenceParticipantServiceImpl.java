package com.zgsu.graduation.service.impl;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.mapper.ConferenceParticipantMapper;
import com.zgsu.graduation.mapper.UserInfoMapper;
import com.zgsu.graduation.model.ConferenceParticipant;
import com.zgsu.graduation.service.ConferenceParticipantService;
import com.zgsu.graduation.utils.AppPushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ConferenceParticipantServiceImpl implements ConferenceParticipantService {
    @Autowired
    private ConferenceParticipantMapper conferenceParticipantMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public ErrorEnum inviteParticipants(Integer conconferenceId,Integer initiatorId, List<Integer> participants) {
        List<String> cidList = new ArrayList<>();
        List<String> phoneList = new ArrayList<>();
        for (Integer participant : participants) {
            Map<String, Object> cidAndPhone = userInfoMapper.showCidAndPhone(participant);
            cidList.add(cidAndPhone.get("cid").toString());
            phoneList.add(cidAndPhone.get("phone").toString());
//            for(String key:cidAndPhone.keySet()){
//                if(key.equals("phone"))
//                    phoneList.add(cidAndPhone.get(key).toString());
//                else{
//                    cidList.add(cidAndPhone.get("cid").toString());
//                }
//            }

            ConferenceParticipant conferenceParticipant = new ConferenceParticipant();
            conferenceParticipant.setConferenceId(conconferenceId);
            conferenceParticipant.setParticipant(participant);
            int result = conferenceParticipantMapper.insertSelective(conferenceParticipant);
            if (result == 0) {
                return ErrorEnum.FAILURE;
            }
        }
        //单独在参与表中插入会议发起者
        ConferenceParticipant conferenceParticipant = new ConferenceParticipant();
        conferenceParticipant.setConferenceId(conconferenceId);
        conferenceParticipant.setParticipant(initiatorId);
        int result = conferenceParticipantMapper.insertSelective(conferenceParticipant);
//        GoEasy goEasy=new GoEasy("http://rest-hangzhou.goeasy.io/publish","BC-fd94510bceb2485fa37fe7caaeb06b27");
//        goEasy.publish(Integer.toString(conconferenceId),"邀请你参与会议");
        //获取IGtPush对象
        IGtPush push = AppPushUtil.getPush();
        //获取模板对象
        NotificationTemplate template = AppPushUtil.getTemplate();
        //获取会议发起者姓名
        String name=userInfoMapper.showUserNameById(initiatorId);
        //获取模板样式
        Style0 style = AppPushUtil.getStyle(name+"发来一个会议邀请");
        template.setStyle(style);
        IPushResult ret=AppPushUtil.listPush(push, template, cidList);
        if(ret!=null)
            System.out.println(ret.getResponse().toString());
        return ErrorEnum.SUCCESS;
    }

    @Override
    public List<Integer> selectByConferenceId(Integer conferenceId) {
        return conferenceParticipantMapper.selectByConferenceId(conferenceId);
    }
}

