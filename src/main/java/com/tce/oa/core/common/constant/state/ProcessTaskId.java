/**
 * Copyright 2018-2020 stylefeng & fengshuonan (https://gitee.com/stylefeng)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tce.oa.core.common.constant.state;

/**
 * 报销流程节点类型
 *
 * @author wangxy
 * @date 2018年11月15日
 */
public enum ProcessTaskId {

    LEADER(1, "leader_approve"),
    ASSISTANT(2, "assistant_approve"),
    FINANCE(3, "finance_approve"),
    BJFINANCE(4, "finance_bj_approve"),
    CQFINANCE(5, "finance_cq_approve"),
    DEPUTYMANAGER(6, "deputyManager_approve"),
    MANAGER(7, "manager_approve"),
    SUBMIT(9, "submitForm");


    int code;
    String message;

    ProcessTaskId(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (ProcessTaskId s : ProcessTaskId.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
