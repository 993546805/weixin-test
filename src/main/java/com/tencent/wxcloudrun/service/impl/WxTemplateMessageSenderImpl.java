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

    private Set<String> loveDescriptionSet = new HashSet<>();
    private Set<String> usedLoveDescrptionSet = new HashSet<>();

    private Set<String> iconSet = new HashSet<>();

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
        messageParam.put("description", new TemplateMessageData(getDescription(Calendar.getInstance()), "#E0C097"));
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
        for (String str : loveDescriptionSet) {
            if (str.contains(content)) {
                return str;
            }
        }
        return getIcon() + getLoveDescription();
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
        String[] loveDescrptionArr = loveDescriptionSet.toArray(new String[0]);
        Random random = new Random();
        int randomNum = random.nextInt();
        int index = randomNum % loveDescriptionSet.size();
        if (index < 0) {
            index = -index;
        }
        String loveDescription = loveDescrptionArr[index];

        loveDescriptionSet.remove(loveDescription);
        usedLoveDescrptionSet.add(loveDescription);

        if (loveDescriptionSet.size() == 0) {
            loveDescriptionSet = new HashSet<>(usedLoveDescrptionSet);
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


        loveDescriptionSet.add("你稍微记一下，我找你的时候，就是爆炸想你，没找你的时候，就是憋着在想你～");
        loveDescriptionSet.add("希望你如锦绣山河，万人向往，却属于我～");
        loveDescriptionSet.add("我看过春风十里，见过夏至未至，试过秋光潋滟，爱过冬日暖阳，全都抵不过你一句，我想见你～");
        loveDescriptionSet.add("在这个世界上我喜欢三件事：太阳、月亮和你～太阳是白昼，月亮是夜晚，而你是我的永远~");
        loveDescriptionSet.add("你是我字典里所有褒义词的集合，胜过我世界里所有美好时刻的总和～");
        loveDescriptionSet.add("你，是我光是想想都会偷着乐的人，甜不可言，乐不可支～");
        loveDescriptionSet.add("人们都说“春天应该暗恋一个人，然后夏天和他私奔～”如今我喜欢你已过四季，你说我们是不是该谈谈余生了～");
        loveDescriptionSet.add("我想变得有趣，变得特别，变成你眼里的一点星光～");
        loveDescriptionSet.add("再遇见你一百次，我也会沦陷一百次～");
        loveDescriptionSet.add("我很贪心，想当你的星星，也想做你的月亮～");
        loveDescriptionSet.add("You complete me～");
        loveDescriptionSet.add("不食人间烟火～怎么会喜欢上你～");
        loveDescriptionSet.add("你怎么这么奇怪～怪惹人爱的～");
        loveDescriptionSet.add("怎么办～每天都更想你一点～");
        loveDescriptionSet.add("万里山河不江川 你是我一生的渴慕～");
        loveDescriptionSet.add("抬头看到天空会想到你～看到日落会想到你～看到星星会想到你～看到日出也会想到你～");
        loveDescriptionSet.add("心动一次不容易、希望一直是你～");
        loveDescriptionSet.add("惩罚你爱我很久很久，久到我们俩头发花白～");
        loveDescriptionSet.add("如果你周五来，那这一周我都是幸福的～");
        loveDescriptionSet.add("心上有你、认真且怂、双木非林、田下有心～");
        loveDescriptionSet.add("因为你看着我时我的眼睛里有星星～");
        loveDescriptionSet.add("屠红斌会一直有秦溪敏～");
        loveDescriptionSet.add("幸好思念无声、否则震耳欲聋～");
        loveDescriptionSet.add("对啊～就是一直喜欢你啊～");
        loveDescriptionSet.add("你就是我遥遥万里最牵挂的女孩子~");
        loveDescriptionSet.add("撒娇和粘人都是被动技能、只有遇到对的人才有触发的可能～");
        loveDescriptionSet.add("秋千水、竹马道、一眼见你、万物不及～");
        loveDescriptionSet.add("最重要的事就是见你～");
        loveDescriptionSet.add("The moment I like you, there is nothing in my eyes.");
        loveDescriptionSet.add("那我十月一去找你，然后我们两个隔着栅栏看一眼～");
        loveDescriptionSet.add("我会今天爱你～明天爱你～后天爱你～天天爱你～我会一直爱你～这是我会～不是我想～");
        loveDescriptionSet.add("你给的爱不算热烈、只是细水长流的细节里、都是你爱我的表现～");

        iconSet.add("\uD83D\uDC23");
        iconSet.add("\uD83E\uDDA9");
        iconSet.add("\uD83C\uDF37");
        iconSet.add("\uD83C\uDF43");
        iconSet.add("\uD83C\uDF8B");
        iconSet.add("\uD83C\uDF31");
        iconSet.add("\uD83C\uDF35");
        iconSet.add("\uD83C\uDF3F");
        iconSet.add("\uD83E\uDEB4");
        iconSet.add("\uD83C\uDF33");
        iconSet.add("⛰");
        iconSet.add("\uD83D\uDCA1");
        iconSet.add("\uD83D\uDEC1");
        iconSet.add("\uD83C\uDF8A");
        iconSet.add("\uD83C\uDF51");
    }


    public String getIcon() {
        Random random = new Random();
        int randomInt = random.nextInt();
        String[] iconArr = iconSet.toArray(new String[0]);
        int index = randomInt % iconSet.size();
        return iconArr[index < 0 ? -index : index];
    }
}
