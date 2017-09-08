package com.usamsl.global.service.data;

import com.usamsl.global.R;
import com.usamsl.global.service.entity.ChatModel;
import com.usamsl.global.service.entity.ItemModel;
import com.usamsl.global.service.entity.VisaConsultationCountry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 * 服务界面签证咨询模块：签证国家的数据
 */
public class ServiceVisaCountryData {
    public static List<VisaConsultationCountry> list;

    public static List<VisaConsultationCountry> initData() {
        list = new ArrayList<>();
        list.add(new VisaConsultationCountry(R.drawable.america, "美洲签证"));
        list.add(new VisaConsultationCountry(R.drawable.japan_korea, "日韩签证"));
        list.add(new VisaConsultationCountry(R.drawable.europe, "欧洲签证"));
        list.add(new VisaConsultationCountry(R.drawable.south_aisa, "东南亚签证"));
        list.add(new VisaConsultationCountry(R.drawable.australia, "澳洲签证"));
        list.add(new VisaConsultationCountry(R.drawable.others, "其他签证"));
        list.add(new VisaConsultationCountry(R.drawable.contact_phone, "联系电话"));
        list.add(new VisaConsultationCountry(R.drawable.after_sale, "售后服务"));
        return list;
    }

    /**
     * 客服聊天首发内容
     *
     * @return
     */
    public static ArrayList<ItemModel> initChatData() {
        ArrayList<ItemModel> models = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent("你好,我们交个朋友吧！");
        models.add(new ItemModel(ItemModel.CHAT_A, model));
        return models;
    }
}
