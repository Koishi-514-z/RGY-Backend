package org.example.rgybackend.Controller;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    UserControllerTest.class,
    PushContentControllerTest.class,
    EmotionControllerTest.class,
    CounselingControllerTest.class,
    ChatControllerTest.class,
    NotificationControllerTest.class,
    CrisisControllerTest.class,
    BlogControllerTest.class
})
public class ControllerTestSuite {
    
}