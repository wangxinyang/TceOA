package com.tce.oa.modular.company.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tce.oa.modular.company.dao.ProjectMapper;
import com.tce.oa.modular.company.model.Project;
import com.tce.oa.modular.company.service.IProjectService;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目管理 服务实现类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {


    private final static String XML_CONFIG_NAME = "projectConfig.xml";
    private static final String XML_CONFIG_PATH="jxls-import";

    @Override
    public List<Project> buildImportData(String filePath) throws Exception {
        List<Project> projectInfoList = new ArrayList<Project>();
        String templatePath = getClass().getClassLoader().getResource(XML_CONFIG_PATH).getPath();
        File file = new File(templatePath, XML_CONFIG_NAME);
        XLSReader mainReader = ReaderBuilder.buildFromXML(file);
        Project projectInfo = new Project();
        InputStream inputXLS = new BufferedInputStream(new FileInputStream(filePath));
        Map<String, Object> beans = new HashMap();
        beans.put("projectInfo", projectInfo);
        beans.put("projectInfoList", projectInfoList);
        XLSReadStatus readStatus = mainReader.read(inputXLS, beans);
        if(readStatus.isStatusOK()){
            System.out.println("jxls读取Excel成功！");
            return projectInfoList;
        }
        return null;
    }
}
