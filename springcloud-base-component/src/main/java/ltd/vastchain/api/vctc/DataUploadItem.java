package ltd.vastchain.api.vctc;

public class DataUploadItem {
    public String type;
    public Object args;

    public DataUploadItem() {

    }

    public DataUploadItem(String type, Object args) {
        this.type = type;
        this.args = args;
    }

    /**
     * 志愿活动注册
     */
    public final static String Type_Voluntary_Activity_Register = "voluntary-activity-register";
    /**
     * 志愿活动签到
     */
    public final static String Type_Voluntary_Activity_SignIn = "voluntary-activity-signIn";
    /**
     * 志愿活动签退
     */
    public final static String Type_Voluntary_Activity_SignOut = "voluntary-activity-signOut";
    /**
     * 智能门锁型号注册
     */
    public final static String Type_Intelligent_Doorlock_Model = "intelligent-doorlock-model";
    /**
     * 智能门锁型号注册
     */
    public final static String Type_Intelligent_Doorlock_Register = "intelligent-doorlock-register";
    /**
     * 提交 everiPass
     */
    public final static String Type_everiPass = "everiPass";

    // 你可以在这里扩展其他的 type

    /**
     * 获取链上 ID 接口
     */
    public final static String Type_Chain_Id_Info = "chain-id-info";
    /**
     * 创建可信积分测试
     */
    public final static String Type_Register_Test = "fungible-token-symbol-register";
    /**
     * 发行可信积分测试
     */
    public final static String Type_Issue_Test = "fungible-token-issue";
    /**
     * 发行可信积分测试
     */
    public final static String Type_Transfer_Test = "fungible-token-transfer";
}
