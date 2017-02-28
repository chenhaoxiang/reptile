package cn.hncu.model;

public class Problem {
    private String id;

    private String problem;

    private String problemAnthor;

    private String quizTime;

    private String problemDescribe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem == null ? null : problem.trim();
    }

    public String getProblemAnthor() {
        return problemAnthor;
    }

    public void setProblemAnthor(String problemAnthor) {
        this.problemAnthor = problemAnthor == null ? null : problemAnthor.trim();
    }

    public String getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(String quizTime) {
        this.quizTime = quizTime == null ? null : quizTime.trim();
    }

    public String getProblemDescribe() {
        return problemDescribe;
    }

    public void setProblemDescribe(String problemDescribe) {
        this.problemDescribe = problemDescribe == null ? null : problemDescribe.trim();
    }
}