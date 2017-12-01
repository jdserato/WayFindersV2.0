/**
 * Created by Serato, Jay Vince on November 27, 2017.
 */
public class FAQ {
    private String question;
    private String answer;
    public String id;

    public FAQ() {
    }

    public FAQ(String id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getTheId() {
        return Integer.parseInt(id);
    }
}
