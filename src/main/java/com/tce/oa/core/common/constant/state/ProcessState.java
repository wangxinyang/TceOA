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
 * 审核流程状态枚举
 *
 * @author wxy
 * @date 2017年6月1日22:50:11
 */
public enum ProcessState {

    SUBMITING(1, "待提交"),
    CHECKING_SELF(2, "待自身审核"),
    CHECKING_LEADER(3, "待部门负责人审核"),
    CHECKING_ASSISTANT(4, "待总裁助理审核"),
    CHECKING_FINANCE(5, "待财务审核"),
    CHECKING_DEPUTY(6, "待副总裁审核"),
    CHECKING_MANAGER(7, "待总裁审核"),
    UN_PASS(8, "审核未通过"),
    PASS(9, "审核通过"),
    WITH_DRAW(10, "撤回");

    int code;
    String message;

    ProcessState(int code, String message) {
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
            for (ProcessState s : ProcessState.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
