package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.core.exception.GlobalException;
import com.tencent.wxcloudrun.dto.TemplateMessageData;
import com.tencent.wxcloudrun.service.WxApi;
import com.tencent.wxcloudrun.service.WxTemplateMessageSender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tu
 * @date 2022-08-22 14:28:32
 */
@Service
public class WxTemplateMessageSenderImpl implements WxTemplateMessageSender, InitializingBean {

    @Autowired
    private WxApi wxApi;

    private String templateId = "EWgKnyuarGWJxly4HYJ8lSzPa98qdgDQSDinGYlFHFA";

    private Set<String> nicknameSet = new HashSet<>();
    private Set<String> usedNicknameSet = new HashSet<>();

    private Set<String> loveDescrptionSet = new HashSet<>();
    private Set<String> usedLoveDescrptionSet = new HashSet<>();

    @Override
    public void send(@RequestBody Map<String, TemplateMessageData> messageParam) {
        Map<String, Object> dataMap = buildData(messageParam);
        List<String> openIdList = wxApi.getWatchOpenIdList();
        for (String openId : openIdList) {
            Map<String, Object> messageBody = buildMessageBody(dataMap, openId, templateId, "#FF0000");
            wxApi.sendTemplateMessage(messageBody);
        }
    }

    @Override
    public synchronized void changeTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public void sendDailyMessage() {
        Map<String, TemplateMessageData> messageParam = new HashMap<>();
        messageParam.put("nickName", new TemplateMessageData(getNickName(), "#000"));
        messageParam.put("description", new TemplateMessageData(getDescription(Calendar.getInstance()), "#FF8066"));
        messageParam.put("loveDescription", new TemplateMessageData(getLoveDescription(), "#FF7878"));

        Map<String, Object> dataMap = buildData(messageParam);


        List<String> openIdList = wxApi.getWatchOpenIdList();
        for (String openId : openIdList) {
            Map<String, Object> messageBody = buildMessageBody(dataMap, openId, templateId, "#FF0000");
            wxApi.sendTemplateMessage(messageBody);
        }
    }

    @Override
    public String getLoveDescription(String content) {
        if (content == null) {
            return getLoveDescription();
        }
        for (String str : loveDescrptionSet) {
            if (str.contains(content)) {
                return str;
            }
        }
        return getLoveDescription();
    }

    private Map<String, Object> buildMessageBody(Map<String, Object> dataMap, String openId, String templateId, String topColor) {
        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("touser", openId);
        if (templateId == null) {
            throw new GlobalException("templateId 为空,请先设置 templateId");
        }
        messageBody.put("template_id", templateId);
        messageBody.put("topcolor", topColor);
        messageBody.put("data", dataMap);
        return messageBody;
    }

    private Map<String, Object> buildData(Map<String, TemplateMessageData> messageParam) {
        Map<String, Object> dataMap = new HashMap<>();
        for (Map.Entry<String, TemplateMessageData> messageDataEntry : messageParam.entrySet()) {
            Map<String, Object> value = new HashMap<>();
            value.put("value", messageDataEntry.getValue().getValue());
            value.put("color", (messageDataEntry.getValue().getColor() == null || "".equals(messageDataEntry.getValue().getColor())) ? "#c3c3c3" : messageDataEntry.getValue().getColor());
            dataMap.put(messageDataEntry.getKey(), value);
        }
        return dataMap;
    }

    public String getNickName() {


        String[] nicknameArr = (String[]) nicknameSet.toArray(new String[0]);
        Random random = new Random();
        int randomNum = random.nextInt();
        int index = randomNum % nicknameSet.size();
        if (index < 0) {
            index = -index;
        }
        String nickname = nicknameArr[index];

        nicknameSet.remove(nickname);
        usedNicknameSet.add(nickname);

        if (nicknameSet.size() == 0) {
            nicknameSet = new HashSet<>(usedNicknameSet);
            usedNicknameSet.clear();
        }
        return nickname;

    }

    public String getDescription(Calendar nowDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar memorialDayDate = new Calendar.Builder().setDate(2022, 7, 25).build();
        Calendar togetherDay = new Calendar.Builder().setDate(2020, 7, 25).build();


        int nowDayNum = nowDate.get(Calendar.DAY_OF_YEAR);
        int memorialDayNum = memorialDayDate.get(Calendar.DAY_OF_YEAR);


        int dayNum = nowDayNum - memorialDayNum;

        long togetherDayNum = (nowDate.getTimeInMillis() - togetherDay.getTimeInMillis()) / (1000 * 3600 * 24);

        String descrption = "今天是${date},${endStr}";
        descrption = descrption.replace("${date}", format.format(nowDate.getTime()));
        if (dayNum < 0) {
            String endStr = "距离两周年还有" + (dayNum * -1) + "天\uD83C\uDF31～";
            descrption = descrption.replace("${endStr}", endStr);
        } else if (dayNum == 0) {
            String endStr = "就是我们两周年纪念日啦,两周年快乐\uD83D\uDC23~";
            descrption = descrption.replace("${endStr}", endStr);
        } else {
            String endStr = "我们在一起" + togetherDayNum + "天啦\uD83C\uDF8A~";
            descrption = descrption.replace("${endStr}", endStr);

        }

        return descrption;
    }

    public String getLoveDescription() {
        String[] loveDescrptionArr = loveDescrptionSet.toArray(new String[0]);
        Random random = new Random();
        int randomNum = random.nextInt();
        int index = randomNum % loveDescrptionSet.size();
        if (index < 0) {
            index = -index;
        }
        String loveDescription = loveDescrptionArr[index];

        loveDescrptionSet.remove(loveDescription);
        usedLoveDescrptionSet.add(loveDescription);

        if (loveDescrptionSet.size() == 0) {
            loveDescrptionSet = new HashSet<>(usedLoveDescrptionSet);
            usedLoveDescrptionSet.clear();
        }
        return loveDescription;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        nicknameSet.add("宝");
        nicknameSet.add("臭宝");
        nicknameSet.add("秦女士");
        nicknameSet.add("小溪");
        nicknameSet.add("美羊羊");
        nicknameSet.add("老婆");


        loveDescrptionSet.add("你稍微记一下，我找你的时候，就是爆炸想你，没找你的时候，就是憋着在想你～");
        loveDescrptionSet.add("希望你如锦绣山河，万人向往，却属于我～");
        loveDescrptionSet.add("我看过春风十里，见过夏至未至，试过秋光潋滟，爱过冬日暖阳，全都抵不过你一句，我想见你～");
        loveDescrptionSet.add("在这个世界上我喜欢三件事：太阳、月亮和你～太阳是白昼，月亮是夜晚，而你是我的永远");
        loveDescrptionSet.add("你是我字典里所有褒义词的集合，胜过我世界里所有美好时刻的总和～");
        loveDescrptionSet.add("你，是我光是想想都会偷着乐的人，甜不可言，乐不可支～");
        loveDescrptionSet.add("人们都说“春天应该暗恋一个人，然后夏天和他私奔～”如今我喜欢你已过四季，你说我们是不是该谈谈余生了～");
        loveDescrptionSet.add("我想变得有趣，变得特别，变成你眼里的一点星光～");
        loveDescrptionSet.add("再遇见你一百次，我也会沦陷一百次～");
        loveDescrptionSet.add("我很贪心，想当你的星星，也想做你的月亮～");
        loveDescrptionSet.add("You complete me～");
        loveDescrptionSet.add("不食人间烟火～怎么会喜欢上你～");
        loveDescrptionSet.add("你怎么这么奇怪～怪惹人爱的～");
        loveDescrptionSet.add("怎么办～每天都更想你一点～");
        loveDescrptionSet.add("万里山河不江川 你是我一生的渴慕～");
        loveDescrptionSet.add("抬头看到天空会想到你～看到日落会想到你～看到星星会想到你～看到日出也会想到你～");
        loveDescrptionSet.add("心动一次不容易、希望一直是你～");
        loveDescrptionSet.add("惩罚你爱我很久很久，久到我们俩头发花白～");
        loveDescrptionSet.add("如果你周五来，那这一周我都是幸福的～");
        loveDescrptionSet.add("心上有你、认真且怂、双木非林、田下有心～");
        loveDescrptionSet.add("因为你看着我时我的眼睛里有星星～");
        loveDescrptionSet.add("屠红斌会一直有秦溪敏～");
        loveDescrptionSet.add("幸好思念无声、否则震耳欲聋～");
        loveDescrptionSet.add("对啊～就是一直喜欢你啊～");
        loveDescrptionSet.add("你就是我遥遥万里最牵挂的女孩子");
        loveDescrptionSet.add("撒娇和粘人都是被动技能、只有遇到对的人才有触发的可能～");
        loveDescrptionSet.add("秋千水、竹马道、一眼见你、万物不及～");
        loveDescrptionSet.add("最重要的事就是见你～");
    }


}
