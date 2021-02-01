package com.tce.oa.modular.company.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tce.oa.core.common.constant.factory.PageFactory;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.common.page.PageInfoBT;
import com.tce.oa.core.jxlss.JxlsUtil;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.modular.company.model.Project;
import com.tce.oa.modular.company.service.IProjectService;
import com.tce.oa.modular.company.vo.ProjectVo;
import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 项目控制器
 *
 * @author wangxinyang
 * @version 1.0
 * @date 2018/11/28 18:22
 **/
@Controller
@RequestMapping("/project/manager")
public class ProjectController extends BaseController {

    private String PREFIX = "/biz/company/project/";
    @Autowired
    private IProjectService projectService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 跳转到项目首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "project.html";
    }

    /**
     * 跳转到添加项目
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return PREFIX + "project_add.html";
    }

    /**
     * 跳转到修改项目
     */
    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable Integer id, Model model) {
        Project project = projectService.selectById(id);
        model.addAttribute("item", project);
        LogObjectHolder.me().set(project);
        return PREFIX + "project_edit.html";
    }

    /**
     * 获取项目列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Project> page = new PageFactory<Project>().defaultPage();
        if (StringUtils.isEmpty(condition)) {
            projectService.selectPage(page, null);
        } else {
            Wrapper<Project> wrapper = new EntityWrapper<>();
            wrapper.like("name", condition);
            projectService.selectPage(page, wrapper);
        }
        return new PageInfoBT<>(page);
    }

    /**
     * 获取项目列表
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) {
        String templateFile = request.getSession().getServletContext().getRealPath("/import");
        try {
            if (multipartFile.isEmpty()) {
                return ResponseData.error(BizExceptionEnum.FILE_NOT_FOUND.getCode(),
                        BizExceptionEnum.FILE_NOT_FOUND.getMessage());
            } else {
                String fileName = multipartFile.getOriginalFilename();
                if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx") && !fileName.endsWith(".xlsm")) {
                    return ResponseData.error(BizExceptionEnum.FILE_NOT_EXCEL.getCode(),
                            BizExceptionEnum.FILE_NOT_EXCEL.getMessage());
                }
                File file = new File(templateFile);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),
                        new File(templateFile, multipartFile.getOriginalFilename()));
                String resultFilePath = templateFile + File.separator + fileName;

                List<Project> projects = projectService.buildImportData(resultFilePath);
                if (projects != null && projects.size() > 0) {
                    for (Project project : projects) {
                        project.setCreatetime(new Date());
                    }
                    //i从1开始，忽略模板中的标题栏
                    projectService.insertBatch(projects);
                    return SUCCESS_TIP;
                } else {
                    return ResponseData.error(BizExceptionEnum.FILE_CONTENT_ERROR.getCode(),
                            BizExceptionEnum.FILE_CONTENT_ERROR.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(BizExceptionEnum.FILE_IMPORT_SYSTEM_ERROR.getCode(),
                    BizExceptionEnum.FILE_IMPORT_SYSTEM_ERROR.getMessage());
        }
    }

    /**
     * 获取项目列表
     */
    @RequestMapping(value = "/file")
    public void upload(HttpServletResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        String fileName = "project_example.xlsx";
        File template = JxlsUtil.getTemplate("project_.xlsx");
        JxlsUtil.export(response, model, fileName, template);
    }

    /**
     * 新增项目
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Project project) {
        if (null != project) {
            project.setCreatetime(new Date());
        }
        projectService.insert(project);
        return SUCCESS_TIP;
    }

    /**
     * 删除项目
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        projectService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改项目
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Project project) {
        projectService.updateById(project);
        return SUCCESS_TIP;
    }

    /**
     * 获取科目的tree列表
     */
    @RequestMapping(value = "/project/info")
    @ResponseBody
    public List<ProjectVo> getProjectInfo() {
        List<ProjectVo> projectVoList = new ArrayList<>();
        Wrapper<Project> projectWrapper = new EntityWrapper<>();
        List<Project> projectList = projectService.selectList(projectWrapper);
        for (Project project : projectList) {
            ProjectVo projectVo = new ProjectVo();
            modelMapper.map(project, projectVo);
            projectVoList.add(projectVo);
        }
        return projectVoList;
    }
}
