package com.zgsu.graduation.service;

import com.zgsu.graduation.enums.ErrorEnum;
import org.springframework.stereotype.Service;


public interface FaceService {
    //会议发起者签退
    ErrorEnum signOff(Integer userId,Integer conferenceId);

}
