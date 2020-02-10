package com.rowenci.timemachine.util;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/10 19:45
 */
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016101800714184";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4oZ6xGcCpfdfPbpgg64xno9qZumpQKxGMkVG55OSRJxoT6kTlA+S9NQNjckieWqTbAzWN/gi8HWGqacaDIwvGt7NCOnrblpDBpGd/mfspHPJontA+6lIvpqsQJH18iosCrfymOdJIHlZUTvNIjpXE+b6me+dSxeS9VBJTRENXZCreXxBaGgExfg1R/TLXhmbSem5YRfPr+WpN97kSIGoTMyA/XESOGq+RoIoYmwCx0BoEpnpuMN6NRFGXnUDsl7hWXRc6gTZZU7Ls4g9FfmFa6POslf9sjveXLyyctkO7e3c610SHkPwn0GP0Rvk8BX5hA6iWLHqZe8gWH0Xfedx1AgMBAAECggEADG6tneVLCN61cn1S9xyFVtE8vg9Ea2OLCfkbFLZqAbAtXFvxCa7Kb0crlD200BzwbuL3bzlE403jLrNyr4/jkhz6kCIZ9cFcu/1TDs+SjwEo/Yx8H1Bt+lFxwWODo6g8lhCbK6JHG54XWBP848zeHuZmRRWgzP0QWwNXPI4jLrYttmLo5bIFIgpguETEILAzR5TXC7xlPKXfHnyXazDkmUtc8VXq7Eu7b40CNG8AHvVxqeO0i5b9YrBkSJyBL/LUtWYJirN1ghA5cm/zI9ThDCgm0xj6vFOCz5rZzuisVoxtaatp9tth0hT2f0Jt1G2/ctxouKsLjDkwQy43jjvqvQKBgQDfoTQ1gmKBNbvBjC5dZr2GZOuHla3LZflRsHrw7l1MPiepU1bbiUrxfy2JWFU3lJUQvrY0cGWqkR8wc+LGXmMaQqY4ZIw0EY/f76xgwmkyNhyGKkCQxkpONzsd99FOR6BMLnOUgruK1RGYriiZMhgDvBeSQK3rTOYWqiA+WRFpnwKBgQDTW0v2XMwpXs+m/E8+72cD07Um4uFtUj+waF3eI2ViRg0iEffSHw1dd+I5HI4mg2mGRC1GcDegQEMfV0/eBF8otWd1J+z9yIFuPp9X2254Hmflk4Qj6hFuMo0BuMOWppIioZouO92VNGscAvJl5/VCdG8Kfs+0LZ9aRM+1TE/pawKBgQC0lX6Nfz6ATJ3Ti5mdPuvo7PHtSQdQ6oXMW5DLohChEsgAShYnrdRg82wYLsaR3WAKZ5bG/cR7WjkvbUVBHtM0T4HepAM7wLrIAMjYUeWU8HFUPiZpQlaalqscZhQU/G4LVq9qy68aSzisbGbIXtVZnrgCGEzZZ/2da8Ah2NbgzQKBgGZjH9Vk9vJDn8yBBdKzHvBXqrX/yryuz3Oh23c1JnQJA2ux0J/Vsx/IZ6rLDoHRv1jvxg8NZaUX/Gw+W85tx7tsxYWXpdL7QtgpNf/VqRo/gI1SfPz48yCzFGP1IarlXn2wU4R6M8mH7ytOsiZzy/g5d2FaNTxPrZaJJyXopf2pAoGBAMKUMxsxTdDiNlfBIYSNxchl/hm/FV9Yx05Qy3gWnn32hMlz+SRwV3+N2rn1V7x8q1z8KameF7O/cMgG/xm7N+Rb42DNBazWJw/tVNZFkY9cDI71Dz21tsUsYyLOGDjZKuEkgwtAVs6vB1CnBV5RKuMFYvOyrR1Cz/zoXb6umE10";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "你自己的支付宝公钥，注意是支付宝公钥，不是应用公钥";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String CHARSET = "utf-8";

    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 日志记录目录
    public static String log_path = "C:\\";

}
