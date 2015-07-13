package hacktx.hacktx2015.models;

import java.util.List;

/**
 * Created by britne on 7/12/15.
 */
public class SlackChannelResponse {

    private List<Channels> channels;

    public List<Channels> getChannels() {
        return channels;
    }

    public void setChannels(List<Channels> channels) {
        this.channels = channels;
    }
}
