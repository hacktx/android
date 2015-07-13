package hacktx.hacktx2015.models;

import java.util.List;

/**
 * Created by britne on 7/12/15.
 */
public class SlackMessageResponse {

    private List<Messages> messages;

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }
}
