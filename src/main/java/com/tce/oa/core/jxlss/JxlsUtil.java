package com.tce.oa.core.jxlss;

import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工具辅助类
 * @Author lnk
 * @Date 2018/1/22
 */
public class JxlsUtil {
    private static Logger logger = LoggerFactory.getLogger(JxlsUtil.class);
    private static final String TEMPLATE_PATH="jxls-template";
    private static final JxlsUtil me = new JxlsUtil();

    private JxlsUtil() {

    }

    /**
     * 获取工具类实例
     *
     * @return
     */
    public static JxlsUtil me() {
        return me;
    }

    /**
     * 如果字符串为{@code null}、空字符串或仅包含空白字符, 则返回false
     *
     * @param text 要进行检查的字符串
     */
    public boolean hasText(String text) {
        return !(text == null || text.length() == 0);

    }

    /**
     * 如果数组为{@code null}或长度为0, 则返回false
     *
     * @param array 要进行检查的数组
     */
    public <T> boolean notEmpty(T[] array) {
        return !(array == null || array.length == 0);
    }

    /**
     * 如果数组里包含有{@code null}的元素, 则抛出异常. 注意: 若数组本身为{@code null}则不会进行处理, 直接返回false
     *
     * @param array 要进行检查的数组
     */
    public <T> boolean noNullElements(T[] array) {
        if (array != null) {
            for (T element : array) {
                if (element == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 如果集合为{@code null},或者不包含任何元素,则返回false
     *
     * @param collection 要进行检查的集合
     */
    public boolean notEmpty(Collection<?> collection) {
        return !(collection == null || collection.isEmpty());
    }

    /**
     * 如果键值对为{@code null},或者不包含任何键值,则返回false
     *
     * @param map 要进行检查的键值对
     */
    public boolean notEmpty(Map<?, ?> map) {
        return !(map == null || map.isEmpty());
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param fmt
     * @return
     */
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数字格式化
     *
     * @param number
     * @param format
     * @return
     */
    public String numFmt(Number number, String format) {
        DecimalFormat dFormat = new DecimalFormat(format);
        return dFormat.format(number);
    }

    /**
     * 返回第一个不为空的对象
     *
     * @param objs
     * @return
     */
    public Object getNotNull(Object... objs) {
        for (Object o : objs) {
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    /**
     * if判断
     *
     * @param b
     * @param o1
     * @param o2
     * @return
     */
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    /**
     * 将图片转成数据
     *
     * @param path 图片绝对路径
     * @return
     */
    public byte[] getImageData(String path) throws IOException {
        try (InputStream ins = new FileInputStream(path)) {
            return IOUtils.toByteArray(ins);
        }
    }

    /**
     * 获取图片后缀
     *
     * @param name 图片路径或名称
     * @return
     */
    public String getImageType(String name) {
        int index = name.lastIndexOf(".");
        if (index > 0) {
            return name.substring(index + 1);
        }
        return null;
    }

    /**
     * 将图片转成JxlsImage数据对象
     *
     * @param imgPath 图片路径
     * @return
     */
    public JxlsImage getJxlsImage(String imgPath) throws IOException {
        JxlsImage img = new JxlsImage();
        img.setPictureData(getImageData(imgPath));
        img.setPictureType(getImageType(imgPath));
        return img;
    }


    /**
     * 将图片转成JxlsImage数据对象
     * @param is
     * @param name
     * @return
     * @throws IOException
     */
    public JxlsImage getJxlsImage(InputStream is, String name) throws IOException {
        JxlsImage img = new JxlsImage();
        img.setPictureData(IOUtils.toByteArray(is));
        img.setPictureType(getImageType(name));
        return img;
    }
    /**
     * 判断路径是否是绝对路径
     *
     * @param path
     * @return
     */
    public boolean isAbsolutePath(String path) {
        return (path.startsWith("/") || path.contains(":"));
    }

    /**
     * 获取集合中的元素
     *
     * @param index
     * @param array
     * @return
     */
    public <T>T get(int index, T[] array) {
        if (notEmpty(array)) {
            return array[index];
        }
        return null;
    }

    /**
     * 获取集合中的元素
     *
     * @param index
     * @param list
     * @return
     */
    public Object get(int index, List<?> list) {
        if (notEmpty(list)) {
            return list.get(index);
        }
        return null;
    }

    /**
     * 获取集合中的元素
     *
     * @param key
     * @param map
     * @return
     */
    public Object get(Object key, Map<?, ?> map) {
        if (notEmpty(map)) {
            return map.get(key);
        }
        return null;
    }

    //获取jxls模版文件
    public static File getTemplate(String name){
        String templatePath = JxlsUtil.class.getClassLoader().getResource(TEMPLATE_PATH).getPath();
        logger.info("-------------templatePath is: " + templatePath);
        File template = new File(templatePath, name);
        logger.info("-------------template is: " + template);
        if(template.exists()){
            return template;
        }
        return null;
    }

    /**
     *@Description 发送响应流方法
     *@Date 15:36 2018/12/13
     *@Param
     *@return
     **/
    public static void setResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        fileName = new String(fileName.getBytes(),"ISO8859-1");
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
    }

    /**
     *@Description 具体导出功能
     *@Date 15:43 2018/12/13
     *@Param [response, model, fileName, template]
     *@return void
     **/
    public static void export(HttpServletResponse response, Map<String, Object> model, String fileName, File template) {
        OutputStream os = null;
        try {
            JxlsUtil.setResponseHeader(response, fileName);
            os = response.getOutputStream();
            JxlsBuilder.getBuilder(template).out(os).putAll(model).build();
            os.flush();
        } catch (Exception e) {
            logger.error("jxls export file is error :" + e);
            throw new ServiceException(BizExceptionEnum.FILE_EXPORT_ERROR);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("close os is error :" + e);
                    throw new ServiceException(BizExceptionEnum.FILE_IO_ERROR);
                }
            }
        }
    }
}
