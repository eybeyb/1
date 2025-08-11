package com.thundersoft.trash.Model;

import java.io.Serializable;
import java.util.List;

public class TrashResponse {
    private int code;
    private String msg;
    private Result result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private List<TrashItem> list;

        public List<TrashItem> getList() {
            return list;
        }

        public void setList(List<TrashItem> list) {
            this.list = list;
        }
    }

    public static class TrashItem implements Serializable {
        private String name;
        private int type;
        private int aipre;
        private String explain;
        private String contain;
        private String tip;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getAipre() {
            return aipre;
        }

        public void setAipre(int aipre) {
            this.aipre = aipre;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getContain() {
            return contain;
        }

        public void setContain(String contain) {
            this.contain = contain;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        @Override
        public String toString() {
            return "TrashItem{" +
                    "name='" + name + '\'' +
                    ", type=" + type +
                    ", explain='" + explain + '\'' +
                    ", contain='" + contain + '\'' +
                    ", tip='" + tip + '\'' +
                    '}';
        }

        public String getCategory() {
            if (type == 1) {
                return "可回收";
            } else if (type == 2) {
                return "不可回收";
            } else if (type == 3) {
                return "可回收";
            } else if (type == 4) {
                return "有害";
            } else {
                return "未知";
            }
        }
    }

}
