package com.zgsu.graduation;

import com.zgsu.graduation.service.ConferenceAppointmentService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConferenceAppointmentTest extends GraduationApplicationTests {
    @Autowired
    private ConferenceAppointmentService conferenceAppointmentService;

    @Test
    public void testAvgDelay() {
        Assert.assertSame(1,conferenceAppointmentService.avgDelay(12));
    }
}
