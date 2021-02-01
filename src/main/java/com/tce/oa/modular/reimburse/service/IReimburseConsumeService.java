package com.tce.oa.modular.reimburse.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.tce.oa.modular.reimburse.model.ReimburseConsume;
import com.tce.oa.modular.reimburse.model.ReimburseConsumeDetail;
import com.tce.oa.modular.reimburse.transfer.ConsumeReimburseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用报销 服务类
 * </p>
 *
 * @author wangxy123
 * @since 2018-11-29
 */
public interface IReimburseConsumeService extends IService<ReimburseConsume> {

    void save(Map<String, Object> modelMap);

    void add(Map<String, Object> modelMap, String assignee);

    void update(ReimburseConsume reimburseConsume, List<ReimburseConsumeDetail> reimburseConsumeDetailList);

    void delete(Integer id);

    void deletePathDetailById(Integer id);

    void printProcessImage(Integer id) throws IOException;

    List<Map<String, Object>> getProcessList();

    void pass(ConsumeReimburseDto consumeReimburseDto);

    void unPass(ConsumeReimburseDto consumeReimburseDto);

    /**
     * 获取数据
     */
    List<Map<String, Object>> getReimburseConsumes(Page<ReimburseConsume> page, String beginTime, String endTime,
                                                   Integer deptid, Integer state,
                                                   List<Integer> ids);

    /**
     * 获取数据
     */
    List<Map<String, Object>> getReimburseConsumes(String beginTime, String endTime,
                                                   Integer deptid, Integer state, List<Integer> ids);
}
