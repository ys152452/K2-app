package com.self.demo01.print;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.self.demo01.Application;
import com.self.demo01.R;
import com.self.demo01.utils.BitmapUtils;
import com.self.demo01.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintUtil {

    private static final String COMMAND = "COMMAND";

    public static Gson gson = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create();

    public static CommandRequest getSimpleTextPrint() {
        CommandRequest request = new CommandRequest();

        PrintTextParamsNew printTextParamsNew = new PrintTextParamsNew();
        printTextParamsNew.text = "IoT能力调用demo";
        printTextParamsNew.textspace = 1;
        printTextParamsNew.size = 18;
        request.command = printTextParamsNew.COMMAND;
        request.params = entityToMap(printTextParamsNew);
        return request;
    }

    public static CommandRequest getTextsPrint() {
        CommandRequest request = new CommandRequest();

        PrintTextsParamsNewSingle printDividingParamsNew = new PrintTextsParamsNewSingle();
        printDividingParamsNew.columns = new ArrayList<>();
        PrintTextsItemParamsNew itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = "名称";
        request.command = printDividingParamsNew.COMMAND;
        printDividingParamsNew.columns.add((itemParamsNew));

        PrintTextsItemParamsNew itemParamsNew1 = new PrintTextsItemParamsNew();
        itemParamsNew1.text = "单价";
        printDividingParamsNew.columns.add((itemParamsNew1));

        PrintTextsItemParamsNew itemParamsNew2 = new PrintTextsItemParamsNew();
        itemParamsNew2.text = "单位";
        printDividingParamsNew.columns.add((itemParamsNew2));

        PrintTextsItemParamsNew itemParamsNew3 = new PrintTextsItemParamsNew();
        itemParamsNew3.text = "总价";
        printDividingParamsNew.columns.add((itemParamsNew3));

        request.params = entityToMap(printDividingParamsNew);
        LogUtil.e("ricardo", gson.toJson(request) + "   " + request);
        return request;
    }

    public static CommandRequest getPicRequest(String base64Str) {
        CommandRequest request = new CommandRequest();

        PrintPicParamsNew printPicParamsNew = new PrintPicParamsNew();
        printPicParamsNew.align = 1;
        printPicParamsNew.data = base64Str;
        request.command = printPicParamsNew.COMMAND;
        request.params = printPicParamsNew;
        return request;
    }

    public static CommandRequest getDividingPrint(int height) {
        CommandRequest request = new CommandRequest();

        PrintDividingParamsNew printDividingParamsNew = new PrintDividingParamsNew();
        printDividingParamsNew.height = height;
        printDividingParamsNew.style = 1;
        request.command = printDividingParamsNew.COMMAND;
        request.params = entityToMap(printDividingParamsNew);
        return request;
    }

    public static CommandRequest getQrcodePrint() {
        CommandRequest request = new CommandRequest();

        PrintQRCodeParamsNew printQRCodeParamsNew = new PrintQRCodeParamsNew();
        printQRCodeParamsNew.code = "IoT能力调用demo";
        printQRCodeParamsNew.align = 1;
        printQRCodeParamsNew.size = 10;
        printQRCodeParamsNew.el = 3;
        request.command = printQRCodeParamsNew.COMMAND;
        request.params = entityToMap(printQRCodeParamsNew);
        return request;
    }

    public static CommandRequest getBarcodePrint() {
        CommandRequest request = new CommandRequest();

        PrintBarCodeParamsNew printBarCodeParamsNew = new PrintBarCodeParamsNew();
        printBarCodeParamsNew.code = "123456789";
//        printBarCodeParamsNew.symbology = "EAN8";
        printBarCodeParamsNew.hri = 1;
        printBarCodeParamsNew.align = 1;
        printBarCodeParamsNew.width = 6;
        printBarCodeParamsNew.height = 80;
        request.command = printBarCodeParamsNew.COMMAND;
        request.params = entityToMap(printBarCodeParamsNew);
        return request;
    }

    /**
     * 获取测试样张打印指令集
     * @return 指令集
     */
    public static List<CommandRequest> printDemoCommands() {
        List<CommandRequest> list = new ArrayList<>();
        list.add(getDemoTitle());
        list.add(getDividingPrint(1));
        list.add(getTextCommand("订单编号：2023010300010", 0, 18, 0, 0, 0, 0));
        list.add(getTextCommand("下单时间：2023-01-03 10:10:10", 0, 18, 0, 0, 0, 0));
        list.add(getTextCommand("支付方式：现金", 0, 18, 0, 0, 0, 0));
        list.add(getDividingPrint(1));
        list.add(getDemoTablePrintCommand("品名", "单价", "数/重量", "金额"));
        list.add(getDividingPrint(2));
        list.add(getDemoTablePrintCommand("套餐1", "20.00", "1", "20.00"));
        list.add(getDemoTablePrintCommand("套餐2", "10.00", "2", "20.00"));
        list.add(getDemoTablePrintCommand("套餐3", "10.00", "1", "10.00"));
        list.add(getDemoTablePrintCommand("套餐4", "10.00", "1", "10.00"));
        list.add(getDemoTablePrintCommand("套餐5-节日定制版", "10.00", "1", "10.00"));
        list.add(getDemoTablePrintCommand("套餐6", "18.00", "1", "18.00"));
        list.add(getDividingPrint(2));
        list.add(getTable2PrintCommand("累计金额", "¥88.00"));
        list.add(getTable2PrintCommand("实际收款", "¥88.00"));
        list.add(getGoLine(1));
        list.add(getDemoBarcodePrint());
        list.add(getTextCommand("此单消费仅为功能样式，非真实交易", 0, 36, 1, 1, 0, 0));
        list.add(getGoLine(2));
        list.add(getPrintLogo());
        list.add(getTextCommand("感谢使用商米！", 1, 36, 1, 0, 0, 0));
        list.add(getDemoQrcodePrint());
        list.add(getGoLine(1));
        list.add(getDemoGuideTextPrint());
        list.add(getGoLine(4));
        list.add(cutPaper());
        return list;
    }

    // 获取样张标题打印指令
    public static CommandRequest getDemoTitle() {
        CommandRequest request = new CommandRequest();

        PrintTextParamsNew printTextParamsNew = new PrintTextParamsNew();
        printTextParamsNew.text = "标准IoT能力调用Demo" + "\n" + "打印样张";
        printTextParamsNew.textspace = 0;
        printTextParamsNew.ver_ratio = 1;
        printTextParamsNew.hor_ratio = 1;
        printTextParamsNew.size = 36;
        printTextParamsNew.align = 1;
        printTextParamsNew.bold = 1;
        request.command = printTextParamsNew.COMMAND;
        request.params = entityToMap(printTextParamsNew);
        return request;
    }

    // 获取文本打印指令（指定参数）
    public static CommandRequest getTextCommand(String text, int align, int size, int bold, int underline, int anticolor, int textspace) {
        CommandRequest request = new CommandRequest();
        PrintTextParamsNew printTextParamsNew = new PrintTextParamsNew();
        printTextParamsNew.text = text;
        printTextParamsNew.align = align;
        printTextParamsNew.size = size;
        printTextParamsNew.bold = bold;
        printTextParamsNew.underline = underline;
        printTextParamsNew.anticolor = anticolor;
        printTextParamsNew.textspace = textspace;
        request.command = printTextParamsNew.COMMAND;
        request.params = entityToMap(printTextParamsNew);
        return request;
    }

    // 获取样张表格打印指令
    public static CommandRequest getDemoTablePrintCommand(String text1, String text2, String text3, String text4) {
        CommandRequest request = new CommandRequest();

        PrintTextsParamsNewSingle printDividingParamsNew = new PrintTextsParamsNewSingle();
        printDividingParamsNew.columns = new ArrayList<>();

        PrintTextsItemParamsNew itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text1;
        itemParamsNew.align = 0;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text2;
        itemParamsNew.align = 1;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text3;
        itemParamsNew.align = 1;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text4;
        itemParamsNew.align = 2;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        request.command = printDividingParamsNew.COMMAND;
        request.params = entityToMap(printDividingParamsNew);
        LogUtil.e("ricardo", gson.toJson(request) + "   " + request);
        return request;
    }

    // 获取两列表格打印指令
    public static CommandRequest getTable2PrintCommand(String text1, String text2) {
        CommandRequest request = new CommandRequest();

        PrintTextsParamsNewSingle printDividingParamsNew = new PrintTextsParamsNewSingle();
        printDividingParamsNew.columns = new ArrayList<>();

        PrintTextsItemParamsNew itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text1;
        itemParamsNew.align = 0;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        itemParamsNew = new PrintTextsItemParamsNew();
        itemParamsNew.text = text2;
        itemParamsNew.align = 2;
        itemParamsNew.weight = 1;
        printDividingParamsNew.columns.add((itemParamsNew));

        request.command = printDividingParamsNew.COMMAND;
        request.params = entityToMap(printDividingParamsNew);
        LogUtil.e("ricardo", gson.toJson(request) + "   " + request);
        return request;
    }

    // 获取样张条形码打印指令
    public static CommandRequest getDemoBarcodePrint() {
        CommandRequest request = new CommandRequest();

        PrintBarCodeParamsNew printBarCodeParamsNew = new PrintBarCodeParamsNew();
        printBarCodeParamsNew.code = "2023010300010";
//        printBarCodeParamsNew.symbology = "EAN8";
        printBarCodeParamsNew.hri = 2;
        printBarCodeParamsNew.align = 1;
        printBarCodeParamsNew.width = 2;
        printBarCodeParamsNew.height = 162;
        request.command = printBarCodeParamsNew.COMMAND;
        request.params = entityToMap(printBarCodeParamsNew);
        return request;
    }

    public static CommandRequest getPrintLogo() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap alipay_bitmap = BitmapFactory.decodeResource(Application.app.getResources(), R.mipmap.print_logo, opts);
        if (alipay_bitmap.getWidth() > 384) {
            int newHeight = (int) (1.0 * alipay_bitmap.getHeight() * 384 / alipay_bitmap.getWidth());
            alipay_bitmap = BitmapUtils.scale(alipay_bitmap, 384, newHeight);
        }
        CommandRequest request = new CommandRequest();

        PrintPicParamsNew printPicParamsNew = new PrintPicParamsNew();
        printPicParamsNew.align = 1;
        printPicParamsNew.data = bitmapToBase64(alipay_bitmap);
        gcBitmap(alipay_bitmap);
        request.command = printPicParamsNew.COMMAND;
        request.params = printPicParamsNew;
        return request;
    }

    // 获取样张二维码打印指令
    public static CommandRequest getDemoQrcodePrint() {
        CommandRequest request = new CommandRequest();

        PrintQRCodeParamsNew printQRCodeParamsNew = new PrintQRCodeParamsNew();
        printQRCodeParamsNew.code = "https://developer.sunmi.com/docs/zh-CN/crxfqeghjk513/zdxeghjk491#h-actiontype：command";
        printQRCodeParamsNew.align = 1;
        printQRCodeParamsNew.size = 4;
        printQRCodeParamsNew.el = 0;
        request.command = printQRCodeParamsNew.COMMAND;
        request.params = entityToMap(printQRCodeParamsNew);
        return request;
    }

    // 获取扫码指示文案打印指令
    public static CommandRequest getDemoGuideTextPrint() {
        CommandRequest request = new CommandRequest();

        PrintTextParamsNew printTextParamsNew = new PrintTextParamsNew();
        printTextParamsNew.text = "扫码获取更多信息";
        printTextParamsNew.textspace = 0;
        printTextParamsNew.size = 20;
        printTextParamsNew.align = 1;
        printTextParamsNew.anticolor = 1;
        request.command = printTextParamsNew.COMMAND;
        request.params = entityToMap(printTextParamsNew);
        return request;
    }

    // 走纸
    public static CommandRequest getGoLine(int line) {
        CommandRequest request = new CommandRequest();
        PrintTextParamsNew printTextParamsNew = new PrintTextParamsNew();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < line; i++) {
            stringBuilder.append("\n");
        }
        printTextParamsNew.text = stringBuilder.toString();
        printTextParamsNew.textspace = 0;
        printTextParamsNew.size = 1;
        printTextParamsNew.align = 1;
        request.command = printTextParamsNew.COMMAND;
        request.params = entityToMap(printTextParamsNew);
        return request;
    }
    public static CommandRequest cutPaper() {
        CommandRequest request = new CommandRequest();
        request.command = "cut_paper";
        request.params = null;
        return request;
    }


    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                if (field.isEnumConstant() || field.getModifiers() == Modifier.FINAL) {
                    continue;
                }

                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                if (field.getName().equals(COMMAND)) {
                    continue;
                } else
                    map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    /**
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: bitmapToBase64
     * @Description: TODO(Bitmap 转换为字符串)
     */

    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;

    }

    public static void gcBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle(); // 回收图片所占的内存
            bitmap = null;
            System.gc(); // 提醒系统及时回收
        }
    }

}
