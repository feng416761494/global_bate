package com.usamsl.global.constants;

/**
 * Created by Administrator on 2017/1/11.
 * url集合
 */
public class UrlSet {

    //ip地址//http://139.196.88.32:8080 上线版//http://139.196.136.150:8080测试版
    public static String IP = "http://139.196.88.32:8080";
    public static String localHost = "http://192.168.1.185:8080";
    //选择的国家对应的表的连接
    public static String countrySurface;
    //登录接口
    public static String login = IP + "/GlobalVisa/app/customer/login?";
    //第三方登录接口
    public static String loginByThird = localHost + "/GlobalVisa/app/customer/login_third_party?";
    //ava语音对接博士后台接口
    public static String ask_ava = "http://139.224.227.86/cgi-bin/ask_ava.py";
    //首页热门国家展示的接口
    public static String index_countryList = IP + "/GlobalVisa/app/index/countryListCom?";
    //注册界面，验证手机是否已经注册
    public static String validation_phone = IP + "/GlobalVisa/app/customer/validationPhone?";
    //发送手机验证码
    public static String sendSMS = IP + "/GlobalVisa/app/customer/sendSMS?";
    //注册
    public static String registerCust = IP + "/GlobalVisa/app/customer/registerCust?";
    //找回密码
    public static String forgotPwd = IP + "/GlobalVisa/app/customer/forgotPwd?";
    //获取好友分组
    public static String groupList = IP + "/GlobalVisa/app/customer/groupList?token=";
    //新增分组
    public static String addGroup = IP + "/GlobalVisa/app/customer/addGroup?";
    //城市加载
    public static String loadVisaAll = IP + "/GlobalVisa/app/visa/loadVisaAll";
    //加载所有领取的所有产品
    public static String loadVisa = IP + "/GlobalVisa/app/area/loadVisa";
    //国家加载
    public static String loadCountry = IP + "/GlobalVisa/app/area/loadCountry";
    //签证展现界面领区加载
    public static String loadVisaArea = IP + "/GlobalVisa/app/visa/loadVisaArea?";
    //签证展现加载
    public static String visa_loadVisa = IP + "/GlobalVisa/app/visa/loadVisa?";
    //更换领区
    public static String loadCountryVisaArea = IP + "/GlobalVisa/app/visa/loadCountryVisaArea?";
    //签证所需资料展现
    public static String loadVisaDatum = IP + "/GlobalVisa/app/visa/loadVisaDatum?";
    //签证所需的所有资料战线
    public static String loadVisaDatumAll = IP + "/GlobalVisa/app/visa/loadVisaDatumAll?visa_id=";
    //签证所需的所有材料(后台处理完的数据)
    public static String loadVisaDatumAll2 = IP + "/GlobalVisa/app/visa/loadVisaDatumFen?visa_id=";
    //全部联系人查询
    public static String selectContactAll = IP + "/GlobalVisa/app/contact/selectContactAll?token=";
    //修改联系人
    public static String updateContact = IP + "/GlobalVisa/app/contact/updateContact?";
    //添加联系人
    public static String addContact = IP + "/GlobalVisa/app/contact/addContact?";
    //删除分组
    public static String deleteGroup = IP + "/GlobalVisa/app/customer/deleteGroup?id=";
    //修改分组
    public static String updateGroup = IP + "/GlobalVisa/app/customer/updateGroup?";
    //新增订单
    public static String orderAdd = IP + "/GlobalVisa/app/order/orderAdd?";
    //付款订单查询
    public static String orderDetail = IP + "/GlobalVisa//app/order/orderDetail?order_id=";
    //上传图片
    public static String upload = IP + "/GlobalVisa/appfileupload/upload?";
    //我的订单材料
    public static String orderDatum = IP + "/GlobalVisa/app/order/orderDatum?id=";
    //上传材料更新
    public static String orderDatumUpdate = IP + "/GlobalVisa/app/order/orderDatumUpdate";
    //根据联系人id进行查询联系人信息
    public static String getContact = IP + "/GlobalVisa/app/contact/getContact?contact_id=";
    //预约面试时间
    public static String orderUpdateTime = IP + "/GlobalVisa//app/order/orderUpdateTime?";
    //我的订单
    public static String AppOrderAll = IP + "/GlobalVisa/app/order/AppOrderAll?token=";
    //订单已完成未完成
    public static String AppOrderFinish = IP + "/GlobalVisa/app/order/AppOrderFinish?token=";
    //取消订单
    public static String orderUpdateStop = IP + "/GlobalVisa/app/order/orderUpdateStop?";
    //在职证明新增
    public static String saveProfession = IP + "/GlobalVisa/app/orderprofession/saveProfession?";
    //在职证明查询
    public static String selectProfession = IP + "/GlobalVisa/app/orderprofession/selectProfession?contact_id=";
    //在校证明新增
    public static String saveStudent = IP + "/GlobalVisa/app/orderprofession/saveStudent?";
    //在校证明查询
    public static String selectStudent = IP + "/GlobalVisa/app/orderprofession/selectStudent?contact_id=";
    //修改订单
    public static String updateOrder = IP + "/GlobalVisa/app/order/updateOrder?";

    //新增接口 8/4  (提价更新H5信息)
    public static String updateInfo = IP + "/GlobalVisa/app/order/updateInfo";
    //支付完成改变订单状态
    public static String ordesrPay = IP + "/GlobalVisa/app/order/orderPay?";
    //隐私政策
    public static String authorize = IP + "/GlobalVisa/app/authorize/authorize.do";
    //银行网点
    public static String bankList = IP + "/GlobalVisa/app/index/bankList.do";
    //获取网点信息
    public static String selectBankId = IP + "/GlobalVisa/app/order/selectBankId?order_id=";
    //线下付款发个通知
    public static String orderOffPayAdd = IP + "/GlobalVisa/app/order/orderOffPayAdd.do";
    //版本号
    public static String clientVersion = IP + "/GlobalVisa/app/index/clientVersion.do";
    //验证token是否有效
    public static String token = IP + "/GlobalVisa/app/index/token.do?token=";
    //线下办理
    public static String orderOffAdd = IP + "/GlobalVisa/app/order/orderOffAdd.do";
    //首页海报1 最大的那张
    public static String poster1 = "http://mp.weixin.qq.com/s/aFVtTWzVFMt44AR597HBng";
    //evus
    public static String goEvusOne = IP + "/GlobalVisa/application/evus/evus.html";
    //支付接口
    public static String aliPayCode = IP + "/GlobalVisa/app/index/aliPayCode.do";
    //表单进度
    public static String formSchedule = IP + "/GlobalVisa/app/order/selectRequest.do?order_id=";
    //提交户口本、一寸照片之后更改订单状态
    public static String updateDatumOrder = IP + "/GlobalVisa/app/order/updateDatumOrder.do";
    //把订单和联系人联系起来（不改状态）
    public static String updateOrderDatum = IP + "/GlobalVisa/app/order/updateOrderDatum";
    //服务里边的问题库
    public static String problemList = localHost + "/GlobalVisa/app/index/problemList";
    //预约面试时间
    public static String orderBespeakTime = IP + "/GlobalVisa/app/order/orderBespeakTime.do?order_id=";
    //与Ava聊天记录保存
    public static String AvaChatLogSave = IP + "/GlobalVisa/app/chatLog/logSave.do";
    //获取与Ava聊天记录
    public static String getAvaChatLog = IP + "/GlobalVisa/app/chatLog/selectLog.do?";
    //首页EB-5的地址
    public static String EB_5_url = "http://mp.weixin.qq.com/s/Mjuc0k0gYAhAxkiDZl5WnQ";
    //用户个人信息查询
    public static String userInfo = IP + "/GlobalVisa/app/customer/getUser?token=";
    //用户上传个人信息
    public static String saveUserInfo = IP + "/GlobalVisa/app/customer/updateUser.do";
    //首页banner图片和bannner对应的链接网址
    public static String index_banner = IP + "/GlobalVisa/app/index/bannerList?";
}
