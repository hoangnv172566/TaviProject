package Models.Reward;

public class Reward {
    private long idAnswerTotal;
    private String code;
    private String content;
    private String expiredDate;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getIdAnswerTotal() {
        return idAnswerTotal;
    }

    public void setIdAnswerTotal(long idAnswerTotal) {
        this.idAnswerTotal = idAnswerTotal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }
}
