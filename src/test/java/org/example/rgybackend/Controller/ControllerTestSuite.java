package org.example.rgybackend.Controller;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    UserControllerTest.class,
    PushContentControllerTest.class,
    EmotionControllerTest.class,
    CounselingControllerTest.class,
    ChatControllerTest.class
})
public class ControllerTestSuite {
    
}