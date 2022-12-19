package com.mymsn.entities.compositeKey;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mymsn.entities.User;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public class FriendshipId implements Serializable {
    @ManyToOne
    @JsonIgnoreProperties({ "listSentFriendships", "listReceivedFriendships", "listConversations" })
    private User senderUser;

    @ManyToOne
    @JsonIgnoreProperties({ "listSentFriendships", "listReceivedFriendships", "listConversations" })
    private User receiverUser;

    public FriendshipId() {
    }

    public FriendshipId(User senderUser, User receiverUser) {
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
    }

    public User getSenderUser() {
        return this.senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public User getReceiverUser() {
        return this.receiverUser;
    }

    public void setReceiverUser(User receiverUser) {
        this.receiverUser = receiverUser;
    }
}
