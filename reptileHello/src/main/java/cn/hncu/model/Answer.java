package cn.hncu.model;

public class Answer {
    private String id;

    private String problemId;

    private String answerTime;

    private String answerAnthor;

    private String anthorGrade;

    private String pointPraise;

    private String contemptNumber;

    private String answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId == null ? null : problemId.trim();
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime == null ? null : answerTime.trim();
    }

    public String getAnswerAnthor() {
        return answerAnthor;
    }

    public void setAnswerAnthor(String answerAnthor) {
        this.answerAnthor = answerAnthor == null ? null : answerAnthor.trim();
    }

    public String getAnthorGrade() {
        return anthorGrade;
    }

    public void setAnthorGrade(String anthorGrade) {
        this.anthorGrade = anthorGrade == null ? null : anthorGrade.trim();
    }

    public String getPointPraise() {
        return pointPraise;
    }

    public void setPointPraise(String pointPraise) {
        this.pointPraise = pointPraise == null ? null : pointPraise.trim();
    }

    public String getContemptNumber() {
        return contemptNumber;
    }

    public void setContemptNumber(String contemptNumber) {
        this.contemptNumber = contemptNumber == null ? null : contemptNumber.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}