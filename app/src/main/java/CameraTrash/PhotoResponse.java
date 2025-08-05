// PhotoResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package CameraTrash;
import java.util.List;

public class PhotoResponse {
    private List<Result> result;
    private String logid;
    private long resultNum;

    public List<Result> getResult() { return result; }
    public void setResult(List<Result> value) { this.result = value; }

    public String getLogid() { return logid; }
    public void setLogid(String value) { this.logid = value; }

    public long getResultNum() { return resultNum; }
    public void setResultNum(long value) { this.resultNum = value; }
}

// Result.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

class Result {
    private double score;
    private String root;
    private String keyword;

    public double getScore() { return score; }
    public void setScore(double value) { this.score = value; }

    public String getRoot() { return root; }
    public void setRoot(String value) { this.root = value; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String value) { this.keyword = value; }
}
