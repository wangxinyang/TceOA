package com.tce.oa.modular.company.service;


import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.company.model.Project;

import java.util.List;

/**
 * <p>
 * 项目管理 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-13
 */
public interface IProjectService extends IService<Project> {

    public List<Project> buildImportData(String resultFilePath) throws Exception;
}
