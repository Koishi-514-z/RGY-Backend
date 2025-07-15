package org.example.rgybackend.Utils;

import org.example.rgybackend.Model.NotificationPrivateModel;

public class NotificationUtil {
    /*
     * 0 ~ 99：系统
     * 100 ~ 199：安全
     * 200 ~ 299：更新
     * 300 ~ 399：消息
     * 400 ~ 499：提醒
     * 500 ~ 599：警告
     * 1000+：公告
     */

    public final static NotificationPrivateModel psyAssist = new NotificationPrivateModel(
        null,
        400L,
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
        500L,
        null,
        null,
        "紧急心理援助",
        "您的安全对我们很重要！如果您正在经历痛苦或有轻生念头，请立即寻求帮助：\n\n 紧急求助电话：\n• 全国危机干预热线：400-161-9995\n• 北京危机干预热线：400-161-9995\n• 上海心理援助热线：021-34289888\n\n请记住：您并不孤单，总有人愿意倾听和帮助您！",
        TimeUtil.now(),
        1L,
        "high"
    );

    public final static NotificationPrivateModel counselingNew = new NotificationPrivateModel(
        null,
        310L, 
        null,
        null,
        "咨询预约已提交",
        "您的心理咨询预约已成功提交！\n\n请耐心等待咨询师确认。\n如有变动，您将收到进一步通知。\n如需修改或取消预约，请在预约详情页操作。\n\n感谢您的信任，祝您一切顺利！",
        TimeUtil.now(),
        1L,
        "low"
    );

    public final static NotificationPrivateModel counselingAccepted = new NotificationPrivateModel(
        null, 
        420L, 
        null,
        null,
        "咨询预约已确认",
        "您的心理咨询预约已被接受！\n\n 请按预约时间准时参加咨询\n 建议提前5-10分钟做好准备\n 如有紧急情况需要调整时间，请及时联系咨询师\n\n祝您咨询顺利！",
        TimeUtil.now(),
        1L,
        "medium"
    );

    public final static NotificationPrivateModel counselingFinished = new NotificationPrivateModel(
        null, 
        320L, 
        null,
        null,
        "咨询已完成",
        "您的心理咨询已顺利完成！\n\n 感谢您的信任与配合\n 请为本次咨询体验进行评价\n 如需后续咨询，可随时预约\n 祝您身心健康，生活愉快！",
        TimeUtil.now(),
        1L,
        "low"
    );

    public final static NotificationPrivateModel counselingRejected = new NotificationPrivateModel(
        null, 
        421L, 
        null,
        null,
        "咨询预约未通过",
        "很抱歉，您的咨询预约未能安排。\n\n可能原因：\n• 该时段已被预约\n• 咨询师时间冲突\n• 其他安排问题\n\n 建议：\n• 选择其他可用时间段\n• 联系其他合适的咨询师\n• 如有疑问可咨询客服\n\n感谢您的理解！",
        TimeUtil.now(),
        1L,
        "medium"
    );

    public final static NotificationPrivateModel counselingNewOrder = new NotificationPrivateModel(
        null,
        340L,
        null,
        null,
        "您有新的咨询预约",
        "您收到了一条新的心理咨询预约。\n\n 请及时查看预约详情并确认是否接受。\n如有时间冲突，请尽快与用户沟通调整。\n\n感谢您的专业服务！",
        TimeUtil.now(),
        1L,
        "low"
    );

    public final static NotificationPrivateModel counselingCanceled = new NotificationPrivateModel(
        null, 
        442L, 
        null,
        null,
        "用户取消咨询预约",
        "有用户取消了咨询预约。\n\n 预约详情：\n• 取消时间：刚刚\n• 原因：用户主动取消\n\n 处理建议：\n• 该时段现已空出，可接受其他预约\n• 如需了解详情可查看预约记录\n• 保持专业态度，理解用户需求变化\n\n感谢您的理解与配合！",
        TimeUtil.now(),
        1L,
        "low"
    );

    public final static NotificationPrivateModel callBackNotifyForUser = new NotificationPrivateModel(
        null,
        450L,
        null,
        null,
        "心理咨询师主动关怀",
        "我们注意到您最近与AI助手进行了较长时间的交流，为了给您提供更专业的帮助，已为您安排心理咨询师主动回访。\n\n回访安排：\n• 专业咨询师将在24小时内联系您\n• 提供更深入的心理支持和建议\n• 完全免费，无需额外操作\n\n如果您暂时不需要回访，可以在设置中关闭此功能。\n\n我们始终关心您的心理健康！",
        TimeUtil.now(),
        1L,
        "medium"
    );

    public static NotificationPrivateModel getCallBackNotifyForPsy(String userid) {
        return new NotificationPrivateModel(
            null,
            451L,
            null,
            null,
            "系统分配主动回访任务",
            "系统检测到用户 " + userid + " 与AI助手交流时间超过1小时，现为您分配主动回访任务。\n\n用户情况：\n• 用户ID：" + userid + "\n• AI交流时长：超过1小时\n• 建议关注：情绪状态和心理需求\n• 回访方式：电话或在线咨询\n\n任务要求：\n• 请在24小时内联系该用户\n• 提供专业的心理支持和建议\n• 评估是否需要进一步干预\n\n如有疑问，请联系系统管理员。\n感谢您的专业服务！",
            TimeUtil.now(),
            1L,
            "medium"
        );
    }
}
