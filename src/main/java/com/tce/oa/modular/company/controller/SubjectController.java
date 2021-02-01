package com.tce.oa.modular.company.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.tce.oa.core.common.constant.factory.ConstantFactory;
import com.tce.oa.core.common.exception.BizExceptionEnum;
import com.tce.oa.core.common.node.ZTreeNode;
import com.tce.oa.core.log.LogObjectHolder;
import com.tce.oa.modular.company.model.Subject;
import com.tce.oa.modular.company.service.ISubjectService;
import com.tce.oa.modular.company.warpper.SubjectWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 资金科目控制器
 *
 * @author wangxy
 * @Date 2018-11-13 19:46:39
 */
@Controller
@RequestMapping("/subject")
public class SubjectController extends BaseController {

    private String PREFIX = "/biz/subject/";

    @Autowired
    private ISubjectService subjectService;

    /**
     * 跳转到资金科目首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "subject.html";
    }

    /**
     * 跳转到添加资金科目
     */
    @RequestMapping("/toAdd")
    public String subjectAdd() {
        return PREFIX + "subject_add.html";
    }

    /**
     * 跳转到修改资金科目
     */
    @RequestMapping("/toUpdate/{id}")
    public String subjectUpdate(@PathVariable Integer id, Model model) {
        Subject subject = subjectService.selectById(id);
        model.addAttribute("item", subject);
        model.addAttribute("pName", ConstantFactory.me().getSubjectName(subject.getPid()));
        LogObjectHolder.me().set(subject);
        return PREFIX + "subject_edit.html";
    }

    /**
     * 获取资金科目列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.subjectService.list(condition);
        return super.warpObject(new SubjectWarpper(list));
    }

    /**
     * 新增资金科目
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Subject subject) {
        if (ToolUtil.isOneEmpty(subject, subject.getSubject())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        //完善pids,根据pid拿到pid的pids
        subjectSetPids(subject);
        subjectService.insert(subject);
        return SUCCESS_TIP;
    }

    /**
     * 删除资金科目
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer id) {
        subjectService.deleteById(id);
        return SUCCESS_TIP;
    }

    /**
     * 修改资金科目
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Subject subject) {
        if (ToolUtil.isEmpty(subject) || subject.getId() == null) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        subjectSetPids(subject);
        subjectService.updateById(subject);
        return SUCCESS_TIP;
    }

    /**
     * 资金科目详情
     */
    @RequestMapping(value = "/detail/{fundSubjectId}")
    @ResponseBody
    public Object detail(@PathVariable("fundSubjectId") Integer fundSubjectId) {
        return subjectService.selectById(fundSubjectId);
    }

    /**
     * 获取科目的tree列表
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = subjectService.tree();
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    private void subjectSetPids(Subject subject) {
        if (ToolUtil.isEmpty(subject.getPid()) || subject.getPid().equals(0)) {
            subject.setPid(0);
            subject.setPids("[0],");
        } else {
            int pid = subject.getPid();
            Subject temp = subjectService.selectById(pid);
            String pids = temp.getPids();
            subject.setPid(pid);
            subject.setPids(pids + "[" + pid + "],");
        }
    }
}
