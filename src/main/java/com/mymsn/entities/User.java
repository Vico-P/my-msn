package com.mymsn.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "login", length = 20)
    private String login;

    @Column(name = "password_hash", length = 60)
    private String passwordHash;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    // TODO Remove fetchtype eager
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({ "user" })
    private Set<ConversationParticipant> listConversations = new HashSet<>();

    // TODO Remove fetchtype eager
    @OneToMany(mappedBy = "id.senderUser", fetch = FetchType.EAGER)
    private Set<Friendship> listSentFriendships = new HashSet<>();

    // TODO Remove fetchtype eager
    @OneToMany(mappedBy = "id.receiverUser", fetch = FetchType.EAGER)
    private Set<Friendship> listReceivedFriendships = new HashSet<>();

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isIsVerified() {
        return this.isVerified;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ConversationParticipant> getListConversations() {
        return this.listConversations;
    }

    public void setListConversations(Set<ConversationParticipant> listConversations) {
        this.listConversations = listConversations;
    }

    public Set<Friendship> getListSentFriendships() {
        return this.listSentFriendships;
    }

    public void setListSentFriendships(Set<Friendship> listSentFriendships) {
        this.listSentFriendships = listSentFriendships;
    }

    public Set<Friendship> getListReceivedFriendships() {
        return this.listReceivedFriendships;
    }

    public void setListReceivedFriendships(Set<Friendship> listReceivedFriendships) {
        this.listReceivedFriendships = listReceivedFriendships;
    }

    public User id(UUID id) {
        setId(id);
        return this;
    }

    public User login(String login) {
        setLogin(login);
        return this;
    }

    public User passwordHash(String passwordHash) {
        setPasswordHash(passwordHash);
        return this;
    }

    public User email(String email) {
        setEmail(email);
        return this;
    }

    public User isVerified(Boolean isVerified) {
        setIsVerified(isVerified);
        return this;
    }

    public User createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public User updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }
}
