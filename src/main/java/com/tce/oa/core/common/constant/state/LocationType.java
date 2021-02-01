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
 * 地点类型
 *
 * @author wangxy
 * @date 2018年11月15日
 */
public enum LocationType {

    CQ(1, "重庆", "cq"),
    BJ(2, "北京", "bj");

    int code;
    String message;
    String prefix;

    LocationType(int code, String message, String prefix) {
        this.code = code;
        this.message = message;
        this.prefix = prefix;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static String prefixValueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (LocationType s : LocationType.values()) {
                if (s.getCode() == status) {
                    return s.getPrefix();
                }
            }
            return "";
        }
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (LocationType s : LocationType.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }

    public static LocationType getByValue(Integer status) {
        LocationType fundType = LocationType.CQ;
        if (status == null) {
            return fundType;
        } else {
            for (LocationType s : LocationType.values()) {
                if (s.getCode() == status) {
                    return s;
                }
            }
            return fundType;
        }
    }
}
