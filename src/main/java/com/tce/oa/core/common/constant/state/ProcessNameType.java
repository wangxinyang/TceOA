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
 * 申请预算类型
 *
 * @author wangxy
 * @date 2018年11月15日
 */
public enum ProcessNameType {

    SALES(1, "业务费用预算申请"),
    TRAVEL(2, "差旅费用预算申请"),
    PURCHASE(3, "采购费用预算申请"),
    OTHER(4, "其它费用预算申请"),
    REIMBURSE_TRAVEL(5, "差旅费报销"),
    REIMBURSE_COMMON(6, "费用报销"),
    PURCHASE_APPLY(7, "采购申请"),
    FEE_APPLY(8, "资金申请"),
    PAYMENT_APPLY(9, "付款申请");

    int code;
    String message;

    ProcessNameType(int code, String message) {
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
            for (ProcessNameType s : ProcessNameType.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }

    public static ProcessNameType getByValue(Integer status) {
        ProcessNameType fundType = ProcessNameType.SALES;
        if (status == null) {
            return fundType;
        } else {
            for (ProcessNameType s : ProcessNameType.values()) {
                if (s.getCode() == status) {
                    return s;
                }
            }
            return fundType;
        }
    }
}
