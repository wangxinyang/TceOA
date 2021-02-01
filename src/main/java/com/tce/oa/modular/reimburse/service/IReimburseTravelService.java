package com.tce.oa.modular.reimburse.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.reimburse.model.ReimburseTravel;
import com.tce.oa.modular.reimburse.model.ReimburseTravelPath;
import com.tce.oa.modular.reimburse.transfer.TravelReimburseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 差旅费用报销 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface IReimburseTravelService extends IService<ReimburseTravel> {

    void save(Map<String, Object> modelMap);

    void add(Map<String, Object> modelMap, String assignee);

    void update(ReimburseTravel reimburseTravel, List<ReimburseTravelPath> reimburseTravelPathList);

    void delete(Integer id);

    void deletePathDetailById(Integer id);

    void printProcessImage(Integer id) throws IOException;

    List<Map<String, Object>> getProcessList();

    void pass(TravelReimburseDto travelReimburseDto);

    void unPass(TravelReimburseDto travelReimburseDto);

    /**
     * 获取数据
     */
    List<Map<String, Object>> getReimburseTravels(Page<ReimburseTravel> page, String beginTime,
                                                  String endTime, Integer deptid, Integer state,
                                                  List<Integer> ids);

    /**
     * 获取数据
     */
    List<Map<String, Object>> getReimburseTravels(String beginTime, String endTime,
                                                  Integer deptid, Integer state, List<Integer> ids);

}
