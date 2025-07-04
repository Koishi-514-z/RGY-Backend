package org.example.rgybackend.Utils;

import org.example.rgybackend.Model.NotificationPrivateModel;

public class NotificationUtil {
    public final static NotificationPrivateModel psyAssist = new NotificationPrivateModel(
        null,
        41L,
        null,
        null,
        "心理关怀提醒",
        "我们注意到您最近的情绪状态不太好，如果您感到压力或困扰，可以尝试：\n• 与信任的朋友或家人交流\n• 适当运动或户外活动\n• 寻求专业心理咨询\n\n心理援助热线：400-161-9995",
        TimeUtil.now(),
        1L,
        "medium"
    );

    public final static NotificationPrivateModel crisis = new NotificationPrivateModel(
        null,
        42L,
        null,
        null,
        "紧急心理援助",
        "您的安全对我们很重要！如果您正在经历痛苦或有轻生念头，请立即寻求帮助：\n\n🚨 紧急求助电话：\n• 全国危机干预热线：400-161-9995\n• 北京危机干预热线：400-161-9995\n• 上海心理援助热线：021-34289888\n\n请记住：您并不孤单，总有人愿意倾听和帮助您！",
        TimeUtil.now(),
        1L,
        "high"
    );


}
