package vn.iotstar.util;

public class UploadConstant {
    // Có thể đổi bằng VM option: -Dshopping.upload.dir=D:\\upload
    public static final String DIR = System.getProperty(
            "shopping.upload.dir",
            System.getProperty("user.home") + java.io.File.separator + "shopping-upload"
    );

    private UploadConstant() {
    }
}
