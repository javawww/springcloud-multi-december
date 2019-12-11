package ltd.vastchain.api.vctc;

/**
 * 代表数据上链的请求参数
 */
public class DataUploadBody {
    private DataUploadItem[] items;

    public DataUploadBody(DataUploadItem[] items) {
        this.items = items;
    }

    public DataUploadItem[] getItems() {
        return items;
    }

    public void setItems(DataUploadItem[] items) {
        this.items = items;
    }
}
